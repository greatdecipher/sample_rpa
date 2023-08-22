package com.albertsons.argus.q2c.inbound.lockbox.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.mim.service.MIMService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domaindbq2c.dto.TargetedProcessFileDetailsDTO;
import com.albertsons.argus.domaindbq2c.exception.OracleServiceException;
import com.albertsons.argus.domaindbq2c.service.DbOracleService;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.q2c.inbound.lockbox.service.LockboxService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

@Service
public class LockboxServiceImpl implements LockboxService{
    private static final Logger LOG = LogManager.getLogger(LockboxServiceImpl.class);

    @Autowired
    private Environment environ;
    @Autowired
	private EmailService emailService;
    @Autowired
    private MIMService mimService;
    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;
    @Autowired
    private DbOracleService dbOracleService;

    @Override
    public void lockboxMain(String filePrefix) {
        Browser browser = null;
		String labelName;
		Boolean isBatchFound = false;
        try {

			if (filePrefix.contains("LCKBXL1")) {
				labelName = environ.getProperty("inbound.lockbox.lckbxl1.label");
			} else {
				labelName = environ.getProperty("inbound.lockbox.lckbxl8.label");
			}
			
            browser = playwrightAutomationService.openBrowser();
            BrowserContext browserContext = browser
                        .newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));

			LOG.debug("Logging in to MIM. . .");
            Page page = mimService.navigateMimLogin(browser, browserContext, "prd10");

			String fileName = getFilenameInMIM(page, labelName);
            page.close();

