package com.albertsons.argus.asn.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.asn.service.AsnSchedulerService;
import com.albertsons.argus.asn.service.AsnService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;

@Service
public class AsnSchedulerServiceImpl implements AsnSchedulerService{
    private static final Logger LOG = LogManager.getLogger(AsnSchedulerServiceImpl.class);
    @Autowired
    private AsnService asnService;

    @Autowired
    private MetricsWebService metricsWebService;

    @Override
    @Scheduled(cron = "${asn.scheduled.task.cron.value}")
    public void runProcess() {
        LOG.info("FAR MISSING ASN PROCESS START . . .");
        try {
            String datetime = asnService.getServerDate();
            
            if(asnService.getEmails(datetime)) {
                try {
                    ResponseStartSaveBO startBO = metricsWebService.startSave("A100076", "1.0");
                    String filename = asnService.createFile(asnService.mergeEmailMessage(), datetime);
                    asnService.saveFileToServer(filename);
                    asnService.sudoTransferFile(filename);
                    asnService.sendMail(filename);
                    asnService.deleteFile(filename);
                    ResponseIncrementTransactionBO incBO = metricsWebService.incrementTransaction(startBO.getId(), "1");
                    ResponseEndSaveBO endBO = metricsWebService.endSave(startBO.getId());
                } catch(Exception e) {
                    LOG.error("error on Increment Transaction or End Save. . .");
                }
            } else { 
                LOG.info("Email quantity incomplete . . .");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        LOG.info("FAR MISSING ASN PROCESS END . . .");
    }
    
}
