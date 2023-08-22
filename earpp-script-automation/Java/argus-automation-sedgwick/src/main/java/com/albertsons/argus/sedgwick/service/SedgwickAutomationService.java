package com.albertsons.argus.sedgwick.service;

import java.io.IOException;
import java.util.List;

import com.albertsons.argus.sedgwick.exception.ArgusSedgwickException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

public interface SedgwickAutomationService {
    
    public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";

    public BrowserContext getBrowserContext(Browser browser) throws PlaywrightException;

    public Page getPage(BrowserContext context) throws PlaywrightException;

	public void delay(long seconds);
    
    public void closePlaywright();

    public boolean checkElementIsVisible(Page page, String selector, String attrib);

    public String runVBScript(String arg) throws IOException;

    public void deleteSedgwickFilesInFolder(String sFolder);

    public Boolean sendEmailToRequestor(String recipientEmail, String mailSubject, String attachmentPath);

    public void processSedgwickEmails(String attachmentFilePath, Page page, String automationRunId);

    public Boolean loginPeoplesoft(Page page);

    public List<String> exportDataFromPeoplesoft(Page page, String empId, String startDate, String endDate, String empName);
}
