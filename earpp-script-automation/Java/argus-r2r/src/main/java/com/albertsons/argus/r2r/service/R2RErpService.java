package com.albertsons.argus.r2r.service;

import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface R2RErpService {
    public static final String ERP_LOGIN_URL = "playwright.uri.oracle.saas.login";
    public static final Integer NORMAL_PRIORITY = 3;
    
    public void mainErpTask(FileDetails fileDetails, String groupId, String execTimestamp) throws ArgusR2RException;

    public Page navigateErpLogin(Browser browser, BrowserContext browserContext) throws ArgusR2RException;

    public Page navigateJournals(FileDetails fileDetails, Browser browser, BrowserContext browserContext);

    public Page checkFileLoaded(FileDetails fileDetails, String groupId, Browser browser, BrowserContext browserContext);

    public boolean filterFileLoaded(Page page, FileDetails fileDetails, String groupId);

    public boolean checkFilePosted(Page page, FileDetails fileDetails, String groupId, Browser browser, BrowserContext browserContext);

    public boolean getFilePostStatus(Page page, FileDetails fileDetails, Browser browser, BrowserContext browserContext, String groupId);
    
    public boolean readFilePostStatus(Page page, FileDetails fileDetails);

    public void sleepNearestHalfHour();

    public void sendErpEmail(String[] recipients, String[] cc, String subject, String body);

}
