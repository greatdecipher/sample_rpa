package com.albertsons.argus.nagware.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.nagware.exception.NagwareException;
import com.albertsons.argus.nagware.model.User;
import com.albertsons.argus.nagware.service.AutomationNagwareScheduleService;
import com.albertsons.argus.nagware.service.AutomationNagwareService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;

@Service
public class AutomationNagwareScheduleServiceImpl implements AutomationNagwareScheduleService{
    private static final Logger LOG = LogManager.getLogger(AutomationNagwareScheduleServiceImpl.class);

	@Autowired
	private AutomationNagwareService automationNagwareService;

	@Autowired
	private MetricsWebService metricsWebService;

    @Override
    @Scheduled(cron = "${nagware.scheduled.task.cron.value}")
    public void run() {
        LOG.info("Starting Nagware Process . . .");
		try {
			ResponseStartSaveBO startBO = metricsWebService.startSave("A100082", "1.0");
			automationNagwareService.processNotification("START", "");
			List<User> projectCenter = automationNagwareService.getProjectCenterDetails();
			List<User> resourceCenter = automationNagwareService.getResourceCenterDetailsXLSX();
			Integer transactionNumber = automationNagwareService.sendMailToList(automationNagwareService.getUserDetails(projectCenter, resourceCenter));
			ResponseIncrementTransactionBO incrementBO = metricsWebService.incrementTransaction(startBO.getId(), transactionNumber.toString());
			ResponseEndSaveBO endBO = metricsWebService.endSave(startBO.getId());
			automationNagwareService.archiveFiles();
			automationNagwareService.processNotification("END", transactionNumber.toString());
		} catch (NagwareException e) {
			automationNagwareService.sendMailError(e.getMessage());
		}
		LOG.info("Nagware Process Finished . . .");
    }
    
}