			if (!fileName.isEmpty()) {
				List<String> queryParams = lockboxQueryValidation(fileName);
				if (!queryParams.isEmpty()) {
					for (int i = 0; i < 3; i++) {
						page = navigateBipPage(browser, browserContext);
						isBatchFound = bipReportValidation(page, queryParams);
						if (!isBatchFound) {
							if (i==2) {
								sendLockboxEmail("Q2C - Inbound Lockbox - Query and BIP count not equal", "Query and BIP count not equal. . .", false);
							}
							else {
								page.close();
								continue;
							}
						}
						break;	
					}	
				}
			}
        } catch (Exception e) {
            LOG.error("Error in lockboxMain method. . .");
			LOG.error(e);
        }
    }

	@Override 
	public Page navigateBipPage(Browser browser, BrowserContext context) {
		Page page = null;
		try {
			page = context.newPage();
			page.setDefaultTimeout(60000);
			page.navigate(environ.getProperty("playwright.uri.oracle.saas.uri") + 
				environ.getProperty("playwright.uri.oracle.otbi.catalog"));
			
			delay(5);

			page.waitForLoadState(LoadState.LOAD);
			
			if (page.content().contains("Sign in with your organizational account")) {
				LOG.info("Microsoft login required. . .");
				loginMFA(page);
			}
		} catch (Exception e) {
			LOG.error("Error in navigateBipPage...");
			LOG.error(e);
		}
		
        return page;
	}

	@Override
	public Boolean bipReportValidation(Page page, List<String> queryResults) {
		String batchValue = "";
		Boolean isBatchFound = false;
		try {
			page.locator("span.treeNodeText:has-text('Shared Folders')").dblclick();
			delay(3);
			page.locator("span.treeNodeText:has-text('Custom')").nth(2).dblclick();
			delay(2);
			page.locator("span.treeNodeText:has-text('ACI_Integrations')").dblclick();
			delay(2);
			page.locator("span.treeNodeText:has-text('Cash_Management')").dblclick();
			delay(2);
			page.locator("span.treeNodeText:text('Reports')").nth(0).dblclick();
			delay(2);
			page.locator("span:has-text('ABS_CM_RPT_Lockbox_Automation')").dblclick();
			delay(2);

			page.locator("span.HeaderTitleBarCaption:has-text('ABS_CM_RPT_Lockbox_Automation')").waitFor();
				
			delay(5);
			page.waitForLoadState();
			page.mainFrame().waitForLoadState();
			for (Frame childFrame: page.mainFrame().childFrames()) {
				if (childFrame.content().contains("_paramsp_lockbox_batchname")) {
					childFrame.locator("id=_paramsp_lockbox_batchname").fill(queryResults.get(0));
					childFrame.locator("id=reportViewApply").click();
					delay(5);
					for (Frame childFrame2 : childFrame.childFrames()) {
						childFrame2.waitForLoadState();
						childFrame2.locator("pre").waitFor();
						batchValue = childFrame2.locator("pre").innerText();
						LOG.info("Element value:" + batchValue);
						if (!batchValue.contains("COUNT")) {
							TimeUnit.MINUTES.wait(30);
							return false;
						}
						break; 
					}
					break;
				}
			}
			delay(2);

			List<String> batchList = Arrays.asList(batchValue.split(","));
			String bipBatchCount = batchList.get(batchList.size() - 2);

			if (queryResults.get(1).equals(bipBatchCount)) {
				LOG.info("Query and BIP count same. . .");	
				sendNotificationEmail("Q2C - Inbound Lockbox - Query and BIP count equal", 
					buildLockboxMailBody(queryResults.get(1), bipBatchCount), true);
				isBatchFound = true;
			}
			else {
				sendLockboxEmail("Q2C - Inbound Lockbox - Query and BIP count not equal", 
					buildLockboxMailBody(queryResults.get(1), bipBatchCount), true);
				isBatchFound = true;
			}
			
			delay(2);
		} catch (Exception e) {
			LOG.error("Error in validateBipReport method...");
			LOG.error(e);
		}
		return isBatchFound;
	}

	@Override
	public Page loginMFA(Page page) {

		try {
			
			page.fill("id=userNameInput", environ.getProperty("encrypted.mim.property.username"));
			page.fill("id=passwordInput", environ.getProperty("encrypted.bip.property.password"));
			page.click("id=submitButton");
			page.waitForLoadState(LoadState.LOAD);
			
			delay(5);
			
			Locator loc = page.locator("label:has-text('Incorrect user ID or password. Type the correct user ID and password, and try again.')");
			if (loc.count() > 0) {
				LOG.error("Incorrect Username or Password. . .");
				try {
					emailService.sendSimpleMessage(environ.getProperty("mail.ip.from"), 
					environ.getProperty("mail.ip.from.alias"), 
					environ.getProperty("mail.ip.recipients",String[].class), 
					environ.getProperty("mail.ip.cc", String[].class), 
					"Q2C - Lockbox - MFA Login Incorrect Password", 
					"Unable to login to BIP site, Please check and update password. . .", 
					1, 
					false);

					System.exit(0);	
				} catch (Exception e) {
					LOG.error("Failed to send email. . .");
					System.exit(0);	
				}
			}

			for (int i = 0; i <= Integer.valueOf(environ.getProperty("mfa.attempts")); i++){				
				TimeUnit.SECONDS.sleep(10);
				
				List<ElementHandle> navigatorElements = page.querySelectorAll("id=idHeaderTitleCell");
			
				if (!navigatorElements.isEmpty()){ // either it's not requiring MFA code because session is saved or login was successful
					return page;
				}
				else { // requiring MFA code because session is not saved				
					if ( i == Integer.valueOf(environ.getProperty("mfa.attempts")) ){ // last attempt
						throw new Exception("MFA code not retrieved...");
					}
					else{
						List<ElementHandle> otcElements = page.querySelectorAll("input[name=otc]");
						
						if (!otcElements.isEmpty()){
							LOG.info("MFA token required, getting MFA Code. . .");
							String mfaCode = getMfaCode(environ.getProperty("mfa.python.script"), environ.getProperty("mfa.secret.key.bip"));
			
							TimeUnit.SECONDS.sleep(10);
			
							if (mfaCode == null || mfaCode.equalsIgnoreCase("") || mfaCode.isEmpty()){
								if (i == (Integer.valueOf(environ.getProperty("mfa.attempts")) - 1)){
									throw new Exception("MFA code not retrieved...");
								}
							}
							else{
								page.fill("id=idTxtBx_SAOTCC_OTC", mfaCode);
								page.check("id=idChkBx_SAOTCC_TD");
								page.click("id=idSubmit_SAOTCC_Continue");
								page.waitForLoadState(LoadState.LOAD);
							}
						}
						
					}
			
				}
			}
		
		} catch (Exception e) {
			LOG.error("ERROR in loginMFA function.");
			LOG.error(e.getMessage());
		}
		
		return page;
	}

	@Override
	public String getMfaCode(String pythonScript, String secretKey){
		LOG.log(Level.DEBUG, () -> "start getMfaCode method. . .");
		
		try {
			String []cmd = {"python", pythonScript, secretKey};
			
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(cmd);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = "", mfaCode = "";
			
			while((s = in.readLine()) != null){
				mfaCode = s.trim();
	
				if (mfaCode.length() == 6){ // make sure it's 6 digits
					return mfaCode;
				}
			}
	
		} catch (Exception e){
			LOG.log(Level.DEBUG, () -> "error retrieving mfa code. . .");
			LOG.error(e);
		}
	
		return null;
	}


	@Override
	public List<String> lockboxQueryValidation(String fileName) {
		String resultStatus = "", instanceID = "";
		List<String> queryFields = new ArrayList<>();

		try {
			for (int i = 0; i < 3; i++) {
				List<TargetedProcessFileDetailsDTO> targetedProcessDTO = dbOracleService.getTargetedProcessFileDetailsDTOs(fileName);
				if (!targetedProcessDTO.isEmpty()) {
					for (TargetedProcessFileDetailsDTO targetedProcessFileDetailsDTO : targetedProcessDTO) {
						resultStatus = targetedProcessFileDetailsDTO.getStatus();
						instanceID = targetedProcessFileDetailsDTO.getInstanceId().toString();
					}
					if (!resultStatus.toUpperCase().contains("PROCESSED")) {
						if (i == 2) {
							sendLockboxEmail("Q2C - Inbound Lockbox - Job Status | " + resultStatus,
								"<html>" +
								"<body>" +
								"<p>Good day,</p>" +
								"<p>Bot has stopped execution, job status is " + resultStatus + ".</p>" +
								"<p>Instance ID: " + instanceID + "</p>" +
								"<p>Thanks,</p>" +
								"</body>" +
								"</html>",
								true);
						}
						LOG.info("Batch run status is NOT PROCESSED, Retrying. . .");
						TimeUnit.MINUTES.sleep(15);
						continue;
					}
					else {
						LOG.info("Batch run status is PROCESED. . .");
						for (TargetedProcessFileDetailsDTO targetedProcessFileDetailsDTO : targetedProcessDTO) {
							queryFields.add(targetedProcessFileDetailsDTO.getBatchName());
							queryFields.add(targetedProcessFileDetailsDTO.getConsumedRec().toString());
							queryFields.add(targetedProcessFileDetailsDTO.getStatus());
							LOG.info("BatchName: " + targetedProcessFileDetailsDTO.getBatchName());
							LOG.info("ConsumedRec: " + targetedProcessFileDetailsDTO.getConsumedRec().toString());
							LOG.info("Status: " + targetedProcessFileDetailsDTO.getStatus());
						}
						break;
					}
				}
				else {
					if (i == 2) {
						sendLockboxEmail("Q2C - Inbound Lockbox - Query result is zero",
							"<html>" +
							"<body>" +
							"<p>Good day,</p>" +
							"<p>Bot has stopped execution, Query returned 0 row(s).</p>" +
							"<p>Thanks,</p>" +
							"</body>" +
							"</html>",
							true);
					}
					LOG.info("Query result is zero, Retrying. . .");
					TimeUnit.MINUTES.sleep(15);
					continue;
				}
			}

		} catch (OracleServiceException | InterruptedException e) {
			LOG.error("Error running query for TargetedProcessFileDetailsDTO");
			LOG.error(e);
		}
		return queryFields;
	}

	@Override
	public void sendLockboxEmail(String mailSubject, String mailBody, Boolean isHtml) {
		try {
			emailService.sendSimpleMessage(environ.getProperty("mail.argus.from"), 
				environ.getProperty("mail.argus.from.alias"), 
				environ.getProperty("mail.argus.recipients", String[].class), 
				environ.getProperty("mail.argus.cc", String[].class), 
				mailSubject, 
				mailBody, 
				1, 
				isHtml);
		} catch (ArgusMailException e) {
			LOG.error("Failed to send email, Subject: " + mailSubject + ". . .");
			LOG.error(e);
		}
	}

    @Override
    public String getFilenameInMIM(Page page, String labelName) {
        String mimFileName = "", labelStatus;
        if (page != null) {
			
			try {
				LOG.debug("Start getFilenameInMIM method...");
				page.navigate(environ.getProperty("playwright.uri.mim.prd3.login") + 
					environ.getProperty("playwright.uri.mim.prd3.monitoring"));
				
				page.click("text=Edit Unsaved Filter");
				delay(1);
				page.fill("id=descriptionCmpId",labelName);
				delay(1);
				page.click("id=x-form-el-daystoshowcombo >> img");
				delay(1);
				page.click("div.x-combo-list-item:has-text('" + environ.getProperty("argus.mim.label.days.show") + "')");
	
				page.check("id=completedCmpId");
				page.check("id=failedCmpId");
				page.check("id=warningCmpId");
				page.check("id=inprogressCmpId");
				page.check("id=fixitCmpId");
				page.click("text=Apply");
				delay(3);
				waitLoadMainPage(page, "Loading Processes...", 300);

				ElementHandle objTable = page.querySelector("id=ext-gen188");
			
				if (objTable != null) {
					List<ElementHandle> obj = objTable.querySelectorAll("tr");
					if (obj.isEmpty()) {
						LOG.info("Label not found. . .");
						mimFileName = "";
						//sendemail
						emailService.sendSimpleMessage(environ.getProperty("mail.argus.from"), 
							environ.getProperty("mail.argus.from.alias"), 
							environ.getProperty("mail.argus.recipients", String[].class), 
							environ.getProperty("mail.argus.cc", String[].class), 
							"Q2C - Inbound Lockbox - File not found in MIM", 
							labelName + " Not found in MIM...", 
							1, 
							false);
					}
					else {
						LOG.info("MIM Label found. . .");
						for (ElementHandle oCrElemTR: obj) {
							List<ElementHandle> oCrElemTD = oCrElemTR.querySelectorAll("td");
						
							mimFileName = oCrElemTD.get(7).innerText();
							labelStatus = oCrElemTD.get(0).getAttribute("class");
							String[] splitFileName = mimFileName.split("/");
							mimFileName = splitFileName[splitFileName.length - 1];
							LOG.info("File Name: " + mimFileName);
							if (!labelStatus.toUpperCase().contains("SUCCESS")) {
								if (labelStatus.toUpperCase().contains("ERROR")) {
									labelStatus = "ERROR";
								}
								else if (labelStatus.toUpperCase().contains("FIXIT")) {
									labelStatus = "FIXIT";
								} 
								else if (labelStatus.toUpperCase().contains("WARNING")) {
									labelStatus = "WARNING";
								}
								else if (labelStatus.toUpperCase().contains("PROGRESS")) {
									labelStatus = "IN PROGRESS";
								} else {
									labelStatus = "BOT UNIDENTIFIED";
								}
								LOG.info("Label status " + labelStatus + "...");
								emailService.sendSimpleMessage(environ.getProperty("mail.argus.from"), 
									environ.getProperty("mail.argus.from.alias"), 
									environ.getProperty("mail.argus.recipients", String[].class), 
									environ.getProperty("mail.argus.cc", String[].class), 
							"Q2C - Inbound Lockbox - Label status " + labelStatus, 
									labelName + " status is " + labelStatus + "...", 
								1, 
									false);
								
								mimFileName = "";
							}
							break;
						}
					}	
				}
				else {
					mimFileName = "";
				}
				LOG.debug("End getFilenameInMIM method...");
			} catch (Exception e) {
				LOG.error("Erorr getting file name in MIM. . .");
				LOG.error(e);
			}
		}
		else {
			mimFileName = "";
		}
        return mimFileName.trim();
    }

    @Override
	public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit) {
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
			
		} catch (Exception pw) {
			LOG.error("Error in waiLoadMainPage method. . .");
            LOG.error(pw);
		}
		
		LOG.log(Level.DEBUG, () -> "End waitLoadMainPage method . . .");
		
	}

    @Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			LOG.error("Error in delay method. . .");
			LOG.error(e);
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
	public String  buildLockboxMailBody(String queryCount, String bipCount) {
		
		String bodyTemplate = "<html>" +
			"<body>" +
			"<p>Good day,</p>" +
			"<p>Please see lockbox validation results below:</p>" +
			"<p>Query count: " + queryCount + "</p>" +
			"<p>BIP count: " + bipCount + "</p>" +
			"<br>" +
			"<p>Thanks</p>" +
			"</body>" +
			"</html>";

		return bodyTemplate;
	}

}
