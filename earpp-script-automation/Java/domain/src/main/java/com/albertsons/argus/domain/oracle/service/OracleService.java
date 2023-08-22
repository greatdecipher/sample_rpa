package com.albertsons.argus.domain.oracle.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface OracleService {

    //TODO: Need to reusable object for oracle login. Please don't used anymore. Please refer to the LoginService
    public Page navigateOracleSaasLogin(Browser browser, BrowserContext browserContext);

    public Page navigateScheduledProcesses(Browser browser, BrowserContext browserContext);

    public Page navigateOracleOtbiJobHistory(Browser browser, BrowserContext browserContext);

    public void expandSearchButton(Page page);
}
