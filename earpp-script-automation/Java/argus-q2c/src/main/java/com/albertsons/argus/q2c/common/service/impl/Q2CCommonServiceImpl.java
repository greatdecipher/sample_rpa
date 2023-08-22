package com.albertsons.argus.q2c.common.service.impl;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.q2c.common.service.Q2CCommonService;
import com.albertsons.argus.q2c.exception.Q2CCommonException;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;

@Service
public class Q2CCommonServiceImpl implements Q2CCommonService{
    private static final Logger LOG = LogManager.getLogger(Q2CCommonServiceImpl.class);

    @Autowired
    Environment environment;

    @Autowired
    EmailService emailService;

    @Autowired
    private PlaywrightAutomationService playwrightService;

    @Override
	public boolean checkIfFileIsRunning(String folder, String filename) throws Exception {
		LOG.log(Level.DEBUG, () -> "start checkIfFileIsRunning method. . .");
		
		try {
			File folderPath = new File(folder);
		
			if (folderPath.isDirectory()) {
				for (File file: folderPath.listFiles()) {
					if (file.getName().contains(filename) || file.getName().equalsIgnoreCase(filename)){
						return true;
					}
				}
			}

			return false;

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "error checking if file is currently running. . .");
			throw new Exception(e.getMessage());
		}
	}
    
    @Override
    public void sendEmail(String[] recipients, String[] cc, String subject, String body, String execTimestamp){
        LOG.log(Level.DEBUG, () -> "start sendEmail method. . .");

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"), recipients, cc,
                    subject + " - " + execTimestamp,
                    body, NORMAL_PRIORITY,true);

        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending email. . .");
            LOG.error(e);
        }
		
		LOG.log(Level.DEBUG, () -> "end sendEmail method. . .");
    }

    @Override
    public void delayTimer(int setTimer) throws Q2CCommonException {
        try {
            LOG.log(Level.INFO, () -> "delayTimer will commence. . .");
            TimeUnit.SECONDS.sleep(setTimer);
        } catch (InterruptedException e) {
            LOG.error(e);
            throw new Q2CCommonException("There is something wrong on delayTimer method. " + e.getMessage());
        }
        
    }

    public String getAttachmentContent(Page page, String filename, String textToLookFor, String altDocId, String execTimestamp) throws Q2CCommonException{
        LOG.log(Level.DEBUG, () -> "start getAttachmentContent method. . .");

        try {                           
            playwrightService.pageClick(page, "", "", "span:has-text(\"" + filename + "\")");
            TimeUnit.SECONDS.sleep(10);

            Download download = null;

            if (textToLookFor.contains("waiting on child process with request id") || textToLookFor.contains("ImportExternalTransactions with request ID")){
                playwrightService.pageLocatorWait(page, "span", "id", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:processDetails:processDetails:attachment1:pglMore'").click();

                download = page.waitForDownload(() -> {
                    page.click("text=" + filename + ".log");
                });
            }
            
            else if (textToLookFor.contains("successfully loaded")||textToLookFor.contains("rows were successfully imported")){            
                download = page.waitForDownload(() -> {
                    page.click("text=ESS_L_" + filename);
                });
            }
            
            download.saveAs(Paths.get(environment.getProperty("q2c.downloads.folder") + filename + ".log"));
            TimeUnit.SECONDS.sleep(5);
            return readAttachment(page, environment.getProperty("q2c.downloads.folder") + filename + ".log", filename, textToLookFor, altDocId, execTimestamp);
            
        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in getAttachmentContent method. . .");
            LOG.error(e);
            return null;
        }

    }

    private String readAttachment(Page page, String filePath, String filename, String textToLookFor, String altDocId, String execTimestamp){
        LOG.log(Level.DEBUG, () -> "start readAttachment method. . .");

        AutomationUtil util = new AutomationUtil();

        try {
            String result = "";

            if (util.fileExists(filePath) == true){
                Scanner sc = new Scanner(new File(filePath));

                int headerFlag = 0;

                while (sc.hasNextLine()) {
                    String text = sc.nextLine();

                    //flag indicating that the child process request id is the correct one (because there are multiple in the file)
                    if (text.toLowerCase().contains("cebankstmtlineinterface.ctl")){
                        headerFlag = 1;
                    }    
    
                    if (text.toLowerCase().contains(textToLookFor.toLowerCase())){

                        if (textToLookFor.contains("waiting on child process with request id") && headerFlag == 1){ // This is for getting Child Process ID

                            List<Integer> numbers = new ArrayList<Integer>();
    
                            Pattern p = Pattern.compile("\\d+");
                            Matcher m = p.matcher(text);
    
                            while(m.find()){
                                numbers.add(Integer.parseInt(m.group()));
                            }
    
                            for (Integer number : numbers){
                                result += number; 
                            }
    
                            sc.close();
                            util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                            return result;

                        }
                        else if (textToLookFor.contains("successfully loaded")){ // This is for getting number of rows successfully loaded
                            String[] results = text.split(" ");

                            for (String textResult : results){
                                if (StringUtils.isNotBlank(textResult)){
                                    List<Integer> numbers = new ArrayList<Integer>();
    
                                    Pattern p = Pattern.compile("\\d+");
                                    Matcher m = p.matcher(textResult);
            
                                    while(m.find()){
                                        numbers.add(Integer.parseInt(m.group()));
                                    }
            
                                    for (Integer number : numbers){
                                        result += number; 
                                    }
                                    
                                    sc.close();
                                    util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                                    return result;

                                }
                            }
                            
                            if (results.length > 0){
                                result = results[0]; // get the number 
                                sc.close();
                                util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                                return result;
                            }
                        }
                        if (textToLookFor.contains("ImportExternalTransactions with request ID")){ // This is for getting Child Process ID

                            List<Integer> numbers = new ArrayList<Integer>();
    
                            Pattern p = Pattern.compile("\\d+");
                            Matcher m = p.matcher(text);
    
                            while(m.find()){
                                numbers.add(Integer.parseInt(m.group()));
                            }
    
                            for (Integer number : numbers){
                                result += number; 
                            }
    
                            sc.close();
                            util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                            return result;

                        }

                        else if (textToLookFor.contains("rows were successfully imported")){ // This is for getting number of rows successfully loaded
                            String[] results = text.split(" ");

                            for (String textResult : results){
                                if (StringUtils.isNotBlank(textResult)){
                                    List<Integer> numbers = new ArrayList<Integer>();
    
                                    Pattern p = Pattern.compile("\\d+");
                                    Matcher m = p.matcher(textResult);
            
                                    while(m.find()){
                                        numbers.add(Integer.parseInt(m.group()));
                                    }
            
                                    for (Integer number : numbers){
                                        result += number; 
                                    }
                                    
                                    sc.close();
                                    util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                                    return result;

                                }
                            }
                            
                            if (results.length > 0){
                                result = results[0]; // get the number 
                                sc.close();
                                util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                                return result;
                            }
                        }
    
                    }
                }
    
                sc.close();
                util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                return result;

            }
            else{
                String body = "<body>Hello, <br><br>";
                String subject = environment.getProperty("inbound.bank.paids.download.failure.subject");

                if (altDocId.contains("EAS_CB_MO_")){
                    subject = subject.replace("Bank Paids", "EAS CB MO");
                }

                body += "There was an issue downloading " + filePath.substring(filePath.lastIndexOf('\\') + 1) + ".log.";

                body += "<br><br>" +
                        "Thanks & Regards, <br>" + 
                        "Bot</body>";

                sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                            environment.getProperty("mail.argus.cc", String[].class), 
                            subject, body, execTimestamp);

                util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
                return null;
            }

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in readAttachment method. . .");
            LOG.error(e);
            util.deleteFile(environment.getProperty("q2c.downloads.folder"), 0, filename + ".log", false);
            return null;
        } 

    }
}
