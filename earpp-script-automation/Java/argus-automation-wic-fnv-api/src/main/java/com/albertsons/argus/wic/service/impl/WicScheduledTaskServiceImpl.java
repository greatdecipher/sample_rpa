package com.albertsons.argus.wic.service.impl;

import com.albertsons.argus.wic.service.WicScheduledTaskService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.albertsons.argus.wic.exception.ArgusWicRuntimeException;
import com.albertsons.argus.wic.service.WicAutomationService;
import com.albertsons.argus.wic.ws.bo.ResponseGetTaskListBO;
import com.albertsons.argus.wic.ws.bo.TaskBO;

@Service
public class WicScheduledTaskServiceImpl implements WicScheduledTaskService{
    private static final Logger LOG = LogManager.getLogger(WicScheduledTaskServiceImpl.class);
	public String automationRunId;

	@Autowired
	public Environment environ;
	
	@Autowired
	private WicAutomationService WicAutomationService;

	@Autowired
	private MetricsWebService metricsWebService;

    @Override
    @Scheduled(cron = "${com.argus.wic.scheduled.task.cron.value}")
    public void runWicJob() {
        try {
			for (int i = 0; i < Integer.parseInt(environ.getProperty("wic.retry")); i++) {
				try {
					ResponseStartSaveBO bo = metricsWebService.startSave(environ.getProperty("wic.automation.id"), environ.getProperty("wic.automation.version"));
					if (bo.getResult().contains("SUCCESS")) {
						automationRunId = bo.getId();
						break;
					}
				} catch (Exception e) {
					LOG.info("Metrics StartSave API call retry: " + Integer.toString(i + 1));
					LOG.error(e);
					WicAutomationService.delay(Integer.parseInt(environ.getProperty(DELAY_S)));
					if (i == Integer.parseInt(environ.getProperty("wic.retry")) - 1) {
						LOG.info("Metrics StartSave API call failed after " + 
							Integer.parseInt(environ.getProperty("wic.retry")) + " retries...");
						LOG.error(e);
					}
				}
			}

			String formattedStoreList;
			ResponseGetTaskListBO getTaskListBO = WicAutomationService.callGetTaskList();
			
			if (getTaskListBO.getTaskBo() != null) {
				LOG.info("Records found: " + getTaskListBO.getTaskBo().size());
				WicAutomationService.deleteWicFilesInFolder(environ.getProperty("wic.outlook.attachment.save.path"));
				
				for (TaskBO task : getTaskListBO.getTaskBo()) {
					
					if (task.getAttachment() != "") {
						if (!WicAutomationService.downloadAttachment(WicAutomationService.getSysIdFromAttachmentUrl(task.getAttachment()))) {
							LOG.info("Unable to download attachment for task " + task.getTaskNumber());
							continue;
						}
						
						if(task.getRit().length() > 0) {
							LOG.info("WIC Mapping started for " + task.getRit() + "-" + task.getTaskNumber());
							
							formattedStoreList = WicAutomationService.formatStoreNumbers(task.getStoreNumberList());
							WicAutomationService.runShCmd(environ.getProperty("encrypted.wic.property.username"), 
								environ.getProperty("encrypted.wic.property.password"), 
								environ.getProperty("jsch.wic.host.name"), task.getRit(), formattedStoreList, task.getTaskNumber());
							
							try {
								ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");    
								if (!bo2.getResult().contains("SUCCESS")) {
									LOG.info("Metrics transaction increment failed. . .");
								}
							} catch (Exception e) {
								LOG.info("Failed metrics increment transaction api call. . .");
								LOG.error(e);
							}

						} else {
							LOG.info("RIT not found...");
						}
						LOG.info("WIC Mapping completed for " + task.getRit() + "-" + task.getTaskNumber());
					}
					else {
						LOG.info("No attachment found for task " + task.getTaskNumber());
					}
				};
			}
			else {
				LOG.info("Unable to get task list. . .");
				try {
					ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "0");    
					if (!bo2.getResult().contains("SUCCESS")) {
						LOG.info("Metrics transaction increment failed. . .");
					}
				} catch (Exception e) {
					LOG.info("Failed metrics increment transaction api call. . .");
					LOG.error(e);
				}
			}
			LOG.log(Level.DEBUG, () -> "WIC Mapping Completed . . .");

			for (int k = 0; k < Integer.parseInt(environ.getProperty("wic.retry")); k++) {
                try {
                    ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                    if (bo3.getResult().contains("SUCCESS")) {
                        LOG.info("End metrics logged. . .");
                        break;
                    }
                } catch (Exception e) {
                    LOG.info("Metrics EndSave API call retry: " + Integer.toString(k + 1));
                    LOG.error(e);
                    WicAutomationService.delay(Integer.parseInt(environ.getProperty(DELAY_S)));
                    if (k == Integer.parseInt(environ.getProperty("wic.retry")) - 1) {
                        LOG.info("Metrics EndSave API call failed after " + 
                            Integer.parseInt(environ.getProperty("wic.retry")) + " retries...");
                        LOG.error(e);
                    }
                }
            }

		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "WIC Mapping Error . . .");
			throw new ArgusWicRuntimeException(e.getMessage());
		}
    }
}
