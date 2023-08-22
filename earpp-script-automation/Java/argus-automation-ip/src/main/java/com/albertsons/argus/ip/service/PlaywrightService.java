package com.albertsons.argus.ip.service;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

public interface PlaywrightService {
    public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib);
    public void waitForLoadingFrame(Page mainPage, String waitElem, String frameName, long waitLimit);
    public Locator pageLocatorWait(Page page, String attribute, String prop, String value) throws PlaywrightException;
}
