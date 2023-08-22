package com.albertsons.argus.r2r.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.albertsons.argus.domain.mim.service.MIMService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2RCommonService;
import com.albertsons.argus.r2r.service.R2RMimService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;

@Service
public class R2RMimServiceImpl implements R2RMimService {
	public static final Logger LOG = LogManager.getLogger(R2RMimServiceImpl.class);

	@Autowired
    private Environment environment;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private R2RCommonService r2rCommonService;

	@Autowired
	private PlaywrightAutomationService playwrightService;

	@Autowired
	private MIMService mimService;

	private String execTimestamp;
	
	@Override
	public List<FileDetails> mainMimTask(FileDetails file, String execTimestamp, String execCompleteTime) throws ArgusR2RException{
		LOG.log(Level.INFO, () -> "start mainMimTask method . . .");
		
		this.execTimestamp = execTimestamp;

		List<FileDetails> filesToProcess = new ArrayList<>();

		Browser browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(Boolean.valueOf(environment.getProperty("playwright.domain.browser.not.show.browser"))).setSlowMo(500).setTimeout(Double.valueOf(environment.getProperty("playwright.domain.browser.timeout.value"))));
		BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
		
		try {
			AutomationUtil util = new AutomationUtil();
			util.deleteFile(environment.getProperty(LOG_FOLDER), Integer.parseInt(environment.getProperty("argus.mim.label.days.show")), "", true);

			for (int i = 0; i <= Integer.parseInt(environment.getProperty("mim.retry.attempts")); i++){
				Page page = mimService.navigateMimLogin(browser, browserContext, file.getMimInterface().toLowerCase());

				navigateTransferStatusPage(page, file);

				waitLoadMainPage(page, "Loading Processes...", 300);
				inputLabelDetails(page, file.getFilePrefix());
				waitLoadMainPage(page, "Loading Processes...", 300); 

				List<String> mimFilesSuccess = getResultTableValues(page, file);

				if (mimFilesSuccess.isEmpty() || mimFilesSuccess == null || mimFilesSuccess.size() < 1){ // no file received in dirmon
					LOG.log(Level.INFO, () -> "no " + file.getFilePrefix() + " file received in MIM. . .");

					if (Boolean.parseBoolean(environment.getProperty("screenshot.failure.page")) == true && file.getFileRequirement().equalsIgnoreCase("mandatory")){
						Date dateNow = new Date();
						String currtime = util.toDateString(dateNow, environment.getProperty("domain.util.failure.format"), environment.getProperty("r2r.timezone"));
		
						String imgFilename = "NM-" + file.getFilePrefix() + "-" + currtime + ".png";
						page.screenshot(new Page.ScreenshotOptions()
						.setPath(Paths.get(environment.getProperty("screenshot.failure.path") + "\\" + imgFilename))
						.setFullPage(true));
					}

					if (i == Integer.parseInt(environment.getProperty("mim.retry.attempts"))){ // last attempt still failed
						// Check if the file is mandatory or optional
						if (file.getFileRequirement().equalsIgnoreCase("mandatory")){
							if (file.getFilePrefix().equalsIgnoreCase("JE_GTR_NH_AMT_") || file.getFilePrefix().equalsIgnoreCase("R2R_Warehouse_INV_PPD_") || file.getFilePrefix().equalsIgnoreCase("JE_JIF_SI_")){ // special case due to period calendar
								// Don't do anything
							}
							else{
								// Email that no file has been received at the given window time
								String body = "<body>Hello, <br><br>";
								
								body += "No <strong>" + file.getFilePrefix() + "</strong> file received in MIM within window " + execCompleteTime + ".";
								
								body += "<br><br>This type of file is mandatory.";

								body += "<br><br>" +
										"Thanks & Regards, <br>" + 
										"Bot</body>";

								String[] recipients = environment.getProperty("mail.argus.oracle.financial.it.email", String[].class);

								sendMimEmail(recipients, 
												environment.getProperty("mail.argus.cc", String[].class), 
												environment.getProperty("mail.subject.mim.no.file"), 
												body);  
							}
						}
						else{
							//No need to email if file is optional
						}
					}

				}
				else{
					if (mimFilesSuccess.contains("mim error")){
						if (Boolean.parseBoolean(environment.getProperty("screenshot.failure.page")) == true && file.getFileRequirement().equalsIgnoreCase("mandatory")){
							Date dateNow = new Date();
							String currtime = util.toDateString(dateNow, environment.getProperty("domain.util.failure.format"), environment.getProperty("r2r.timezone"));
			
							String imgFilename = "NM-" + file.getFilePrefix() + "-" + currtime + ".png";
							page.screenshot(new Page.ScreenshotOptions()
							.setPath(Paths.get(environment.getProperty("screenshot.failure.path") + "\\" + imgFilename))
							.setFullPage(true));
						}

						if (i == Integer.parseInt(environment.getProperty("mim.retry.attempts"))){ // last attempt still failed
							// Check if the file is mandatory or optional
							if (file.getFileRequirement().equalsIgnoreCase("mandatory")){
								if (file.getFilePrefix().equalsIgnoreCase("JE_GTR_NH_AMT_") || file.getFilePrefix().equalsIgnoreCase("R2R_Warehouse_INV_PPD_") || file.getFilePrefix().equalsIgnoreCase("JE_JIF_SI_")){ // special case due to period calendar
									// Don't do anything
								}
								else{
									// Email that no file has been received at the given window time
									String body = "<body>Hello, <br><br>";
									
									body += "Unable to retrieve <strong>" + file.getFilePrefix() + "</strong> file after retrying " + Integer.parseInt(environment.getProperty("mim.retry.attempts")) + " more time(s) due to MIM website error that occurred within window " + execCompleteTime + ".";
									
									body += "<br><br>This type of file is mandatory.";
	
									body += "<br><br>" +
											"Thanks & Regards, <br>" + 
											"Bot</body>";
	
									String[] recipients = environment.getProperty("mail.argus.oracle.financial.it.email", String[].class);
	
									sendMimEmail(recipients, 
													environment.getProperty("mail.argus.cc", String[].class), 
													environment.getProperty("mail.subject.mim.no.file"), 
													body);  
								}
							}
							else{
								//No need to email if file is optional
							}
						}
						else{
							//Will proceed to retrying
						}
					}
					else if (mimFilesSuccess.contains("source failure") || mimFilesSuccess.contains("destination failure") || mimFilesSuccess.contains("post exit failure")){
						// email already sent for respective types of failures so no need to send another email
						return null;
					}
					else{
						// Create FileDetails object including filename and add into list of files that will proceed with processing
						for (String filename: mimFilesSuccess){
							if (filename.trim().contains(file.getFilePrefix())){
								FileDetails fileDetails = new FileDetails(filename.trim(), file.getFilePrefix(), file.getInterfaceName(), file.getJobName(), file.getMimInterface(), file.getLabelContact(), file.getFileRequirement(), file.getDataSize(), file.getKafkaConsumeTime(), file.getOicRuntime());
								filesToProcess.add(fileDetails);
							}
							else{
								mimFilesSuccess.remove(filename); // handle possibility of getting the wrong file
							}
						}

						// checkpoint email
						String body = "<body>Hello, <br><br>";

						body += "The following files have been successfully processed in MIM interface: <br>";

						body += "<ul style='list-style-type:disc'>";

						for (String filename: mimFilesSuccess){
							body += "<li>" + filename.trim() + "</li>";
						}

						body += "</ul>";

						body += "<br><br> These files will now be processed in OIC interface."; 

						body += "<br><br>" +
								"Thanks & Regards, <br>" + 
								"Bot</body>";

						sendMimEmail(environment.getProperty("mail.argus.recipients", String[].class), 
										environment.getProperty("mail.argus.cc", String[].class), 
										environment.getProperty("mail.subject.mim.success"), 
										body);    

						// end of MIM interface
						LOG.log(Level.INFO, () -> "moving to OIC interface . . .");

						return filesToProcess;
					}
					
				}

				//page.close();
			}			

		} catch (Exception e) {
            LOG.log(Level.INFO, () -> "error in MIM interface. . .");
			LOG.error(e);
		} finally {
			browserContext.close();
			browser.close();
		}

