package com.albertsons.argus.wic.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.wic.exception.ArgusWicException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;

public interface WicAutomationService {
	
	public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String MIM_LOGIN_URL = "playwright.uri.mim.login";
    public static final String LOG_FOLDER = "argus.mim.log.folder";
	public static final String WIC_LOCAL_FOLDER = "wic.outlook.attachment.save.path";

    public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusWicException;
    
    public void waitForLoadingFrame(Page MainPage, String WaitElem, String FrameName, long WaitLimit) throws ArgusWicException;
    
    public void delay(long seconds);
    
    public String padRight(String sInStr, int iLength);
    
    public boolean fileExists(String FilePath);
    
    public long getFTimestamp(String sFile);
    
    public void writeLog(String strMessage, String sFile);
    
    public void deleteFilesByAge(String sFolder, Integer iMaxAge);
    
    public void createFolder(String FolderURL);

	public void runShCmd(String user, String password, String host, String emailSubject) throws IOException, JSchException, SftpException, ArgusMailException;

	public String runOutlookScript(String arg) throws IOException;

    public String formatStoreNumbers();

    public String runCmd(String cmd) throws JSchException, IOException;

    public String captureCmdOutput(InputStream in, Channel channel) throws IOException;
    public String captureCmdMultiOutput(InputStream in, Channel channel) throws IOException;

    public void openSession() throws JSchException;
    
    public void closeSession();

    public String runSudoExecCmd(Session jschSession, List<String> cmdMulti);
    
    public void sendValidationResultEmail(String resultString, String ritNo) throws ArgusMailException;

    public void deleteWicFilesInFolder(String sFolder);
}
