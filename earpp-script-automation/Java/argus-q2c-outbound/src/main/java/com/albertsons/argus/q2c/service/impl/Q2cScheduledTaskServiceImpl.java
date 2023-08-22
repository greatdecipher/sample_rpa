package com.albertsons.argus.q2c.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.dataq2c.bo.oracle.custom.OutboundProcessDetailsBO;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.q2c.service.Q2cAutomationService;
import com.albertsons.argus.q2c.service.Q2cScheduledTaskService;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@Service
public class Q2cScheduledTaskServiceImpl implements Q2cScheduledTaskService{
    private static final Logger LOG = LogManager.getLogger(Q2cScheduledTaskServiceImpl.class);
    
    public String automationRunId;
    
    @Autowired
    private com.albertsons.argus.dataq2c.repo.oracle.GetOracleTablesCustomRepo GetOracleTablesCustomRepo;
    
    @Autowired
    private Q2cAutomationService q2cAutomationService;

    @Autowired
    private Environment environ;

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.ar}")
    public void runARQueryOutbound() {
        try {

            LOG.info("Run Outbound AR Query Started");
            q2cAutomationService.sendNotificationEmail("Outbound AR Query - STARTED", "Run Outbound AR Query started", false);
            List<OutboundProcessDetailsBO> outboundARCustBO = GetOracleTablesCustomRepo.getArCustProcessDetailsBOs();
            
            List<String> errList = q2cAutomationService.processRecordHourly(outboundARCustBO, "Q2C Outbound ERROR FOUND - AR", null);

            if (errList.size() > 0) {
                String mailBody = q2cAutomationService.buildMailBody(errList, "AR");
                q2cAutomationService.sendOutboundErrorEmail("Q2C - Outbound AR - ERROR FOUND", mailBody, true);
            }

            q2cAutomationService.sendNotificationEmail("Outbound AR Query - ENDED", "Run Outbound AR Query Ended", false);
            LOG.info("Run Outbound AR Query Ended");
        }
        catch(Exception e){
            LOG.error("Error in runARQueryOutbound. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.cross}")
    public void runCrosswalkQueryOutbound() {
        try {
            LOG.info("Run Outbound Crosswalk Query Started");
            q2cAutomationService.sendNotificationEmail("Outbound Crosswalk Query - STARTED", "Run Outbound Crosswalk Query started", false);

            List<OutboundProcessDetailsBO> outboundCrosswalkBO = GetOracleTablesCustomRepo.getCrosswalkProcessDetailsBOs();
            
            List<String> errList = q2cAutomationService.processRecordHourly(outboundCrosswalkBO, "Q2C - OUTBOUND CROSSWALK - ERROR FOUND", null);
            
            if (errList.size() > 0) {
                String mailBody = q2cAutomationService.buildMailBody(errList, "CROSSWALK");
                q2cAutomationService.sendOutboundErrorEmail("Q2C - Outbound Crosswalk - ERROR FOUND", mailBody, true);
            }

            q2cAutomationService.sendNotificationEmail("Outbound Crosswalk Query - ENDED", "Run Outbound Crosswalk Query Ended", false);
            LOG.info("Run Outbound Crosswalk Query Ended");
        }
        catch(Exception e){
            LOG.error("Error in runCrosswalkQueryOutbound. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.bal1}", zone="MST")
    public void runBalanceQueryOutbound1() {
        try {
            LOG.info("Run Outbound Customer Balance Query Sched 1 Started");
            q2cAutomationService.sendNotificationEmail("Outbound Customer Balance Query 1 - STARTED", "Run Outbound Customer Balance Query started", false);
            q2cAutomationService.outboundCustBalanceProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Customer Balance Query 1 - ENDED", "Run Outbound Customer Balance Query ended", false);
            LOG.info("Run Outbound Customer Balance Query Sched 1 Ended");
        }
        catch(Exception e){
            LOG.error("Error in runBalanceQueryOutbound1. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.bal2}", zone="MST")
    public void runBalanceQueryOutbound2() {
        try {
            LOG.info("Run Outbound Customer Balance Query Sched 2 Started");
            q2cAutomationService.sendNotificationEmail("Outbound Customer Balance Query 2 - STARTED", "Run Outbound Customer Balance Query started", false);
            q2cAutomationService.outboundCustBalanceProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Customer Balance Query 2 - ENDED", "Run Outbound Customer Balance Query ended", false);
            LOG.info("Run Outbound Customer Balance Query Sched 2 Ended");
        }
        catch(Exception e){
            LOG.error("Error in runBalanceQueryOutbound2. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.bal3}", zone="MST")
    public void runBalanceQueryOutbound3() {
        try {
            LOG.info("Run Outbound Customer Balance Query Sched 3 Started");
            q2cAutomationService.sendNotificationEmail("Outbound Customer Balance Query 3 - STARTED", "Run Outbound Customer Balance Query started", false);
            q2cAutomationService.outboundCustBalanceProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Customer Balance Query 3 - ENDED", "Run Outbound Customer Balance Query ended", false);
            LOG.info("Run Outbound Customer Balance Query Sched 3 Ended");
        }
        catch(Exception e){
            LOG.error("Error in runBalanceQueryOutbound3. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.stat1}",zone = "MST")
    public void runStatementQueryOutbound1() {
        try {
            LOG.info("Run Outbound Customer Statement Query 1 Started");
            q2cAutomationService.sendNotificationEmail("Outbound Customer Statement Query 1 - STARTED", "Run Outbound Customer Statement Query started", false);
            q2cAutomationService.outboundCustStatementProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Customer Statement Query 1 - ENDED", "Run Outbound Customer Statement Query ended", false);
            LOG.info("Run Outbound Customer Statement Query 1 Ended");
        }
        catch(Exception e){
            LOG.error("Error in runStatementQueryOutbound1. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.stat2}",zone = "MST")
    public void runStatementQueryOutbound2() {
        try {
            LOG.info("Run Outbound Statement Query 2 Started");
            q2cAutomationService.sendNotificationEmail("Outbound Customer Statement Query 2 - STARTED", "Run Outbound Customer Statement Query started", false);
            q2cAutomationService.outboundCustStatementProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Customer Statement Query 2 - ENDED", "Run Outbound Customer Statement Query ended", false);
            LOG.info("Run Outbound Statement Query 2 Ended");
        }
        catch(Exception e){
            LOG.error("Error in runStatementQueryOutbound2. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.stat3}",zone = "MST")
    public void runStatementQueryOutbound3() {
        try {
            LOG.info("Run Outbound Statement Query 3 Started");
            q2cAutomationService.sendNotificationEmail("Outbound Customer Statement Query 3 - STARTED", "Run Outbound Customer Statement Query started", false);
            q2cAutomationService.outboundCustStatementProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Customer Statement Query 3 - ENDED", "Run Outbound Customer Statement Query ended", false);
            LOG.info("Run Outbound Statement Query 3 Ended");
        }
        catch(Exception e){
            LOG.error("Error in runStatementQueryOutbound3. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.prg}",zone = "MST")
    public void runPRGQueryOutbound() {
        try {
            LOG.info("Run Outbound PRG Query Started");
            q2cAutomationService.sendNotificationEmail("Outbound PRG Query - STARTED", "Run PRG Query started", false);
            q2cAutomationService.outboundPrgProcess();
            q2cAutomationService.sendNotificationEmail("Outbound PRG Query - ENDED", "Run PRG Query ended", false);
            LOG.info("Run Outbound PRG Query Ended");
        }
        catch(Exception e){
            LOG.error("Error in runPRGQueryOutbound. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.sdcheck1}",zone = "MST")
    public void runStaleDatedQueryOutbound1() {
        try {
            LOG.info("Run Outbound Stale Dated Query Started");
            q2cAutomationService.sendNotificationEmail("Outbound Stale Dated Checks Query 1 - STARTED", "Run Stale Dated Checks Query started", false);
            q2cAutomationService.outboundStaleDatedProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Stale Dated Checks Query 1 - ENDED", "Run Stale Dated Checks Query ended", false);
            LOG.info("Run Outbound Stale Dated Query Started");
        }
        catch(Exception e){
            LOG.error("Error in runStaleDatedQueryOutbound1. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.sdcheck2}",zone = "MST")
    public void runStaleDatedQueryOutbound2() {
        try {
            LOG.info("Run Outbound Stale Dated Query Started");
            q2cAutomationService.sendNotificationEmail("Outbound Stale Dated Checks Query 2 - STARTED", "Run Stale Dated Checks Query started", false);
            q2cAutomationService.outboundStaleDatedProcess();
            q2cAutomationService.sendNotificationEmail("Outbound Stale Dated Checks Query 2 - ENDED", "Run Stale Dated Checks Query ended", false);
            LOG.info("Run Outbound Stale Dated Query Started");
        }
        catch(Exception e){
            LOG.error("Error in runStaleDatedQueryOutbound2. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.uware}",zone = "MST")
    public void runUwareValidationOutbound1() {
        try {
            LOG.info("Run Uware Validation Started");
            AutomationUtil util = new AutomationUtil();
            q2cAutomationService.sendNotificationEmail("Outbound Uware Query - STARTED", "Run Uware Query started", false);
            
            Browser browser = Playwright.create().chromium()
	                .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
			
		    BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
            Page page = context.newPage();
            q2cAutomationService.navigateMIMLogin(page);
            String labelName = q2cAutomationService.filterOIPLabMIM(page);
            
            if (labelName.isEmpty()) {
                page.close();
                TimeUnit.MINUTES.sleep(60);
                page = context.newPage();
                q2cAutomationService.navigateMIMLogin(page);
                labelName = q2cAutomationService.filterOIPLabMIM(page);
                if (labelName.isEmpty()) {
                    page.close();
                    String formattedDate = util.toDateString(new Date(), "yyyyMMdd", "");
                    String mailBody = q2cAutomationService.getUwareMailBody();
                    q2cAutomationService.sendOutboundErrorEmail("Q2C - UWARE - No MIM File - " + formattedDate, mailBody, true);   
                }  
            }

            browser.close();
            q2cAutomationService.sendNotificationEmail("Outbound Uware Query - ENDED", "Run Uware Query ended", false);
            LOG.info("Run Uware Validation Ended");
        }
        catch(Exception e){
            LOG.error("Error in runUwareValidationOutbound1. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.pospay1}",zone = "MST")
    public void runPospayVoidsOutbound1() {
        try {
            LOG.info("Run Positive Pay and Voids Query STARTED");
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched1 - STARTED", "Run Positive Pay and Voids Query started", false);
            List<String> labelList = Arrays.asList(environ.getProperty("q2c.automation.pospay.labels.sched1").split("\\|"));
            q2cAutomationService.outboundPosPayVoidsProcess(labelList);
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched1 - ENDED", "Run Positive Pay and Voids Query ended", false);
            LOG.info("Run Positive Pay and Voids Query ENDED");
        }
        catch(Exception e){
            LOG.error("Error in runPospayVoidsOutbound1. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.pospay2}",zone = "MST")
    public void runPospayVoidsOutbound2() {
        try {
            LOG.info("Run Positive Pay and Voids Query STARTED");
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched2 - STARTED", "Run Positive Pay and Voids Query started", false);
            List<String> labelList = Arrays.asList(environ.getProperty("q2c.automation.pospay.labels.sched2").split("\\|"));
            q2cAutomationService.outboundPosPayVoidsProcess(labelList);
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched2 - ENDED", "Run Positive Pay and Voids Query ended", false);
            LOG.info("Run Positive Pay and Voids Query ENDED");
        }
        catch(Exception e){
            LOG.error("Error in runPospayVoidsOutbound2. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.pospay3}",zone = "MST")
    public void runPospayVoidsOutbound3() {
        try {
            LOG.info("Run Positive Pay and Voids Query STARTED");
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched3 - STARTED", "Run Positive Pay and Voids Query started", false);
            List<String> labelList = Arrays.asList(environ.getProperty("q2c.automation.pospay.labels.sched2").split("\\|"));
            q2cAutomationService.outboundPosPayVoidsProcess(labelList);
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched3 - ENDED", "Run Positive Pay and Voids Query ended", false);
            LOG.info("Run Positive Pay and Voids Query ENDED");
        }
        catch(Exception e){
            LOG.error("Error in runPospayVoidsOutbound3. . .");
            LOG.error(e);
        }
    }

    @Override
    @Scheduled(cron = "${com.argus.q2c.scheduled.task.cron.value.pospay4}",zone = "MST")
    public void runPospayVoidsOutbound4() {
        try {
            LOG.info("Run Positive Pay and Voids Query STARTED");
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched4 - STARTED", "Run Positive Pay and Voids Query started", false);
            List<String> labelList = Arrays.asList(environ.getProperty("q2c.automation.pospay.labels.sched2").split("\\|"));
            q2cAutomationService.outboundPosPayVoidsProcess(labelList);
            q2cAutomationService.sendNotificationEmail("Outbound Positive Pay and Voids Sched4 - ENDED", "Run Positive Pay and Voids Query ended", false);
            LOG.info("Run Positive Pay and Voids Query ENDED");
        }
        catch(Exception e){
            LOG.error("Error in runPospayVoidsOutbound4. . .");
            LOG.error(e);
        }
    }
}
