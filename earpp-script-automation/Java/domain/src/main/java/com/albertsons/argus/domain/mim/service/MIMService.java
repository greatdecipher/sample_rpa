package com.albertsons.argus.domain.mim.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface MIMService {
    
    public Page navigateMimLogin(Browser browser, BrowserContext browserContext, String loginBase);
    
}
