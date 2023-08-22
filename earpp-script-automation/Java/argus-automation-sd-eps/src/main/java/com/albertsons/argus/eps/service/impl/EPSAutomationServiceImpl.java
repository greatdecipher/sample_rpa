package com.albertsons.argus.eps.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.eps.exception.ArgusEPSException;
import com.albertsons.argus.eps.service.EPSAutomationService;
import com.albertsons.argus.eps.ws.bo.ResponseGetIncidentDetailBO;
import com.albertsons.argus.eps.ws.bo.ResponseUpdateIncidentDetailsBO;
import com.albertsons.argus.eps.ws.bo.ResponseValidateIncidentBO;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;

@Service
public class EPSAutomationServiceImpl implements EPSAutomationService {
    public static final Logger LOG = LogManager.getLogger(EPSAutomationServiceImpl.class);
	
	@Autowired
    private Environment environ;
	
	@Autowired
    private RestTemplate restTemplate;
    
	@Autowired
	private PlaywrightAutomationService PlaywrightService;

	@Autowired
	private EmailService emailService;

	private Browser browser = null;
	private BrowserContext context;
	private Page page;

	private final String botRunner = System.getProperty("user.name");

	@Autowired
    private AutomationService<ResponseUpdateIncidentDetailsBO> jsonResponseUpdateIncidentBOService;
	
	@Autowired
	private AutomationService<ResponseValidateIncidentBO> jsonResponseValidateIncidentBOService;

	@Autowired
	private AutomationService<ResponseGetIncidentDetailBO> jsonResponseGetIncidentDetailBOService;

	@Override
	public ResponseGetIncidentDetailBO getIncidentDetailFromJson(String jsonString) {
		return jsonResponseGetIncidentDetailBOService.toJson(jsonString);
	}

	@Override
	public ResponseValidateIncidentBO getValidateIncidentDetails(String requestBody, String decodedUrl) throws RestClientException {
		LOG.log(Level.DEBUG, () -> "start method getValidateIncidentDetails(). . .");
		
 		return jsonResponseValidateIncidentBOService.toJson(restTemplate.exchange(decodedUrl, 
		HttpMethod.GET, getHttpEntity(requestBody,"Authorization"),String.class).getBody());
	}

