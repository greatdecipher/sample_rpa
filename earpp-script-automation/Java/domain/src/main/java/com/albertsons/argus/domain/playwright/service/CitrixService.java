package com.albertsons.argus.domain.playwright.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface CitrixService {
    public Page navigateCitrixSaasLogin(Browser browser, BrowserContext browserContext, String secret, String userName, String password);
}
