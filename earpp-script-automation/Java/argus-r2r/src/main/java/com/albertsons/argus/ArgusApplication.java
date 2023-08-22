package com.albertsons.argus;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.domaindbr2r.dto.RecordDTO;
import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.service.R2RCommonService;
import com.albertsons.argus.r2r.service.R2RErpService;
import com.albertsons.argus.r2r.service.R2RMimService;
import com.albertsons.argus.r2r.service.R2ROicService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.r2r", "com.albertsons.argus.datar2r", "com.albertsons.argus.domain", "com.albertsons.argus.domaindbr2r", "com.albertsons.argus.mail"})
public class ArgusApplication implements CommandLineRunner{
	private static final Logger LOG = LogManager.getLogger(ArgusApplication.class);
	
	@Autowired
	public Environment environment;

	@Autowired
	public R2RMimService r2rMimService;

	@Autowired
	public R2ROicService r2rOicService;

	@Autowired
	public R2RErpService r2rErpService;

	@Autowired R2RCommonService r2rCommonService;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArgusApplication.class, args);
		LOG.debug("Shutdown Springboot . . .");
		System.exit(SpringApplication.exit(context, () -> 0));
	}
	
	@Override
	public void run(String... args) throws Exception {     
		LOG.log(Level.INFO, () -> "start R2R script execution . . .");

		String[] progArgs = args;
		String filePrefix = progArgs[0];
		List<FileDetails> filesToProcessList = new ArrayList<>();
		int deleteFilePrefix = 0; // flag indicating if this process is the one actively running the file prefix
		String folder = "",  execTimestamp = "", execCompleteTime = "";

		AutomationUtil util = new AutomationUtil();
		
		try {
			Date dateNow = new Date();
			execTimestamp = util.toDateString(dateNow, environment.getProperty("domain.util.date.format"), environment.getProperty("r2r.timezone"));
			execCompleteTime = util.toDateString(dateNow, environment.getProperty("domain.util.date.format.word"), environment.getProperty("r2r.timezone"));

			folder = environment.getProperty("file.prefixes.running");

			// Validate that no other CMD window is processing the same type of 
			// file prefix found in the reference file.
			if (r2rCommonService.checkIfFileIsRunning(folder, filePrefix) == true){

				// CMD with this file prefix is currently running
				LOG.log(Level.INFO, () -> "cancelling execution of R2R script. . .");
			}
			else {
				deleteFilePrefix = 1;

				// Save file prefix in specified folder to indicate it is now being processed
				String filePath = environment.getProperty("file.prefixes.running") + "\\" + filePrefix;
				util.createFile("This file prefix is currently running", filePath);	

				FileDetails fileDetails = r2rMimService.getFileDetailsFromRefFile(filePrefix); // Get file details
				
				filesToProcessList = r2rMimService.mainMimTask(fileDetails, execTimestamp, execCompleteTime);

				if (filesToProcessList == null || filesToProcessList.isEmpty() || filesToProcessList.size() < 1){
					// nothing to process, so don't proceed
					LOG.log(Level.INFO, () -> "no files to process. . .");
				}
				else{
					final List<FileDetails> filesList = filesToProcessList;
					final String timestamp = execTimestamp;

					// From this point, files to process will run in parallel
					IntStream.range(0, filesToProcessList.size()).parallel().forEach(i->{
						try {
							List<Object> getOicItems = new ArrayList<>();
							RecordDTO fileInRecord = new RecordDTO();
							FileDetails file = new FileDetails();

							getOicItems = r2rOicService.mainOicTask(filesList.get(i), timestamp);

							if (!getOicItems.isEmpty() && getOicItems.size() == 2){ // expect only 2 objects inside: 1 string and 1 RecordDTO
								
								for (Object returnOicItem : getOicItems){
									if (returnOicItem.getClass().equals(FileDetails.class)){
										file = (FileDetails) returnOicItem;
									}
									else if (returnOicItem.getClass().equals(RecordDTO.class)){
										fileInRecord = (RecordDTO) returnOicItem;
									}
								}

								if (fileInRecord.getGroup_Id() != 0 || !fileInRecord.getGroup_Id().equals(null)){
									String groupId = Long.toString(fileInRecord.getGroup_Id());
									r2rErpService.mainErpTask(file, groupId, timestamp);
								}
								else {
									// end automation
								}
							}
							else{
								// a problem was encounted in OIC interface so end automation
							}
							
						} catch (Exception e){
							LOG.log(Level.INFO, () -> "exception caught in parallel run of files. . .");
							LOG.error(e);
							e.printStackTrace();
						}
					});
				}
				
			}

		} catch (Exception e) {
			LOG.log(Level.INFO, () -> "exception caught in R2R script execution. . .");
			LOG.error(e);
			e.printStackTrace();

			// send email to developers that exception was caught in R2R
			String body = "<body>Hello, <br><br>";
						
			body += "An error was caught in the R2R automation of " + filePrefix + ".";

			body += "<br><br>" +
					"Thanks & Regards, <br>" + 
					"Bot</body>";

			r2rMimService.sendMimEmail(environment.getProperty("mail.argus.recipients", String[].class), environment.getProperty("mail.argus.cc", String[].class), environment.getProperty("mail.subject.automation.exception.caught"), body);

		} finally {
			
			if (deleteFilePrefix == 1){
				// Delete file prefix in specified folder to indicate it is not processing anymore
				util.deleteFile(folder, 0, filePrefix, false);
			}
			else{
				// Another process is running the existing file prefix so don't delete anything
			}

		}
		
		LOG.log(Level.INFO, () -> "R2R script execution ended . . .");
	}
}
