package com.albertsons.argus.q2c.service.impl;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.dataq2c.bo.oracle.custom.OutboundProcessDetailsBO;
import com.albertsons.argus.dataq2c.repo.oracle.GetOracleTablesCustomRepo;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.q2c.exception.ArgusQ2cException;
import com.albertsons.argus.q2c.exception.ArgusQ2cRuntimeException;
import com.albertsons.argus.q2c.service.Q2cAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;

@Service
public class Q2cAutomationServiceImpl implements Q2cAutomationService {
    public static final Logger LOG = LogManager.getLogger(Q2cAutomationServiceImpl.class);
	
	@Autowired
    private Environment environ;

	@Autowired
	private EmailService emailService;
	
	@Autowired
    private GetOracleTablesCustomRepo GetOracleTablesCustomRepo;

	@Override
	public void outboundCustBalanceProcess(){
		try {
			LOG.info("Method outboundCustBalanceProcess start. . .");
			List<OutboundProcessDetailsBO> outboundCustBalBO = GetOracleTablesCustomRepo.getCustBalProcessDetailsBOs();
			List<String> outboundErrList = new ArrayList<>();

			for (OutboundProcessDetailsBO outboundItem : outboundCustBalBO) {
				if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
					outboundErrList.add(outboundItem.getInstance_Id().toString());
				}
			}

			if (outboundErrList.size() > 0) {
				outboundCustBalBO.clear();
				outboundErrList.clear();
				TimeUnit.HOURS.sleep(1);
				outboundCustBalBO = GetOracleTablesCustomRepo.getCustBalProcessDetailsBOs();
				for (OutboundProcessDetailsBO outboundItem : outboundCustBalBO) {
					if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
						outboundErrList.add(outboundItem.getInstance_Id().toString());
					}
				}
				
				if (outboundErrList.size() > 0) {
					String mailBody = buildMailBody(outboundErrList, "Customer Balance");
					sendOutboundErrorEmail("Q2C - CUSTOMER BALANCE - ERROR FOUND", mailBody, true);
				}

			}
			LOG.info("Method outboundCustBalanceProcess end. . .");
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
			
	}

	@Override
	public void outboundCustStatementProcess(){
		try {
			LOG.info("Method outboundCustStatementProcess start. . .");
			List<OutboundProcessDetailsBO> outboundCustStatementBO = GetOracleTablesCustomRepo.getCustStatementsProcessDetailsBOs();
			List<String> outboundErrList = new ArrayList<>();

			for (OutboundProcessDetailsBO outboundItem : outboundCustStatementBO) {
				if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
					outboundErrList.add(outboundItem.getInstance_Id().toString());
				}
			}
	
			if (outboundErrList.size() > 0) {
				outboundCustStatementBO.clear();
				outboundErrList.clear();
				TimeUnit.HOURS.sleep(1);
				outboundCustStatementBO = GetOracleTablesCustomRepo.getCustStatementsProcessDetailsBOs();
				for (OutboundProcessDetailsBO outboundItem : outboundCustStatementBO) {
					if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
						outboundErrList.add(outboundItem.getInstance_Id().toString());
					}
				}

				if (outboundErrList.size() > 0) {
					String mailBody = buildMailBody(outboundErrList, "Customer Statement");
					sendOutboundErrorEmail("Q2C - CUSTOMER STATEMENT - ERROR FOUND", mailBody, true);
				}

			}
			LOG.info("Method outboundCustStatementProcess end. . .");
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
			
	}


	@Override
	public void outboundPrgProcess() {
		try {
			List<OutboundProcessDetailsBO> outboundPrgExtractsBO = GetOracleTablesCustomRepo.getPrgExtractsProcessDetailsBOs();
			List<String> outboundErrList = new ArrayList<>();

			for (OutboundProcessDetailsBO outboundItem : outboundPrgExtractsBO) {
				if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
					outboundErrList.add(outboundItem.getInstance_Id().toString());
				}
			}
			
			if (outboundErrList.size() > 0) {
				outboundPrgExtractsBO.clear();
				outboundErrList.clear();
				TimeUnit.HOURS.sleep(1);
				outboundPrgExtractsBO = GetOracleTablesCustomRepo.getPrgExtractsProcessDetailsBOs();
				for (OutboundProcessDetailsBO outboundItem2 : outboundPrgExtractsBO) {
					if (outboundItem2.getStatus().toUpperCase().contains("ERROR")) {
						outboundErrList.add(outboundItem2.getInstance_Id().toString());
					}
				}
				
				if (outboundErrList.size() > 0) {
					String mailBody = buildMailBody(outboundErrList, "PRG");
					sendOutboundErrorEmail("Q2C - PRG - ERROR FOUND", mailBody, true);
				}

			}
		} catch (Exception e) {
			LOG.error("ERROR in outboundPrgProcess method. . .");
			LOG.error(e);
		}
		
	}

	@Override
	public void outboundStaleDatedProcess() {
		try {
			List<OutboundProcessDetailsBO> outboundSDChecksBO = GetOracleTablesCustomRepo.getSDChecksProcessDetailsBOs();
			List<String> outboundErrList = new ArrayList<>();

			for (OutboundProcessDetailsBO outboundItem : outboundSDChecksBO) {
				if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
					outboundErrList.add(outboundItem.getInstance_Id().toString());
				}
			}
			
			if (outboundErrList.size() > 0) {
				outboundSDChecksBO.clear();
				outboundErrList.clear();
				TimeUnit.HOURS.sleep(1);
				outboundSDChecksBO = GetOracleTablesCustomRepo.getSDChecksProcessDetailsBOs();
				for (OutboundProcessDetailsBO outboundItem2 : outboundSDChecksBO) {
					if (outboundItem2.getStatus().toUpperCase().contains("ERROR")) {
						outboundErrList.add(outboundItem2.getInstance_Id().toString());
					}
				}
				
				if (outboundErrList.size() > 0) {
					String mailBody = buildMailBody(outboundErrList, "Stale Dated Checks");
					sendOutboundErrorEmail("Q2C - STALE DATED CHECK - ERROR FOUND", mailBody, true);
				}

			}
		} catch (Exception e) {
			LOG.error("ERROR in outboundStaleDatedProcess method. . .");
			LOG.error(e);
		}
	}

	@Override
	public Boolean validateOipTransLabel(Page mainPage) {
		boolean isOIPFound = false;
		try {
			ElementHandle objTable = mainPage.querySelector("id=ext-gen188");
			
			if (objTable != null) {
				List<ElementHandle> obj = objTable.querySelectorAll("tr");
				if (obj.isEmpty()) {
					LOG.info("OIPTrans label not found. . .");
					return false;
				}
				else {
					for (ElementHandle oCrElemTR: obj) {
						if (oCrElemTR.innerHTML().toUpperCase().contains("OIPTRANS")) {
							isOIPFound = true;	
						}
					}
					if (isOIPFound) {
						return true;
					}
				}	
			}
			else {
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return isOIPFound;
	}

	@Override
	public String filterOIPLabMIM(Page mainPage) {
		String labelName = "";
		if (mainPage != null) {
			
			try {
			
				mainPage.navigate(environ.getProperty("playwright.uri.mim.prd3.login") + 
					environ.getProperty("playwright.uri.mim.prd3.monitoring"));
				
				mainPage.click("text=Edit Unsaved Filter");
				delay(1);
				mainPage.fill("id=descriptionCmpId",environ.getProperty("argus.mim.label.oip"));
				delay(1);
				mainPage.click("id=x-form-el-daystoshowcombo >> img");
				delay(1);
				mainPage.click("div.x-combo-list-item:has-text('" + environ.getProperty("argus.mim.label.days.show") + "')");
	
				mainPage.check("id=completedCmpId");
				mainPage.check("id=failedCmpId");
				mainPage.check("id=warningCmpId");
				mainPage.check("id=inprogressCmpId");
				mainPage.check("id=fixitCmpId");
				mainPage.click("text=Apply");
				delay(3);
				waitLoadMainPage(mainPage, "Loading Processes...", 300);
				delay(10);
				ElementHandle objTable = mainPage.querySelector("id=ext-gen188");
			
				if (objTable != null) {
					List<ElementHandle> obj = objTable.querySelectorAll("tr");
					if (obj.isEmpty()) {
						LOG.info("OIPTrans label not found. . .");
						labelName = "";
					}
					else {
						for (ElementHandle oCrElemTR: obj) {
							if (oCrElemTR.innerHTML().toUpperCase().contains("OIPTRANS")) {
								ElementHandle oCrElemTD = oCrElemTR.querySelectorAll("td").get(1);
								labelName = oCrElemTD.innerText();	
								break;
							}
						}
					}	
				}
				else {
					labelName = "";
				}
			
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			labelName = "";
		}
		return labelName;
	}

	@Override
	public List<String> filterPospayLabelsMIM(Page mainPage, List<String> labelList) {
		String labelStatus = "";
		List<String> errList = new ArrayList<>();
		int iIconStart, iIconEnd;
		if (mainPage != null) {
			
			try {
				for (String label : labelList) {
					mainPage.navigate(environ.getProperty("playwright.uri.mim.prd3.login") + 
					environ.getProperty("playwright.uri.mim.prd3.monitoring"));
				
					mainPage.click("text=Edit Unsaved Filter");
					delay(1);
					mainPage.fill("id=descriptionCmpId",label);
					delay(1);
					mainPage.click("id=x-form-el-daystoshowcombo >> img");
					delay(1);
					mainPage.click("div.x-combo-list-item:has-text('" + environ.getProperty("argus.mim.label.days.show") + "')");
		
					mainPage.check("id=completedCmpId");
					mainPage.check("id=failedCmpId");
					mainPage.check("id=warningCmpId");
					mainPage.check("id=inprogressCmpId");
					mainPage.check("id=fixitCmpId");
					mainPage.click("text=Apply");
					delay(3);
					waitLoadMainPage(mainPage, "Loading Processes...", 300);
					delay(10);
					ElementHandle objTable = mainPage.querySelector("id=ext-gen188");
				
					if (objTable != null) {
						List<ElementHandle> obj = objTable.querySelectorAll("tr");
						if (obj.isEmpty()) {
							LOG.info(label + " label not found. . .");
							errList.add(label + "|Not Found");
						}
						else {
							for (ElementHandle oCrElemTR: obj) {
								if (oCrElemTR.innerHTML().toUpperCase().contains(label)) {
									//List<ElementHandle> oCrElemTD = oCrElemTR.querySelectorAll("td");
									//labelName = oCrElemTD.get(1).innerText();
									iIconStart = oCrElemTR.innerHTML().indexOf("td-statusimg ") + 13;
									iIconEnd = oCrElemTR.innerHTML().indexOf("\" style=");
									labelStatus = oCrElemTR.innerHTML().substring(iIconStart,iIconEnd);
									if (!labelStatus.toUpperCase().contains("SUCCESS")) {
										if (labelStatus.toUpperCase().contains("ERROR")) {
											labelStatus = "ERROR";
										}
										else if (labelStatus.toUpperCase().contains("WARNING")) {
											labelStatus = "WARNING";
										}
										else if (labelStatus.toUpperCase().contains("FIXIT")) {
											labelStatus = "FIXIT";
										}
										else if (labelStatus.toUpperCase().contains("PROGRESS")) {
											labelStatus = "IN PROGRESS";
										}
										errList.add(label + "|" + labelStatus);
									}

									break;
								}
							}
						} 	
					}
					else {
						errList.add(label + "|Unable to get table data");
					}
				}

			} catch (Exception e) {
				LOG.error("Error in filterPospayLabelsMIM method. . .");
				LOG.error(e);
			}
			mainPage.close();
		}
		else {
			LOG.info("MIM Page not loaded. . .");
		}
		return errList;
	}

	@Override
	public void outboundPosPayVoidsProcess(List<String> labelList) {
		try {
			List<OutboundProcessDetailsBO> outboundPospayVoidsBO = GetOracleTablesCustomRepo.getPosPayAndVoidsProcessDetailsBOs();
			List<String> outboundErrList = new ArrayList<>();

			for (OutboundProcessDetailsBO outboundItem : outboundPospayVoidsBO) {
				if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
					LOG.info("Error found for Instance ID: " + outboundItem.getInstance_Id().toString());
					outboundErrList.add(outboundItem.getInstance_Id().toString());
				}
			}
			outboundErrList.clear();
			if (outboundErrList.size() > 0) {
				outboundPospayVoidsBO.clear();
				outboundErrList.clear();
				//TimeUnit.HOURS.sleep(1);
				outboundPospayVoidsBO = GetOracleTablesCustomRepo.getPosPayAndVoidsProcessDetailsBOs();
				for (OutboundProcessDetailsBO outboundItem2 : outboundPospayVoidsBO) {
					if (outboundItem2.getStatus().toUpperCase().contains("ERROR")) {
						LOG.info("Error found for Instance ID: " + outboundItem2.getInstance_Id().toString());
						outboundErrList.add(outboundItem2.getInstance_Id().toString());
					}
				}
				
				if (outboundErrList.size() > 0) {
					String mailBody = buildMailBody(outboundErrList, "POSITIVE PAY AND VOIDS");
					sendOutboundErrorEmail("Q2C - POSITIVE PAY AND VOIDS - QUERY ERROR FOUND", mailBody, true);
					return;
				}
			}

			AutomationUtil util = new AutomationUtil();
            Browser browser = Playwright.create().chromium()
	                .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
			
		    BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
            context.setDefaultTimeout(240000);
			Page page = context.newPage();
            navigateMIMLogin(page);

			List<String> errList = new ArrayList<>();
			errList = filterPospayLabelsMIM(page, labelList);
		
			if (!errList.isEmpty()) {
				String mailBody = buildMailBodyPospay(errList);
				sendOutboundErrorEmail("Q2C - POSITIVE PAY AND VOIDS - MIM Validation Failure (OPTIONAL FILES)", mailBody, true);
			}
			else {
				sendNotificationEmail("Q2C - POSITIVE PAY AND VOIDS - No Errors found", "No Errors found for Positive Pay and Voids", false);
			}

            browser.close();

		} catch (Exception e) {
			LOG.error("ERROR in outboundPosPayVoidsProcess method. . .");
			LOG.error(e);
		}
	}

	@Override
	public Page navigateMIMLogin(Page page) throws ArgusQ2cException {
		LOG.log(Level.DEBUG, () -> "Start navigateMIMLogin method . . .");
		
		try {
			
	        page.navigate(environ.getProperty("playwright.uri.mim.prd3.login"));
	        
	        page.waitForSelector("input[name=userId]");
			page.fill("input[name=userId]", environ.getProperty("encrypted.mim.property.username"));
	        page.fill("input[name=password],type[password]", environ.getProperty("encrypted.mim.property.password"));
	        
	        page.click("input[name=logonButton]");
	        
	        page.mainFrame().waitForLoadState(LoadState.LOAD);
	        
	        LOG.log(Level.DEBUG, () -> "End navigateMIMLogin method . . .");
	        
	        return page;
	        
		} catch (PlaywrightException pw) {
			throw new ArgusQ2cException(pw.getMessage(), pw);
		}
	}

	@Override
	public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit) throws ArgusQ2cException {
		LOG.log(Level.DEBUG, () -> "Start waitLoadMainPage method . . .");
		
		long lWaitCtr = 0;
		try {
			ElementHandle elemHandle = mainPage.querySelector("text=" + waitElement);
			while (elemHandle != null) {
				lWaitCtr += 1;
				elemHandle = mainPage.querySelector("text=" + waitElement);
				delay(1);
				if (lWaitCtr > waitLimit) {
					LOG.log(Level.DEBUG, () -> "waitLoadMainPage Wait Limit Exceeded. . .");
					throw new RuntimeException();
				}
			}
			
		} catch (PlaywrightException pw) {
			throw new ArgusQ2cException(pw.getMessage(), pw);
		}
		
		LOG.log(Level.DEBUG, () -> "End waitLoadMainPage method . . .");
		
	}

	@Override
	public List<String> processRecordHourly(List<OutboundProcessDetailsBO> outboundItemList, String processName, ZonedDateTime zdt) throws ArgusQ2cException{
		try {
			delay(1);
			List<String> outboundErrList = new ArrayList<>();
			for (OutboundProcessDetailsBO outboundItem : outboundItemList) {
				if (outboundItem.getStatus().toUpperCase().contains("ERROR")) {
					LOG.info("ERROR status found for - " + outboundItem.getInstance_Id().toString());
					if (fileExists(environ.getProperty(OUTBOUND_FILE_PATH) + 
						"\\" + outboundItem.getInstance_Id().toString())) {
						outboundErrList.add(outboundItem.getInstance_Id().toString());
					}
					else {
						writeToFile(environ.getProperty(OUTBOUND_FILE_PATH) + "\\" +
							outboundItem.getInstance_Id().toString());
					}
				}
				else {
					if (fileExists(environ.getProperty(OUTBOUND_FILE_PATH) + "\\" + 
						outboundItem.getInstance_Id().toString())) {
						deleteFilesByName(environ.getProperty(OUTBOUND_FILE_PATH),
							environ.getProperty(OUTBOUND_FILE_PATH) + "\\" + outboundItem.getInstance_Id().toString());
					}
				}
			}

			return outboundErrList;
			
		} catch (Exception e) {
			throw e;
		}
		
	}

	@Override
	public String getUwareMailBody() {
		AutomationUtil util = new AutomationUtil();
		String formattedDatetime = util.toDateString(new Date(), "MMMMM dd, yyyy HH:mm:ss z", "MST");
		String mailBody = "<html>" +
			"<body>" +
			"<p>Hello,</p>" +
			"<p>No " + environ.getProperty("argus.mim.label.oip").replace("%", "OIP") + " file received in MIM within window " + formattedDatetime +".</p>" +
			"<br>" +
			"<p>Thanks & Regards,</p>" +
			"<p>Bot</p>" +
			"</body>" +
			"</html>";
		return mailBody;
	}

	@Override
	public String  buildMailBody(List<String> instanceID, String processName) {
		String instanceString = "";

		for (String str : instanceID) {
			instanceString = instanceString + "<tr>" + 
				"<td>" + str + "</td>" +
				"</tr>";
		}

		String bodyTemplate = "<html>" +
			"<body>" +
			"<p>Good day,</p>" +
			"<p>Please see error(s) below for " + processName + " query.</p>" +
			"<table border='1'>" +
			"<tr>" +
			"<th>Instance ID</th>" +
			"</tr>" +
			instanceString +
			"</table>" +
			"</body>" +
			"</html>";

		return bodyTemplate;
	}

	@Override
	public void sendOutboundErrorEmail(String processName, String mailBody, boolean isHTML) {
		try {
			emailService.sendSimpleMessage(environ.getProperty("mail.argus.from"), 
			environ.getProperty("mail.argus.from.alias"), 
			environ.getProperty("mail.argus.recipients", String[].class),
			environ.getProperty("mail.argus.cc", String[].class), 
			processName, 
			mailBody, 
			NORMAL_PRIORITY,  
			isHTML);
		} catch (ArgusMailException e) {
			LOG.error("Error sending email. . .");
			LOG.error(e.getMessage());
		}
		
	}

	@Override
	public void sendNotificationEmail(String subject, String mailBody, boolean isHTML) {
		try {
			emailService.sendSimpleMessage(environ.getProperty("mail.argus.from"), 
			environ.getProperty("mail.argus.from.alias"), 
			environ.getProperty("mail.argus.cc", String[].class),
			environ.getProperty("mail.argus.cc", String[].class), 
			subject, 
			mailBody, 
			NORMAL_PRIORITY,  
			isHTML);
		} catch (ArgusMailException e) {
			LOG.error("Error sending email. . .");
			LOG.error(e.getMessage());
		}
		
	}

	@Override
	public void writeToFile(String sFileName) {
		LOG.log(Level.DEBUG, () -> "Start writeToFile method . . .");
		try {
			if (!fileExists(sFileName)) {
				File myObj = new File(sFileName);
				myObj.createNewFile();	
			}
		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "writeToFile method failed . . .");
		}
		
		LOG.log(Level.DEBUG, () -> "End writeLog method . . .");
	}

	@Override
	public boolean fileExists(String filePath) {
		LOG.log(Level.DEBUG, () -> "Run fileExists method . . .");
		
		try {
			File fileName = new File(filePath);
			
			if (fileName.exists()) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public long getFTimestamp(String sFile) {
		LOG.log(Level.DEBUG, () -> "Run getFTimestamp method . . .");
		
		File fileName = new File(sFile);
		return fileName.lastModified();
	}

	@Override
	public void deleteFilesByName(String sFolder,String sFileName) {
		LOG.log(Level.DEBUG, () -> "Start deleteFilesByName method . . .");
		
		File folderPath = new File(sFolder);
	
		if (folderPath.isDirectory()) {
			try {
				for (File fName: folderPath.listFiles()) {
					System.out.println(fName.getName());
					if (fName.getName().equals(sFileName)) {
						fName.delete();
						break;
					}
				}
			} catch (Exception e) {
				LOG.log(Level.DEBUG, () -> "deleteFilesByName method failed . . .");
			}
			
		}
		
		LOG.log(Level.DEBUG, () -> "End deleteFilesByName method . . .");
	}

	@Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	@Override
	public String buildMailBodyPospay(List<String> labelStatusList) {
		String rowString = "";
		String[] labelDetails;
		for (String str : labelStatusList) {
			labelDetails = str.split("\\|");
			rowString = rowString + "<tr>" + 
				"<td>" + labelDetails[0] + "</td>" +
				"<td>" + labelDetails[1] + "</td>" +
				"</tr>";
		}

		String bodyTemplate = "<html>" +
			"<body>" +
			"<p>Good day,</p>" +
			"<p>Please see unsuccessful labels for Positive Pay and Voids MIM validation.</p>" +
			"<p>*NOTE (These files are optional).</p>" +
			"<table border='1'>" +
			"<tr>" +
			"<th>Label</th>" +
			"<th>Status</th>" +
			"</tr>" +
			rowString +
			"</table>" +
			"<p>Thanks.</p>" +
			"</body>" +
			"</html>";

		return bodyTemplate;
	}
}