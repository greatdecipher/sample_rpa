package com.albertsons.argus.toggle.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.domain.bo.generated.TmaItem;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.toggle.bo.IncidentDetailBO;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentDetailBO;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentListBO;
import com.albertsons.argus.toggle.bo.ResponseUpdateIncidentBO;
import com.albertsons.argus.toggle.dto.QueueDetails;
import com.albertsons.argus.toggle.exception.ArgusTmaException;
import com.albertsons.argus.toggle.service.ToggleAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

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

@Service
public class ToggleAutomationServiceImpl implements ToggleAutomationService{
    private static final Logger LOG = LogManager.getLogger(ToggleAutomationServiceImpl.class);
    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PlaywrightAutomationService playwrightService;

    @Autowired
    private AutomationService<ResponseUpdateIncidentBO> jsonResponseUpdateIncidentBOService;

    @Autowired
    private AutomationService<ResponseGetIncidentListBO> jsonResponseGetIncidentListBOService;

    @Autowired
    private AutomationService<ResponseGetIncidentDetailBO> jsonResponseGetIncidentDetailBOService;
    
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseUpdateIncidentBO updateIncident(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start updateIncident method. . .");

        String uri = environment.getProperty("uri.snow.base") + environment.getProperty("uri.snow.update.inc.status");
        return jsonResponseUpdateIncidentBOService.toJson(restTemplate.exchange(uri, HttpMethod.PUT, getHttpEntity(requestBody, "Token"),String.class).getBody());
    }

