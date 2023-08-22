package com.albertsons.argus.r2r.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2RCommonService;
import com.albertsons.argus.r2r.service.R2RErpService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class R2RErpServiceImpl implements R2RErpService {
	public static final Logger LOG = LogManager.getLogger(R2RErpServiceImpl.class);

	@Autowired
    private Environment environment;
    
	@Autowired
	private EmailService emailService;

	@Autowired
	private PlaywrightAutomationService playwrightService;

	@Autowired
	private R2RCommonService r2rCommonService;

	private String execTimestamp;

	@Override
    public void mainErpTask(FileDetails fileDetails, String groupId, String execTimestamp) throws ArgusR2RException {
        LOG.log(Level.INFO, () -> "start mainErpTask method . . .");

		this.execTimestamp = execTimestamp;

		Browser browser = null;
		BrowserContext browserContext = null;
	
        try {
			// wait time before proceeding
			if (fileDetails.getDataSize().equalsIgnoreCase("big")){ //if big data
				LOG.log(Level.DEBUG, () -> "found big data. . .");
				TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("check.file.loaded.retry.wait.time.big.data").trim()));
			}
			else{ //if small data
				LOG.log(Level.DEBUG, () -> "found small data. . .");
				TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("check.file.loaded.retry.wait.time.small.data").trim()));
			}

			browser = playwrightService.openBrowser();
			browserContext = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
			Page page = checkFileLoaded(fileDetails, groupId, browser, browserContext);

			if (page != null){

				if (checkFilePosted(page, fileDetails, groupId, browser, browserContext) == true){

					// send email success to rpa only for visibility
					String body = "<body>Hello, <br><br>";

					body += "File: " + fileDetails.getFilename() + " has been successfully processed in ESS Jobs interface.";

					body += "<br><br> R2R automation for this file is completed. This file will now be added to the list of successful validations."; 

					body += "<br><br>" +
							"Thanks & Regards, <br>" + 
							"Bot</body>";
					
					sendErpEmail(environment.getProperty("mail.argus.recipients", String[].class), 
									environment.getProperty("mail.argus.cc", String[].class), 
									environment.getProperty("mail.subject.erp.success"), 
									body);    
					
					// add to folder containing list of successful R2R automations for the day
					String filename = fileDetails.getFilename() ;
					String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
					String filePath = environment.getProperty("folder.success.files") + "\\" + filenameNoExtension;
					
					AutomationUtil util = new AutomationUtil();
					util.createFile("R2R automation successful for this file", filePath);						

					page.close();

				}
				else{
					//end automation
				}

			}
			else{
				//end automation
			}

			page.close();

		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			browserContext.close();
			browser.close();
		}

        LOG.log(Level.DEBUG, () -> "end mainErpTask method . . .");
	}

    @Override
	public Page navigateErpLogin(Browser browser, BrowserContext browserContext) throws ArgusR2RException {
		LOG.log(Level.DEBUG, () -> "start navigateErpLogin method . . .");

		try {
            browserContext.setDefaultTimeout(120000); //2 minutes max wait

			Page page = browserContext.newPage();

			page.navigate(environment.getProperty(ERP_LOGIN_URL));
			
			TimeUnit.SECONDS.sleep(10);

			List<ElementHandle> ssoBtnElements = page.querySelectorAll("button[id=ssoBtn]");
			if (!ssoBtnElements.isEmpty()){ 
				playwrightService.pageClick(page, "button", "id", "ssoBtn");
			}
			
			TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("site.load.wait.time.in.seconds")));

			List<ElementHandle> emailElements = page.querySelectorAll("input[type=email]");
			if (!emailElements.isEmpty()){ 
				playwrightService.pageFill(page, "input", "type", "email", environment.getProperty("ldap.property.username"));
				
				List<ElementHandle> submitElements = page.querySelectorAll("input[type=submit]"); // check if the initial email entry page is skipped
				if (!submitElements.isEmpty()){ 
					playwrightService.pageClick(page, "input", "type", "submit");
					TimeUnit.SECONDS.sleep(10);
				}

				List<ElementHandle> passwordElements = page.querySelectorAll("input[id=passwordInput]");
				if (!passwordElements.isEmpty()){ 
					playwrightService.pageFill(page, "input", "id", "passwordInput", environment.getProperty("encrypted.ldap.property.password"));
					playwrightService.pageClick(page, "span", "id", "submitButton");
				}
			}

			for (int i = 0; i <= Integer.valueOf(environment.getProperty("mfa.attempts").trim()); i++){				
				TimeUnit.SECONDS.sleep(10);

				List<ElementHandle> navigatorElements = page.querySelectorAll("svg[aria-label=Navigator]");

				if (!navigatorElements.isEmpty()){ // either it's not requiring MFA code because session is saved or login was successful
					return page;
				}
				else{ // requiring MFA code because session is not saved	
					if ( i == Integer.valueOf(environment.getProperty("mfa.attempts").trim()) ){ // last attempt
						throw new Exception("MFA code not retrieved...");
					}
					else{
						page.navigate(environment.getProperty(ERP_LOGIN_URL)); //reload page
						TimeUnit.SECONDS.sleep(20);

						List<ElementHandle> otcElements = page.querySelectorAll("input[name=otc]");
						
						if (!otcElements.isEmpty()){
							TimeUnit.SECONDS.sleep(10);

							String mfaCode = r2rCommonService.getMfaCode(environment.getProperty("mfa.python.script"), environment.getProperty("mfa.secret.key"));

							if (mfaCode == null || mfaCode.equalsIgnoreCase("") || mfaCode.isEmpty()){
								if (i == (Integer.valueOf(environment.getProperty("mfa.attempts").trim()) - 1)){
									throw new Exception("MFA code not retrieved...");
								}
							}
							else{
								playwrightService.pageFill(page, "input", "name", "otc", mfaCode);
								playwrightService.pageLocatorWait(page, "input", "name", "rememberMFA").check();
								playwrightService.pageClick(page, "input", "value", "Verify");
							}
						}
						
					}

				}
			}
									
			LOG.log(Level.DEBUG, () -> "end navigateErpLogin method . . .");
			return page;
		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem navigating ERP login . . .");
			LOG.error(e);
			return null;
		}
		
	}

	@Override
	public Page navigateJournals(FileDetails fileDetails, Browser browser, BrowserContext browserContext){
		LOG.log(Level.DEBUG, () -> "start navigateJournals method. . .");

		try {
			Page page = navigateErpLogin(browser, browserContext);

			page.navigate(environment.getProperty("playwright.uri.oracle.saas.journals"));

			//make sure correct page
			List<ElementHandle> ledgerTitle = page.querySelectorAll("td:has-text(\"ACI_Primary Ledger_US\")");
			//playwrightService.pageLocatorWait(page, "", "", "td:has-text(\"ACI_Primary Ledger_US\")"); // can also use >> nth=0

			if (ledgerTitle.size() > 0){ // correct page
				List<ElementHandle> selectDropdown = page.querySelectorAll("select");
				
				for (ElementHandle elemHandle: selectDropdown) {
					elemHandle.selectOption("ACI_Primary Ledger_US");
					playwrightService.pageClick(page, "", "", "button:has-text(\"OK\")");
					break;
				}

				playwrightService.pageLocatorWait(page, "div", "title", "Tasks");
				playwrightService.pageClick(page, "div", "title", "Tasks");

				TimeUnit.SECONDS.sleep(10);

				playwrightService.pageLocatorWait(page, "", "", "li:has-text(\"Manage Journals\")");
				playwrightService.pageClick(page, "", "", "li:has-text(\"Manage Journals\")");

				TimeUnit.SECONDS.sleep(10);
				
				LOG.log(Level.DEBUG, () -> "end navigateJournals method. . .");
				return page;
			}
			else{
				return null;
			}

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem navigating journals. . .");
            LOG.error(e);
			return null;
		}

	}

	@Override
	public Page checkFileLoaded(FileDetails fileDetails, String groupId, Browser browser, BrowserContext browserContext) {
		LOG.log(Level.DEBUG, () -> "start checkFileLoaded method. . .");

		try {
			Page page = navigateJournals(fileDetails, browser, browserContext);
			
			if (filterFileLoaded(page, fileDetails, groupId) == true){
				LOG.log(Level.DEBUG, () -> "end checkFileLoaded method. . .");
				return page;
			}
			else{
				for (int i = 0; i < Integer.valueOf(environment.getProperty("check.file.loaded.retry.attempts").trim()); i++){

					if (fileDetails.getDataSize().equalsIgnoreCase("big")){ //if big data
						if (i == (Integer.valueOf(environment.getProperty("check.file.loaded.retry.attempts").trim()) - 1) ){
							TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("check.file.loaded.retry.wait.time.big.data.last.attempt").trim()));
						}
						else{
							TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("check.file.loaded.retry.wait.time.big.data").trim()));
						}
					
					}
					else{ //if small data
						TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("check.file.loaded.retry.wait.time.small.data").trim()));
					}

					page = navigateJournals(fileDetails, browser, browserContext);

					if (filterFileLoaded(page, fileDetails, groupId) == true){
						LOG.log(Level.DEBUG, () -> "end checkFileLoaded method. . .");
						return page;
					}
					else{ // not found
						String accountingPeriod = playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:MAnt2:1:pt1:ap1:queryP:value20'").inputValue();
						
						LOG.log(Level.DEBUG, () -> "accounting period text: " + accountingPeriod);

						String[] period = accountingPeriod.split("-");

						String integersOnly = period[0].replaceAll("\\D", "");
						String month = "";

						LOG.log(Level.DEBUG, () -> "month with leading 0: " + integersOnly);

						if (integersOnly.charAt(0) == '0'){ // a single-digit integer
							month = integersOnly.substring(0, 0) + "" + integersOnly.substring(1);
						}

						if (Integer.valueOf(month) >= 2){ // current period is not the start of calendar year
							int previousMonth = Integer.valueOf(month) - 1;
							String prevMonth = String.valueOf(previousMonth);

							if (previousMonth < 10){ // single digit, so add leading zero
								prevMonth = "0" + previousMonth;
							}

							playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:MAnt2:1:pt1:ap1:queryP:value20'").fill("P" + prevMonth + "-" + period[1]);

							if (filterFileLoaded(page, fileDetails, groupId) == true){
								LOG.log(Level.DEBUG, () -> "end checkFileLoaded method. . .");
								return page;
							}
						}
						else{ // current period is start of calendar year
							page = navigateJournals(fileDetails, browser, browserContext);

							int previousYear = Integer.valueOf(period[1]) - 1;
							playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:MAnt2:1:pt1:ap1:queryP:value20'").fill("P13-" + previousYear);

							if (filterFileLoaded(page, fileDetails, groupId) == true){
								LOG.log(Level.DEBUG, () -> "end checkFileLoaded method. . .");
								return page;
							}
							else{
								page = navigateJournals(fileDetails, browser, browserContext);
								playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:MAnt2:1:pt1:ap1:queryP:value20'").fill("P12-" + previousYear);

								if (filterFileLoaded(page, fileDetails, groupId) == true){
									LOG.log(Level.DEBUG, () -> "end checkFileLoaded method. . .");
									return page;
								}
							}

						}

					}
	
				}

			}

			page.close();

			//email OracleFinancials.IT that file is still not loaded in ERP after n attempts and n wait time
			String body = "<body>Hello, <br><br>";

			body += "Filename: " + fileDetails.getFilename() + " with group ID: " + groupId + " is not loaded in ERP after waiting " + environment.getProperty("check.file.loaded.retry.wait.time." + fileDetails.getDataSize() + ".data") + " minute(s) " + environment.getProperty("check.file.loaded.retry.attempts") + " times.";
			
			body += "<br><br> The automation for this file has now ended."; 

			body += "<br><br>" +
					"Thanks & Regards, <br>" + 
					"Bot</body>";
			
			sendErpEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
							environment.getProperty("mail.argus.cc", String[].class), 
							environment.getProperty("mail.subject.erp.file.not.loaded"), 
							body);

			LOG.log(Level.INFO, () -> "file not loaded in erp. . .");
			LOG.log(Level.DEBUG, () -> "end checkFileLoaded method. . .");
			return null;

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem checking if file loaded in ERP. . .");
            LOG.error(e);
			return null;
		}

	}

	@Override
	public boolean filterFileLoaded(Page page, FileDetails fileDetails, String groupId){
		LOG.log(Level.DEBUG, () -> "start filterFileLoaded method. . .");

		try {
			
			playwrightService.pageLocatorWait(page, "", "", "select:right-of(:text(\"Journal Batch\")) >> nth=0").selectOption("CONTAINS");

			//playwrightService.pageLocatorWait(page, "", "", "input:right-of(:text(\"Journal Batch\")) >> nth=6").fill(groupId);
			playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:MAnt2:1:pt1:ap1:queryP:value10'").fill(groupId);

			playwrightService.pageClick(page, "", "", "button:has-text(\"Search\")");
		
			TimeUnit.SECONDS.sleep(10);
			
			List<ElementHandle> rowResults = page.querySelectorAll("table:has-text(\"No results found.\")");

			LOG.log(Level.DEBUG, () -> "rowResults size: " + rowResults.size());
			AutomationUtil util = new AutomationUtil();

			if (rowResults.size() == 1){

				if (Boolean.parseBoolean(environment.getProperty("screenshot.failure.page")) == true){
					String filename = fileDetails.getFilename();
					String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
					Date dateNow = new Date();
					String currtime = util.toDateString(dateNow, environment.getProperty("domain.util.failure.format"), environment.getProperty("r2r.timezone"));

					String imgFilename = "L-" + filenameNoExtension + "-" + currtime + ".png";
					page.screenshot(new Page.ScreenshotOptions()
					.setPath(Paths.get(environment.getProperty("screenshot.failure.path") + "\\" + imgFilename))
					.setFullPage(true));
				}

				
				
				return false;

			}
			else if (rowResults.size() > 1){
				LOG.log(Level.INFO, () -> "'no results found' appeared more than once. . .");

				if (Boolean.parseBoolean(environment.getProperty("screenshot.failure.page")) == true){
					String filename = fileDetails.getFilename();
					String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
					Date dateNow = new Date();
					String currtime = util.toDateString(dateNow, environment.getProperty("domain.util.failure.format"), environment.getProperty("r2r.timezone"));

					String imgFilename = "L-" + filenameNoExtension + "-" + currtime + ".png";
					page.screenshot(new Page.ScreenshotOptions()
					.setPath(Paths.get(environment.getProperty("screenshot.failure.path") + "\\" + imgFilename))
					.setFullPage(true));
				}

				return false;
			}
			else{
				LOG.log(Level.INFO, () -> "found results from search conducted. . .");
				return true;
			}

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem checking if file loaded in ERP. . .");
            LOG.error(e);
			return false;
		}

	}

	@Override
	public boolean checkFilePosted(Page page, FileDetails fileDetails, String groupId, Browser browser, BrowserContext browserContext) {
		LOG.log(Level.DEBUG, () -> "start checkFilePosted method. . .");

		try {

			if (readFilePostStatus(page, fileDetails) == true){
				LOG.log(Level.DEBUG, () -> "end checkFilePosted method. . .");
				return true;
			}
			else{
				AutomationUtil util = new AutomationUtil();

				Date dateNow = new Date();
				Calendar calendar = util.dateToCalendar(dateNow, environment.getProperty("r2r.timezone"));
	
				String emailMessage = "";

				if (calendar.get(Calendar.DAY_OF_WEEK) == 1){ // Sunday MST
					if (calendar.get(Calendar.HOUR_OF_DAY) == 23 || calendar.get(Calendar.HOUR_OF_DAY) == 0){ // 11 PM MST TO 12 AM MST
						emailMessage = " after waiting for 1 AM MST.";

						int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
						int minute = calendar.get(Calendar.MINUTE);

						if (hourOfDay == 0){
							hourOfDay = 24;
						}

						int hoursToWait = (24 % hourOfDay) + 1;
						int hoursInMinutes = hoursToWait * 60;
						int waitTime = hoursInMinutes - minute;
						
						TimeUnit.MINUTES.sleep(waitTime);
						
						if (getFilePostStatus(page, fileDetails, browser, browserContext, groupId) == true){
							return true;
						}
					}
					else if (calendar.get(Calendar.HOUR_OF_DAY) == 1){ // 1 AM MST
						emailMessage = " after waiting for 1 AM MST.";

						// no wait needed, proceed to retry
						if (getFilePostStatus(page, fileDetails, browser, browserContext, groupId) == true){
							return true;
						}
					}
					else{
						emailMessage = " after waiting " + environment.getProperty("check.file.posted.retry.attempts") + " times.";

						for (int i = 0; i < Integer.valueOf(environment.getProperty("check.file.posted.retry.attempts").trim()); i++){
							sleepNearestHalfHour();

							if (getFilePostStatus(page, fileDetails, browser, browserContext, groupId) == true){
								return true;
							}
						}
					}
				}
				else{
					if (calendar.get(Calendar.HOUR_OF_DAY) >= 19 || calendar.get(Calendar.HOUR_OF_DAY) == 0){ // 11 PM MST TO 12 AM MST
						emailMessage = " after waiting for 1 AM MST.";

						int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
						int minute = calendar.get(Calendar.MINUTE);

						if (hourOfDay == 0){
							hourOfDay = 24;
						}

						int hoursToWait = (24 % hourOfDay) + 1;
						int hoursInMinutes = hoursToWait * 60;
						int waitTime = hoursInMinutes - minute;
						
						TimeUnit.MINUTES.sleep(waitTime);

						if (getFilePostStatus(page, fileDetails, browser, browserContext, groupId) == true){
							return true;
						}
					}
					else if (calendar.get(Calendar.HOUR_OF_DAY) == 1){ // 1 AM MST
						emailMessage = " after waiting for 1 AM MST.";

						// no wait needed, proceed to retry
						if (getFilePostStatus(page, fileDetails, browser, browserContext, groupId) == true){
							return true;
						}
					}
					else{
						emailMessage = " after waiting " + environment.getProperty("check.file.posted.retry.attempts") + " times.";

						for (int i = 0; i < Integer.valueOf(environment.getProperty("check.file.posted.retry.attempts").trim()); i++){
							sleepNearestHalfHour();

							if (getFilePostStatus(page, fileDetails, browser, browserContext, groupId) == true){
								return true;
							}
						}
					}
				}

				//email OracleFinancials.IT that file is still not posted in ERP after n attempts
				String body = "<body>Hello, <br><br>";
			
				body += "Filename: " + fileDetails.getFilename() + " with group ID: " + groupId + " is unposted in ERP " + emailMessage;

				body += "<br><br> The automation for this file has now ended."; 

				body += "<br><br>" +
						"Thanks & Regards, <br>" + 
						"Bot</body>";
				
				sendErpEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
								environment.getProperty("mail.argus.cc", String[].class), 
								environment.getProperty("mail.subject.erp.file.not.posted"), 
								body);

				return false;

			}

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem checking if file posted in ERP. . .");
            LOG.error(e);
		}

		return false;
	}

	@Override
	public boolean getFilePostStatus(Page page, FileDetails fileDetails, Browser browser, BrowserContext browserContext, String groupId){
		LOG.log(Level.DEBUG, () -> "start getFilePostStatus method. . .");

		try{
			page = navigateJournals(fileDetails, browser, browserContext);

			if (filterFileLoaded(page, fileDetails, groupId) == true){
				if (readFilePostStatus(page, fileDetails) == true){
					LOG.log(Level.DEBUG, () -> "end getFilePostStatus method. . .");
					return true;
				}
			}
			else{
				// loaded earlier but did not now
			}

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem getting file's post status. . .");
            LOG.error(e);
		}

		return false;
	}

	@Override
	public boolean readFilePostStatus(Page page, FileDetails fileDetails){
		LOG.log(Level.DEBUG, () -> "start readFilePostStatus method. . .");

		AutomationUtil util = new AutomationUtil();
		
		try {

			List<ElementHandle> spanResults = page.querySelectorAll("span");

			LOG.log(Level.DEBUG, () -> "spanResults size: " + spanResults.size());
			
			for (ElementHandle elemHandle: spanResults) {
				if (elemHandle.textContent().contains("Posted")){
					LOG.log(Level.DEBUG, () -> "end readFilePostStatus method. . .");
					return true;
				}
			}

			if (Boolean.parseBoolean(environment.getProperty("screenshot.failure.page")) == true){
				String filename = fileDetails.getFilename();
				String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
				Date dateNow = new Date();
				String currtime = util.toDateString(dateNow, environment.getProperty("domain.util.failure.format"), environment.getProperty("r2r.timezone"));

				String imgFilename = "P-" + filenameNoExtension + "-" + currtime + ".png";
				page.screenshot(new Page.ScreenshotOptions()
				.setPath(Paths.get(environment.getProperty("screenshot.failure.path") + "\\" + imgFilename))
				.setFullPage(true));
			}

			return false;

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem reading file's post status. . .");
            LOG.error(e);

			return false;
		}

	}

	@Override
	public void sleepNearestHalfHour(){
		LOG.log(Level.DEBUG, () -> "start sleepNearestHalfHour method. . .");

		try {
			//let the nearest half hour pass + 10 mins then check again
			Date dateNow = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateNow);

			int unroundedMinutes = calendar.get(Calendar.MINUTE);
			int waitMins = 0;

			// if unrounded minutes is < 30 then next run is at n:30 + 10, else n:00 + 10
			if (unroundedMinutes < 30){
				waitMins = (30 - unroundedMinutes) + 10;
				
			}
			else{
				waitMins = (30 - (unroundedMinutes % 30)) + 10;
			}

			TimeUnit.MINUTES.sleep(waitMins);

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem waiting for nearest half hour. . .");
            LOG.error(e);
		}

		LOG.log(Level.DEBUG, () -> "end sleepNearestHalfHour method. . .");
	}

	@Override
    public void sendErpEmail(String[] recipients, String[] cc, String subject, String body) {
		LOG.log(Level.DEBUG, () -> "start sendErpEmail method. . .");

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"), recipients, cc,
                    subject + " - " + execTimestamp,
                    body, NORMAL_PRIORITY,true);

        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending erp email. . .");
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end sendErpEmail method. . .");
	}

}