	@Override
    public ResponseUpdateIncidentDetailsBO updateIncidentDetails(String requestBody, String decodedUrl) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method updateIncidentDetails(). . .");
		String strJsonToObject = "";
		HttpEntity httpEnt = getHttpEntity(requestBody,"Authorization");
		String respEnt = restTemplate.exchange(decodedUrl, HttpMethod.PUT,httpEnt,String.class).getBody();
        LOG.debug(respEnt);
		try {
			strJsonToObject = respEnt.substring(0,respEnt.indexOf("assigned_to"));
			strJsonToObject = strJsonToObject + "assigned_to\":\"\"}}";
			LOG.debug(strJsonToObject);
		} catch (Exception e) {
			LOG.error("Error encountered in updateIncidentDetails. . .");
			LOG.error(e);
		}
		return jsonResponseUpdateIncidentBOService.toJson(strJsonToObject);
    }
    
    private HttpEntity<Object> getHttpEntity(String requestBody,String headerName){
        HttpHeaders headers = new HttpHeaders();
		LOG.debug("Start method getHttpEntity. . .");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(headerName, "Basic " + environ.getProperty("eps.web.service.auth.header.value"));
        
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
			LOG.error(e);
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
			LOG.error(e);
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
			LOG.error(e);
		}

		return page;
	}

	@Override
	public Page mainEPSProcess(String sysId, String sysUpdatedBy, String incidentNumber, String callerId, String snowUrlGet, String snowUrlUpdate, 
		String firstName, String lastName, String prescription, String description) {
		
		String storeNum;
		String siteAvail = "";
		String epsWebsite = environ.getProperty("playwright.uri.eps");
		String epsWebsiteCheck = "";
		String responseBody= "", emailBodyFail = "";
		AutomationUtil util = new AutomationUtil();

		LOG.info("EPS Main Task - Bot checks for the prescription number from workflow token monitor and removes the task");

		try {
			if (callerId.length() <= 0) {
				LOG.info("Store Number is Null");
				epsFourStoreNotFound(sysId,sysUpdatedBy,incidentNumber,snowUrlGet,snowUrlUpdate,description);				
			}
			else {
				if (callerId.contains("c")) {
					storeNum = callerId.substring(callerId.indexOf("Store") + 6, callerId.indexOf("c")).trim();	
				}
				else if(callerId.toUpperCase().contains("DEV")){
					storeNum = callerId.substring(0,callerId.indexOf("DEV")).trim();
				}
				else {
					storeNum = callerId.substring(0,callerId.indexOf(".store")).trim();
				}

				if (storeNum == "") {
					LOG.info("Store Number is Null");
					epsFourStoreNotFound(sysId,sysUpdatedBy,incidentNumber,snowUrlGet,snowUrlUpdate,description);				
				} else { 
					epsWebsite = epsWebsite.replace("XXXX", storeNum);
					epsWebsiteCheck = epsWebsite;
					for (int i = 0; i < 3; i++) {
						switch (i) {
							case 0:
								epsWebsiteCheck = epsWebsite.replace("YYYYY",environ.getProperty("eps.port1"));
								break;
							case 1:
								epsWebsiteCheck = epsWebsite.replace("YYYYY",environ.getProperty("eps.port2"));
								break;
							case 2:
								epsWebsiteCheck = epsWebsite.replace("YYYYY",environ.getProperty("eps.port3"));	
								break;
							default:
								break;
						}
	
						LOG.info("Checking if URL is accessible: " + epsWebsiteCheck);
	
						siteAvail = runCheckSiteAvailabilityScript(environ.getProperty("eps.script.command") + " " + epsWebsiteCheck);
	
						if (siteAvail.contains("0")) {
							LOG.info("Opening " + epsWebsiteCheck);
							
							browser = getBrowser();
							context = getBrowserContext(browser);
							page = getPage(context);
	
							navigateEPSSite(page, epsWebsiteCheck);
							if (checkElementIsVisible(page, "Search Criteria", "text")) {
								LOG.info("Logged in to the Workflow Monitor Token website successfully.");
								removePrescriptionEPS(page, firstName, lastName, prescription, sysId, sysUpdatedBy, incidentNumber, snowUrlGet, snowUrlUpdate, description);
							} else {
								epsLoginFailed(description, incidentNumber, sysId, sysUpdatedBy, snowUrlGet, snowUrlUpdate);
							}
	
							browser.close();
							break;
						}
					}
	
					if (siteAvail.contains("1")) {
						LOG.info("EPS Workflow Token Monitor failed to load.");
						responseBody = "{\"assignment_group\":\""+environ.getProperty("eps.assignment.group") +
							"\",\"assigned_to\":\"" + sysUpdatedBy + "\",\"work_notes\":\"EPS Workflow Token Monitor failed to load. Please continue troubleshooting BAU. Worklog Updated by : BOT-"+ botRunner + 
							"\",\"state\":\"In Progress\"}";
						
						emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
							incidentNumber + "\n" +
							"--------------------------------------------------------------------------------------------------" + "\n" +
							"PUT Request Data:\n" + responseBody;
	
						ResponseUpdateIncidentDetailsBO updateIncidentBO = callUpdateIncidentApi(sysUpdatedBy, snowUrlUpdate, responseBody, emailBodyFail);
						
						emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
							incidentNumber + "\n" +
							"--------------------------------------------------------------------------------------------------" + "\n" +
							"Failed to validate Put request";
						
						ResponseValidateIncidentBO validateIncidentBO = callValidateIncidentApi(sysUpdatedBy, snowUrlGet, emailBodyFail);
						
						if (!validateIncidentBO.getIncidentDetailsBo()
								.get(0).getWorkNotes()
								.contains("Token Monitor failed to load") || 
							!validateIncidentBO.getIncidentDetailsBo()
								.get(0).getState().contains("In Progress") ||
							!validateIncidentBO.getIncidentDetailsBo()
								.get(0).getAssignmentGroup()
								.getDisplayValue().contains(environ.getProperty("eps.assignment.group"))){
							
							emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
								environ.getProperty("mail.eps.from.alias"), 
								environ.getProperty("mail.eps.recipients",String[].class), 
								environ.getProperty("mail.eps.cc",String[].class), 
								"Compare Failed for " + incidentNumber, 
								"Put validation failed for " + incidentNumber + "\n\n" +
								"Process: mainEPSProcess",
								HIGH_PRIORITY, false);
							System.exit(0);	
						}
						return page;
					}
				}

				
			}

		} catch (Exception e) {
			LOG.error("Error encountered in mainEPSProcess. . .");
			LOG.error(e);
			if (browser != null) {
				browser.close();
			}
		}

		return page;
	}	

	public void epsLoginFailed (String description, String incidentNumber, String sysId, String sysUpdatedBy, String snowGetUrl, String snowUpdateUrl) {
		AutomationUtil util = new AutomationUtil();
		String emailBodyFail = "", responseBody = "";
		try {
			emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
				environ.getProperty("mail.eps.from.alias"), 
				environ.getProperty("mail.eps.recipients",String[].class), 
				environ.getProperty("mail.eps.cc",String[].class), 
				environ.getProperty("mail.eps.subject.update.fail").replace("incNo", incidentNumber), 
				"Description: " + description + " \n\n" +
				"Incident Number : " + incidentNumber + " \n\n" +
				"EPS Workflow Token Monitor login failed. Please continue troubleshooting BAU. \n\n" +
				botRunner + "\n\n" +
				util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),""),
				HIGH_PRIORITY, false);

			responseBody = "{\"assignment_group\":\"" + environ.getProperty("eps.assignment.group") + 
				"\",\"assigned_to\":\"" + sysUpdatedBy + "\"," + 
				"\"work_notes\":\"EPS Workflow Token Monitor login failed. Please continue troubleshooting BAU.Worklog Updated by : BOT-" + 
				botRunner + "\",\"state\":\"In Progress\"}";

			emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
				incidentNumber + "\n" +
				"--------------------------------------------------------------------------------------------------" + "\n" +
				"PUT Request Data:\n" + responseBody;
			
			ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = callUpdateIncidentApi(sysUpdatedBy, snowUpdateUrl, responseBody, emailBodyFail);

			emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
				incidentNumber + "\n" +
				"--------------------------------------------------------------------------------------------------" + "\n" +
				"Failed to validate Put request";

			ResponseValidateIncidentBO validateIncidentDetailsBO = callValidateIncidentApi(sysUpdatedBy, snowGetUrl, emailBodyFail);

			if (!validateIncidentDetailsBO.getIncidentDetailsBo()
					.get(0).getWorkNotes()
					.contains("EPS Workflow Token Monitor login failed") || 
				!validateIncidentDetailsBO.getIncidentDetailsBo()
					.get(0).getState().contains("In Progress") ||
				!validateIncidentDetailsBO.getIncidentDetailsBo()
					.get(0).getAssignmentGroup()
					.getDisplayValue().contains(environ.getProperty("eps.assignment.group"))){
				
				emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
					environ.getProperty("mail.eps.from.alias"), 
					environ.getProperty("mail.eps.recipients",String[].class), 
					environ.getProperty("mail.eps.cc",String[].class), 
					"Compare Failed for " + incidentNumber, 
					"Put validation failed for " + incidentNumber + "\n\n" +
					"Process: epsLoginFailed",
					HIGH_PRIORITY, false);
				System.out.println("Failed to update worknotes for EPS Workflow Token Monitor login failed. . .");
				System.exit(0);	
			}
			else {
				System.out.println("EPS Workflow Token Monitor login failed. Please continue troubleshooting BAU.");
			}

		} catch (ArgusMailException e) {
			LOG.error("Error on epsLogin method. . .");
			LOG.error(e);
		}

	}

	public ResponseValidateIncidentBO callValidateIncidentApi(String sysUpdatedBy, String snowGetUrl, String emailBodyFail) {
		ResponseValidateIncidentBO validateIncidentBO = null;

		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				validateIncidentBO = getValidateIncidentDetails("", snowGetUrl);	
				break;
			} catch (Exception e) {
				if (i == Integer.parseInt(environ.getProperty("eps.retry")) -1) {
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
							environ.getProperty("mail.eps.from.alias"), 
							environ.getProperty("mail.eps.recipients",String[].class), 
							environ.getProperty("mail.eps.cc",String[].class), 
							environ.getProperty("mail.eps.subject.update.fail"), 
							emailBodyFail,
							HIGH_PRIORITY, false);
					} catch (ArgusMailException e1) {
						LOG.error("Failed to send email update. . .");
						LOG.info(emailBodyFail);
					}
					System.exit(0);
				}
			}
		}
		return validateIncidentBO;
	}

	public ResponseUpdateIncidentDetailsBO callUpdateIncidentApi(String sysUpdatedBy, String snowUpdateUrl, 
		String responseBody, String emailBodyFail) {
		ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = null;

		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				updateIncidentDetailsBO = updateIncidentDetails(responseBody, snowUpdateUrl);	
				break;
			} catch (Exception e) {
				if (i == Integer.parseInt(environ.getProperty("eps.retry")) -1) {
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
							environ.getProperty("mail.eps.from.alias"), 
							environ.getProperty("mail.eps.recipients",String[].class), 
							environ.getProperty("mail.eps.cc",String[].class), 
							environ.getProperty("mail.eps.subject.update.fail"), 
							emailBodyFail,
							HIGH_PRIORITY, false);
					} catch (ArgusMailException e1) {
						LOG.error("Failed to send email update. . .");
						LOG.info(emailBodyFail);
					}
					System.exit(0);
				}
			}
		}
		return updateIncidentDetailsBO;
	}

	@Override
	public String epsValidateInput(String sysId, String sysUpdatedBy, String incidentNumber,
			String description, String snowUrlGet, String snowUrlUpdate, String callerId) {
		
		String output = "", firstName = "", lastName = "", prescription = "";
		String state = "Active";

		try {
			LOG.info("Checking  for Patients Last OR  First Name OR  Prescription is empty,if empty it is not valid incident and sends an email to SNOW and Ticket creator else it will continue with the flow.");
			
			description = description.replace("\\r", "");
			
			String descriptionLower = description.toLowerCase();
			prescription = descriptionLower.substring(descriptionLower.indexOf("rx") + 2, descriptionLower.indexOf("\\n",descriptionLower.indexOf("rx"))).trim();
			firstName = description.substring(description.toLowerCase().indexOf("first name:") + 10, description.toLowerCase().indexOf("\\n",description.toLowerCase().indexOf("first name"))).replace(":","").trim();
			
			try {
				lastName = description.substring(description.toLowerCase().indexOf("last name:") + 9, description.toLowerCase().indexOf("\\n",description.toLowerCase().indexOf("last name:"))).replace(":","").trim();	
			} catch (Exception e) {
				LOG.info("End of string is Last Name. . .");
				lastName = description.substring(description.toLowerCase().indexOf("last name:") + 9).replace(":", "").trim();	
			}
			
			description = description.replace("\\n", "");

			LOG.info(firstName + " " + lastName + "-" + prescription);
			LOG.info("Posting the acknowledgement to SNOW");

			output = updateValidInput(firstName,lastName,prescription,sysId,snowUrlUpdate,snowUrlGet,sysUpdatedBy,description);

			mainEPSProcess(sysId,sysUpdatedBy,incidentNumber,callerId,snowUrlGet,snowUrlUpdate, firstName, lastName, prescription, description);

		} catch (Exception e) {
			if (browser != null) {
				browser.close();
			}
			LOG.info("Invalid command : Prescription Number/First Name/Last Name is missing in the description. " 
					+ firstName + " " + lastName + " " + prescription);
			
			updateErrorIncidentAPICall(state, sysId, sysUpdatedBy, description, snowUrlUpdate, snowUrlGet);	
			output = "INVALID INPUT";
		}

		return output;
	}

	public String updateValidInput(String firstName, String lastName, String prescription, String sysID, String snowUrlUpdate, String snowUrlGet, String sysUpdatedBy, String description) {
		String output = "";
		String putUpdate = "";
		AutomationUtil util = new AutomationUtil();
		
		putUpdate = util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + 
			" begin EPS No Task Available in Data Entry for " +
			firstName + " " + lastName + " " + prescription;

		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				LOG.info("Updating Worknotes and State to Active. . .");
				ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = updateIncidentDetails("{"
					+ "\"work_notes\": \"" + putUpdate + "\","
					+ "\"state\": \"Active\"}", snowUrlUpdate);
				LOG.info("Updating Worknotes and State complete. . .");

				if (!updateIncidentDetailsBO.getIncidentDetailsBo().getWorkNotes().contains("begin EPS No Task Available") ||
					!updateIncidentDetailsBO.getIncidentDetailsBo().getState().contains("Active")) {
						if (i == Integer.parseInt(environ.getProperty("eps.retry")) -1) {
							emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
							environ.getProperty("mail.eps.from.alias"), 
							environ.getProperty("mail.eps.recipients",String[].class), 
							environ.getProperty("mail.eps.cc",String[].class), 
							environ.getProperty("mail.eps.subject.update.fail"), 
							"SNOW Update overwritten by SNOW business rules. \n\n" +
							botRunner + "\n\n" + 
							updateIncidentDetailsBO.toString(),
							HIGH_PRIORITY, false);
							System.out.println("Input validation worknotes and state update failed. . .");
							System.exit(0);
						}
				}
				else {
					break;
				}
				
			} catch (Exception err) {
				LOG.error(err.getMessage() + 
					"Posting the acknowledgement to SNOW " +
					"Posting to SNOW Worklog failed." +
					"URI: " + snowUrlUpdate +
					"vsys_id: " + sysID +
					"vcounter: " + i);

				if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
					LOG.error("Failed posting Worklog to SNOW -  Invalid command: " +
						"Prescription Number/First Name/Last Name is missing in the description." +
						"Please continue troubleshooting BAU");
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
						environ.getProperty("mail.eps.from.alias"), 
						environ.getProperty("mail.eps.recipients",String[].class), 
						environ.getProperty("mail.eps.cc",String[].class), 
						environ.getProperty("mail.eps.subject.fail"), 
						"PUT Request Data:\n\n" +
						"{'work_notes':'"+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + 
						"begin EPS No Task Available in Data Entry for " + firstName + " " + lastName + " " + prescription + ". Worklog Updated by : " +
						"'" + botRunner + "','state':'Active'}",
						HIGH_PRIORITY, false);
						System.out.println("Failed posting Worklog to SNOW -  Invalid command: " +
							"Prescription Number/First Name/Last Name is missing in the description." +
							"Please continue troubleshooting BAU");
						System.exit(0);

					} catch (ArgusMailException e) {
						LOG.error(e);
						LOG.info("Failed to send email - Update Incident Failed API.");
						System.out.println("Failed to send email - Update Incident API Failed");
						System.exit(0);
					}
						
				}
			}
		}

		return output;
	}

	public String updateErrorIncidentAPICall(String incState, String sysId, String sysUpdatedBy, String description, String snowUrlUpdate, String snowUrlGet) {
		String outputString = "", emailBodyFail;
		String workNotes = "";
		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = updateIncidentDetails("{"
					+ "\"assignment_group\": \"" + environ.getProperty("eps.web.service.assignment.group") + "\","
					+ "\"assigned_to\": \"" + environ.getProperty("eps.web.service.assigned.to") + "\","
					+ "\"work_notes\": \"" + workNotes + "\","
					+ "\"state\": \""+ incState +"\"}", snowUrlUpdate);
				System.out.println(updateIncidentDetailsBO.toString());
				break;
			} catch (Exception err) {
				LOG.error(err.getMessage() + 
					" vput_response: " + err + ". vsys_id: " + sysId + 
					". vsys_updated_by: " + sysUpdatedBy + 
					". Runner: " + System.getProperty("user.name") + 
					". PUT url: https://safeway.service-now.com/api/now/table/incident/" + sysId + "?sysparm_display_value=true&sysparm_fields=number%2Cstate%2Cwork_notes%2Cassigned_to%2Csys_updated_by%2Cclose_code%2Cclose_notes%2Cassignment_group. " +
					"Retry count: " + i + ".");

				if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
					LOG.error("Failed posting Worklog to SNOW -  Invalid command: " +
						"Prescription Number/First Name/Last Name is missing in the description." +
						"Please continue troubleshooting BAU");
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
						environ.getProperty("mail.eps.from.alias"), 
						environ.getProperty("mail.eps.recipients",String[].class), 
						environ.getProperty("mail.eps.cc",String[].class), 
						environ.getProperty("mail.eps.subject.fail"), 
						"Hi "+ sysUpdatedBy + "," + "\n\n" +
						"Invalid command: Prescription Number/First Name/Last Name is missing in the description :  " + description + " .\n" + 
						"Please continue troubleshooting BAU .\n\n\n" +
						System.getProperty("user.name"),
						HIGH_PRIORITY, false);
						System.exit(0);
					} catch (ArgusMailException e) {
						LOG.error(e);
						LOG.info("Failed to send email - Update Incident Failed API.");
						System.exit(0);
					}
						
				}
			}
		}

		LOG.info("Validating  the PUT2 updation by subsequent GET operation");
		
		emailBodyFail = "Hi "+ sysUpdatedBy + "," + "\n\n" +
			"Invalid command: Prescription Number/First Name/Last Name is missing in the description :  " + description + " .\n" + 
			"Please continue troubleshooting BAU .\n\n\n" +
			System.getProperty("user.name");
		
		validatePutUpdate(sysId, sysUpdatedBy, description, emailBodyFail, snowUrlGet);

		return outputString;
	}

	public ResponseValidateIncidentBO validatePutUpdate(String sysId, String sysUpdatedBy, String description, String emailBodyFail, String snowUrlGet) {
		ResponseValidateIncidentBO validateIncidentBO = null;
		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				validateIncidentBO = getValidateIncidentDetails("",snowUrlGet);
				break;
			} catch (Exception e) {
				LOG.error(e.getMessage() + 
					" vsys_id: " + sysId + 
					". vsys_updated_by: " + sysUpdatedBy + 
					". Runner: " + System.getProperty("user.name") + 
					". GET url: " + snowUrlGet + ". " +
					"Retry count: " + i + ".");

				if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
					LOG.error(e.getMessage() + 
					"GET to Validate PUT Update failed." +
					"URI: " + snowUrlGet +
					"vsys_id: " + sysId +
					"vcounter: " + i +
					"vGet_response: " + e);

					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
						environ.getProperty("mail.eps.from.alias"), 
						environ.getProperty("mail.eps.recipients",String[].class), 
						environ.getProperty("mail.eps.cc",String[].class), 
						environ.getProperty("mail.eps.subject.fail"), 
						emailBodyFail,
						HIGH_PRIORITY, false);
						System.exit(0);
					} catch (ArgusMailException er) {
						LOG.error(e);
						LOG.info("Failed to send email - Validate Failed update.");
						return validateIncidentBO;
					}

				}
			}
		}
		return validateIncidentBO;
	}

	

	@Override
	public void epsFourStoreNotFound(String sysId, String sysUpdatedBy, String incidentNumber, String snowUrlGet, String snowUrlUpdate, String description) {
		String emailBodyFail;
		AutomationUtil util = new AutomationUtil();
		LOG.info("Store Number Not Found,it will post the worklog in SNOW");
		
		emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
			incidentNumber + "\n" +
			"--------------------------------------------------------------------------------------------------" + "\n" +
			"PUT Request Data:\n" +
			"{\"assignment_group\":\"Service Desk Support\",\"assigned_to\":\"" + sysUpdatedBy + "\"," +
			"\"work_notes\":\"Failed to truncate store number. Please continue troubleshooting BAU.Worklog Updated by : " + botRunner + "\",\"state\":\"In Progress\"}";
		
		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				
				ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = updateIncidentDetails("{\"assignment_group\":\"Service Desk Support\"," +
				"\"assigned_to\":\"" + sysUpdatedBy + "\"," +
				"\"work_notes\":\"Failed to truncate store number. Please continue troubleshooting BAU.Worklog Updated by : BOT-" + botRunner + "\"," +
				"\"state\":\"In Progress\"}", snowUrlUpdate);

				if (!updateIncidentDetailsBO.getIncidentDetailsBo().getWorkNotes().contains("Failed to truncate store number") ||
					!updateIncidentDetailsBO.getIncidentDetailsBo().getState().contains("In Progress")) {
					if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
							environ.getProperty("mail.eps.from.alias"), 
							environ.getProperty("mail.eps.recipients",String[].class), 
							environ.getProperty("mail.eps.cc",String[].class), 
							environ.getProperty("mail.eps.subject.update.fail"), 
							"SNOW Update overwritten by SNOW business rules. \n\n" +
							botRunner + "\n\n" + 
							incidentNumber + "\n" +
							updateIncidentDetailsBO.toString(),
							HIGH_PRIORITY, false);
						System.out.println("Failed to to update worknotes on truncating store number. . .");
						return;
					}
				}
				else {
					System.out.println("Failed to truncate store number. Please continue troubleshooting BAU");
					break;
				}

			} catch (Exception e) {
				LOG.info("Failed to update the worklog. Retry count: " + i);
				if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
						environ.getProperty("mail.eps.from.alias"), 
						environ.getProperty("mail.eps.recipients",String[].class), 
						environ.getProperty("mail.eps.cc",String[].class), 
						environ.getProperty("mail.eps.subject.fail"), 
						emailBodyFail,
						HIGH_PRIORITY, false);
						System.out.println("Failed to to update worknotes on truncating store number. . .");
						System.exit(0);
					} catch (ArgusMailException er) {
						LOG.error(e);
						LOG.info("Failed to send email - Validate Failed update.");
						return;
					}
				}
				
			}
		}
		
		LOG.info("Validate the PUT updation by subsequent GET operation");
		
		//validatePutUpdate(sysId, sysUpdatedBy, description, emailBodyFail, snowUrlGet);
	}

	@Override
	public void epsFivePutBackTask(String sysId, String sysUpdatedBy, String incidentNumber, String snowUrlGet, String snowUrlUpdate, String description, String prescription) {
		String emailBodyFail;
		AutomationUtil util = new AutomationUtil();
		LOG.info("Bot has put back the task with the prescription number given in SNOW and post the worklog in SNOW");
		
		emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
			incidentNumber + "successfully removed " + prescription + " from workflow token monitor\n" +
			"--------------------------------------------------------------------------------------------------" + "\n" +
			"PUT Request Data:\n" +
			"{\"assignment_group\":\"Service Desk Support\",\"assigned_to\":\"" + sysUpdatedBy + "\"," +
			"\"work_notes\":\"Successfully removed " + prescription + " from workflow token monitor.Worklog Updated by : " + botRunner + "\",\"state\":\"In Progress\"}\n\n" +
			"{\"close_code\":\"Solved (Work Around)\",\"close_notes\":\"Successfully removed " + prescription + " from workflow token monitor\"}";
		
		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				
				ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = updateIncidentDetails("{\"assignment_group\":\"Service Desk Support\"," +
					"\"assigned_to\":\"" + sysUpdatedBy + "\"," +
					"\"work_notes\":\"Successfully removed "+ prescription + " from workflow token monitor Worklog Updated by : BOT-" + botRunner + "\"," +
					"\"state\":\"In Progress\"}", snowUrlUpdate);

				updateIncidentDetailsBO = updateIncidentDetails("{\"assignment_group\":\"Service Desk Support\"," +
					"\"close_code\":\"Solved (Work Around)\"," +
					"\"close_notes\":\"Successfully removed " + prescription + " from workflow token monitor\"}", 
					snowUrlUpdate);
				
				LOG.info("Update posted to SNOW successfully");
				if (!updateIncidentDetailsBO.getIncidentDetailsBo().getCloseCode().contains("Solved") ||
					!updateIncidentDetailsBO.getIncidentDetailsBo().getCloseNotes().contains("Successfully removed") ||
					!updateIncidentDetailsBO.getIncidentDetailsBo().getState().contains("In Progress") ) {
					if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
							environ.getProperty("mail.eps.from.alias"), 
							environ.getProperty("mail.eps.recipients",String[].class), 
							environ.getProperty("mail.eps.cc",String[].class), 
							environ.getProperty("mail.eps.subject.update.fail"), 
							"Failed SNOW Update overwritten by SNOW business rules. \n\n" +
							botRunner + "\n\n" + 
							incidentNumber + "\n" +
							updateIncidentDetailsBO.toString(),
							HIGH_PRIORITY, false);
						System.out.println("Successfully removed "+ prescription + " from workflow token monitor Worklog but failed to update incident details. . .");
						return;
					}
				}
				else {
					System.out.println("Successfully removed "+ prescription + " from workflow token monitor Worklog Updated by : BOT-" + botRunner);
					break;
				}

			} catch (Exception e) {
				LOG.info("Failed to update the worklog. Retry count: " + i);
				if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
						environ.getProperty("mail.eps.from.alias"), 
						environ.getProperty("mail.eps.recipients",String[].class), 
						environ.getProperty("mail.eps.cc",String[].class), 
						environ.getProperty("mail.eps.subject.fail"), 
						emailBodyFail,
						HIGH_PRIORITY, false);
						LOG.info("PUT failure emails sent.");
						System.out.println("Successfully removed "+ prescription + " from workflow token monitor but failed to update incident details. . .");
						System.exit(0);
					} catch (ArgusMailException er) {
						LOG.error(e);
						LOG.info("Failed to send email - Validate Failed update.");
						return;
					}
				}
				
			}
		}
		
		LOG.info("Validate the PUT updation by subsequent GET operation");
	}

	@Override
	public void epsSixCheckedOutNotFound(String sysId, String sysUpdatedBy, String incidentNumber, String snowUrlGet, String snowUrlUpdate, String description, String prescription, 
		String firstName, String lastName, String ldapId) {
		String emailBodyFail;
		AutomationUtil util = new AutomationUtil();
		LOG.info("INFO|Posting the worklog to SNOW after checking the LDAP ID and if it is not found - Issue does not fall under Scenario 1 - Multiple station accessing same prescription. Incident: " + incidentNumber);
		
		emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
			incidentNumber + "Failed Posting the worklog to SNOW after checking the LDAP ID and if it is not found - Issue does not fall under Scenario 1 - Multiple station accessing same prescription.\n" +
			"--------------------------------------------------------------------------------------------------" + "\n" +
			"PUT Request Data:\n" +
			"{\"assignment_group\":\"Service Desk Support\"," +
			"\"assigned_to\":\"" + sysUpdatedBy + "\"," +
			"\"work_notes\":\"Prescription Number : " + prescription + 
			"  for " + firstName + " " + lastName + " is not checked out. Please continue troubleshooting BAU. Worklog Updated by : BOT-" + botRunner + "\"," +
			"\"state\":\"In Progress\"}";
		
		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				
				ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = updateIncidentDetails("{\"assignment_group\":\"Service Desk Support\"," +
					"\"assigned_to\":\"" + sysUpdatedBy + "\"," +
					"\"work_notes\":\"Prescription Number : " + prescription + 
					"  for " + firstName + " " + lastName + " is not checked out. Please continue troubleshooting BAU. Worklog Updated by : BOT-" + botRunner + "\"," +
					"\"state\":\"In Progress\"}", snowUrlUpdate);

				if (!updateIncidentDetailsBO.getIncidentDetailsBo().getWorkNotes().contains("is not checked out")) {
					if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
							environ.getProperty("mail.eps.from.alias"), 
							environ.getProperty("mail.eps.recipients",String[].class), 
							environ.getProperty("mail.eps.cc",String[].class), 
							environ.getProperty("mail.eps.subject.update.fail"), 
							"SNOW Update overwritten by SNOW business rules. \n\n" +
							botRunner + "\n\n" + 
							incidentNumber + "\n" +
							updateIncidentDetailsBO.toString(),
							HIGH_PRIORITY, false);
						System.out.println("Failed to update worklogs for prescription not checked out. . .");
						return;
					}
				}
				else {
					System.out.println("Prescription Number : " + prescription + "  for " + firstName + " " + lastName + " is not checked out. Please continue troubleshooting BAU. Worklog Updated by : BOT-" + botRunner);
					break;
				}

				LOG.info("INFO|PUT SUCCESS. vsys_id: " + sysId + ". vsys_updated_by: " + sysUpdatedBy + 
					". vPatients_FirstName: "+firstName+". vPatients_LastName: "+lastName+". BOT: ."+botRunner+
					" PUT url: " + snowUrlUpdate);
				
				

			} catch (Exception e) {
				LOG.info("Failed to update the worklog. Retry count: " + i);
				if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
						environ.getProperty("mail.eps.from.alias"), 
						environ.getProperty("mail.eps.recipients",String[].class), 
						environ.getProperty("mail.eps.cc",String[].class), 
						environ.getProperty("mail.eps.subject.fail"), 
						emailBodyFail,
						HIGH_PRIORITY, false);
						LOG.info("PUT failure emails sent.");
						System.out.println("Failed to update worklogs for prescription not checked out. . .");
						System.exit(0);
					} catch (ArgusMailException er) {
						LOG.error(e);
						LOG.info("Failed to send email - Validate Failed update.");
						return;
					}
				}
				
			}
		}
		
	}

	@Override
	public Page navigateEPSSite(Page page, String siteUrl) throws ArgusEPSException {
		
		try {
			
			page.navigate(siteUrl);
			delay(Integer.parseInt(environ.getProperty(DELAY_M)));
			
			page.waitForLoadState();
			
			LOG.info("Entering the credentials for Workflow Monitor Token website");

			PlaywrightService.pageFill(page, "input", "id", "username", environ.getProperty("encrypted.eps.property.username"));
			delay(Integer.parseInt(environ.getProperty(DELAY_S)));
			PlaywrightService.pageFill(page, "input", "id", "password", environ.getProperty("encrypted.eps.property.password"));
			delay(Integer.parseInt(environ.getProperty(DELAY_S)));
			PlaywrightService.pageClick(page, "input", "id", "submit");
			page.waitForLoadState();
			delay(Integer.parseInt(environ.getProperty(DELAY_S)));
			return page;
			
		} catch (PlaywrightException e) {
			LOG.error("Error in navigateEPSSite. . .");
			LOG.error(e);
			throw new ArgusEPSException(e.getMessage(), e);
		}
		
	}

	@Override
	public Page removePrescriptionEPS(Page page, String firstName, String lastName, String prescription, String sysId, String sysUpdatedBy,
		String incidentNumber, String snowUrlGet, String snowUrlUpdate, String description) throws PlaywrightException{
		Boolean prescriptionFound = false;
		String ldapId;
		ElementHandle eh = getElementByAttribute(page, "input", "name", "searchCriteria.firstName");
		delay(1);
		eh.fill(firstName);
		delay(1);
		eh = getElementByAttribute(page, "input", "name", "searchCriteria.lastName");
		delay(1);
		eh.fill(lastName);
		delay(1);
		eh= getElementByAttribute(page, "input", "name", "submit");
		delay(1);
		eh.click();
		page.waitForLoadState();
		delay(1);
		eh = page.querySelector(":nth-match(table, 3)");
		List<ElementHandle> lstRow = eh.querySelectorAll("tr");
		
		for (ElementHandle tr : lstRow) {
			if (tr.innerHTML().contains(prescription)) {
				LOG.info("Prescription found");
				prescriptionFound = true;
				ElementHandle chkboxCol = tr.querySelector(":nth-match(td, 1)");
				ElementHandle assignedCol = tr.querySelector(":nth-match(td, 5)");
				ldapId = assignedCol.innerText();
				if (!assignedCol.innerText().contains("*")) {
					LOG.info("Tick prescription checkbox in EPS monitor");
					chkboxCol.querySelector(":nth-match(input, 1)").check();
					eh = getElementByAttribute(page, "input", "value", "Put Back Checked Tasks");
					eh.click();
					epsFivePutBackTask(sysId, sysUpdatedBy, incidentNumber, snowUrlGet, snowUrlUpdate, description, prescription);
				}
				else {
					LOG.info("Reassigning  the ticket to SD with updating worklog \"Issue does not fall under Scenario 1 - Multiple station accessing same prescription\". Did not find CheckedOutLogin: " + ldapId + ".");
					epsSixCheckedOutNotFound(sysId, sysUpdatedBy, incidentNumber, snowUrlGet, snowUrlUpdate, description, prescription, firstName, lastName, ldapId);
				}

				break;
			}
		}

		if (!prescriptionFound) {
			LOG.info("Reassigning  the ticket to SD with updating worklog \"Prescription Number not found under patient record\"");
			prescriptionNotFoundUpdate(sysId, sysUpdatedBy, snowUrlUpdate, snowUrlGet, prescription, firstName, lastName, incidentNumber);
		}

		return page;
	}

	public void prescriptionNotFoundUpdate(String sysId, String sysUpdatedBy, String snowUrlUpdate, String snowUrlGet, String prescription, String firstName, String lastName, String incidentNumber) {
		AutomationUtil util = new AutomationUtil();
		
		String emailBodyFail = botRunner + "-" + util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"") + "\n" +
		incidentNumber +
		"--------------------------------------------------------------------------------------------------" + "\n" +
		"PUT Request Data:\n" +
		"{\"assignment_group\":\"Service Desk Support\"," +
		"\"assigned_to\":\"" + sysUpdatedBy + "\"," +
		"\"work_notes\":\"Prescription Number : " + prescription + 
		"  not found under " + firstName + " " + lastName + ". Please continue troubleshooting BAU. Worklog Updated by : BOT-" + botRunner + "\"," +
		"\"state\":\"In Progress\"}";

		for (int i = 0; i < Integer.parseInt(environ.getProperty("eps.retry")); i++) {
			try {
				
				ResponseUpdateIncidentDetailsBO updateIncidentDetailsBO = updateIncidentDetails("{\"assignment_group\":\"Service Desk Support\"," +
					"\"assigned_to\":\"" + sysUpdatedBy + "\"," +
					"\"work_notes\":\"Prescription Number : " + prescription + 
					"  not found under " + firstName + " " + lastName + ". Please continue troubleshooting BAU. Worklog Updated by : BOT-" + botRunner + "\"," +
					"\"state\":\"In Progress\"}", snowUrlUpdate);
				break;

			} catch (Exception e) {
				LOG.info("Failed to update the worklog. Retry count: " + i);
				if (i == Integer.parseInt(environ.getProperty("eps.retry")) - 1) {
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
						environ.getProperty("mail.eps.from.alias"), 
						environ.getProperty("mail.eps.recipients",String[].class), 
						environ.getProperty("mail.eps.cc",String[].class), 
						environ.getProperty("mail.eps.subject.fail"), 
						emailBodyFail,
						HIGH_PRIORITY, false);
						LOG.info("PUT failure emails sent.");
						System.exit(0);
					} catch (ArgusMailException er) {
						LOG.error(e);
						LOG.info("Failed to send email - Validate Failed update.");
						return;
					}
				}
				
			}
		}

		ResponseValidateIncidentBO getIncidentDetailBO = validatePutUpdate(sysId, sysUpdatedBy, prescription, emailBodyFail, snowUrlGet);
		
		if (getIncidentDetailBO != null) {
			if (!getIncidentDetailBO.getIncidentDetailsBo().get(0).getWorkNotes().contains("not found under") ||
			!getIncidentDetailBO.getIncidentDetailsBo().get(0).getState().contains("In Progress")) {
				
				try {
					emailService.sendSimpleMessage(environ.getProperty("mail.eps.from"), 
							environ.getProperty("mail.eps.from.alias"), 
							environ.getProperty("mail.eps.recipients",String[].class), 
							environ.getProperty("mail.eps.cc",String[].class), 
							environ.getProperty("mail.eps.subject.fail"), 
							"SNOW Update overwritten by SNOW business rules. \n\n" +
							botRunner + "\n\n" + 
							getIncidentDetailBO.toString(),
							HIGH_PRIORITY, false);
				} catch (ArgusMailException e) {
					LOG.error("Error in prescriptionNotFoundUpdate method. . .");
					LOG.error(e);
				}

				System.out.println("Failed to update worklogs for Prescription Number : " + prescription + "  not found under " + firstName + " " + lastName);
				LOG.info("GET comparison failure email sent for " + incidentNumber + ".");
				System.exit(0);
			}
			else {
				System.out.println("Prescription Number : " + prescription + "  not found under " + firstName + " " + lastName + ". Please continue troubleshooting BAU. Worklog Updated by : BOT-" + botRunner);
			}
		}
		
	}

	@Override
	public Integer checkSiteAvailability(String stringUrl) {
		URL url;
		int responseCode = 0;
		Integer output = 0;

		try {
			url = new URL(stringUrl);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setRequestMethod("GET");
			huc.setAllowUserInteraction(false);
			responseCode = huc.getResponseCode();
			if (responseCode < 200 | responseCode > 300) {
				output = 1;
			}
			else {
				output = 0;
			}
		} catch (Exception e) {
			LOG.info("Error in checking site availability. . .");
			LOG.error(e);
			output = 0;
		}
		
		return output;
	}

	@Override
	public String runCheckSiteAvailabilityScript(String arg) throws IOException{
		
		Process p = Runtime.getRuntime().exec(arg);
		
		String text = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));

		return text;
	}

	@Override
	public ElementHandle getElementByAttribute(Page page, String attribute, String property, String value) {
		ElementHandle outputElemHandle = null;
		List<ElementHandle> eh = page.querySelectorAll(attribute);
		
		for (ElementHandle element : eh) {
			if (element.getAttribute(property).contains(value)) {
				outputElemHandle = element;
				break;
			}
		}

		return outputElemHandle;
	}

	@Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			LOG.error("Error in delay method...");
			LOG.error(e);
		}
	}

	@Override
	public boolean checkElementIsVisible(Page page, String selector, String attrib){
		LOG.log(Level.DEBUG, () -> "Start checkElementIsVisible method . . .");
		boolean bool = false;

		if (page.content().contains(selector)) {
			bool = page.waitForSelector(attrib + "=" + selector).isVisible();
		}
		
		LOG.log(Level.DEBUG, () -> "End checkElementIsVisible method . . .");
		return bool;

	}
}
