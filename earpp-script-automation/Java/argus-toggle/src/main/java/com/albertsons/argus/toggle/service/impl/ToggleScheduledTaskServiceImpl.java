package com.albertsons.argus.toggle.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.toggle.bo.IncidentBO;
import com.albertsons.argus.toggle.bo.IncidentDetailBO;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentDetailBO;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentListBO;
import com.albertsons.argus.toggle.bo.ResponseUpdateIncidentBO;
import com.albertsons.argus.toggle.dto.QueueDetails;
import com.albertsons.argus.toggle.service.ToggleAutomationService;
import com.albertsons.argus.toggle.service.ToggleScheduledTaskService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.microsoft.playwright.Page;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ToggleScheduledTaskServiceImpl implements ToggleScheduledTaskService{
    private static final Logger LOG = LogManager.getLogger(ToggleScheduledTaskServiceImpl.class);

    @Autowired
    private ToggleAutomationService toggleAutomationService;

    @Autowired
    private AutomationService<ByQueueBO> automationService;

    @Autowired
    MetricsWebService metricsWebService;

    @Autowired
    Environment environment;

    @Override
    @Scheduled(cron = "${com.argus.tma.toggle.scheduled.task.cron.value}")
    public void runToggleJob() {

        Page pageTMA = null;

        try {
            List<String> getContentLists = new ArrayList<>();
            List<ByQueueBO> tmaBOs = new ArrayList<>();
            String emailMessage = "";
            File txtFile = new File(environment.getProperty("outlook.vbs.in.folder") + environment.getProperty("outlook.email.content")); 

            if (txtFile.exists()){ // cancel script execution because same task is running
                LOG.log(Level.INFO, () -> "Cancelling Toggle script execution because task is running. . ."); 
            }
            else{
                String getIncListRequestBody =  "{" +
                                                    "\"short_description\":\"" + environment.getProperty("uri.snow.short.description.keywords") + "\"," +
                                                    "\"assignment_group\":\"" + environment.getProperty("uri.snow.assignment.group") + "\"" +
                                                "}";

                ResponseGetIncidentListBO responseGetIncidentListBO = toggleAutomationService.getIncidentList(getIncListRequestBody);
                List<IncidentBO> incidents = new ArrayList<>();
                incidents = responseGetIncidentListBO.getIncidentList();

                if (incidents.size() > 0){

                    for (IncidentBO incidentBO : incidents){

                        String automationRunId = "";

                        try {
                            ResponseStartSaveBO bo = metricsWebService.startSave("A100029", "1.0");
                        
                            if (bo.getResult().contains("SUCCESS")){
                                automationRunId = bo.getId();
                            }
                            else{
                                LOG.log(Level.DEBUG, () -> "problem on Start Save. . .");
                            }
                
                        } catch (Exception e){
                            LOG.error("error on Start Save. . .");
                            LOG.error(e);
                        }

                        QueueDetails queueDetails = new QueueDetails();
                        queueDetails.setIncTicket(incidentBO.getIncidentNumber());
                        
                        try {
                            String getIncDetailRequestBody =    "{" +
                                                                    "\"inc_number\":\"" + incidentBO.getIncidentNumber() + "\"" +
                                                                "}";

                            ResponseGetIncidentDetailBO responseGetIncidentDetailBO = toggleAutomationService.getIncidentDetail(getIncDetailRequestBody);
                            IncidentDetailBO incidentDetailBO = responseGetIncidentDetailBO.getIncident();

                            queueDetails = toggleAutomationService.readIncident(incidentDetailBO);

                            pageTMA = toggleAutomationService.navigateLoginNonSSL();
    
                            String[] ticketDesc = (queueDetails.getTicketDesc()).split("\\.");
                            String projectName = ticketDesc[0];
                            String storeNumber = ticketDesc[1];
                            String localQueue = ticketDesc[2] + ticketDesc[3];
    
                            if (toggleAutomationService.goToQueue(projectName, storeNumber, localQueue, pageTMA, queueDetails) == true){
                                
                                int maxToggleAttempts = Integer.valueOf(environment.getProperty("toggle.attempts"));
                                boolean toggleFlag = false;
                                boolean noToggleFlag = true; // True if toggle was not performed, False if toggle was done at least once
    
                                for (int i = 0; i < maxToggleAttempts; i++){
    
                                    TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("toggle.seconds.wait")));
    
                                    getContentLists = toggleAutomationService.getContentLists(projectName, storeNumber);
                                    tmaBOs = automationService.getToJsonLists(getContentLists);
    
                                    if (toggleAutomationService.checkCurrentQDepth(tmaBOs, queueDetails) == true){
                                        toggleAutomationService.toggleQueue(pageTMA);
                                        noToggleFlag = false; 
    
                                        if (i == (maxToggleAttempts - 1)){ // Last toggle attempt
                                            if (toggleAutomationService.checkCurrentQDepth(tmaBOs, queueDetails) == false){
                                                toggleFlag = true; // Desired queue depth is achieved
                                                break;
                                            }
                                        }
                                    }
                                    else{
                                        toggleFlag = true; // Desired queue depth is achieved
                                        break;
                                    }
                                }

                                pageTMA.close();
    
                                if (toggleFlag == true){
                                    if (noToggleFlag == true){
                                        String updateRequestBody =  "{" +
                                                                        "\"inc_number\":\"" + queueDetails.getIncTicket() + "\"," +
                                                                        "\"work_notes\":\"Queue already has a current queue depth of 0.\"," +
                                                                        "\"state\":\"Resolved\"" +
                                                                    "}";
                                        
                                        ResponseUpdateIncidentBO responseUpdateIncidentBO = toggleAutomationService.updateIncident(updateRequestBody);
    
                                        if (responseUpdateIncidentBO != null && responseUpdateIncidentBO.getResult().contains("has been updated")){ //successful incident update
                                            emailMessage = "Queue " + queueDetails.getTicketDesc() + " already has a current queue depth of 0. Incident " + queueDetails.getIncTicket() + " is now resolved." ;

                                            if (StringUtils.isNotBlank(automationRunId)){
                                                try {
                                                    ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");
                            
                                                    if (bo2.getResult().contains("SUCCESS")){
                                                        LOG.log(Level.DEBUG, () -> "Ending save. . .");
                                                        ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                            
                                                        if (bo3.getResult().contains("SUCCESS")){
                                                            LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                                                        }
                                                        else{
                                                            LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                                                        }
                                                    }
                                                    else{
                                                        LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                                                }    }
                                                 catch (Exception e){
                                                    LOG.error("error on Increment Transaction or End Save. . .");
                                                    LOG.error(e);
                                                }
                                            }

                                        }
                                        else{
                                            emailMessage = "Queue " + queueDetails.getTicketDesc() + " already has a current queue depth of 0. However, bot is unable to resolve incident. You may resolve the incident or wait for it to auto-resolve: " + queueDetails.getIncTicket() + "." ;

                                            if (StringUtils.isNotBlank(automationRunId)){
                                                try {
                                                    ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");
                            
                                                    if (bo2.getResult().contains("SUCCESS")){
                                                        LOG.log(Level.DEBUG, () -> "Ending save. . .");
                                                        ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                            
                                                        if (bo3.getResult().contains("SUCCESS")){
                                                            LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                                                        }
                                                        else{
                                                            LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                                                        }
                                                    }
                                                    else{
                                                        LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                                                }    }
                                                 catch (Exception e){
                                                    LOG.error("error on Increment Transaction or End Save. . .");
                                                    LOG.error(e);
                                                }
                                            }

                                        }
                                    }
                                    else if (noToggleFlag == false){
                                        String updateRequestBody =  "{" +
                                                                        "\"inc_number\":\"" + queueDetails.getIncTicket() + "\"," +
                                                                        "\"work_notes\":\"Queue has been successfully restarted.\"," +
                                                                        "\"state\":\"Resolved\"" +
                                                                    "}";
                                        
                                        ResponseUpdateIncidentBO responseUpdateIncidentBO = toggleAutomationService.updateIncident(updateRequestBody);
    
                                        if (responseUpdateIncidentBO != null && responseUpdateIncidentBO.getResult().contains("has been updated")){ //successful incident update
                                            emailMessage = "Queue " + queueDetails.getTicketDesc() + " has been successfully restarted. Incident " + queueDetails.getIncTicket() + " is now resolved." ;

                                            if (StringUtils.isNotBlank(automationRunId)){
                                                try {
                                                    ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");
                            
                                                    if (bo2.getResult().contains("SUCCESS")){
                                                        LOG.log(Level.DEBUG, () -> "Ending save. . .");
                                                        ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                            
                                                        if (bo3.getResult().contains("SUCCESS")){
                                                            LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                                                        }
                                                        else{
                                                            LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                                                        }
                                                    }
                                                    else{
                                                        LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                                                }    }
                                                 catch (Exception e){
                                                    LOG.error("error on Increment Transaction or End Save. . .");
                                                    LOG.error(e);
                                                }
                                            }
                                            
                                        }
                                        else{
                                            emailMessage = "Queue " + queueDetails.getTicketDesc() + " has been successfully restarted. However, bot is unable to resolve incident. You may resolve the incident or wait for it to auto-resolve: " + queueDetails.getIncTicket() + "." ;

                                            if (StringUtils.isNotBlank(automationRunId)){
                                                try {
                                                    ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");
                            
                                                    if (bo2.getResult().contains("SUCCESS")){
                                                        LOG.log(Level.DEBUG, () -> "Ending save. . .");
                                                        ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                            
                                                        if (bo3.getResult().contains("SUCCESS")){
                                                            LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                                                        }
                                                        else{
                                                            LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                                                        }
                                                    }
                                                    else{
                                                        LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                                                }    }
                                                 catch (Exception e){
                                                    LOG.error("error on Increment Transaction or End Save. . .");
                                                    LOG.error(e);
                                                }
                                            }
                                        }
                                    }
                                }
                                else{
                                    emailMessage = "Queue " + queueDetails.getTicketDesc() + " was restarted " + maxToggleAttempts + " times, but queue depth is still above the threshold. Click here to view the incident: " + queueDetails.getIncTicket() + ".";

                                    if (StringUtils.isNotBlank(automationRunId)){
                                        try {
                                            ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");
                    
                                            if (bo2.getResult().contains("SUCCESS")){
                                                LOG.log(Level.DEBUG, () -> "Ending save. . .");
                                                ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                    
                                                if (bo3.getResult().contains("SUCCESS")){
                                                    LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                                                }
                                                else{
                                                    LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                                                }
                                            }
                                            else{
                                                LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                                        }    }
                                         catch (Exception e){
                                            LOG.error("error on Increment Transaction or End Save. . .");
                                            LOG.error(e);
                                        }
                                    }
                                }
                                
                            }
                            else{
                                LOG.log(Level.INFO, () -> "Problem finding queue in TMA. . .");
    
                                emailMessage = "There was a problem navigating the TMA site. The queue " + queueDetails.getTicketDesc() + " was not restarted. Please check if TMA is up and if the queue exists. Click here to view the incident: " + queueDetails.getIncTicket() + ".";
                            
                                if (StringUtils.isNotBlank(automationRunId)){
                                    try {
                                        ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "0");
                
                                        if (bo2.getResult().contains("SUCCESS")){
                                            LOG.log(Level.DEBUG, () -> "Ending save. . .");
                                            ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                
                                            if (bo3.getResult().contains("SUCCESS")){
                                                LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                                            }
                                            else{
                                                LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                                            }
                                        }
                                        else{
                                            LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                                    }    }
                                     catch (Exception e){
                                        LOG.error("error on Increment Transaction or End Save. . .");
                                        LOG.error(e);
                                    }
                                    
                                }
                            }
    
                            toggleAutomationService.sendTmaEmail(emailMessage, queueDetails);
    
                        } catch (Exception e){
                            LOG.log(Level.ERROR, () -> "Problem performing the automation. . .");
                            LOG.error(e);
    
                            emailMessage = "There was a problem peforming the automation: an exception was caught. The queue " + queueDetails.getTicketDesc() + " was not restarted. Click here to view the incident: " + queueDetails.getIncTicket() + ".";
    
                            toggleAutomationService.sendIssueEmail(emailMessage, queueDetails);

                            if (StringUtils.isNotBlank(automationRunId)){
                                try {
                                    ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "0");
            
                                    if (bo2.getResult().contains("SUCCESS")){
                                        LOG.log(Level.DEBUG, () -> "Ending save. . .");
                                        ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
            
                                        if (bo3.getResult().contains("SUCCESS")){
                                            LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                                        }
                                        else{
                                            LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                                        }
                                    }
                                    else{
                                        LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                                }    }
                                 catch (Exception e2){
                                    LOG.error("error on Increment Transaction or End Save. . .");
                                    LOG.error(e2);
                                }
                                
                            }
    
                        } finally {
                            if (pageTMA != null){
                                pageTMA.close();
                            }
                            toggleAutomationService.deleteFile();
                            toggleAutomationService.moveLogFile();
                        }
                    }

                }
                else{
                    LOG.log(Level.INFO, () -> "Cancelling Toggle script execution because there are no current incidents. . ."); 
                    toggleAutomationService.deleteFile();
                    toggleAutomationService.moveLogFile();
                }
            }
        } catch (Exception e){
            LOG.log(Level.INFO, () -> "Problem performing the toggle automation. . ."); 
            LOG.error(e);
        }

    }

}
