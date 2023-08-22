package com.albertsons.argus.sedgwick.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.LoginSSOAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.sedgwick.service.SedgwickAutomationService;
import com.albertsons.argus.sedgwick.service.SedgwickScheduledTaskService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;


@Service
public class SedgwickScheduledTaskServiceImpl implements SedgwickScheduledTaskService{
    private static final Logger LOG = LogManager.getLogger(SedgwickScheduledTaskServiceImpl.class);
    
    private Browser browser;
	private BrowserContext context;
	private Page page;
    private String automationRunId;

    @Autowired
	private Environment environ;
	
	@Autowired
	private SedgwickAutomationService sedgwickAutomationService;

    @Autowired
    private MetricsWebService metricsWebService;

    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;

    @Autowired
    private LoginSSOAutomationService loginSSOAutomationService;

    @Override
    @Scheduled(cron = "${com.argus.sedgwick.scheduled.task.cron.value}")
    public void runSedgwickJob() {
        AutomationUtil util = new AutomationUtil();
        try {
            LOG.info("Sedgwick Emails processing start...");
            try {
                ResponseStartSaveBO bo = metricsWebService.startSave(environ.getProperty("sedgwick.automation.id"), 
                    environ.getProperty("sedgwick.automation.version"));
                if (bo.getResult().contains("SUCCESS")) {
                    LOG.info("Metrics StartSave logged. . .");
                    automationRunId = bo.getId();
                }
            } catch (Exception e) {
                sedgwickAutomationService.delay(1);
                LOG.info("Metrics StartSave API call failed...");
                LOG.error(e);
            }
            String arg = environ.getProperty("sedgwick.exe.name") + " " + environ.getProperty("sedgwick.outlook.script.path") + environ.getProperty("sedgwick.script.getemail.name") +
				" \"" + environ.getProperty("sedgwick.outlook.folder.path") + "|" + environ.getProperty("sedgwick.outlook.subject.name") +
				"|" + environ.getProperty("sedgwick.outlook.mail.markread") + "|" + environ.getProperty("sedgwick.outlook.attachment.downloadall") +
				"|" + environ.getProperty("sedgwick.outlook.script.param.detail") + "|" + environ.getProperty(ATTACH_PATH) +
				"|" + environ.getProperty("sedgwick.outlook.attachment.type") + "|" + environ.getProperty("sedgwick.outlook.attachment.overwrite") + "\"";	
			
			String extractedSender = "";
			sedgwickAutomationService.deleteSedgwickFilesInFolder(environ.getProperty(ATTACH_PATH));

			do {
				extractedSender = sedgwickAutomationService.runVBScript(arg);
				//----------------------Log email details------------------------//
				LOG.info("Sedgwick Emails started for " + extractedSender);
				
				if(extractedSender.length() > 0) {
                    if (util.fileExists(environ.getProperty(ATTACH_PATH) + environ.getProperty(ATTACH_NAME))) {
                        
                        browser =  playwrightAutomationService.openBrowser();
                        context = sedgwickAutomationService.getBrowserContext(browser);
                        page = context.newPage();
             
                        loginSSOAutomationService.loginSSO(page, environ.getProperty("playwright.uri.peoplesoft"), 
                            environ.getProperty("encrypted.sedgwick.property.username"), 
                            environ.getProperty("encrypted.sedgwick.property.password"), 
                            environ.getProperty("encrypted.sedgwick.property.secret"));
                        LOG.info("MS Login success...");
                        
                        sedgwickAutomationService.delay(10);

                        Boolean isPSLoggedIn = sedgwickAutomationService.loginPeoplesoft(page);
                        if (isPSLoggedIn) {
                            LOG.info("Peoplesoft Login success...");
                            sedgwickAutomationService.processSedgwickEmails(environ.getProperty(ATTACH_PATH) + environ.getProperty(ATTACH_NAME), page, automationRunId);
                        }
                        else {
                            LOG.info("Unable to login to Peoplesoft, please check. . .");
                            try { 
                                ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "0");    
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
                            return;
                        }
                    }
                    else {
                        LOG.info("Attachment file not found. . .");
                        try { 
                            ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "0");    
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
                        return;
                    }
                    
                    if (browser != null) {
                        browser.close();
                    }  
				} else {
					LOG.info("No unread emails found...");
                    try { 
                        ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "0");    
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
 
			} while (extractedSender.length() > 0);
            
            //------Metrics end save------
            try {
                ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                if (bo3.getResult().contains("SUCCESS")) {
                    LOG.info("End metrics logged. . .");
                }
            } catch (Exception e) {
                LOG.info("Metrics EndSave API call failed. . .");
                LOG.error(e);
                
            }

            LOG.info("Sedgwick Emails Processing Complete. . .");
        } catch (Exception e) {
            LOG.error("ERROR in runSedgwickJob");
            LOG.error(e.getMessage());
        }
        finally {
            if (browser != null) {
                browser.close();
            }   
        }
    }
}
