package com.albertsons.argus.ip.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.ip.exception.ArgusIpException;
import com.albertsons.argus.ip.service.IPAutomationService;
import com.albertsons.argus.ip.service.JsonService;
import com.albertsons.argus.ip.ws.bo.ResponseGetInvoiceListBO;
import com.albertsons.argus.mail.service.EmailService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.ElementHandle.WaitForSelectorOptions;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

@Service
public class IPAutomationServiceImpl implements IPAutomationService {
    public static final Logger LOG = LogManager.getLogger(IPAutomationServiceImpl.class);
	
	@Autowired
    private Environment environ;
	
	@Autowired
    private RestTemplate restTemplate;
    
	@Autowired
    private AutomationService<ResponseGetInvoiceListBO> jsonResponseGetInvoiceListBOService;
    
	@Autowired
	private EmailService emailService;
	
	public Browser browser;
	public BrowserContext context;
	public Page page;

	public List<Browser> browserList;

    public List<BrowserContext> browserContextList;

    public List<Page> pageList;

	@Override
    public String updateInvoice(String requestBody, String poLine, String noteToSupplier, String noteToReceiver,
	String approver, String accountingGroup, String projectNo, String lawsonReceiver, String fastId, String status) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method updateTask(). . .");
		
        return restTemplate.exchange(environ.getProperty("ip.web.service.url.update"), HttpMethod.PATCH, getHttpEntity(requestBody,"Token", "UPDATE",
			poLine, noteToSupplier, noteToReceiver, approver, accountingGroup, projectNo, lawsonReceiver, fastId, status),String.class).getBody() ;
    }

    @Override
    public ResponseGetInvoiceListBO getInvoiceLists(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method getTaskLists(). . .");

        return jsonResponseGetInvoiceListBOService.toJson(restTemplate.exchange(environ.getProperty("ip.web.service.url.get"), HttpMethod.POST, getHttpEntity(requestBody,"Token", "GET",
			"","","","","","","","",""),String.class).getBody());
    }
    
    private HttpEntity<Object> getHttpEntity(String requestBody,String headerName, String processType, String poLine, String noteToSupplier, String noteToReceiver,
		String approver, String accountingGroup, String projectNo, String lawsonReceiver, String fastId, String status){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(headerName, environ.getProperty("ip.web.service.auth.header.value"));
        if (processType.toUpperCase().contains("UPDATE")) {
			headers.set("poline", poLine);
			headers.set("nSupplier", noteToSupplier);
			headers.set("nReceiver", noteToReceiver);
			headers.set("approver", approver);
			headers.set("accountinggroup", accountingGroup);
			headers.set("projectnumber", projectNo);
			headers.set("lawsonreceiver", lawsonReceiver);
			headers.set("fastId", fastId);
			headers.set("Status", status);
		}
		else if (processType.toUpperCase().contains("GET")) {
			headers.set("Status", environ.getProperty("ip.web.service.get.status"));
		} else {
			
		}
		return new HttpEntity<>(requestBody,headers);
    }
	
	@Override
	public Browser getBrowser() throws PlaywrightException{
		Browser browser = null;

		try {
			browser = Playwright.create().chromium()
			.launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
		} catch (Exception e) {
			LOG.error("Error launching browser");
		}
		
		return browser;
	}

	@Override
	public BrowserContext getBrowserContext(Browser browser) throws PlaywrightException{
		BrowserContext context = null;

		try {
			context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
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
	public Page navigateIPOracle(Page page) throws ArgusIpException {
		
		try {
			LOG.info("Navigating to oracle site. . .");
			page.navigate(environ.getProperty("playwright.uri.ip"));
			delay(Integer.parseInt(environ.getProperty(DELAY_M)));
			
			page.waitForLoadState();
			
			if (page.content().contains("Sign in with your organizational account")) {
				LOG.info("Microsoft login required. . .");
				loginMFA(page);
				page.waitForLoadState();	
			}

			delay(Integer.parseInt(environ.getProperty(DELAY_S)));

			return page;
			
		} catch (PlaywrightException e) {
			LOG.error(e);
			throw new ArgusIpException(e.getMessage(), e);
		}
		
	}

	@Override
	public String searchInvoiceDetails(Page page, String poNumber, String fastId) throws PlaywrightException{
		String noteToSupplier = "", noteToReceiver = "", attribute1 = "", approver = "", accountingGroup = "", projectNumber = "";
		String updateStatus = "", poNum = "";

		try {

			delay(Integer.parseInt(environ.getProperty(DELAY_M)));

			poNum = poNumber;

			for (int i = 0; i < 2; i++) {
				Locator loc;
				navigateIPOracle(page);

				LOG.debug("Click oracle navigator. . .");
				if (page.isHidden("id=pt1:_UISnvr:0:nvgpgl2_groupNode_procurement")) {
					page.click("id=pt1:_UISmmLink");
					page.waitForLoadState();
					delay(Integer.parseInt(environ.getProperty(DELAY_M)));
				}
	
				if (page.isHidden("id=pt1:_UISnvr:0:nv_itemNode_procurement_PurchaseOrders")) {
					page.click("id=pt1:_UISnvr:0:nvgpgl2_groupNode_procurement");
					page.waitForLoadState();
					delay(Integer.parseInt(environ.getProperty(DELAY_M)));
				}
				
				LOG.debug("Click Purchase Orders page. . .");
		
				page.click("id=pt1:_UISnvr:0:nv_itemNode_procurement_PurchaseOrders");
				page.waitForLoadState();
				delay(Integer.parseInt(environ.getProperty(DELAY_M)));
				page.waitForLoadState();

				LOG.info("Search in Purchase Orders page. . .");
				loc = page.locator("div.x1g9:has(img[title=Search])");
				loc.click();
			
				page.waitForLoadState();
				delay(Integer.parseInt(environ.getProperty(DELAY_S)));
				loc = page.locator("span.x1z:has(select.x2h)").first();
				loc.locator("select.x2h").first().click();
				page.waitForLoadState();
				delay(Integer.parseInt(environ.getProperty(DELAY_M)));
				loc.locator("select.x2h").selectOption("1");
				
				page.waitForLoadState();
				delay(Integer.parseInt(environ.getProperty(DELAY_S)));
				loc = page.locator("table >> input[placeholder=Keywords]");
				loc.click();
				loc.fill(poNum);
				
				page.waitForLoadState();
				delay(Integer.parseInt(environ.getProperty(DELAY_S)));
				loc = page.locator("a:has(img[title=Search])").nth(1);
				loc.click();
				
				page.waitForLoadState(LoadState.NETWORKIDLE);
				page.waitForLoadState();
				delay(Integer.parseInt(environ.getProperty(DELAY_M)));
				page.waitForLoadState(LoadState.NETWORKIDLE);
				page.waitForLoadState();

				loc = page.locator("label:has-text('ATTRIBUTE1 : PO Type')");
				delay(Integer.parseInt(environ.getProperty(DELAY_L)));
				LOG.info(loc.count());
				
				if (loc.count() <= 0) {
					if (i==1) {
						LOG.info("Po Number " + poNum + " Not Found");
						updateStatus = "Not Found";
						updateInvoiceDetails("","","","","","","",fastId, environ.getProperty("ip.web.service.update.status.invalid"));
						
						return updateStatus;
					}
					poNum = "AC" + poNum;
				}
				else {
					break;
				}
			}
		
			page.click("text=Notes and Attachments");
			page.waitForLoadState();

			delay(Integer.parseInt(environ.getProperty(DELAY_M)));

			attribute1 = page.locator("span.xpg:right-of(label:has-text('ATTRIBUTE1 : PO Type'))").first().textContent();
			noteToSupplier = page.locator("td:right-of(label:has-text('Note to Supplier'))").first().textContent();
			noteToReceiver = page.locator("td:right-of(label:has-text('Note to Receiver'))").first().textContent();
			
			if (attribute1.toUpperCase().contains("N") || attribute1.toUpperCase().contains("E")) {
				accountingGroup = "Equipment Accounting";
			}
			else {
				accountingGroup = "Expense Accounting";
			}
			
			LOG.info("Updating record in Database and Sharepoint. . .");
			updateStatus = updateInvoiceDetails("",noteToSupplier,noteToReceiver,approver,accountingGroup,projectNumber,"",fastId, environ.getProperty("ip.web.service.update.status"));
			
			emailService.sendSimpleMessage(environ.getProperty("mail.ip.from"), 
				environ.getProperty("mail.ip.from.alias"), 
				environ.getProperty("mail.ip.recipients",String[].class), 
				environ.getProperty("mail.ip.cc", String[].class), 
				"Invoice Processing - Update Status | " + poNum, 
				updateStatus, 
				HIGH_PRIORITY, 
				false);

		} catch (Exception e) {
			updateStatus = "ERROR";
			LOG.error("Error in searchInvoiceDetail function for poNumber " + poNumber);
			LOG.error(e.getMessage());
		}
		
		return updateStatus;
	}

	public String formatPONum(String poNumber){
		String outString = "";

		if (!poNumber.contains("AC")) {
			outString = "AC" + poNumber;	
		}
		else {
			outString = poNumber;	
		}

		if (poNumber.length() > 9) {
			outString = outString.substring(0,9);
		}

		if (!useRegex(outString,"\\d\\d\\d\\d\\d\\d\\d")) {
			outString = "No";
		}

		return outString;
	}
	
	private boolean useRegex(String input, String strPatttern) {
        final Pattern pattern = Pattern.compile(strPatttern, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
	}

	public String getValueByLabelRow(Page page, String elemId, String labelName) {
		String elemValue = "";
		ElementHandle elem = page.querySelector("id=" + elemId);
		List<ElementHandle> elemList = elem.querySelectorAll("td");
		for (ElementHandle elem2 : elemList) {
			if (!elem2.innerText().contains(labelName)) {
				elemValue = elem2.innerText();
			}
		}
		elem = null;
		elemList.clear();
		return elemValue;
	}
	
	@Override
	public String updateInvoiceDetails(String poLine, String noteToSupplier, String noteToReceiver,
    	String approver, String accountingGroup, String projectNo, String lawsonReceiver, String fastId, String status){
		String updateInvoiceDetails = "";
		
		LOG.info("Updating Invoice details. . .");

		for (int i = 0; i < Integer.parseInt(environ.getProperty("invoice.update.retry")); i++) {
			try {
				delay(Integer.parseInt(environ.getProperty(DELAY_S)));
				updateInvoiceDetails = updateInvoice("", poLine, noteToSupplier, noteToReceiver, approver, accountingGroup, projectNo,
					lawsonReceiver, fastId, status);
				if (updateInvoiceDetails.toUpperCase().contains("SUCCESS")) {
					LOG.info("Update success. . .");
					return updateInvoiceDetails;
				}
				
			} catch (RestClientException e) {
				LOG.error(e);
				LOG.info("Update API request for " + fastId + " failed, retry: " + Integer.toString(i + 1));
				if (i == (Integer.parseInt(environ.getProperty("invoice.update.retry"))-1)) {
					LOG.error(e);
					return e.getMessage();
				}
			} 
		}

		return updateInvoiceDetails;

	}

	@Override
	public Page loginMFA(Page page) {

		try {
			
			page.fill("id=userNameInput", environ.getProperty("encrypted.ip.property.username"));
			page.fill("id=passwordInput", environ.getProperty("encrypted.ip.property.password"));
			page.click("id=submitButton");
			page.waitForLoadState(LoadState.LOAD);
			
			delay(Integer.parseInt(environ.getProperty("delay.medium")));
			
			Locator loc = page.locator("label:has-text('Incorrect user ID or password. Type the correct user ID and password, and try again.')");
			if (loc.count() > 0) {
				LOG.error("Incorrect Username or Password. . .");
				try {
					emailService.sendSimpleMessage(environ.getProperty("mail.ip.from"), 
					environ.getProperty("mail.ip.from.alias"), 
					environ.getProperty("mail.ip.recipients",String[].class), 
					environ.getProperty("mail.ip.cc", String[].class), 
					"Invoice Processing - Incorrect Password", 
					"Unable to login to Oracle site, Please check and update password. . .", 
					HIGH_PRIORITY, 
					false);

					System.exit(0);	
				} catch (Exception e) {
					LOG.error("Failed to send email. . .");
					System.exit(0);	
				}
			}

			for (int i = 0; i <= Integer.valueOf(environ.getProperty("mfa.attempts")); i++){				
				TimeUnit.SECONDS.sleep(10);
				
				List<ElementHandle> navigatorElements = page.querySelectorAll("id=pt1:_UISmmLink");
			
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
							String mfaCode = getMfaCode(environ.getProperty("ip.mfa.python.script.path"), environ.getProperty("ip.mfa.secret"));
			
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

}