    @Override
    public ResponseGetIncidentListBO getIncidentList(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start getIncidentList method. . .");

        String uri = environment.getProperty("uri.snow.base") + environment.getProperty("uri.snow.get.inc.list");
        return jsonResponseGetIncidentListBOService.toJson(restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(requestBody, "Token"),String.class).getBody());
    }

    @Override
    public ResponseGetIncidentDetailBO getIncidentDetail(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start getIncidentDetail method. . .");

        String uri = environment.getProperty("uri.snow.base") + environment.getProperty("uri.snow.get.inc.details");
        return jsonResponseGetIncidentDetailBOService.toJson(restTemplate.exchange(uri, HttpMethod.POST, getHttpEntity(requestBody, "Token"),String.class).getBody());
    }

    private HttpEntity<Object> getHttpEntity(String requestBody, String headerName){
        LOG.log(Level.DEBUG, () -> "start getHttpEntity method. . .");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(headerName, environment.getProperty("encrypted.esca.snow.token"));
        return new HttpEntity<>(requestBody, headers);
    }

    @Override
    public void sendTmaEmail(String emailMessage, QueueDetails queueDetails) {
        LOG.log(Level.DEBUG, () -> "start sendTmaEmail method. . .");
        AutomationUtil util = new AutomationUtil();

        String body = "<body>Hello, <br><br>" +
                        emailMessage + "<br>";

                body += "<br><br>" +
                        "Thanks & Regards, <br>" + 
                        "Bot</body>";

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"),
                    environment.getProperty("mail.argus.recipients", String[].class),
                    environment.getProperty("mail.argus.cc", String[].class),
                    "Toggle Queue " + queueDetails.getTicketDesc() + " - "
                            + util.toDateString(new Date(), environment.getProperty("domain.util.date.format"), ""),
                    body, NORMAL_PRIORITY,true);

        } catch (ArgusMailException e) {
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end sendTmaEmail method. . .");

    }

    @Override
    public void sendIssueEmail(String emailMessage, QueueDetails queueDetails) {
        LOG.log(Level.DEBUG, () -> "start sendIssueEmail method. . .");
        AutomationUtil util = new AutomationUtil();

        String body = "<body>Hello, <br><br>" +
                        emailMessage + "<br>";

                body += "<br><br>" +
                        "Thanks & Regards, <br>" + 
                        "Bot</body>";

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.tma.issue.from"),
                    environment.getProperty("mail.tma.issue.from.alias"),
                    environment.getProperty("mail.tma.issue.recipients", String[].class),
                    environment.getProperty("mail.tma.issue.cc", String[].class),
                    "Toggle Queue " + queueDetails.getTicketDesc() + " - "
                            + util.toDateString(new Date(), environment.getProperty("domain.util.date.format"), ""),
                    body, NORMAL_PRIORITY,true);

        } catch (ArgusMailException e) {
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end sendIssueEmail method. . .");
    }

    @Override
    public void toggleQueue(Page pageTMA) throws ArgusTmaException {
        LOG.log(Level.DEBUG, () -> "start toggleQueue method. . .");

        try {
            playwrightService.pageFill(pageTMA, "", "", "input:right-of(:text(\"Trigger Control\"))", "Off");
            TimeUnit.SECONDS.sleep(3);
            playwrightService.pageClick(pageTMA, "", "", "span[title=Save]");
            TimeUnit.SECONDS.sleep(5);
            playwrightService.pageFill(pageTMA, "", "", "input:right-of(:text(\"Trigger Control\"))", "On");
            TimeUnit.SECONDS.sleep(3);
            playwrightService.pageClick(pageTMA, "", "", "span[title=Save]");

        } catch (PlaywrightException pw) {
            LOG.error(pw);
            throw new ArgusTmaException(pw.getMessage());
        } catch (InterruptedException e) {
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end toggleQueue method. . .");
    }

    public boolean checkCurrentQDepth(List<ByQueueBO> tmaBOs, QueueDetails queueDetails) {
        LOG.log(Level.DEBUG, () -> "start checkCurrentQDepth method. . .");

        for (ByQueueBO obj : tmaBOs) {
            if (obj.getItems() != null && !obj.getItems().isEmpty()) {
                for (TmaItem item : obj.getItems()) {

                    if (item.getQName().equalsIgnoreCase(queueDetails.getTicketDesc())){
                        LOG.log(Level.DEBUG, () -> item.getQName() + " has a current queue depth of " + item.getCurrentQDepth() + ".");

                        if (item.getCurrentQDepth() > Integer.valueOf(environment.getProperty("desired.queue.depth"))){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                }
            }
        }

        return false;
        
    }

    @Override
    public List<String> getContentLists(String projectName, String storeNumber) throws ArgusTmaException {
        LOG.log(Level.DEBUG, () -> "start getContentLists method. . .");

        List<String> contentLists = new ArrayList<>();
        Page page = null;

        try {
            page = navigateLoginNonSSL();
            TimeUnit.SECONDS.sleep(5);

            page.navigate(environment.getProperty(PLAYWRIGHT_URI)
                    + environment.getProperty("playwright.uri.tma.local.queue.1") + storeNumber
                    + environment.getProperty("playwright.uri.tma.local.queue.2") + "*" + projectName + "*"
                    + environment.getProperty("playwright.uri.tma.local.queue.3") + projectName.substring(0, (projectName.length() - 2)));

            contentLists.add(page.textContent(TO_JSON_STR));

        } catch (PlaywrightException pw) {
            LOG.error(pw);
            throw new ArgusTmaException(pw.getMessage());
        } catch (InterruptedException e) {
            LOG.error(e);
        } finally {
            if (page != null){
                page.close();
            }
        }

        LOG.log(Level.DEBUG, () -> "end getContentLists method. . .");

        return contentLists;
    }
    
    @Override
    public boolean goToQueue(String projectName, String storeNumber, String localQueue, Page pageTMA, QueueDetails queueDetails) throws ArgusTmaException {
        LOG.log(Level.DEBUG, () -> "start goToQueue method. . ."); 

        projectName = projectName.substring(0, (projectName.length() - 2));

        try {
            pageTMA.navigate(environment.getProperty(PLAYWRIGHT_URI) + environment.getProperty("playwright.uri.tma.login"));
            
            TimeUnit.SECONDS.sleep(7);

            playwrightService.pageFill(pageTMA, "", "", "input[id=dijit_form_TextBox_10]", projectName);
            TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("textbox.seconds.wait")));
            playwrightService.pageClick(pageTMA, "", "", "span[data-dojo-attach-point=nextActionButton]");

            playwrightService.pageFill(pageTMA, "", "", "input[id=dijit_form_TextBox_10]", storeNumber);
            TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("textbox.seconds.wait")));
            playwrightService.pageClick(pageTMA, "", "", "span[data-dojo-attach-point=nextActionButton]");

            playwrightService.pageFill(pageTMA, "", "", "input[id=dijit_form_TextBox_10]", "Local Queues");
            TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("textbox.seconds.wait")));
            playwrightService.pageClick(pageTMA, "", "", "span[data-dojo-attach-point=nextActionButton]");

            TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("textbox.seconds.wait")));

            playwrightService.pageFill(pageTMA, "", "", "input[id=dijit_form_TextBox_10]", queueDetails.getTicketDesc());
            TimeUnit.SECONDS.sleep(5);
            playwrightService.pageClick(pageTMA, "", "", "span[data-dojo-attach-point=textElement]");

            if (pageTMA.textContent("div:has(div.editorTitle)").contains("Properties for the Local Queue " + queueDetails.getTicketDesc())){
                LOG.log(Level.DEBUG, () -> "end GoToQueue method. . ."); 
                return true;
            }
            else{
                LOG.log(Level.DEBUG, () -> "Was not able to locate the correct queue. . .");
                LOG.log(Level.DEBUG, () -> "end goToQueue method. . ."); 
                return false;
            }

        } catch (Exception e) {
            LOG.log(Level.DEBUG, () -> "Error occurred while going to queue. . .");
            LOG.error(e);
            return false;
        }

    }

    @Override
    public Page navigateLoginNonSSL() throws ArgusTmaException {
        LOG.log(Level.DEBUG, () -> "start navigateLoginNonSSL method. . .");
        Page page = null;

        int maxAttempts = Integer.valueOf(environment.getProperty("browser.attempts"));

        for (int i = 0; i < maxAttempts; i++){
        
            try {                
                Browser browser = playwrightService.openBrowser();
                BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
                context.setDefaultTimeout(Integer.valueOf(environment.getProperty("browser.milliseconds.wait")));
                
                page = context.newPage();
                
                page.navigate(environment.getProperty(PLAYWRIGHT_URI) + environment.getProperty("playwright.uri.tma.login"));
                LOG.log(Level.DEBUG, () -> "page navigate successful. . .");

                playwrightService.pageLocatorWait(page, "input", "name", "j_username").fill(environment.getProperty("encrypted.tma.property.username"));
                playwrightService.pageLocatorWait(page, "input", "name", "j_password").fill(environment.getProperty("encrypted.tma.property.password"));
                playwrightService.pageLocatorWait(page, "button", "id", "loginButton").click();

                return page;

            } catch (Exception e) {

                if (i == (maxAttempts - 1)){ // final attempt 
                    LOG.error(e);
                    throw new ArgusTmaException(e.getMessage());
                }

            }
            
        }

        return page;
    }

    @Override
    public QueueDetails readIncident(IncidentDetailBO incidentDetailBO){
        LOG.log(Level.DEBUG, () -> "start readIncident method. . .");
        
        QueueDetails queueDetails = new QueueDetails();
        
        queueDetails.setIncTicket(incidentDetailBO.getIncidentNumber());

        try{
            String[] tmaProjects = environment.getProperty("tma.projects", String[].class);
            String[] stringContents = incidentDetailBO.getShortDescription().split(" ");

            for (int i = 0; i < stringContents.length; i++){
                String text = stringContents[i];

                for (int j = 0; j < tmaProjects.length; j++){
                    if (text.toUpperCase().contains(tmaProjects[j])){
                        queueDetails.setTicketDesc(text);
                    }
                }

                if ((text.toUpperCase().contains(environment.getProperty("queue.depth.name")))){
                    int tempIndex = i + 2;
                    String queueDepth = stringContents[tempIndex];
                    queueDetails.setCurrentQDepth(Integer.valueOf(queueDepth));
                }
            }
            
            return queueDetails;

        } 
        catch (Exception e){
            LOG.log(Level.ERROR, () -> "Error reading txt file with email content");
            LOG.error(e);
            return null;
        }

    }

    @Override
    public void deleteFile(){
        LOG.log(Level.DEBUG, () -> "start deleteFile method. . .");

        try {
            File txtFile = new File(environment.getProperty("outlook.vbs.in.folder") + "\\" + environment.getProperty("outlook.email.content")); 
            
            if (txtFile.exists()){
                txtFile.delete();
            }
        } catch (Exception e){
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end deleteFile method. . .");
    }

    @Override
    public void moveLogFile(){
        LOG.log(Level.DEBUG, () -> "start moveLogFile method. . .");

        try {
            Process vbscript = Runtime.getRuntime().exec("wscript " + environment.getProperty("automations.folder") + environment.getProperty("move.log.vbscript") + " " + environment.getProperty("automations.toggle.folder") + " " + environment.getProperty("log.file.name") + " " + environment.getProperty("log.file.name.extension") + " " + environment.getProperty("logging.folder"));
            vbscript.waitFor();

        } catch (Exception e){
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end moveLogFile method. . .");
    }

}
