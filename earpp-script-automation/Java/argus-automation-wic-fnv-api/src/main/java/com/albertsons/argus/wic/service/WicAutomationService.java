package com.albertsons.argus.wic.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.wic.exception.ArgusWicException;
import com.albertsons.argus.wic.ws.bo.ResponseGetTaskListBO;
import com.albertsons.argus.wic.ws.bo.ResponseUpdateTaskBO;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.springframework.web.client.RestClientException;

public interface WicAutomationService {
	
	public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String MIM_LOGIN_URL = "playwright.uri.mim.login";
    public static final String LOG_FOLDER = "argus.mim.log.folder";
	public static final String WIC_LOCAL_FOLDER = "wic.outlook.attachment.save.path";

    public void delay(long seconds);
    public boolean fileExists(String FilePath);
    public void writeLog(String strMessage, String sFile);
    public void createFolder(String FolderURL);
	public void runShCmd(String user, String password, String host, String ritNumber, String formattedStores, String taskNumber) throws IOException, JSchException, SftpException, ArgusMailException;
	public String runOutlookScript(String arg) throws IOException;
    public String formatStoreNumbers(String storeList);
    public String runCmd(String cmd) throws JSchException, IOException;
    public String captureCmdOutput(InputStream in, Channel channel) throws IOException;
    public String captureCmdMultiOutput(InputStream in, Channel channel) throws IOException;
    public void openSession(String user, String password, String host) throws JSchException, ArgusMailException;
    public void closeSession();
    public String runSudoExecCmd(Session jschSession, List<String> cmdMulti);
    public void sendValidationResultEmail(String resultString, String ritNo, String taskNo) throws ArgusMailException;
    public void deleteWicFilesInFolder(String sFolder);

    public ResponseUpdateTaskBO updateTask(String requestBody) throws RestClientException;
    public ResponseGetTaskListBO getTaskLists(String requestBody) throws RestClientException;
    public Boolean downloadAttachment(String attachmentURL);
    public ResponseGetTaskListBO callGetTaskList();
    public ResponseUpdateTaskBO callUpdateTask(String responseBody, String taskNumber);
    public String getSysIdFromAttachmentUrl(String attachmentUrl);
} 
