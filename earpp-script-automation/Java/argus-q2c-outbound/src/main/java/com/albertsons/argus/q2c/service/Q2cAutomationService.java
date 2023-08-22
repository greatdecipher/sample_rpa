package com.albertsons.argus.q2c.service;

import java.time.ZonedDateTime;
import java.util.List;

import com.albertsons.argus.dataq2c.bo.oracle.custom.OutboundProcessDetailsBO;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.q2c.exception.ArgusQ2cException;
import com.albertsons.argus.q2c.exception.ArgusQ2cRuntimeException;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface Q2cAutomationService {
    
    public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";
    public static final String OUTBOUND_FILE_PATH = "com.argus.outbound.file.path";

    public void outboundCustBalanceProcess();
    public void outboundCustStatementProcess();
    public void outboundPrgProcess();
    public void outboundStaleDatedProcess();
    public Boolean validateOipTransLabel(Page page);
    public String filterOIPLabMIM(Page mainPage);
    public Page navigateMIMLogin(Page context) throws ArgusQ2cException;
    public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit) throws ArgusQ2cException;
    public List<String> processRecordHourly(List<OutboundProcessDetailsBO> outboundItemList, String processName, ZonedDateTime zdt) throws ArgusQ2cException;
    public String getUwareMailBody();
    public String buildMailBody(List<String> instanceID, String processName);
    public void sendOutboundErrorEmail(String processName, String mailBody, boolean isHTML);
    public void sendNotificationEmail(String subject, String mailBody, boolean isHTML);
    public void writeToFile(String sFileName);
    public boolean fileExists(String filePath);
    public long getFTimestamp(String sFile);
    public void deleteFilesByName(String sFolder,String sFileName);
    public void delay(long seconds);
    public void outboundPosPayVoidsProcess(List<String> labelList);
    public List<String> filterPospayLabelsMIM(Page mainPage, List<String> labelList);
    public String buildMailBodyPospay(List<String> labelStatusList);
}
