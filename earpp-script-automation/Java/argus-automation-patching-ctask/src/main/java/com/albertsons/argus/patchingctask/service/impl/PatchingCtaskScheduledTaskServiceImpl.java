package com.albertsons.argus.patchingctask.service.impl;

import java.net.InetAddress;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.patchingctask.service.PatchingCtaskAutomationService;
import com.albertsons.argus.patchingctask.service.PatchingCtaskScheduledTaskService;
import com.albertsons.argus.patchingctask.ws.bo.ChangeDetailsBO;
import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeByIDBO;
import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeListBO;
import com.albertsons.argus.patchingctask.ws.bo.ResponseSaveChangeBO;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;

@Service
public class PatchingCtaskScheduledTaskServiceImpl implements PatchingCtaskScheduledTaskService{
    private static final Logger LOG = LogManager.getLogger(PatchingCtaskScheduledTaskServiceImpl.class);
	public String automationRunId;

	@Autowired
	public Environment environ;
	
	@Autowired
	private PatchingCtaskAutomationService PatchingCtaskAutomationService;
	
	@Autowired
	private MetricsWebService metricsWebService;

	@Autowired
	private EmailService emailService;

    @Override
    @Scheduled(cron = "${com.argus.patching.scheduled.task.cron.value}")
    public void runPatchingCtaskJob(){
        AutomationUtil util = new AutomationUtil();
		
		ResponseGetChangeListBO changeListBO = null;
		ResponseGetChangeByIDBO responseGetChangeById = null;
		String attachmentDownloadUrl, chgEnvironment, chgShortDesc, chgNumber;
		Integer intFirstDashIndex, intSecDashIndex;
		Boolean isAttachmentDownloaded = true;

		try {
			ResponseStartSaveBO bo = metricsWebService.startSave(environ.getProperty("patching.automation.id"), 
				environ.getProperty("patching.automation.version"));
			if (bo.getResult().contains("SUCCESS")) {
				LOG.info("Metrics StartSave logged. . .");
				automationRunId = bo.getId();
			}
		} catch (Exception e) {
			PatchingCtaskAutomationService.delay(1);
			LOG.info("Metrics StartSave API call failed...");
			LOG.error(e);
		}

		LOG.info("Getting change list. . .");
		try {
			changeListBO = PatchingCtaskAutomationService.getChangeLists("{\"short_description\":\"" + environ.getProperty("patching.change.short.description") + "\"}");	
		} catch (Exception e) {
			LOG.info("No CHG found...");
			LOG.error(e);
		}
		
		if (changeListBO != null) {
			LOG.info("Number of CHG found: " + changeListBO.getChangeList().size());
			for (ChangeDetailsBO changeDetails : changeListBO.getChangeList()) {
				chgNumber = changeDetails.getChangeNumber();

				LOG.info("Process start for CHG " + chgNumber);
				if (changeDetails.getAttachment().isEmpty()) {
					LOG.info("Attachment not found, ending execution for this CHG. . .");
					continue;
				}

				try {
					responseGetChangeById = PatchingCtaskAutomationService.getChangeById("", chgNumber);
					if (responseGetChangeById != null) {
						if (responseGetChangeById.getResult().toUpperCase().contains("SUCCESS")) {
							LOG.info("CHG number is existing in database, ending execution...");
							continue;
						}
					} else {
						LOG.error("Unabe to validate CHG " + chgNumber + " number in Database. . .");
						continue;
					}
				} catch (Exception e) {
					LOG.error("Error in getting change by ID");
					LOG.error(e);
					continue;
				}

				chgShortDesc = changeDetails.getShortDescription();
				intFirstDashIndex = chgShortDesc.indexOf("-") + 1;
				intSecDashIndex = chgShortDesc.indexOf("-",intFirstDashIndex);
				chgEnvironment = chgShortDesc.substring(intFirstDashIndex, intSecDashIndex).trim();
				
				if (chgEnvironment.toUpperCase().contains("PROD")) {
					chgEnvironment = "Production";
				}
				else if(chgEnvironment.toUpperCase().contains("DEV")) {
					chgEnvironment = "Development";
				}

				LOG.info("CHG environment: " + chgEnvironment);
				LOG.info("Downloading attachment for " + chgNumber + ". . .");

				attachmentDownloadUrl = PatchingCtaskAutomationService.getSysIdFromAttachmentUrl(changeDetails.getAttachment().replace(",", "").trim());
				
				isAttachmentDownloaded = PatchingCtaskAutomationService.downloadAttachment(attachmentDownloadUrl);
				if (!isAttachmentDownloaded) {
					LOG.info("Unable to download attachment. . .");
					continue;
				}

				LOG.info("Extracting ctask details from excel file. . .");
				PatchingCtaskAutomationService.prepExcel(environ.getProperty("patching.web.service.download.attachment.path"), chgEnvironment,chgNumber);

				LOG.info("Saving CHG number in database...");
                try {
                    ResponseSaveChangeBO responseSaveChangeBO = PatchingCtaskAutomationService.saveChangeNumber(null, chgNumber, 
					environ.getProperty("patching.automation.id"), "4", InetAddress.getLocalHost().getHostName(), System.getProperty("user.name"));
                } catch (Exception e) {
                    LOG.info("Error in getting responseSaveChangeBO. . .");
                    LOG.error(e);
                }
				
				LOG.info("Patching CTask Creation process COMPLETE...");

				try { 
					ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");    
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
				
				try {
					String bodyTemplate = "<html>" +
						"<body>" +
						"<p>Patching CTask Creation for CHG - " + changeDetails.getChangeNumber() + " COMPLETE.</p>" +
						"<p>Please assign someone from your Team to do the Pre and Post validation.</p>" +
						"<br>" +
						"<p>Thanks.</p>" +
						"<p>This is an automated notification - please DO NOT REPLY to this email.</p>" +
						"</body>" +
						"</html>";

                    emailService.sendSimpleMessage(environ.getProperty("mail.patching.monitoring.from"), 
                    	environ.getProperty("mail.patching.monitoring.from.alias"), 
                    	environ.getProperty("mail.patching.monitoring.recipients", String[].class), 
                    	environ.getProperty("mail.patching.monitoring.cc", String[].class), 
                    	environ.getProperty("mail.patching.monitoring.subject") + "| COMPLETED - "
                    	+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),""),
                    	bodyTemplate, 3, true);
                } catch (ArgusMailException e) {
                    LOG.info("Error in sending email. . .");
                    LOG.error(e);
                }
			}
		}
		else {
			LOG.info("No CHG found, ending execution...");
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
		
		try {
			ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
			if (bo3.getResult().contains("SUCCESS")) {
				LOG.info("End metrics logged. . .");
			}
		} catch (Exception e) {
			LOG.info("Metrics EndSave API call failed. . .");
			LOG.error(e);
			
		}
    }
}
