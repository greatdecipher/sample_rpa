package com.albertsons.argus.wic.service.impl;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.wic.service.JsonService;
import com.albertsons.argus.wic.service.WicAutomationService;
import com.albertsons.argus.wic.ws.bo.ResponseGetTaskListBO;
import com.albertsons.argus.wic.ws.bo.ResponseUpdateTaskBO;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


@Service
public class WicAutomationServiceImpl implements WicAutomationService {
	public static final Logger LOG = LogManager.getLogger(WicAutomationServiceImpl.class);
	static Session session;
    static String strMultiOutput = "";
	static String sendScriptOutput = "";
	
	@Autowired
	private Environment environ;

	@Autowired
	private EmailService emailService;

	@Autowired
    private RestTemplate restTemplate;
    
	@Autowired
    private JsonService<ResponseGetTaskListBO> jsonResponseGetTaskListBOService;
    
	@Autowired
    private JsonService<ResponseUpdateTaskBO> jsonResponseUpdateTaskBOService;

    @Override
    public ResponseUpdateTaskBO updateTask(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method updateTask(). . .");
        return jsonResponseUpdateTaskBOService.toJson(restTemplate.exchange(environ.getProperty("wic.web.service.url.update"), 
			HttpMethod.PUT, getHttpEntity(requestBody,"Token"),String.class).getBody()) ; //TODO: Need full URI
    }

