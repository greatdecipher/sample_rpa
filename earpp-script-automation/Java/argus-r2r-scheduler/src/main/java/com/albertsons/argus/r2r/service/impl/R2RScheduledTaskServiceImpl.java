package com.albertsons.argus.r2r.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.albertsons.argus.r2r.service.R2RSchedulerService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.r2r.service.R2RScheduledTaskService;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class R2RScheduledTaskServiceImpl implements R2RScheduledTaskService {
    private static final Logger LOG = LogManager.getLogger(R2RScheduledTaskServiceImpl.class);
    
    @Autowired
	private R2RSchedulerService r2rSchedulerService;

	@Autowired
	private EmailService emailService;

    @Autowired
    private MetricsWebService metricsWebService;

    @Autowired
    Environment environment;

	@Override
	@Scheduled(cron = "${com.argus.r2r.scheduled.task.end.of.day.cron.value}")
	public void runR2RSchedulerEODJob(){
        LOG.log(Level.INFO, () -> "start R2R End of Day email script execution . . .");

		try {
			File folderPath = new File(environment.getProperty("folder.success.files"));
			Path path; 
			LocalDateTime ldt; 
			BasicFileAttributes bfa;

			File[] filesInFolder = folderPath.listFiles();
			
			long dateDiff;
			
			if (folderPath.isDirectory()) {
				try {
					for (File file: filesInFolder) {
						path = Paths.get(file.getPath());
						bfa = Files.readAttributes(path, BasicFileAttributes.class);
						ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(bfa.creationTime().toMillis()), TimeZone.getDefault().toZoneId());
						dateDiff = ChronoUnit.DAYS.between(ldt, LocalDateTime.now());
						
						if (dateDiff > 1) { // anything more than 1 day
							LOG.log(Level.DEBUG, () -> "deleting file " + file.getName() + ". . .");
							file.delete();
						}
					}

				} catch (Exception e) {
					LOG.log(Level.INFO, () -> "error while deleting files by age. . .");
					LOG.error(e);
				}
			}

			AutomationUtil util = new AutomationUtil();
			Date dateNow = new Date();
			String execCompleteTime = util.toDateString(dateNow, environment.getProperty("domain.util.date.format.word"), environment.getProperty("r2r.timezone"));

			String body = "<body>Hello, <br><br>";

			if (filesInFolder.length > 0){ // at least 1 file was successful
				body += "The following files had successful R2R monitoring for end of day " + execCompleteTime + ".<br>";
				
				body += "<ul style='list-style-type:disc'>";

					for (File file: filesInFolder){
						body += "<li>" + file.getName() + "</li>";
					}

					for (File file: filesInFolder){
						file.delete();
					}

				body += "</ul>";
			}
			else{ // no files were successful
				body += "No files had successful R2R monitoring for end of day " + execCompleteTime + ".<br>";
			}

			body += "<br><br>" +
					"Thanks & Regards, <br>" + 
					"Bot</body>";

			emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
						environment.getProperty("mail.argus.from.alias"), 
						environment.getProperty("mail.argus.oracle.financial.it.email", String[].class),
						environment.getProperty("mail.argus.cc", String[].class),
						environment.getProperty("mail.subject.automation.end.of.day"),
						body, NORMAL_PRIORITY,true);

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error emailing End of Day R2R automation for the day. . .");
            LOG.error(e);
		}

        LOG.log(Level.INFO, () -> "R2R End of Day email script execution ended . . .");
    }

    @Override
    @Scheduled(cron = "${com.argus.r2r.scheduled.task.cron.value}")
    public void runR2RSchedulerJob() {
        LOG.log(Level.INFO, () -> "start R2R scheduler script execution . . .");

		String automationRunId = "";

		try {
			ResponseStartSaveBO bo = metricsWebService.startSave("A100018", "1.0");
		
			if (bo.getResult().contains("SUCCESS")){
				automationRunId = bo.getId();
			}
			else{
				LOG.log(Level.DEBUG, () -> "problem on Start Save. . .");
			}

		} catch (Exception e){
			LOG.error("error on Start Save. . .");
			LOG.error(e);
		}

		List<String> filePrefixesToRun = new ArrayList<>();

		try {
			filePrefixesToRun = r2rSchedulerService.getFilesToRunFromRefFile();

			if (filePrefixesToRun.size() >= 1){
				for (String filePrefix : filePrefixesToRun){
					// runs script that launches a cmd per filePrefix
					Runtime.getRuntime().exec("wscript " + environment.getProperty("run.cmd.vbscript.path") + " " + filePrefix);
				}
			}
			else{
				LOG.log(Level.INFO, () -> "no file scheduled for window time. . .");
			}

		} catch (Exception e) {
			LOG.log(Level.INFO, () -> "exception caught in R2R scheduler script execution. . .");
			LOG.error(e);
		}

		if (StringUtils.isNotBlank(automationRunId)){
			try {
				ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");

				if (bo2.getResult().contains("SUCCESS")){
					LOG.log(Level.DEBUG, () -> "Ending save. . .");
					ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);

					if (bo3.getResult().contains("SUCCESS")){
						LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
					}
					else{
						LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
					}
				}
				else{
					LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
				}    
			}
			catch (Exception e){
				LOG.error("error on Increment Transaction or End Save. . .");
				LOG.error(e);
			}
		}
		
		LOG.log(Level.INFO, () -> "R2R scheduler script execution ended . . .");
    }
}
