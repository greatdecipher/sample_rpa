package com.albertsons.argus;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.q2c.common.service.Q2CCommonService;
import com.albertsons.argus.q2c.dto.InboundMask;
import com.albertsons.argus.q2c.inbound.bankpaids.service.BankPaidsOicService;
import com.albertsons.argus.q2c.inbound.invoices.service.ArInvoicesService;
import com.albertsons.argus.q2c.inbound.lockbox.service.LockboxService;
import com.albertsons.argus.q2c.service.EmployeePayrollService;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.q2c", "com.albertsons.argus.dataq2c", "com.albertsons.argus.domain", "com.albertsons.argus.domaindbq2c", "com.albertsons.argus.mail"})
public class ArgusQ2CApplication implements CommandLineRunner {
	private static final Logger LOG = LogManager.getLogger(ArgusQ2CApplication.class);

	@Autowired
	Environment environment;

	@Autowired
	BankPaidsOicService bankPaidsOicService;

	@Autowired
	private ArInvoicesService arInvoicesService;

	@Autowired
	private EmployeePayrollService employeePayrollService;
	
	@Autowired
	private LockboxService lockboxService;
	
	@Autowired
	Q2CCommonService q2cCommonService;


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArgusQ2CApplication.class, args);
		LOG.debug("Shutdown Springboot . . .");
		System.exit(SpringApplication.exit(context, () -> 0));
	}
	
	@Override
	public void run(String... args) throws Exception {     
		LOG.log(Level.INFO, () -> "start Q2C script execution . . .");

		String[] progArgs = args;
		String filePrefix = progArgs[0];

		// String filePrefix = "REM_LCKBXL1_";

		int deleteFilePrefix = 0; // flag indicating if this process is the one actively running the file prefix
		String folder = "";

		AutomationUtil util = new AutomationUtil();

		Date dateNow = new Date();
		String execTimestamp = util.toDateString(dateNow, environment.getProperty("domain.util.date.format"), environment.getProperty("q2c.timezone"));

		try {
			folder = environment.getProperty("file.prefixes.running");

			// Validate that no other CMD window is processing the same type of 
			// file prefix found in the reference file.
			if (q2cCommonService.checkIfFileIsRunning(folder, filePrefix) == true){
				// CMD with this file prefix is currently running
				LOG.log(Level.INFO, () -> "cancelling execution of Q2C script. . .");
			}
			else {
				deleteFilePrefix = 1;

				// Save file prefix in specified folder to indicate it is now being processed
				String filePath = environment.getProperty("file.prefixes.running") + "\\" + filePrefix;
				util.createFile("This file prefix is currently running", filePath);	

				Calendar calendar = util.dateToCalendar(dateNow, environment.getProperty("q2c.timezone"));
				calendar.add(Calendar.DATE, -1);

				Date yesterday = calendar.getTime();

				String yestDate = util.toDateString(yesterday, environment.getProperty("domain.util.date.only.format"), environment.getProperty("q2c.timezone"));
				String currentDate = util.toDateString(dateNow, environment.getProperty("domain.util.date.only.format"), environment.getProperty("q2c.timezone"));

				InboundMask inboundMask = getInboundMaskDetailsFromRefFile(filePrefix); // Get inbound mask details

				TimeUnit.SECONDS.sleep(10);

				//if test mode is enabled, test.date property will overwrite yestDate and currentDate
				if (Boolean.valueOf(environment.getProperty("test.date.enabled")) == true){
					yestDate = environment.getProperty("test.date");
					currentDate = environment.getProperty("test.date");
				}

				//Call the corresponding service based on what type of process it is 
				if (inboundMask.getInboundProcessType().equalsIgnoreCase("AR Invoices")){
					//Call AR Invoices service
					if(inboundMask.getFilePrefix().toLowerCase().contains("IAR_RETCHK".toLowerCase())){
						LOG.log(Level.INFO, () -> "IAR_RETCHKDV get current date. . .");
						arInvoicesService.processArInvoices(inboundMask.getFilePrefix(),currentDate);
					}
					else
						arInvoicesService.processArInvoices(inboundMask.getFilePrefix(),yestDate);
				}
				else if (inboundMask.getInboundProcessType().equalsIgnoreCase("Employee Payroll")){
					//Call Employee Payroll service
					employeePayrollService.employeePayroll(inboundMask.getFilePrefix(), yestDate);
					// employeePayrollService.employeePayroll(inboundMask.getFilePrefix(), "20230327");
				}
				else if (inboundMask.getInboundProcessType().equalsIgnoreCase("Lockbox")){
					//Call Lockbox service
					lockboxService.lockboxMain(filePrefix);
				}
				else if (inboundMask.getInboundProcessType().equalsIgnoreCase("Bank Paids")){
					bankPaidsOicService.bankPaidsMain(inboundMask.getFilePrefix(), currentDate, execTimestamp);
				}
			}
   
		} catch (Exception e){
			LOG.log(Level.INFO, () -> "exception caught in Q2C script execution. . .");
			LOG.error(e);

			// send email to developers that exception was caught in Q2C
			String body = "<body>Hello, <br><br>";
						
			body += "An error was caught in the Q2C automation of " + filePrefix + ".";

			body += "<br><br>" +
					"Exception Message: " + 
					e.getMessage();

			body += "<br><br>" +
					"Thanks & Regards, <br>" + 
					"Bot</body>";

			q2cCommonService.sendEmail(environment.getProperty("mail.argus.recipients", String[].class), environment.getProperty("mail.argus.cc", String[].class), environment.getProperty("q2c.automation.exception.caught"), body, execTimestamp);

		} finally {
			if (deleteFilePrefix == 1){
				// Delete file prefix in specified folder to indicate it is not processing anymore
				util.deleteFile(folder, 0, filePrefix, false);
			}
			else{
				// Another process is running the existing file prefix so don't delete anything
			}
		}
		
	}

	private InboundMask getInboundMaskDetailsFromRefFile(String filePrefix){
		LOG.log(Level.DEBUG, () -> "start getInboundMaskDetailsFromRefFile method . . .");

		InboundMask inboundMask = new InboundMask();

		try {
			inboundMask.setFilePrefix(filePrefix);
		
			File myFile = new File(environment.getProperty("q2c.inbound.reference.file")); 
			FileInputStream fis = new FileInputStream(myFile);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowNum = 0;
	
			for (Row row : sheet) {
				if (row.getRowNum() != 0){ // don't count first row because it's the header
					if (row.getCell(0).getStringCellValue().equalsIgnoreCase(filePrefix)){
						rowNum = row.getRowNum(); // get the row number of the file prefix being checked
						break;
					}
				}
			}

			if (rowNum != 0){
				Row row = sheet.getRow(rowNum);

				for (Cell cell : row) {
					if (cell.getColumnIndex() == 0){
						inboundMask.setFilePrefix(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 1){
						inboundMask.setInterfaceName(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 2){
						inboundMask.setInboundProcessType(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 3){
						inboundMask.setFileRequirement(cell.getStringCellValue());
					}
				}
			}
	
			wb.close();

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error retrieving details from reference file. . .");
			LOG.error(e);
		}

		return inboundMask;
	}

}
