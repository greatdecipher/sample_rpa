package com.albertsons.argus.rundb.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import com.albertsons.argus.rundb.service.DBRevocationService;
import com.albertsons.argus.rundb.service.RunDBScheduledTaskService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RunDBScheduledTaskServiceImpl implements RunDBScheduledTaskService {
    private static final Logger LOG = LogManager.getLogger(RunDBScheduledTaskServiceImpl.class);
    
    @Autowired
	private DBRevocationService dbRevocationService;

    @Autowired
    private MetricsWebService metricsWebService;

    @Autowired
    Environment environment;

    private String automationRunId;

    private Integer totalUserCount;

    @Override
    @Scheduled(cron = "${com.argus.rundb.scheduled.task.cron.value}")
    public void runRunDBJob() {
        LOG.log(Level.DEBUG, () -> "start Run DB Script. . .");

        String vDevOptions, vProdOptions, vOtherOptions;

        vDevOptions = environment.getProperty("common.db.revocation.dev.options");
        vProdOptions = environment.getProperty("common.db.revocation.prod.options");
        vOtherOptions = environment.getProperty("common.db.revocation.other.options");

        if (dbRevocationService.validateRun() == true){ // checks if previous tasks worked
            try{

                dbRevocationService.setPartialUserCount(Integer.parseInt(environment.getProperty("common.db.revocation.partial.user.count")));
    
                try {
                    ResponseStartSaveBO bo = metricsWebService.startSave("A100012", "1.0");
                
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

                totalUserCount = dbRevocationService.getUsersList().size();
                
                while (dbRevocationService.getUsersList().size() > 0){
    
                    if (dbRevocationService.getPartialUserCount() <= dbRevocationService.getUsersList().size()){
                        dbRevocationService.setPartialUserCount(Integer.parseInt(environment.getProperty("common.db.revocation.partial.user.count")));
    
                        String startTime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now()); 
                        dbRevocationService.sendDBRevocationEmailStart(startTime);
    
                        CompletableFuture<Boolean> cfDev = dbRevocationService.runShCmd("eap02rp", environment.getProperty("encrypted.db.revocation.property.server.password"), "d2iudv18", "su - informix", vDevOptions);
                        CompletableFuture<Boolean> cfProd = dbRevocationService.runShCmd("eap02rp", environment.getProperty("encrypted.db.revocation.property.server.password"), "d2iudv18", "su - informix", vProdOptions);
                        CompletableFuture<Boolean> cfOther = dbRevocationService.runShCmd("eap02rp", environment.getProperty("encrypted.db.revocation.property.server.password"), "d2iudv18", "su - informix", vOtherOptions);
                        
                        if ((cfDev.get() == true) && (cfProd.get() == true) && (cfOther.get() == true)){
                            String endTime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now()); 
                            dbRevocationService.sendDBRevocationEmailSuccess(startTime, endTime);
                            dbRevocationService.updateUserList();
                        }
                        else{
                            String endTime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now()); 
                            dbRevocationService.sendDBRevocationEmailFailed(startTime, endTime);
                            dbRevocationService.updateUserList();
                        }
    
                    }
                    else{
                        dbRevocationService.setPartialUserCount((dbRevocationService.getUsersList().size() % Integer.parseInt(environment.getProperty("common.db.revocation.partial.user.count"))));
    
                        String startTime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now()); 
                        dbRevocationService.sendDBRevocationEmailStart(startTime);
                        
                        CompletableFuture<Boolean> cfDev = dbRevocationService.runShCmd("eap02rp", environment.getProperty("encrypted.db.revocation.property.server.password"), "d2iudv18", "su - " + environment.getProperty("common.db.revocation.user"), vDevOptions);
                        CompletableFuture<Boolean> cfProd = dbRevocationService.runShCmd("eap02rp", environment.getProperty("encrypted.db.revocation.property.server.password"), "d2iudv18", "su - " + environment.getProperty("common.db.revocation.user"), vProdOptions);
                        CompletableFuture<Boolean> cfOther = dbRevocationService.runShCmd("eap02rp", environment.getProperty("encrypted.db.revocation.property.server.password"), "d2iudv18", "su - " + environment.getProperty("common.db.revocation.user"), vOtherOptions);
                        
                        if ((cfDev.get() == true) && (cfProd.get() == true) && (cfOther.get() == true)){
                            String endTime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now()); 
                            dbRevocationService.sendDBRevocationEmailSuccess(startTime, endTime);
                            dbRevocationService.updateUserList();
                        }
                        else{
                            String endTime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(LocalDateTime.now()); 
                            dbRevocationService.sendDBRevocationEmailFailed(startTime, endTime);
                            dbRevocationService.deleteFile(true, false);
                            dbRevocationService.updateUserList();
                        }
    
                        break;
                    }
    
                }

                if (StringUtils.isNotBlank(automationRunId)){
                    try {
                        ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, totalUserCount.toString());

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

                dbRevocationService.deleteFile(true, true);
    
            } catch (Exception e){
                LOG.error(e);
                dbRevocationService.deleteFile(true, false);
            }
            finally {
                dbRevocationService.moveLogFile();
            }
    
        }
        else{
            LOG.log(Level.INFO, () -> "Cancelling execution of Run DB Script. . .");
            dbRevocationService.moveLogFile();
        }
    }
}
