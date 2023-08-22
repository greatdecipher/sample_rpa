package com.albertsons.argus.sla.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.sla.dto.SLAReport;
import com.albertsons.argus.sla.service.SLAScheduleService;
import com.albertsons.argus.sla.service.SLAService;


@Service
public class SLAScheduleServiceImpl implements SLAScheduleService{
    private static final Logger LOG = LogManager.getLogger(SLAScheduleServiceImpl.class);

    @Autowired
	private Environment environment;
    
    @Autowired
    private SLAService slaService;

    @Override
    @Scheduled(cron = "${sla.schedule.cron.value}")
    public void run() {
        LOG.info("Starting Out of SLA Follow-up process . . .");
        try {
            boolean reportFound = slaService.checkIfReportExists();
            if (reportFound) {
                
            } else {
                slaService.sendMailNotification("MISSING_REPORT", 0);
            }

            long timeDelay = Long.parseLong(environment.getProperty("sla.delay.time"));
            TimeUnit.MINUTES.sleep(timeDelay);
            
            if (reportFound || slaService.checkIfReportExists()) {
                slaService.sendMailNotification("START", 0);
                Map<String, List<SLAReport>> reportMap = slaService.readExcel();
                Integer transactionNumber = slaService.prepareAndSendReport(reportMap);
                slaService.sendMailNotification("FINISH", transactionNumber);
            } else {
                slaService.sendMailNotification("NO_REPORT", 0);
            }
        } catch(Exception e) {
            slaService.sendMailNotification("ERROR", 0);
            LOG.error(e);
        }
        LOG.info("Out of SLA Follow-up process finished . . .");
    }
    

}