		return null;
	}

	@Override
	public FileDetails getFileDetailsFromRefFile(String filePrefix){
		LOG.log(Level.DEBUG, () -> "start getFileDetailsFromRefFile method . . .");

		FileDetails fileDetails = new FileDetails();

		try {
			fileDetails.setFilePrefix(filePrefix);
		
			File myFile = new File(environment.getProperty("r2r.reference.file")); 
			FileInputStream fis = new FileInputStream(myFile);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowNum = 0;
	
			for (Row row : sheet) {
				if (row.getRowNum() != 0){ // don't count first row because it's the header
					if (row.getCell(1).getStringCellValue().equalsIgnoreCase(filePrefix)){
						rowNum = row.getRowNum(); // get the row number of the file prefix being checked
						break;
					}
				}
			}

			if (rowNum != 0){
				Row row = sheet.getRow(rowNum);

				for (Cell cell : row) {
					
					// filename will not be set yet until after MIM processing
		
					if (cell.getColumnIndex() == 2){
						fileDetails.setInterfaceName(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 3){ // job name can be null
						if (!cell.getStringCellValue().equals("") && cell.getStringCellValue() != null){
							fileDetails.setJobName(cell.getStringCellValue());
						}
						else{
							fileDetails.setJobName("");
						}
					}
					else if (cell.getColumnIndex() == 4){
						fileDetails.setMimInterface(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 5){
						fileDetails.setLabelContact(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 6){
						fileDetails.setFileRequirement(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 7){
						fileDetails.setDataSize(cell.getStringCellValue());
					}
					else if (cell.getColumnIndex() == 8){
						fileDetails.setKafkaConsumeTime((int) cell.getNumericCellValue());
					}
					else if (cell.getColumnIndex() == 9){
						fileDetails.setOicRuntime((int) cell.getNumericCellValue());
					}

				}
			}
	
			wb.close();

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error retrieving details from reference file. . .");
			LOG.error(e);
		}

		return fileDetails;
	}

	@Override
	public void inputLabelDetails(Page mainPage, String labelName) throws ArgusR2RException {
		LOG.log(Level.DEBUG, () -> "start inputLabelDetails method . . .");
		
		try {
			List<ElementHandle> selectorColList;
			playwrightService.pageClick(mainPage, "", "", "text=Edit Unsaved Filter");
			TimeUnit.SECONDS.sleep(1);
			playwrightService.pageFill(mainPage, "", "", "id=destpathCmpId", "%" + labelName + "%");
			TimeUnit.SECONDS.sleep(1);
			playwrightService.pageClick(mainPage, "", "", "id=x-form-el-daystoshowcombo >> img");
			TimeUnit.SECONDS.sleep(1);
			selectorColList = mainPage.querySelectorAll("div");
			
			for (ElementHandle elemHandle: selectorColList) {
				if (elemHandle.innerText().length() >= 1 && elemHandle.innerText().length() <= 2) {
					if (elemHandle.getAttribute("class").contains("x-combo-list-item") && elemHandle.innerText().contains(environment.getProperty("argus.mim.label.days.show"))) {
						elemHandle.click();
					}
				}
			}
			
			mainPage.click("text=Apply");
			TimeUnit.SECONDS.sleep(3);
			
			LOG.log(Level.DEBUG, () -> "end inputLabelDetails method . . .");

		} catch (PlaywrightException | InterruptedException e) {
			throw new ArgusR2RException(e.getMessage());
		}

	}
	
	@Override
	public void navigateTransferStatusPage(Page mainPage, FileDetails fileDetails) {
		LOG.log(Level.DEBUG, () -> "start navigateTransferStatusPage method . . .");
		
		mainPage.navigate(environment.getProperty("playwright.uri.mim." + fileDetails.getMimInterface().toLowerCase() + ".login") + environment.getProperty("playwright.uri.mim." + fileDetails.getMimInterface().toLowerCase() + ".monitoring"));
		
		LOG.log(Level.DEBUG, () -> "end navigateTransferStatusPage method . . .");
	}
	
	@Override
	public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit) throws ArgusR2RException {
		LOG.log(Level.DEBUG, () -> "start waitLoadMainPage method . . .");
		
		long lWaitCtr = 0;
		try {
			ElementHandle elemHandle = mainPage.querySelector("text=" + waitElement);
			while (elemHandle != null) {
				lWaitCtr += 1;
				elemHandle = mainPage.querySelector("text=" + waitElement);
				TimeUnit.SECONDS.sleep(1);
				if (lWaitCtr > waitLimit) {
					LOG.log(Level.INFO, () -> "waitLoadMainPage Wait Limit Exceeded. . .");
					throw new ArgusR2RException("Element: text=" + waitElement);
				}
			}
			
		} catch (PlaywrightException | InterruptedException e) {
			throw new ArgusR2RException(e.getMessage());
		}
		
		LOG.log(Level.DEBUG, () -> "end waitLoadMainPage method . . .");
	}

	@Override
	public List<String> getResultTableValues(Page page,  FileDetails file) {
		LOG.log(Level.DEBUG, () -> "start getResultTableValues method . . .");
		
		int iconStart, iconEnd, charToAscii, ctr;
		String progressIcon = "", description, processId, startTime, sourcePath, destPath, labelState, filePath, issue = "";
		char charAt;

		List<String> columns = new ArrayList<>();
		List<String> successList = new ArrayList<>();
		List<String> sourceFailList = new ArrayList<>();
		List<String> destFailList = new ArrayList<>();
		List<String> postExitFailList = new ArrayList<>();
		List<String> unidentifiedFailList = new ArrayList<>();
		
		try {
			ElementHandle objTable = page.querySelector("id=ext-gen188");
			
			if (objTable != null) {
				for (ElementHandle oCrElemTR: objTable.querySelectorAll("tr")) {
					columns.clear();
					ctr = 0;
					iconStart = oCrElemTR.innerHTML().indexOf("td-statusimg ") + 13;
					iconEnd = oCrElemTR.innerHTML().indexOf("\" style=");
					
					if (iconEnd - iconStart > 0) {
						progressIcon = oCrElemTR.innerHTML().substring(iconStart,iconEnd);
					}	
					
					for (ElementHandle oCrElemTD: oCrElemTR.querySelectorAll("td")) {
						ctr += 1;

						if (ctr == 2 | ctr == 4 | ctr == 5 | ctr == 7 | ctr == 8 | ctr == 9) {
							charAt = oCrElemTD.innerText().charAt(0);
							charToAscii = charAt;
							if (charToAscii == 160) {
								columns.add("");
							}
							else {
								columns.add(oCrElemTD.innerText());
							}
						}
					}

					if (columns.size() > 0) {
					
						description = columns.get(0);
						processId = columns.get(1);
						startTime = columns.get(2);
						sourcePath = columns.get(3);
						destPath = columns.get(4);
						labelState = columns.get(5);

						String folder = environment.getProperty(LOG_FOLDER);
						String filename = destPath.substring(destPath.lastIndexOf("/") + 1, destPath.length());
						String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
						
						if (r2rCommonService.checkIfFileIsRunning(folder, filenameNoExtension) == true){
							// do not proceed with automation for this file because it is currently ongoing R2R automation
						}
						else{
							// save file as processing
							AutomationUtil util = new AutomationUtil();
							filePath = environment.getProperty(LOG_FOLDER) + "\\" + filenameNoExtension;
							util.createFile(description + " " + processId + " " + startTime + " " + labelState, filePath);
							
							switch (progressIcon) {
								case "success":
								case "successsummary":
									successList.add(filename);
									break;
								case "error":
								case "errorsummary":
									issue = checkUnsuccessType(page, oCrElemTR, description);
									closeUnsuccessTab(page);
									break;
								case "warning":
								case "warningsummary":
									issue = checkUnsuccessType(page, oCrElemTR, description);
									closeUnsuccessTab(page);
									break;
								case "fixit":
								case "fixitsummary":
									issue = checkUnsuccessType(page, oCrElemTR, description);
									closeUnsuccessTab(page);
									break;
								default:
									break;
							}

							// add unsuccessful files into respective lists
							if (!issue.equalsIgnoreCase("") || issue != null || !issue.isEmpty()){
											
								if (issue.equalsIgnoreCase("Source")){
									sourceFailList.add(filename);
								}
								else if (issue.equalsIgnoreCase("Destination")){
									destFailList.add(filename);
								}
								else if (issue.equalsIgnoreCase("Post Exit")){
									postExitFailList.add(filename);
								}
								else if (issue.equalsIgnoreCase("Unidentified")){
									unidentifiedFailList.add(filename);
								}

							}
							
						}
						
					}
					else {
						LOG.log(Level.DEBUG, () -> "unable to get row info. . .");
					}
				}

				// Email unsuccessful files
				if (sourceFailList.size() > 0){
					String body = "<body>Hello, <br><br>";
	
					body += "The following files were unsuccessfully processed in MIM due to Source failure: <br>";
					
					body += "<ul style='list-style-type:disc'>";

						for (String filename: sourceFailList){
							body += "<li>" + filename + "</li>";
						}
	
					body += "</ul>";

					body += "<br><br>" +
						"Thanks & Regards, <br>" + 
						"Bot</body>";

					String[] recipients = {file.getLabelContact(), environment.getProperty("mail.argus.recipients")};

					sendMimEmail(recipients, 
						environment.getProperty("mail.argus.cc", String[].class), 
						environment.getProperty("mail.subject.mim.source.failure"), body);

					sourceFailList.add("source failure"); //serves as source failure flag
					return sourceFailList;
				}
				
				if (destFailList.size() > 0){	
					String body = "<body>Hello, <br><br>";
	
					body += "The following files were unsuccessfully processed in MIM due to Destination failure: <br>";
					
					body += "<ul style='list-style-type:disc'>";

						for (String filename: destFailList){
							body += "<li>" + filename + "</li>";
						}
	
					body += "</ul>";

					body += "<br><br>" +
						"Thanks & Regards, <br>" + 
						"Bot</body>";

					sendMimEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
									environment.getProperty("mail.argus.cc", String[].class), 
									environment.getProperty("mail.subject.mim.dest.failure"), body);
					
					destFailList.add("destination failure"); //serves as destination failure flag
					return destFailList;
				}

				if (postExitFailList.size() > 0){
					String body = "<body>Hello, <br><br>";
	
					body += "The following files were unsuccessfully processed in MIM due to Post Exit failure: <br>";
					
					body += "<ul style='list-style-type:disc'>";

						for (String filename: postExitFailList){
							body += "<li>" + filename + "</li>";
						}
	
					body += "</ul>";

					body += "<br><br>" +
						"Thanks & Regards, <br>" + 
						"Bot</body>";

						sendMimEmail(environment.getProperty("mail.argus.edis.email", String[].class), 
						environment.getProperty("mail.argus.cc", String[].class), 
						environment.getProperty("mail.subject.mim.post.exit.failure"), body);

					postExitFailList.add("post exit failure"); //serves as post exit failure flag
					return postExitFailList;
				}

				if (objTable.innerHTML().toLowerCase().contains("error retrieving process summary data") || objTable.innerHTML().toLowerCase().contains("transaction aborted")){					
					successList.add("mim error"); //serves as mim error flag
				}

				return successList; // Proceed with next steps for successful files

			}
			else {
				LOG.log(Level.DEBUG, () -> "no results in MIM. . .");
				return null;
			}
	
		} catch (Exception e) {
			LOG.log(Level.INFO, () -> "error getting table result values. . .");
			LOG.error(e);
		}
		
		return null;
		
	}

	@Override
	public String checkUnsuccessType(Page page, ElementHandle row, String label){
		LOG.log(Level.DEBUG, () -> "start checkUnsuccessType method. . .");

		String issue = "";

		try {
			row.dblclick();

			TimeUnit.SECONDS.sleep(5);

			for (ElementHandle rowStep: page.querySelectorAll("div.x-tree-node-el.x-unselectable.x-tree-node-collapsed")) {

				if (rowStep != null) {
					for (ElementHandle oCrElemTR: rowStep.querySelectorAll("img")) {
						String imgResult = oCrElemTR.getAttribute("class");

						if (imgResult.toLowerCase().contains("activityerror")){
							for (ElementHandle rowInner: rowStep.querySelectorAll("a.x-tree-node-anchor")) {
								for (ElementHandle rowText: rowInner.querySelectorAll("span")) {

									if (rowText.innerHTML().toLowerCase().contains("send")){
										issue = "Source";
										return issue;
									}
									else if (rowText.innerHTML().toLowerCase().contains("receive")){
										issue = "Destination";
										return issue;
									}
									else if (rowText.innerHTML().toLowerCase().contains("post rec exit")){
										issue = "Post Exit";
										return issue;
									}

								}

							}

						}
					}
					
				}
			}

			return "Unidentified"; //default if specified issue is not the commonly encountered type

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error checking the type of unsuccessful label. . .");
			LOG.error(e);
		}

		return null;
		
	}

	@Override
	public void closeUnsuccessTab(Page page){
		LOG.log(Level.DEBUG, () -> "start closeUnsuccessTab method. . .");

		ElementHandle panel = page.querySelector("id=ext-gen15");

		for (ElementHandle tab: panel.querySelectorAll("li.x-tab-strip-closable.x-tab-with-icon.x-tab-strip-active")) {
			for (ElementHandle closeButton: tab.querySelectorAll("a.x-tab-strip-close")) {
				closeButton.click(); // close tab
			}
		}

		LOG.log(Level.DEBUG, () -> "end closeUnsuccessTab method. . .");
	}

	@Override
	public void sendMimEmail(String[] recipients, String[] cc, String subject, String body) {
		LOG.log(Level.DEBUG, () -> "start sendMimEmail method. . .");

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"), recipients, cc,
                    subject + " - " + execTimestamp,
                    body, NORMAL_PRIORITY,true);

        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending mim email. . .");
            LOG.error(e);
        }
		
		LOG.log(Level.DEBUG, () -> "end sendMimEmail method. . .");
	}

}
