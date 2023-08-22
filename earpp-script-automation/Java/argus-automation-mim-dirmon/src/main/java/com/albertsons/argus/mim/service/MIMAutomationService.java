package com.albertsons.argus.mim.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.albertsons.argus.mim.exception.ArgusMimException;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;

public interface MIMAutomationService {
	
	public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String MIM_LOGIN_URL = "playwright.uri.mim.login";
    public static final String LOG_FOLDER = "argus.mim.log.folder";
	
    public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException;
    
    public Page navigateMIMLogin() throws ArgusMimException;
    
    public void inputLabelDetails(Page MainPage, String LabelName, String LabelThresh) throws ArgusMimException;
    
    public String getResultTableValues(Page MainPage, String LabelThresh) throws ArgusMimException;
    
    public String[] getLabelDetails(String LabelNames);
    
    public void navigateTransferStatusPage(Page MainPage);
    
    public void waitLoadMainPage(Page MainPage, String WaitElement, long WaitLimit) throws ArgusMimException;
    
    public void waitForLoadingFrame(Page MainPage, String WaitElem, String FrameName, long WaitLimit) throws ArgusMimException;
    
    public void mimFormatAndSendEmailBody(String EmailBody) throws ArgusMimException;
    
    public void delay(long seconds);
    
    public String padRight(String sInStr, int iLength);
    
    public boolean fileExists(String FilePath);
    
    public long getFTimestamp(String sFile);
    
    public void writeLog(String strMessage, String sFile);
    
    public void deleteFilesByAge(String sFolder, Integer iMaxAge);
    
    public void createFolder(String FolderURL);

	public void selectOptionElementInFrame(Frame mainframe, String selector, String framename, String attrib, String labelValue) throws ArgusMimException;

	public void inputElementInFrame(Frame mainframe, String selector, String framename, String attrib, String inputValue) throws ArgusMimException;

	public void clickElementInFrameByTagList(Frame mainframe, String selector, String framename, String attrib, String tag) throws ArgusMimException;

	public void dblClickElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException;

	public void startMimDirmon(Page page) throws ArgusMimException;

	public String getTextElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException;

	public void createDirmonInstance(Page page, String uniqueNodeName, String registryLabel, String queueManager) throws ArgusMimException;

	public void createDirmonEntry(Page page, String uniqueNodeName, String requestTemplate, String monitoredDir,
			String include, String queueManager) throws ArgusMimException;

	public String[] getDestinationName(String destLabel, String strdqm);

	public void setupExitRoutine(Page page, String exitRoutine, String scriptName, String exitMethod, String queueManager,
			String userData, String label, String targetUid, String uniqueNodeName) throws ArgusMimException;

	public void createDirmonTemplate(Page page, String requestName, String label, String transferType, String writeMode,
			String ioAccess, String userArgs, String exitRoutine, String sqm, String dqm, String destFileName,
			String organization, String recordFormat, String lrecl, String priQuantity, String secQuantity,
			String dirBlocks, String spaceUnits, Boolean isMultipleDestination) throws ArgusMimException;

	public boolean checkElementInFrame(Frame mainframe, String selector, String framename, String attrib)
			throws ArgusMimException;

	public void disableDirmonLabel(Page page, String uniqueNodeName, String label) throws ArgusMimException;

	public void checkUncheckElementInFrame(Frame mainframe, String selector, String framename, String attrib, String check)
			throws ArgusMimException;

	public void deleteDirmonEntry(Page page, String uniqueNodeName, String label) throws ArgusMimException;

	public void verifyRunningMIM(Page page, String sourceServer, String destServer) throws ArgusMimException;

	public void verifyCommunicationMIM(Page page, String sourceServer, String destServer) throws ArgusMimException;

	public void verifyExistingDirmonEntry(Page page, String uniqueNodeName) throws ArgusMimException;

	public Map<Integer, List<String>> readExcelValues(String fileName) throws IOException;

	public void createDestinationTemplate(Page page, String destName, String dqm, String destFileName, String userArgs,
			String destLabel, String[] destinations, String requestName, String label, String transferType,
			String writeMode, String ioAccess, String exitRoutine, String sqm, String organization, String recordFormat,
			String lrecl, String priQuantity, String secQuantity, String dirBlocks, String spaceUnits,
			String templatePath) throws ArgusMimException, IOException;

	public String inputMultipleDestinationDetails(Page page, String uniqueNodeName, Map<Integer, List<String>> map) throws ArgusMimException;

	public void selectMultipleDestination(Page page, Map<Integer, List<String>> map, String listName) throws ArgusMimException;
}
