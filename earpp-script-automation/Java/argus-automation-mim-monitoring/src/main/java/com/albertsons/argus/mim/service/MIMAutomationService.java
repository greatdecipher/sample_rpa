package com.albertsons.argus.mim.service;

import com.albertsons.argus.mim.exception.ArgusMimException;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;

public interface MIMAutomationService {
	
	public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String MIM_LOGIN_URL = "playwright.uri.mim.login";
    public static final String LOG_FOLDER = "argus.mim.log.folder";
	
    public String mainMimMonitoring(String[] LabelList) throws ArgusMimException;
    
    public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException;
    
    public Page navigateMIMLogin(BrowserContext context) throws ArgusMimException;
    
    public void inputLabelDetails(Page mainPage, String labelName, String labelThresh) throws ArgusMimException;
    
    public String getResultTableValues(Page mainPage, String labelThresh) throws ArgusMimException;
    
    public String[] getLabelDetails(String labelNames);
    
    public void navigateTransferStatusPage(Page mainPage);
    
    public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit) throws ArgusMimException;
    
    public void waitForLoadingFrame(Page mainPage, String waitElem, String frameName, long waitLimit) throws ArgusMimException;
    
    public void mimFormatAndSendEmailBody(String emailBody) throws ArgusMimException;
    
    public void delay(long seconds);
    
    public String padRight(String sInStr, int iLength);
    
    public boolean fileExists(String filePath);
    
    public long getFTimestamp(String sFile);
    
    public void writeLog(String strMessage, String sFile);
    
    public void deleteFilesByAge(String sFolder, Integer iMaxAge);
    
    public void createFolder(String folderURL);
      
}
