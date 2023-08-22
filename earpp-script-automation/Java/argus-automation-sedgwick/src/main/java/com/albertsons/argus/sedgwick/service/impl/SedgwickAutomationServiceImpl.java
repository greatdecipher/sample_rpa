package com.albertsons.argus.sedgwick.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.sedgwick.exception.ArgusSedgwickException;
import com.albertsons.argus.sedgwick.service.SedgwickAutomationService;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;


@Service
public class SedgwickAutomationServiceImpl implements SedgwickAutomationService {
    public static final Logger LOG = LogManager.getLogger(SedgwickAutomationServiceImpl.class);
	
	@Autowired
    private Environment environ;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private MetricsWebService metricsWebService;

	private Browser browser;
	private BrowserContext context;
	private Page page;

	@Override
	public BrowserContext getBrowserContext(Browser browser) throws PlaywrightException{
		BrowserContext context = null;

		try {
			context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true).setAcceptDownloads(true));
			context.setDefaultTimeout(240000);
		} catch (Exception e) {
			LOG.error("Error launching browser context");
		}

		return context;
	}

	@Override
	public Page getPage(BrowserContext context) throws PlaywrightException{
		Page page = null;

		try {
			page = context.newPage();
		} catch (Exception e) {
			LOG.error("Error launching browser page");
		}

		return page;
	}

	@Override
	public Boolean loginPeoplesoft(Page page) {
		Boolean isLoginComplete = false;
		page.locator("id=userid").fill(environ.getProperty("encrypted.sedgwick.property.username"));
		page.locator("id=pwd").fill(environ.getProperty("encrypted.sedgwick.property.password"));
		page.locator("input[name=Submit]").click();
		delay(Integer.parseInt(environ.getProperty("delay.medium")));
		page.waitForLoadState(LoadState.LOAD);
	
		Locator loc = page.locator("span:has-text('Your User ID and/or Password are invalid.')");
		if (loc.count() > 0) {
			LOG.info("Invalid User ID or Password in Peoplesoft. . .");
			try {
				emailService.sendSimpleMessage(environ.getProperty("mail.sedgwick.from"), 
					environ.getProperty("mail.sedgwick.from.alias"), 
					environ.getProperty("mail.sedgwick.recipients",String[].class), 
					environ.getProperty("mail.sedgwick.cc", String[].class), 
					"Sedgwick Emails Processing - Incorrect Password", 
					"Unable to login to Peoplesoft site, Please check and update password. . .", 
					HIGH_PRIORITY, 
					false);
			} catch (Exception e) {
				LOG.info("Failed to send email for invalid User ID or Password. . .");
			}
		}
		else {
			Frame frm = page.frame("TargetContent");
			Locator loc2 = frm.locator("input[id=InputKeys_EMPLID]");
			if (loc2.count() > 0) {
				isLoginComplete = true;
			}
		}

		return isLoginComplete;
	}

	
	@Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
    public void closePlaywright(){
        LOG.log(Level.DEBUG, () -> "start closePlaywright method. . .");

        browser.close();
		context.close();
		
        LOG.log(Level.DEBUG, () -> "end closePlaywright method. . .");
    }

	@Override
	public boolean checkElementIsVisible(Page page, String selector, String attrib){
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		boolean bool = false;

		if (page.content().contains(selector)) {
			bool = page.waitForSelector(attrib + "=" + selector).isVisible();
		}
		
		LOG.log(Level.DEBUG, () -> "End checkElementIsVisible method . . .");
		return bool;

	}

	@Override
	public String runVBScript(String arg) throws IOException{
		LOG.debug("runVBScript method started...");
		Process p = Runtime.getRuntime().exec(arg);

		String text = new BufferedReader(
      	new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
		
		LOG.debug("runVBScript method ended...");
		return text;
	}

	@Override
	public void deleteSedgwickFilesInFolder(String sFolder) {
		LOG.log(Level.DEBUG, () -> "Start deleteSedgwickFilesInFolder method, deleting files in "+ sFolder +" . . .");
		File FolderPath = new File(sFolder);
		if (FolderPath.isDirectory()) {
			try {
				for (File fName : FolderPath.listFiles()) {
					if (fName.isDirectory() | fName.getName().toUpperCase().contains("GETEMAIL")) {
						continue;
					} else {
						fName.delete();
					}
				}
			} catch (Exception e) {
				LOG.log(Level.DEBUG, () -> "deleteSedgwickFilesInFolder method failed . . .");
			}

		}

		LOG.log(Level.DEBUG, () -> "End deleteWicFilesInFolder method . . .");
	}

	@Override
	public Boolean sendEmailToRequestor(String recipientEmail, String mailSubject, String attachmentPath) {
		LOG.debug("sendEmailToRequestor method started...");
		Boolean isEmailSent = false;
		try {
			String[] strArray = new String[] {recipientEmail}; 
			String mailBody = "<html>" +
				"<body>" +
				"<p>Good Day,</p>" +
				"<br>" +
				"<p>Here are the wages you've requested.</p>" +
				"<br>" +
				"<p>Regards,</p>" +
				"<p>ESCWork Verification <ESCWork.Verification@albertsons.com></p>" +
				"</body>" +
				"</html>";
			emailService.sendMessageWithAttachment(environ.getProperty("mail.sedgwick.from"), 
				environ.getProperty("mail.sedgwick.from.alias"), 
				strArray,
				environ.getProperty("mail.sedgwick.cc", String[].class),
				mailSubject, 
				mailBody, 
				attachmentPath, 
				HIGH_PRIORITY, 
				true);

			LOG.info("Email to sent to " + recipientEmail);
			isEmailSent = true;
		} catch (Exception e) {
			LOG.info("Error in sendEmailToRequestor method. . .");
			LOG.error(e);
		}
		LOG.debug("sendEmailToRequestor method ended...");
		return isEmailSent;
	}

	@Override
	public void processSedgwickEmails(String attachmentFilePath, Page page, String automationRunId) {
		LOG.debug("processSedgwickEmails method started...");
		FileInputStream file = null;
        XSSFSheet sheet = null;
		List<String> rowValues = new ArrayList<>();
		String empId, dateStart, dateEnd, empName, mailSubject = "", mailRecipient;
		List<String> extractReturnList = new ArrayList<>();
		AutomationUtil util = new AutomationUtil();

		try {
			LOG.info("Opening Sedgwick input file for processing...");
			file = new FileInputStream(new File(attachmentFilePath));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheet(environ.getProperty("sedgwick.excel.sheet.name"));
			DecimalFormat decimalFormat = new DecimalFormat("#");

            for(Row r : sheet) {
                if (r.getRowNum() == 0) {
                    continue;
                }
				deleteSedgwickFilesInFolder(environ.getProperty("sedgwick.peoplesoft.export.folder"));
				rowValues.clear();
				for (Cell cell : r) {
					switch (cell.getCellType()) {
						case STRING:  
							rowValues.add(cell.getRichStringCellValue().getString());
						break;
						case NUMERIC: 
							if(DateUtil.isCellDateFormatted(cell)){
								String dateString = util.toDateString(cell.getDateCellValue(), "MM/dd/yyyy", "Asia/Singapore");
								//String dateString = cell.getDateCellValue().toString();
								rowValues.add(dateString);
							}else{
								rowValues.add(decimalFormat.format(cell.getNumericCellValue()));
							}
						break;
						case BOOLEAN: 

							break;
						case FORMULA: 
						
						break;
						default: rowValues.add(" ");
					}
				}
				
				if (rowValues.get(0) == " ") {
					continue;
				}
				
				empName = rowValues.get(0);
				empId = rowValues.get(1);
				dateStart = rowValues.get(2);
                dateEnd = rowValues.get(3);
				mailSubject = rowValues.get(4);
				
				if (mailSubject == " ") {
					mailSubject = empName + "-" + empId;
				}
				
				mailRecipient = rowValues.get(5);
			
				extractReturnList = exportDataFromPeoplesoft(page, empId, dateStart, dateEnd, empName);
				if (extractReturnList.size() > 0) {
					runVBScript(environ.getProperty("sedgwick.exe.name") + " " + 
						environ.getProperty("sedgwick.outlook.script.path") + 
						environ.getProperty("sedgwick.script.runmacro.name"));
					
					sendEmailToRequestor(mailRecipient, mailSubject, extractReturnList.get(1));
					r.createCell(6).setCellValue("Email sent to Requestor");
					try { 
						ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");    
						if (bo2.getResult().contains("SUCCESS")) {
							LOG.info("Metrics transaction increment logged. . .");
						}
						else {
							LOG.info("Metrics transaction failed. . .");
						}
					} catch (Exception e) {
						LOG.info("Failed metrics increment transaction api call. . .");
						LOG.error(e);
					}
				}
				else {
					r.createCell(6).setCellValue("Failed to extract user data.");
				}
			
            }

			file.close();
			LOG.debug("Writing update to input file...");
			FileOutputStream outputStream = new FileOutputStream(attachmentFilePath);
            workbook.write(outputStream);
            
            workbook.close();
            outputStream.close();

			emailService.sendMessageWithAttachment(environ.getProperty("mail.sedgwick.from"), 
				environ.getProperty("mail.sedgwick.from.alias"), 
				environ.getProperty("mail.sedgwick.recipients", String[].class),
				environ.getProperty("mail.sedgwick.cc", String[].class),
				"Sedgwick Emails Processing Complete", 
				SedgwickTeamMailBody(), 
				attachmentFilePath, 
				HIGH_PRIORITY, 
				true);

		} catch (Exception e) {
			LOG.error("Error in processSedgwickEmails. . .");
			LOG.error(e);
		}
		LOG.debug("processSedgwickEmails method ended...");
	}

	@Override
	public List<String> exportDataFromPeoplesoft(Page page, String empId, String startDate, String endDate, String empName) {
		LOG.debug("exportDataFromPeoplesoft method started...");
		List<String> returnList = new ArrayList<>();
		String extractStatus = "No"; 
		String extractFilename = empName + "-" + empId + ".xls";
		LOG.info("Processing export data for EmpName:	" + empName);
		LOG.info("Emp ID:	" + empId);
		LOG.info("From Date:	" + startDate);
		LOG.info("To Date:	" + endDate);
		try {
	
			Frame frame = page.frame("TargetContent");
			frame.locator("input[id=InputKeys_EMPLID]").fill(empId);
			frame.locator("input[id='InputKeys_bind2']").fill(startDate);
			frame.locator("input[id='InputKeys_bind3']").fill(endDate);
			Download download = page.waitForDownload(() -> {
				frame.locator("input[value='View Results']").click();	
			});
			download.saveAs(Paths.get(environ.getProperty("sedgwick.peoplesoft.export.folder") + extractFilename));
			
			File dir = new File(environ.getProperty("sedgwick.peoplesoft.export.folder"));
            File[] files = dir.listFiles();
			if (files.length > 0) {
				extractStatus = "Yes";
				returnList.add(extractStatus);
				returnList.add(environ.getProperty("sedgwick.peoplesoft.export.folder") + extractFilename);
				LOG.info("Peoplesoft data exported to " + returnList.get(1) + ". . .");
			}
			
		} catch (Exception e) {
			LOG.error("Error in exportDataFromPeoplesoft method. . .");
			LOG.error(e);
		}
		LOG.debug("exportDataFromPeoplesoft method ended...");
		return returnList;
	}

	private String SedgwickTeamMailBody() {
		String body = "<html>" +
				"<body>" +
				"<p>Good Day,</p>" +
				"<br>" +
				"<p>Bot has completed execution of Sedgwick Emails processing, please see attached updated file.</p>" +
				"<br>" +
				"<p>Thanks,</p>" +
				"<p>This is an automated notification - please DO NOT REPLY to this email.</p>" +
				"</body>" +
				"</html>";
		return body;
	}
}