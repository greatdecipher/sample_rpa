package com.albertsons.argus.domain.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface LoginService {
    /**
     * @apiNote - Can used any generic uri, username,password, mfa secrets
     * @param browser
     * @param browserContext
     * @param uri
     * @param username
     * @param password
     * @param mfaSecret
     * @return
     */
    public Page loginPage(Browser browser, BrowserContext browserContext, String uri, String username, String password, String mfaSecret);
}
