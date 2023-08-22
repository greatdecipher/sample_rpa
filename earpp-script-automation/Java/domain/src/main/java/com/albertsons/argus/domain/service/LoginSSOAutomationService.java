package com.albertsons.argus.domain.service;

import com.microsoft.playwright.Page;

public interface LoginSSOAutomationService {
    public Page openBrowser();
    public Page loginSSO(Page page, String url, String username, String password, String secretKey) throws Exception;
    public String getMfaCode(String pyScript, String secretKey);
}
