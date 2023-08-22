package com.albertsons.argus.q2c.inbound.lockbox.service;

import java.util.List;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface LockboxService {
    public static final Integer NORMAL_PRIORITY = 3;

    public void lockboxMain(String filePrefix);
    public String getFilenameInMIM(Page page, String labelName);
    public void delay(long seconds);
    public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit);
    public List<String> lockboxQueryValidation(String fileName);
    public void sendLockboxEmail(String mailSubject, String mailBody, Boolean isHtml);
    public Boolean bipReportValidation(Page page, List<String> queryResults);
    public Page navigateBipPage(Browser browser, BrowserContext context);
    public Page loginMFA(Page page);
    public String getMfaCode(String pythonScript, String secretKey);
    public void sendNotificationEmail(String subject, String mailBody, boolean isHTML);
    public String  buildLockboxMailBody(String queryCount, String bipCount);
}
