package com.albertsons.argus.wic.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.wic.exception.ArgusWicException;
import com.albertsons.argus.wic.service.WicAutomationService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;

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
	
	@Override
	public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib)
			throws ArgusWicException {
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");

		try {
			if (mainframe != null) {
				for (Frame child : mainframe.childFrames()) {
					child.waitForLoadState(LoadState.LOAD);
					if (child.name().contains(framename)) {
						if (child.content().contains(selector)) {
							child.waitForSelector(attrib + "=" + selector);
							child.click(attrib + "=" + selector);
							break;
						}
					} else {
						clickElementInFrame(child, selector, framename, attrib);
					}
				}
			}
		} catch (PlaywrightException pw) {
			throw new ArgusWicException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	}

	@Override
	public void waitForLoadingFrame(Page MainPage, String WaitElem, String FrameName, long WaitLimit)
			throws ArgusWicException {
		LOG.log(Level.DEBUG, () -> "Start waitForLoadingFrame method . . .");

		long lWaitCounter = 0;
		try {
			Frame frm = MainPage.frame(FrameName);

			while (frm.content().contains(WaitElem)) {
				frm = MainPage.frame(FrameName);
				delay(1);
				lWaitCounter += 1;
				if (lWaitCounter > WaitLimit) {
					LOG.log(Level.DEBUG, () -> "waitLoadMainPage Wait Limit Exceeded. . .");
					throw new ArgusWicException("Element: text=" + WaitElem);
				}
			}

		} catch (PlaywrightException pw) {
			throw new ArgusWicException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End waitForLoadingFrame method . . .");
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
	public String padRight(String sInStr, int iLength) {
		LOG.log(Level.DEBUG, () -> "Run padRight method . . .");

		return String.format("%1$-" + iLength + "s", sInStr).substring(0, iLength);
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
	public long getFTimestamp(String sFile) {
		LOG.log(Level.DEBUG, () -> "Run getFTimestamp method . . .");

		File FileName = new File(sFile);
		return FileName.lastModified();
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
	public void deleteFilesByAge(String sFolder, Integer iMaxAge) {
		LOG.log(Level.DEBUG, () -> "Start deleteFilesByAge method . . .");

		File FolderPath = new File(sFolder);
		Path path;
		LocalDateTime ldt;
		BasicFileAttributes bfa;

		long iDateDiff;

		if (FolderPath.isDirectory()) {
			try {
				for (File fName : FolderPath.listFiles()) {
					path = Paths.get(fName.getPath());
					bfa = Files.readAttributes(path, BasicFileAttributes.class);
					ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(bfa.creationTime().toMillis()),
							TimeZone.getDefault().toZoneId());
					iDateDiff = ChronoUnit.DAYS.between(ldt, LocalDateTime.now());
					if (iDateDiff > iMaxAge) {
						fName.delete();
					}
				}
			} catch (Exception e) {
				LOG.log(Level.DEBUG, () -> "deleteFilesByAge method failed . . .");
			}

		}

		LOG.log(Level.DEBUG, () -> "End deleteFilesByAge method . . .");
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
	public void runShCmd(String user, String password, String host, String emailSubject) throws IOException, JSchException, SftpException, ArgusMailException {
		AutomationUtil util = new AutomationUtil();

		String sRIT = emailSubject.substring(emailSubject.toUpperCase().indexOf("RITM"));
		
		String formattedStores = "", cmdOutput;
		List <String> multiCmdParam = new ArrayList<>();
		String startTime;
		String endTime;
		long  attFileSize = 0;

		startTime = DateTimeFormatter.ofPattern("yyyyMMdd.HH:mm:ss").format(LocalDateTime.now());

		//-------------------Connect to host and open SFTP Channel---------------------//
		openSession();

		LOG.info("Opening sftp channel");
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp sftp = (ChannelSftp) channel;
		sftp.cd(environ.getProperty("jsch.wic.server.directory"));
	
		formattedStores = formatStoreNumbers();
		LOG.info("Store List: " + formattedStores);

		cmdOutput = runCmd("[ -d " + environ.getProperty("jsch.wic.server.directory") + sRIT + " ] && echo $?");
		LOG.info(cmdOutput);

		if (cmdOutput.contains("0")) {
			LOG.info("Existing directory found for " + sRIT + ", Terminating session. . ." );
			deleteWicFilesInFolder(environ.getProperty("wic.outlook.attachment.save.path"));
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
					file.renameTo(renameFile);
					break;
				}
			}
		}
		LOG.info("Attachment file size: " + attFileSize + " bytes");

		//-----------------------SFTP put .DAT file in server------------------------//
		sftp.put(renameFile.getAbsolutePath(), renameFile.getName());
		LOG.info("SFTP put " + renameFile.getName() + " in server");
		sftp.disconnect();
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
		sendValidationResultEmail(cmdOutput, sRIT);

		LOG.info("WIC Mapping Process Completed . . .");
		
		endTime = DateTimeFormatter.ofPattern("yyyyMMdd.HH:mm:ss").format(LocalDateTime.now());
		LOG.info("Start: " + startTime + "\nEnd  : " + endTime);
	}

	@Override
	public void sendValidationResultEmail(String resultString, String ritNo) throws ArgusMailException{
		AutomationUtil util = new AutomationUtil();
		try {
			if (resultString.toUpperCase().contains("FAIL") | resultString.toUpperCase().contains("ERROR") | 
				resultString.toUpperCase().contains("SEND.SH VALIDATION"))  {
				emailService.sendSimpleMessage(environ.getProperty("mail.wic.monitoring.from"), 
						environ.getProperty("mail.wic.monitoring.from.alias"), 
						environ.getProperty("mail.wic.monitoring.recipients", String[].class), 
						environ.getProperty("mail.wic.monitoring.cc", String[].class), 
						environ.getProperty("mail.wic.monitoring.subject").replace("RITNo", ritNo) + " FAILED - " 
						+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"MST"),
						resultString, NORMAL_PRIORITY, false);
			} else {
				emailService.sendSimpleMessage(environ.getProperty("mail.wic.monitoring.from"), 
						environ.getProperty("mail.wic.monitoring.from.alias"), 
						environ.getProperty("mail.wic.monitoring.recipients", String[].class), 
						environ.getProperty("mail.wic.monitoring.cc", String[].class), 
						environ.getProperty("mail.wic.monitoring.subject").replace("RITNo", ritNo) + " SUCCESS - " 
						+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"MST"),
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
	public String formatStoreNumbers(){
		String formattedStores = "";
		
		try {
			File emailBodyFile = new File(environ.getProperty("wic.outlook.mail.body.file.path"));
			Scanner scanner;
			scanner = new Scanner(emailBodyFile);
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
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return formattedStores;
	}

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

    public void openSession() throws JSchException {
        JSch jSch = new JSch();
        session = jSch.getSession(environ.getProperty("encrypted.wic.property.username"), environ.getProperty("jsch.wic.host.name"), 22);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(environ.getProperty("encrypted.wic.property.password"));
        LOG.info("Connecting SSH to " + environ.getProperty("jsch.wic.host.name") + " - Please wait for few seconds... ");
        session.connect();
        LOG.info("Connected!");
    }

    public void closeSession() {
        session.disconnect();
        LOG.info("Disconnected channel and session");
    }

}


