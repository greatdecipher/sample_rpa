package com.albertsons.argus.r2r.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domaindbr2r.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbr2r.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbr2r.dto.RecordDTO;
import com.albertsons.argus.domaindbr2r.service.R2ROracleService;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.albertsons.argus.r2r.service.R2RCommonService;
import com.albertsons.argus.r2r.service.R2ROicService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class R2ROicServiceImpl implements R2ROicService {
    private static final Logger LOG = LogManager.getLogger(R2ROicServiceImpl.class);

    @Autowired
    private R2ROracleService oracleService;
    
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
    public List<Object> mainOicTask(FileDetails fileDetails, String execTimestamp) throws ArgusR2RException {
        LOG.log(Level.INFO, () -> "start mainOicTask method. . .");

		this.execTimestamp = execTimestamp;
        List<Object> returnOicObjects = new ArrayList<>(); // initialize
        RecordDTO recordDTO = new RecordDTO(); // initialize

        try {
            TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("wait.before.oic.check").trim())); //wait before checking if there is a file from EDIS

            String updatedFilename = checkFileReceived(fileDetails);

            if (updatedFilename != null){

                fileDetails.setFilename(updatedFilename); // update the filename

                returnOicObjects.add(fileDetails);

                TimeUnit.MINUTES.sleep(fileDetails.getKafkaConsumeTime()); //wait before checking if file is consumed

                recordDTO = checkFileConsumed(fileDetails, 0);

                if (recordDTO != null){
                    LOG.log(Level.INFO, () -> "file is consumed. . .");

                    if (checkTransformStatus(fileDetails) == true){
                        LOG.log(Level.INFO, () -> "file is transformed by query. . .");

                        //end of OIC interface
                        if (recordDTO.getGroup_Id() != null){

                            LOG.log(Level.INFO, () -> "moving to ESS Jobs interface . . .");
                            //checkpoint email
                            String body = "<body>Hello, <br><br>";

                            body += "File: " + fileDetails.getFilename() + " has been successfully processed in OIC interface.";

                            body += "<br><br> This file will now be processed in ESS Jobs interface."; 

                            body += "<br><br>" +
                                    "Thanks & Regards, <br>" + 
                                    "Bot</body>";
                            
                            sendOicEmail(environment.getProperty("mail.argus.recipients", String[].class), 
                                            environment.getProperty("mail.argus.cc", String[].class), 
                                            environment.getProperty("mail.subject.oic.success"), 
                                            body);    
                            
                            returnOicObjects.add(recordDTO);
                            return returnOicObjects;
                        }
                        else{ //group ID is null even if file is transformed, so don't proceed
                        
                            String body = "<body>Hello, <br><br>";

                            body += "File: " + fileDetails.getFilename() + " is consumed and transformed, but group ID is null.";

                            body += "<br><br> The automation for this file has now ended."; 

                            body += "<br><br>" +
                                    "Thanks & Regards, <br>" + 
                                    "Bot</body>";
                            
                            sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                            environment.getProperty("mail.argus.cc", String[].class), 
                                            environment.getProperty("mail.subject.oic.failure"), 
                                            body);   

                        }
                    }

                }

            }
            
            // end automation
            return null;
               

        } catch (Exception e){
            LOG.log(Level.INFO, () -> "error in OIC interface. . .");
            LOG.error(e);
            return null;
        }
        
	}

    @Override
    public String checkFileReceived(FileDetails fileDetails){
        LOG.log(Level.DEBUG, () -> "start checkFileReceived method. . .");
    
        try {
            List<ProcessFileDetailsDTO> pfdDTOs = new ArrayList<>();

            for (int i = 0; i <= Integer.valueOf(environment.getProperty("check.file.received.retry.attempts").trim()); i++){
                
                if (i > 0){ // a retry attempt
                    TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("check.file.received.retry.wait.time").trim())); // wait before trying again
                }

                pfdDTOs = oracleService.getProcessFileDetailsDTOs(fileDetails.getFilename());

                if (!pfdDTOs.isEmpty() && pfdDTOs.size() == 1){ // validation that file is received 
                    
                    String file = pfdDTOs.get(0).getFile_Name();
                    LOG.log(Level.DEBUG, () -> "file exists: " + file + ". . .");

                    LOG.log(Level.DEBUG, () -> "end checkFileReceived method. . .");

                    return file; // pass the filename to the update it in the object
                }
                else{
                    LOG.log(Level.INFO, () -> "file not found in OIC. . .");

                    if (i == Integer.valueOf(environment.getProperty("check.file.received.retry.attempts").trim())){ // end turn
                        
                        String body = "<body>Hello, <br><br>";

                        body += "File: " + fileDetails.getFilename() + " is still missing in OIC after after sending an email alert and waiting for another " + environment.getProperty("check.file.received.retry.wait.time") + " minute(s).";
                
                        body += "<br><br> The automation for this file has now ended."; 

                        body += "<br><br>" +
                                "Thanks & Regards, <br>" + 
                                "Bot</body>";
                            
                        sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                        environment.getProperty("mail.argus.cc", String[].class), 
                                        environment.getProperty("mail.subject.oic.failure"), 
                                        body);   
                                        
                        return null;
                    }
                    else{
                        //email EDIS that file is missing
                        String body = "<body>Hello, <br><br>";

                        body += "File: " + fileDetails.getFilename() + " is missing in OIC after waiting for " + environment.getProperty("wait.before.oic.check") + " minute(s).";

                        body += "<br><br>" +
                                "Thanks & Regards, <br>" + 
                                "Bot</body>";
                        
                        sendOicEmail(environment.getProperty("mail.argus.edis.email", String[].class), 
                                        environment.getProperty("mail.argus.cc", String[].class), 
                                        environment.getProperty("mail.subject.oic.edis.check"), 
                                        body); 
                    }

                }

            }     

            return null;
            
        } catch (Exception e) {
            LOG.log(Level.INFO, () -> "error while checking if file received in OIC. . .");
            LOG.error(e);
            return null;
        }

    }

    @Override
    public RecordDTO checkFileConsumed(FileDetails fileDetails, int greaterConsumedFlag){
        LOG.log(Level.DEBUG, () -> "start checkFileConsumed method. . .");

        try {
            List<RecordDTO> recordDTOs = new ArrayList<>();
            recordDTOs = oracleService.getRecordDTOs(fileDetails.getFilename());

            if (!recordDTOs.isEmpty() && recordDTOs.size() == 1){ // query has results

                RecordDTO record = recordDTOs.get(0);
                int difference = Long.compare(record.getConsumed_Rec(), record.getTotal_Rec());

                LOG.log(Level.DEBUG, () -> "difference of consumed: " + record.getConsumed_Rec() + " and total: " + record.getTotal_Rec() + " is " + difference);

                if (difference == 0){ // equal consumed rec and total rec
                    LOG.log(Level.DEBUG, () -> "consumed is equal to total. . .");
                    return record;
                }
                else if (difference > 0){ // consumed rec is greater than total rec
                    LOG.log(Level.DEBUG, () -> "consumed is greater than total. . .");
                    
                    if (fileDetails.getFilename().contains("EP_MAINT") || fileDetails.getFilename().contains("EP_ISSUE") || fileDetails.getFilename().contains("JE_GTR_NH_PY")){ // only these always have consumed less than total
                        return record;
                    }
                    else{
                        if (greaterConsumedFlag == 0){
                            TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("greater.consumed.retry.wait.time").trim()));
                            checkFileConsumed(fileDetails, 1); // make flag 1 to indicate this is not the first time method is called
                        }
                        else{
                            //email OracleFinancials.IT and end automation
                            String body = "<body>Hello, <br><br>";
    
                            body += "File: " + fileDetails.getFilename() + " has a greater consumed record count than total record count after " + environment.getProperty("greater.consumed.retry.wait.time") + " minute(s).";
    
                            body += "<br><br> The automation for this file has now ended."; 
    
                            body += "<br><br>" +
                                    "Thanks & Regards, <br>" + 
                                    "Bot</body>";
                            
                            sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                            environment.getProperty("mail.argus.cc", String[].class), 
                                            environment.getProperty("mail.subject.oic.ora.fin.it.greater.consumed"), 
                                            body);
    
                            return null;
                        }
                        
                        return null;
                    }
                }
                else{
                    LOG.log(Level.DEBUG, () -> "consumed is less than total. . .");

                    if (fileDetails.getFilename().contains("EP_MAINT") || fileDetails.getFilename().contains("EP_ISSUE") || fileDetails.getFilename().contains("JE_GTR_NH_PY")){ // only these always have consumed less than total
                        return record;
                    }
                    else{
                        for (int i = 0; i <= Integer.valueOf(environment.getProperty("edis.request.republish.attempts").trim()); i++){

                            for (int j = 0; j < Integer.valueOf(environment.getProperty("not.consumed.retry.attempts").trim()); j++){
    
                                TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("not.consumed.wait.time").trim())); // wait before checking again
    
                                List<RecordDTO> recordDTOs2 = new ArrayList<>();
                                recordDTOs2 = oracleService.getRecordDTOs(fileDetails.getFilename());
    
                                if (!recordDTOs2.isEmpty() && recordDTOs2.size() == 1){ // query has results

                                    RecordDTO record2 = recordDTOs2.get(0);
                                    int difference2 = Long.compare(record2.getConsumed_Rec(), record2.getTotal_Rec());
    
                                    if (difference2 >= 0){
                                        return record2;
                                    }
                                }
    
                            }
    
                            //after designated wait time and record is not consumed
                            if (fileDetails.getFilename().substring(0, 6).equalsIgnoreCase("JE_JIF")){
                                
                                //email the job name to OracleFinancials.IT that this is now for ODR and end automation
                                String body = "<body>Hello, <br><br>";
    
                                if (fileDetails.getJobName() != null && !fileDetails.getJobName().equals("") && !fileDetails.getJobName().isEmpty()){
                                    body += "File: " + fileDetails.getFilename() + " with job name: " + fileDetails.getJobName() + " is now for ODR.";
                                }
                                else{
                                    body += "File: " + fileDetails.getFilename() + " (no job name indicated) is now for ODR.";
                                }
    
                                body += "<br><br> The automation for this file has now ended."; 
    
                                body += "<br><br>" +
                                        "Thanks & Regards, <br>" + 
                                        "Bot</body>";
                                
                                sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                                environment.getProperty("mail.argus.cc", String[].class), 
                                                environment.getProperty("mail.subject.oic.ora.fin.it.odr"), 
                                                body);
    
                                return null;
                            }
                            else{
                                if (i > 0){ // a retry attempt after a republish attempt
                                    //email OracleFinancials.IT that file is still not consumed even after requesting edis to republish and end automation
                                    String body = "<body>Hello, <br><br>";
    
                                    body += "File: " + fileDetails.getFilename() + " is still not consumed even after request to republish file.";
                                    
                                    body += "<br><br> The automation for this file has now ended."; 
    
                                    body += "<br><br>" +
                                            "Thanks & Regards, <br>" + 
                                            "Bot</body>";
                                    
                                    sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                                    environment.getProperty("mail.argus.cc", String[].class), 
                                                    environment.getProperty("mail.subject.oic.ora.fin.it.not.consumed"), 
                                                    body);
                                    
                                    return null;
                                }
                                else{
                                    // email EDIS to republish file
                                    String body = "<body>Hello, <br><br>";
    
                                    body += "File: " + fileDetails.getFilename() + " needs to be republished.";
    
                                    body += "<br><br>" +
                                            "Thanks & Regards, <br>" + 
                                            "Bot</body>";
                                    
                                    sendOicEmail(environment.getProperty("mail.argus.edis.email", String[].class), 
                                                    environment.getProperty("mail.argus.cc", String[].class), 
                                                    environment.getProperty("mail.subject.oic.edis.republish"), 
                                                    body);
                                }
                            }
    
                        }

                        return null;
                    }

                }
                 
            }
            else{
                LOG.log(Level.INFO, () -> "file does not exist. . .");
                return null;
            }
            
        } catch (Exception e) {
            LOG.log(Level.INFO, () -> "error while checking if file consumed. . .");
            LOG.error(e);
            return null;
        }

    }

    @Override
    public ProcessInstanceDTO queryTransformStatus(FileDetails fileDetails){
        LOG.log(Level.DEBUG, () -> "start queryTransformStatus method. . .");

        try {
            List<ProcessInstanceDTO> processInstanceDTOs = new ArrayList<>();
            processInstanceDTOs = oracleService.getProcessInstanceDTOs(fileDetails.getFilename());
        
            if (!processInstanceDTOs.isEmpty() && processInstanceDTOs.size() >= 1){ // query has results

                if (processInstanceDTOs.get(0).getTransformation_Status().equalsIgnoreCase("TRANSFORMED_ERP")){
                    LOG.log(Level.INFO, () -> "file is transformed. . .");
                    return processInstanceDTOs.get(0);
                }
                else{
                    // file exists but not yet transformed
                    return processInstanceDTOs.get(0);
                }
                
            }
            else{
                return null;
            }

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error checking if query is transformed. . .");
            LOG.error(e);
            return null;
        }

    }

    @Override
    public boolean checkTransformStatus(FileDetails fileDetails){
        LOG.log(Level.DEBUG, () -> "start checkTransformStatus method. . .");

        try {
            ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();
            processInstanceDTO = queryTransformStatus(fileDetails);

            if (processInstanceDTO.getTransformation_Status().equalsIgnoreCase("TRANSFORMED_ERP")){
                return true;
            }
            else{
                
                TimeUnit.MINUTES.sleep(fileDetails.getOicRuntime());
                
                if (fileDetails.getDataSize().equalsIgnoreCase("small")){ // if small data
                    ProcessInstanceDTO processInstanceDTO2 = new ProcessInstanceDTO();
                    processInstanceDTO2 = queryTransformStatus(fileDetails);

                    if (processInstanceDTO2.getTransformation_Status().equalsIgnoreCase("TRANSFORMED_ERP")){
                        return true;
                    }
                    else{
                        
                        String result = checkOicUi(fileDetails, processInstanceDTO2);

                        if (result == null || result.equalsIgnoreCase("") || result.isEmpty()){
                            // issue checking OIC UI
                        }
                        else{
                            if (result.equalsIgnoreCase("Error") || result.equalsIgnoreCase("Succeeded")){
                                ProcessInstanceDTO processInstanceDTO3 = new ProcessInstanceDTO();
                                processInstanceDTO3 = queryTransformStatus(fileDetails);

                                if (processInstanceDTO3.getTransformation_Status().equalsIgnoreCase("TRANSFORMED_ERP")){
                                    return true;
                                }
                                else{
                                    //Email generated update query to OracleFinancials.IT
                                    String query = generateUpdateQuery(fileDetails, processInstanceDTO3);
    
                                    String body = "<body>Hello, <br><br>";
    
                                    body += "File: " + fileDetails.getFilename() + " is not transformed after checking the OIC UI and querying 3 times. <br><br>";
                                    body += "Here is the generated <b>UPDATE</b> query: <br>";
                                    body += "<i>" + query + "</i>";
                                    
                                    body += "<br><br> The automation for this file has now ended."; 
    
                                    body += "<br><br>" +
                                            "Thanks & Regards, <br>" + 
                                            "Bot</body>";
                                    
                                    sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                                    environment.getProperty("mail.argus.cc", String[].class), 
                                                    environment.getProperty("mail.subject.oic.ora.fin.it.not.transformed"), 
                                                    body);
    
                                }
                            }
                            else if (result.equalsIgnoreCase("Queued") || result.equalsIgnoreCase("In Progress")){
                                ProcessInstanceDTO processInstanceDTO3 = new ProcessInstanceDTO();
                                processInstanceDTO3 = queryTransformStatus(fileDetails);

                                if (processInstanceDTO3.getTransformation_Status().equalsIgnoreCase("TRANSFORMED_ERP")){
                                    return true;
                                }
                                else{
                                    String result2 = checkOicUi(fileDetails, processInstanceDTO3);
    
                                    if (result2.equalsIgnoreCase("Error") || result2.equalsIgnoreCase("Succeeded")){
                                        ProcessInstanceDTO processInstanceDTO4 = new ProcessInstanceDTO();
                                        processInstanceDTO4 = queryTransformStatus(fileDetails);
        
                                        if (processInstanceDTO4.getTransformation_Status().equalsIgnoreCase("TRANSFORMED_ERP")){
                                            return true;
                                        }
                                        else{
                                            //Email generated update query to OracleFinancials.IT
                                            String query = generateUpdateQuery(fileDetails, processInstanceDTO4);
    
                                            String body = "<body>Hello, <br><br>";
    
                                            body += "File: " + fileDetails.getFilename() + " is not transformed after checking the OIC UI and querying 4 times. <br><br>";
                                            body += "Here is the generated UPDATE query: <br>";
                                            body += query;
                                            
                                            body += "<br><br> The automation for this file has now ended."; 
    
                                            body += "<br><br>" +
                                                    "Thanks & Regards, <br>" + 
                                                    "Bot</body>";
                                            
                                            sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                                            environment.getProperty("mail.argus.cc", String[].class), 
                                                            environment.getProperty("mail.subject.oic.ora.fin.it.not.transformed"), 
                                                            body);
                                        }
                                    }
                                    else if (result2.equalsIgnoreCase("Queued") || result2.equalsIgnoreCase("In Progress")){
                                        //Email generated update query to OracleFinancials.IT
                                        String query = generateUpdateQuery(fileDetails, processInstanceDTO3);
    
                                        String body = "<body>Hello, <br><br>";
    
                                        body += "File: " + fileDetails.getFilename() + " is not transformed after checking the OIC UI and querying 3 times. <br><br>";
                                        body += "Here is the generated UPDATE query: <br>";
                                        body += query;
                                        
                                        body += "<br><br> The automation for this file has now ended."; 
    
                                        body += "<br><br>" +
                                                "Thanks & Regards, <br>" + 
                                                "Bot</body>";
                                        
                                        sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                                        environment.getProperty("mail.argus.cc", String[].class), 
                                                        environment.getProperty("mail.subject.oic.ora.fin.it.not.transformed"), 
                                                        body);
                                    }
                                }
                            }
                        }                        
                        
                    }

                }
                else{ // if big data

                    for (int i = 0; i <= Integer.valueOf(environment.getProperty("check.file.transformed.big.data.retry.attempts").trim()); i++){
                        ProcessInstanceDTO processInstanceDTO2 = new ProcessInstanceDTO();
                        processInstanceDTO2 = queryTransformStatus(fileDetails);

                        if (processInstanceDTO2.getTransformation_Status().equalsIgnoreCase("TRANSFORMED_ERP")){
                            return true;
                        }
                        else{
                            if ( i == Integer.valueOf(environment.getProperty("check.file.transformed.big.data.retry.attempts").trim()) ){
                                //Email generated update query to OracleFinancials.IT
                                String query = generateUpdateQuery(fileDetails, processInstanceDTO2);

                                String body = "<body>Hello, <br><br>";

                                body += "File: " + fileDetails.getFilename() + " is not transformed after querying " + i + " times. <br><br>";
                                body += "Here is the generated UPDATE query: <br>";
                                body += query;
                                
                                body += "<br><br> The automation for this file has now ended."; 

                                body += "<br><br>" +
                                        "Thanks & Regards, <br>" + 
                                        "Bot</body>";
                                
                                sendOicEmail(environment.getProperty("mail.argus.oracle.financial.it.email", String[].class), 
                                                environment.getProperty("mail.argus.cc", String[].class), 
                                                environment.getProperty("mail.subject.oic.ora.fin.it.not.transformed"), 
                                                body);
                            }
                            else{
                                TimeUnit.MINUTES.sleep(Integer.valueOf(environment.getProperty("check.file.transformed.big.data.second.wait.time").trim()));
                            }

                        }
                        
                    }
                    
                    

                }

            }

            return false;
            
        } catch (Exception e){
            LOG.log(Level.INFO, () -> "error while checking transformation status by query. . .");
            LOG.error(e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String checkOicUi(FileDetails fileDetails, ProcessInstanceDTO processInstanceDTO){
        
        Browser browser = null;
        BrowserContext browserContext = null;
    
        try {
            browser = playwrightService.openBrowser();
            browserContext = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));

            Page page = navigateOicLogin(browser, browserContext);

            return checkTransformStatusOIC(fileDetails, page, processInstanceDTO);
        
        } catch (Exception e){
            // end automation
            return null;
        }

    }

    @Override
    public Page navigateOicLogin(Browser browser, BrowserContext browserContext){
        LOG.log(Level.DEBUG, () -> "start navigateOicLogin method . . .");

		try {
            browserContext.setDefaultTimeout(120000); //2 minutes max wait

			Page page = browserContext.newPage();

            page.navigate(environment.getProperty("playwright.uri.oic"));

			TimeUnit.SECONDS.sleep(10);

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

				List<ElementHandle> integElements = page.querySelectorAll("h1[title=Integration]");

				if (!integElements.isEmpty()){ // either it's not requiring MFA code because session is saved or login was successful
					return page;
				}
				else{ // requiring MFA code because session is not saved				
					if ( i == Integer.valueOf(environment.getProperty("mfa.attempts").trim()) ){ // last attempt
						throw new Exception("MFA code not retrieved...");
					}
					else{
                        page.navigate(environment.getProperty(environment.getProperty("playwright.uri.oic"))); //reload page
						TimeUnit.SECONDS.sleep(15);

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
									
			LOG.log(Level.DEBUG, () -> "end navigateOicLogin method . . .");
			return page;
		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem navigating OIC login . . .");
			LOG.error(e);
			return null;
		}
        
    }

    @Override
    public String checkTransformStatusOIC(FileDetails fileDetails, Page page, ProcessInstanceDTO processInstanceDTO){
        LOG.log(Level.DEBUG, () -> "start checkTransformStatusOIC method. . .");

        try {

            if (processInstanceDTO != null){ 
                page.navigate(environment.getProperty("playwright.uri.oic") + environment.getProperty("playwright.uri.oic.tracking"));

                //make sure correct page
                playwrightService.pageLocatorWait(page, "", "", "span:has-text(\"Track Instances\")");
                
                TimeUnit.SECONDS.sleep(5);

                playwrightService.pageClick(page, "div", "id", "toolbar-filter-launcher-icon_opaas-table-toolbar_trackingBrowserComponentId");
                playwrightService.pageClick(page, "div", "id", "ojChoiceId_toolbar-filter-date-time-listfilter-by-timewindow");

                List<ElementHandle> filterOptions = page.querySelectorAll("ul[id=oj-listbox-results-toolbar-filter-date-time-listfilter-by-timewindow] >> li");

                if (filterOptions.size() > 1){
                    for (ElementHandle elemHandle: filterOptions) {
                        if (elemHandle.innerText().contains("Last 3 Days")){
                            elemHandle.click();
                        }
                    }
                }

                playwrightService.pageClick(page, "", "", "span:has-text(\"Apply\")");
                TimeUnit.SECONDS.sleep(10); // allow results to load
                
                playwrightService.pageClick(page, "a", "id", "toolbar-search-open_opaas-table-toolbar_trackingBrowserComponentId");
                playwrightService.pageFill(page, "", "", "oj-input-text[id=toolbar-search-input_opaas-table-toolbar_trackingBrowserComponentId] >> input", String.valueOf(processInstanceDTO.getInstance_Id()));
                page.keyboard().press("Enter");
                TimeUnit.SECONDS.sleep(10); // allow results to load

                playwrightService.pageLocatorWait(page, "", "", ".oj-table-body-row.oab-asset__row.oab-asset__max-height"); // there are results
                
                List<ElementHandle> rowResults = page.querySelectorAll(".oj-table-body-row.oab-asset__row.oab-asset__max-height");

				LOG.log(Level.INFO, () -> "rowResults size: " + rowResults.size());

                if (rowResults.size() == 1){

                    for (ElementHandle elemHandle: rowResults) {
                        LOG.log(Level.INFO, () -> "rowResult: " + elemHandle.textContent());

                        if (elemHandle.textContent().contains("Succeeded")){
                            return "Succeeded";
                        }
                        else if (elemHandle.textContent().contains("Queued")){
                            return "Queued";
                        }
                        else if (elemHandle.textContent().contains("In Progress")){
                            return "In Progress";
                        }
                        else if (elemHandle.textContent().contains("Error")){
                            return "Error";
                        }
                    }

                    return null;
                }
                else if (rowResults.size() > 1){
                    LOG.log(Level.INFO, () -> "row results are greater than 1. . .");
                    
                    for (ElementHandle elemHandle: rowResults) { //returns the first (latest) result
                        LOG.log(Level.DEBUG, () -> "rowResult: " + elemHandle.textContent());

                        if (elemHandle.textContent().contains("Succeeded")){
                            return "Succeeded";
                        }
                        else if (elemHandle.textContent().contains("Queued")){
                            return "Queued";
                        }
                        else if (elemHandle.textContent().contains("In Progress")){
                            return "In Progress";
                        }
                        else if (elemHandle.textContent().contains("Error")){
                            return "Error";
                        }
                    }

                    return null;
                }
                else{
                    LOG.log(Level.INFO, () -> "no results shown. . .");
                }

                LOG.log(Level.DEBUG, () -> "end checkTransformStatusOIC method. . .");
                return null; 

            }
            else{
                // no result from query in the first place
                return null;
            }

        } catch (Exception e){
            LOG.log(Level.INFO, () -> "error checking status in OIC interface. . .");
            LOG.error(e);
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String generateUpdateQuery(FileDetails fileDetails, ProcessInstanceDTO processInstanceDTO){
        LOG.log(Level.DEBUG, () -> "start generateUpdateQuery method. . .");

        try {
            String queryTemplate = environment.getProperty("update.query.template");

            String strFullDate = processInstanceDTO.getProcess_Time();

            String strDate = strFullDate.substring(0, strFullDate.lastIndexOf(".")); 
            String microSecond = strFullDate.substring(strFullDate.lastIndexOf("."), strFullDate.length()); 

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(strDate, format);

            String setProcessTime = (date.minus(6, ChronoUnit.HOURS)).toString();
            setProcessTime = setProcessTime.replaceAll("T", " ");
            setProcessTime += microSecond;

            String updateQuery = queryTemplate.replaceAll(":setProcessTime", setProcessTime);
            updateQuery = updateQuery.replaceAll(":filename", processInstanceDTO.getBatch_Name());
            updateQuery = updateQuery.replaceAll(":instanceId", String.valueOf(processInstanceDTO.getInstance_Id()));
            updateQuery = updateQuery.replaceAll(":processTime", processInstanceDTO.getProcess_Time());
    
            LOG.log(Level.DEBUG, () -> "end generateUpdateQuery method. . .");
            return updateQuery;

        } catch (Exception e){
            LOG.log(Level.INFO, () -> "error generating update query. . .");
            LOG.error(e);
            return null;
        }
        
    }

    @Override
    public void sendOicEmail(String[] recipients, String[] cc, String subject, String body) {
        LOG.log(Level.DEBUG, () -> "start sendOicEmail method. . .");

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"), recipients, cc,
                    subject + " - " + execTimestamp,
                    body, NORMAL_PRIORITY,true);

        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending oic email. . .");
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end sendOicEmail method. . .");
	}

}
