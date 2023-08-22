package com.albertsons.argus.domain.playwright.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;

/**
 * @author kbuen03
 * @version 1.0
 * @since 04/04/22
 * 
 */
@Service
public class PlaywrightAutomationServiceImpl implements PlaywrightAutomationService {
    private static final Logger LOG = LogManager.getLogger(PlaywrightAutomationServiceImpl.class);

    @Autowired
    private Environment environment;

    @Override
    public Browser openBrowser() throws DomainException {
        LOG.log(Level.DEBUG, () -> "start openBrowser. . .");

        String timeoutStr = environment.getProperty("playwright.domain.browser.timeout.value");

        Browser browser = Playwright.create().chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(
                                Boolean.valueOf(environment.getProperty("playwright.domain.browser.not.show.browser")))
                        .setSlowMo(500)
                        .setTimeout(Double.valueOf(timeoutStr)));
        LOG.log(Level.DEBUG, () -> "end openBrowser. . .");

        return browser;
    }

    @Override
    public void closedBrowser(Browser browser) {
        LOG.log(Level.DEBUG, () -> "closedBrowser. . .");
        browser.close();

    }

    @Override
    public void pageClick(Page page, String attribute, String prop, String value) throws PlaywrightException {
        page.waitForLoadState(LoadState.NETWORKIDLE);

        if (StringUtils.isNotBlank(attribute))
            page.click(attribute + "[" + prop + " = " + value + "]");
        else if (StringUtils.isNotBlank(prop))
            page.click(prop + " = " + value);
        else // Custom selector
            page.click(value);
    }

    @Override
    public void pageFill(Page page, String attribute, String prop, String value, String value2)
            throws PlaywrightException {
        page.waitForLoadState(LoadState.NETWORKIDLE);

        if (StringUtils.isNotBlank(attribute))
            page.fill(attribute + "[" + prop + " = " + value + "]", value2);
        else if (StringUtils.isNotBlank(prop))
            page.fill(prop + " = " + value, value2);
        else// Custom selector
            page.fill(value, value2);
    }

    @Override
    public Locator pageLocatorWait(Page page, String attribute, String prop, String value) throws PlaywrightException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        
        Locator loc = null;
        if (StringUtils.isNotBlank(attribute)) {
            loc = page.locator(attribute + "[" + prop + " = " + value + "]");
            loc.waitFor();
        } else if (StringUtils.isNotBlank(prop)) {
            loc = page.locator(prop + " = " + value);
            loc.waitFor();
        } else { // Custom selector
            loc = page.locator(value);
            loc.waitFor();
        }

        return loc;
    }

    @Override
    public Browser openIeBrowser() throws DomainException {
        LOG.log(Level.DEBUG, () -> "start openIeBrowser. . .");
        
        String timeoutStr = environment.getProperty("playwright.domain.browser.timeout.value");
        
        Browser browser = Playwright.create().chromium()
                .launch(new BrowserType.LaunchOptions()
                .setChannel("msedge")
                .setHeadless(Boolean.valueOf(environment.getProperty("playwright.domain.browser.not.show.browser")))
                .setSlowMo(500)
                .setTimeout(Double.valueOf(timeoutStr)));
        LOG.log(Level.DEBUG, () -> "end openIeBrowser. . .");
        
        return browser;
    }
    
    @Override
    public void screenshotSaveFile(Page page, String pathDirectory) {
        AutomationUtil automationUtil = new AutomationUtil();

        String timestampStr = automationUtil.toDateString(new Date(), "yyyyMMddHHmmss", "Asia/Singapore");
        String fileName = "/ss_"+timestampStr+".png";
        
        try {
        // Capture a screenshot
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        LOG.debug("File Name and directory: "+fileName);
            Files.write(Path.of(pathDirectory+fileName), screenshot, StandardOpenOption.CREATE);
        } catch (IOException | PlaywrightException e) {
           LOG.error("screenshotSaveFile() exception: "+e);
        }
    }

}