    @Override
    public ResponseGetTaskListBO getTaskLists(String requestBody) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method getTaskLists(). . .");
        return jsonResponseGetTaskListBOService.toJson(restTemplate.exchange(environ.getProperty("wic.web.service.url.get"), HttpMethod.POST, getHttpEntity(requestBody,"Token"),String.class).getBody());  //TODO: Need full URI
    }
    
    private HttpEntity<Object> getHttpEntity(String requestBody,String headerName){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(headerName, environ.getProperty("wic.web.service.auth.header.value"));//TODO: Need add value
        return new HttpEntity<>(requestBody,headers);
    }
	
	@Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean fileExists(String FilePath) {
		LOG.log(Level.DEBUG, () -> "Run fileExists method . . .");

		try {
			File FileName = new File(FilePath);

			if (FileName.exists()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public void writeLog(String strMessage, String sFile) {
		LOG.log(Level.DEBUG, () -> "Start writeLog method . . .");

		try {

			if (!fileExists(sFile)) {
				File myObj = new File(sFile);
				myObj.createNewFile();
			}

			FileWriter fw = new FileWriter(sFile, true);
			fw.write(strMessage + "\n");
			fw.close();

		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "writeLog method failed . . .");
		}

		LOG.log(Level.DEBUG, () -> "End writeLog method . . .");

	}

	@Override
	public void deleteWicFilesInFolder(String sFolder) {
		LOG.log(Level.DEBUG, () -> "Start deleteWicFilesInFolder method . . .");
		File FolderPath = new File(sFolder);
		if (FolderPath.isDirectory()) {
			try {
				for (File fName : FolderPath.listFiles()) {
					if (fName.isDirectory() | fName.getName().toUpperCase().contains("GETEMAIL")) {
						continue;
					} else {
						fName.delete();
					}
				}
			} catch (Exception e) {
				LOG.log(Level.DEBUG, () -> "deleteWicFilesInFolder method failed . . .");
			}

		}

		LOG.log(Level.DEBUG, () -> "End deleteWicFilesInFolder method . . .");
	}

	@Override
	public void createFolder(String FolderURL) {
		LOG.log(Level.DEBUG, () -> "Start createFolder method . . .");
		try {
			File FolderName = new File(FolderURL);
			if (!FolderName.isDirectory()) {
				FolderName.mkdir();
			}
		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "createFolder method failed . . .");
		}

		LOG.log(Level.DEBUG, () -> "End createFolder method . . .");
	}

	@Override
	public void runShCmd(String user, String password, String host, String ritNumber, String formattedStores, String taskNumber) throws IOException, JSchException, SftpException, ArgusMailException {
		String sRIT = ritNumber.substring(ritNumber.toUpperCase().indexOf("RITM"));
		
		String cmdOutput, fileExistsServer;
		List <String> multiCmdParam = new ArrayList<>();
		String startTime;
		String endTime;
		long  attFileSize = 0;

		startTime = DateTimeFormatter.ofPattern("yyyyMMdd.HH:mm:ss").format(LocalDateTime.now());

		//-------------------Connect to host and open SFTP Channel---------------------//
		openSession(user, password, host);

		LOG.info("Store List: " + formattedStores);

		cmdOutput = runCmd("[ -d " + environ.getProperty("jsch.wic.server.directory") + sRIT + " ] && echo $?");
		LOG.info("Directory Exists: " + cmdOutput);
		fileExistsServer = runCmd("[ -f " + environ.getProperty("jsch.wic.server.directory") + "CVBCONVN_" + sRIT + ".DAT ] && echo $?");
		LOG.info("File Exists: " + cmdOutput);

		if (cmdOutput.contains("0") || fileExistsServer.contains("0")) {
			LOG.info("Existing directory / file found for " + sRIT + ", Terminating session. . ." );
			deleteWicFilesInFolder(environ.getProperty("wic.outlook.attachment.save.path"));
			sendValidationResultEmail("Existing file / directory found for " + sRIT, sRIT, taskNumber);
			return;
		}

		//----------------------Create store.txt file in server------------------------//
		multiCmdParam.add("echo '" + formattedStores + "' > " + environ.getProperty("jsch.wic.server.directory") + "store.txt; echo \'done\'");
		cmdOutput = runSudoExecCmd(session, multiCmdParam);
		multiCmdParam.clear();
		LOG.info("store.txt successfully created in server . . .");

		//--------------------Rename .DAT file email attachment-----------------------//
		File[] files = new File(environ.getProperty(WIC_LOCAL_FOLDER)).listFiles();
		File renameFile = null;
		for (File file : files) {
			if (file.isFile()) {
				if (file.getName().toUpperCase().endsWith(".DAT")) {
					attFileSize = file.length();
					renameFile = new File(environ.getProperty(WIC_LOCAL_FOLDER) + "CVBCONVN_" + sRIT + ".DAT");
					
					if (file.renameTo(renameFile)) {
						break;
					}
					else {
						LOG.info("Unable to rename file");
						return;
					}
				}
			}
		}
		LOG.info("Attachment file size: " + attFileSize + " bytes");
		
		LOG.info("Opening sftp channel");
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp sftp = (ChannelSftp) channel;

		sftp.cd(environ.getProperty("jsch.wic.server.directory") + "tempdir/");
		//-----------------------SFTP put .DAT file in server------------------------//
		sftp.put(renameFile.getAbsolutePath(), renameFile.getName());
		LOG.info("SFTP put " + renameFile.getName() + " in server");
		sftp.disconnect();

		multiCmdParam.add("mv " + environ.getProperty("jsch.wic.server.directory") + "tempdir/" + renameFile.getName() + 
		" " + environ.getProperty("jsch.wic.server.directory") + renameFile.getName() + "; echo \'done\'");
		cmdOutput = runSudoExecCmd(session, multiCmdParam);
		multiCmdParam.clear();
		deleteWicFilesInFolder(environ.getProperty("wic.outlook.attachment.save.path"));
		
		//-------------------------Run validate.sh script----------------------------//
		multiCmdParam.add("cd " + environ.getProperty("jsch.wic.server.directory"));
		multiCmdParam.add("sh validate.sh " + sRIT + "; echo \'done\'");
		cmdOutput = runSudoExecCmd(session, multiCmdParam);
		multiCmdParam.clear();
		LOG.info("validate.sh executed. . .");
		
		//-------------Check contents of generated file from validate.sh-------------//
		cmdOutput = runCmd("cd " + environ.getProperty("jsch.wic.server.directory") + "; cat " + sRIT + ".txt");
		LOG.info(cmdOutput);

		//-------------------Make directory for RITM store logs----------------------//
		multiCmdParam.add("cd " + environ.getProperty("jsch.wic.server.directory"));
		multiCmdParam.add("mkdir " + environ.getProperty("jsch.wic.server.directory") + sRIT);
		multiCmdParam.add("chmod 775 " + environ.getProperty("jsch.wic.server.directory") + sRIT + "; echo \'done\'");
		cmdOutput = runSudoExecCmd(session, multiCmdParam);
		multiCmdParam.clear();

		LOG.info("directory for " + sRIT + " created . . .");

		//---------------------------Run send.sh script------------------------------//
		multiCmdParam.add("cd " + environ.getProperty("jsch.wic.server.directory"));
		multiCmdParam.add("sh send.sh -l " + sRIT + ".txt -i CVBCONVN_" + sRIT + ".DAT; echo \'done\'");
		cmdOutput = runSudoExecCmd(session, multiCmdParam);
		multiCmdParam.clear();

		LOG.info("send.sh executed. . .");
		
		//--------------------------Moving RITM files to folder-----------------------------//
		multiCmdParam.add("cd " + environ.getProperty("jsch.wic.server.directory"));
		multiCmdParam.add("mv CVBCONVN_" + sRIT + ".DAT " + sRIT);
		multiCmdParam.add("mv " + sRIT + ".txt " + sRIT);
		multiCmdParam.add("cp run.send.log " + sRIT + "; echo \'done\'");
		cmdOutput = runSudoExecCmd(session, multiCmdParam);
		multiCmdParam.clear();

		//-----------------------------Check logs for failure-------------------------------//
		LOG.info("Checking logs for failure. . .");
		if (sendScriptOutput.length() > 0) {
			multiCmdParam.add("cd " + environ.getProperty("jsch.wic.server.directory"));
			multiCmdParam.add("grep ssh run.send.log ; egrep -i 'fail|err' ./" + sRIT + "/i*.log; echo \'done\'");
			cmdOutput = runSudoExecCmd(session, multiCmdParam);
			cmdOutput = sendScriptOutput + "\n" + cmdOutput;
			multiCmdParam.clear();
		} else {
			delay(420);

			cmdOutput = runCmd("cd " + environ.getProperty("jsch.wic.server.directory") + sRIT + ";egrep -i 'error|fail' i*.log");
			LOG.info(cmdOutput);
			
		}
		closeSession();
		LOG.info(cmdOutput);
		
		//--------------------------Send validation email-----------------------------//
		LOG.info("Sending validation result email. . .");
		sendValidationResultEmail(cmdOutput, sRIT, taskNumber);

		endTime = DateTimeFormatter.ofPattern("yyyyMMdd.HH:mm:ss").format(LocalDateTime.now());
		LOG.info("Start: " + startTime + "\nEnd  : " + endTime);
	}
	

	@Override
	public void sendValidationResultEmail(String resultString, String ritNo, String taskNo) throws ArgusMailException{
		AutomationUtil util = new AutomationUtil();
		ResponseUpdateTaskBO updateTaskBO;
		try {
			if (resultString.toUpperCase().contains("FAIL") | resultString.toUpperCase().contains("ERROR") | 
				resultString.toUpperCase().contains("SEND.SH VALIDATION") | resultString.toUpperCase().contains("EXISTING")) {

				updateTaskBO = callUpdateTask("{\"number\":\"" + taskNo + "\"," +
					"\"work_notes\":\"" + environ.getProperty("wic.snow.worknotes.failed")+ "\"," + 
					"\"assignment_group\":\"" + environ.getProperty("wic.snow.assignment.group") +
					"\"}",taskNo);
				
				emailService.sendSimpleMessage(environ.getProperty("mail.wic.monitoring.from"), 
						environ.getProperty("mail.wic.monitoring.from.alias"), 
						environ.getProperty("mail.wic.monitoring.recipients", String[].class), 
						environ.getProperty("mail.wic.monitoring.cc", String[].class), 
						environ.getProperty("mail.wic.monitoring.subject").replace("RITNo", ritNo) + " FAILED - " 
						+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),""),
						resultString, NORMAL_PRIORITY, false);
			} else {
				
				updateTaskBO = callUpdateTask("{\"number\":\"" + taskNo + "\"," +
					"\"state\":\""+environ.getProperty("wic.snow.state.close") + "\"," +
					"\"work_notes\":\"" + environ.getProperty("wic.snow.worknotes.success")+ "\"," + 
					"\"assignment_group\":\"" + environ.getProperty("wic.snow.assignment.group") +
					"\"}",taskNo);

				emailService.sendSimpleMessage(environ.getProperty("mail.wic.monitoring.from"), 
						environ.getProperty("mail.wic.monitoring.from.alias"), 
						environ.getProperty("mail.wic.monitoring.recipients", String[].class), 
						environ.getProperty("mail.wic.monitoring.cc", String[].class), 
						environ.getProperty("mail.wic.monitoring.subject").replace("RITNo", ritNo) + " SUCCESS - " 
						+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),""),
						"", NORMAL_PRIORITY, false);
			}
			LOG.info("Validation result email sent. . .");
		} catch (Exception e) {
			LOG.info("Email sending error. . .");
			System.out.println(e.getMessage());
		}
	}

	@Override
	public String runSudoExecCmd(Session jschSession, List<String> cmdMulti) {
		String outStr = "";
		Boolean isSendScript = false;
		try {
			InputStream in = null;
			ChannelExec channelExec = (ChannelExec) jschSession.openChannel("exec");
			
			((ChannelExec) channelExec).setCommand(environ.getProperty("jsch.wic.sudo"));
			((ChannelExec) channelExec).setErrStream(System.err);
			
			channelExec.connect(5000);
			channelExec.setInputStream(null);
			for (String cmd : cmdMulti) {
				LOG.info("Executing command: " + cmd);
				if (cmd.toUpperCase().contains("SEND.SH")) {
					isSendScript = true;
				}
				OutputStream out = channelExec.getOutputStream();
				in = channelExec.getInputStream();
				InputStream err = channelExec.getErrStream();
				out.write((cmd + "\n").getBytes());
				out.flush();
			}

			LocalDateTime startTime;
			startTime = LocalDateTime.now();

			while (strMultiOutput.contains("done") == false) {
				outStr = captureCmdMultiOutput(in, channelExec);
				if (isSendScript) {
					long diff = ChronoUnit.SECONDS.between(startTime, LocalDateTime.now());
					if (diff > 180) {
						sendScriptOutput = "send.sh script validation timeout exceeded. . .";
						break;
					}
				}
			}
			LOG.debug(strMultiOutput);
			channelExec.disconnect();
		
			strMultiOutput = "";
			return outStr;
		} catch (Exception e) {
			return outStr;
		}

	}

	@Override
	public String runOutlookScript(String arg) throws IOException{
		
		Process p = Runtime.getRuntime().exec(arg);

		String text = new BufferedReader(
      	new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));

		return text;
	}

	@Override
	public String formatStoreNumbers(String storeList){
		String formattedStores = "";
		
		try {
			Scanner scanner;
			scanner = new Scanner(storeList);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				try {
					Integer a = Integer.parseInt(line);
					switch (line.length()) {
						case 1:
							line = "000" + line;
							break;
						case 2:
							line = "00" + line;
							break;
						case 3:
							line = "0" + line;
							break;
						default:
							break;
					}
				} catch (Exception e) {
					continue;
				}
				formattedStores = formattedStores + line;
				if (scanner.hasNext()) {
					formattedStores = formattedStores + "\n";
				}
			}
			scanner.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return formattedStores;
	}

	@Override
	public String runCmd(String cmd) throws JSchException, IOException {
            LOG.info("Executing command: " + cmd);
			String strOutput = "";
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();
            channel.connect();
            //passing creds only when you switch user
            if (cmd.startsWith("su -")) {
                LOG.info("Setting suPasswd now....");
                out.write((environ.getProperty("encrypted.wic.property.password") + "\n").getBytes());
                out.flush();
                LOG.info("Flushed suPasswd to cli...");
            }
            strOutput = captureCmdOutput(in, channel);
            channel.setInputStream(null);
            channel.disconnect();
			return strOutput;
    }

	@Override
    public String captureCmdOutput(InputStream in, Channel channel) throws IOException {
        LOG.info("Capturing cmdOutput now...");
		String strOutput = "";
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                strOutput = strOutput + new String(tmp, 0, i);
            }
            if (channel.isClosed()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
                LOG.error(ee.getMessage());
            }
        }
		return strOutput;
    }

	@Override
	public String captureCmdMultiOutput(InputStream in, Channel channel) throws IOException {
        LOG.info("Capturing cmdMultiOutput now...");
		
        byte[] tmp = new byte[1024];
		
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                strMultiOutput = strMultiOutput + new String(tmp, 0, i);
            }

            try {
				Thread.sleep(1000);
			} catch (Exception ee) {
				LOG.error(ee.getMessage());
			}
			break;
        }
		return strMultiOutput;
    }

	@Override
    public void openSession(String user, String password, String host) throws JSchException, ArgusMailException {
		try {
			JSch jSch = new JSch();
			session = jSch.getSession(user, host, 22);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(password);
			LOG.info("Connecting SSH to " + host + " - Please wait for few seconds... ");
			session.connect();
			LOG.info("Connected!");
		} catch (Exception e) {
			LOG.error("Unable to connect to " + host + " , Terminating process. . .");
			LOG.error(e);
			emailService.sendSimpleMessage(environ.getProperty("mail.wic.monitoring.from"), 
			environ.getProperty("mail.wic.monitoring.from.alias"), 
			environ.getProperty("mail.wic.monitoring.cc", String[].class), 
			environ.getProperty("mail.wic.monitoring.cc", String[].class), 
			"Unable to connect to " + host,
			"Unable to connect to " + host, NORMAL_PRIORITY, false);
			System.exit(0);
		}
        
    }

	@Override
    public void closeSession() {
        session.disconnect();
        LOG.info("Channel and Session disconnected successfully");
    }

	@Override
	public Boolean downloadAttachment(String attachmentURL){
		Boolean bool = true;
			try {
			
				URL myUrl = new URL(attachmentURL.replaceAll("[\\n\\t ]", ""));
				System.out.println(myUrl.toString());
				HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
				conn.setDoOutput(true);
				conn.setReadTimeout(60000);
				conn.setConnectTimeout(60000);
				//conn.setUseCaches(false);
				//conn.setAllowUserInteraction(false);
				//conn.setRequestProperty("Content-Type", "application/json");
				conn.setRequestProperty("Accept-Charset", "UTF-8");
				conn.setRequestMethod("GET");
				String basicAuth = environ.getProperty("wic.web.service.auth.header.value.basic");
				conn.setRequestProperty ("Authorization", basicAuth);
				
				InputStream in = conn.getInputStream();
				FileOutputStream out = new FileOutputStream(environ.getProperty("wic.web.service.download.attachment.path"));
				int c;
				byte[] b = new byte[1024];

				while ((c = in.read(b)) != -1){
					out.write(b, 0, c);
				}
				
				in.close();
				out.close();
			} catch (Exception e) {
				LOG.info("Error downloading attachment. . .");
				LOG.error(e);
				bool = false;
			}

			return bool;
			
		
	}

	@Override
	public String getSysIdFromAttachmentUrl(String attachmentUrl) {
		String outputUrl = "", sysId ="";
		sysId = attachmentUrl.substring(attachmentUrl.indexOf("sys_id=") + 7);
		outputUrl = environ.getProperty("wic.web.service.get.attachment.by.sysid").replace("{sys_id}", sysId).replace(",","");
		return outputUrl;
	
	}

	@Override
	public ResponseGetTaskListBO callGetTaskList() {
		ResponseGetTaskListBO taskListBO = null;
		AutomationUtil util = new AutomationUtil();

		for (int i = 0; i < Integer.parseInt(environ.getProperty("wic.retry")); i++) {
			try {
				taskListBO = getTaskLists("{\"short_description\":\"" + 
					environ.getProperty("wic.snow.task.description") + "\"}");
				break;
			} catch (Exception e) {
				LOG.info("API call failed for getTaskList. Retry " + i + 1);
				delay(Integer.parseInt(environ.getProperty("delay.small")));
				if (i == Integer.parseInt(environ.getProperty("wic.retry")) - 1) {
					LOG.error("Failed API call for getTaskList");
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.wic.monitoring.from"), 
							environ.getProperty("mail.wic.monitoring.from.alias"), 
							environ.getProperty("mail.wic.monitoring.recipients", String[].class), 
							environ.getProperty("mail.wic.monitoring.cc", String[].class), 
							"Failed to get Task List - " 
							+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),""),
							"Error in getting task list. . .", NORMAL_PRIORITY, false);
					} catch (ArgusMailException emailException) {
						LOG.error(emailException);
						LOG.error("Failed to send email. . .");
					}
				}
			}
		}

		return taskListBO;
	}

	@Override
	public ResponseUpdateTaskBO callUpdateTask(String responseBody, String taskNumber) {
		ResponseUpdateTaskBO updateTaskBO = null;
		AutomationUtil util = new AutomationUtil();

		for (int i = 0; i < Integer.parseInt(environ.getProperty("wic.retry")); i++) {
			try {
				updateTaskBO = updateTask(responseBody);
				break;
			} catch (Exception e) {
				LOG.info("API call failed for updateTask. Retry " + i + 1);
				if (i == Integer.parseInt(environ.getProperty("wic.retry")) - 1) {
					LOG.error("Failed API call for UpdateTask. . .");
					delay(Integer.parseInt(environ.getProperty("delay.small")));
					try {
						emailService.sendSimpleMessage(environ.getProperty("mail.wic.monitoring.from"), 
							environ.getProperty("mail.wic.monitoring.from.alias"), 
							environ.getProperty("mail.wic.monitoring.recipients", String[].class), 
							environ.getProperty("mail.wic.monitoring.cc", String[].class), 
							"Failed to update task - " + taskNumber + " - "
							+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),""),
							"Error in updating task. . .", NORMAL_PRIORITY, false);
					} catch (Exception emailException) {
						LOG.error(emailException);
						LOG.error("Failed to send email. . .");
					}
					
				}
				
			}
		}
		return updateTaskBO;
	}
}