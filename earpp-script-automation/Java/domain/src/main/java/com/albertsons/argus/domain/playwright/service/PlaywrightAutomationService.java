package com.albertsons.argus.domain.playwright.service;

import com.albertsons.argus.domain.exception.DomainException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

/**
 * @author kbuen03 
 * @version 1.0
 * @since 04/04/22
 * 
 */
public interface PlaywrightAutomationService {
    public Browser openBrowser() throws DomainException;
    public Browser openIeBrowser() throws DomainException;
    public void closedBrowser(Browser browser);
    public void pageClick(Page page, String attribute, String prop, String value) throws PlaywrightException;
    public void pageFill(Page page, String attribute, String prop, String value, String value2) throws PlaywrightException;
    public Locator pageLocatorWait(Page page, String attribute, String prop, String value) throws PlaywrightException;
    public void screenshotSaveFile(Page page, String fileNameDirector) throws PlaywrightException;
}
