package com.albertsons.argus.rundb.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.rundb.service.DBRevocationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DBRevocationServiceImpl implements DBRevocationService {

    private static final Logger LOG = LogManager.getLogger(DBRevocationServiceImpl.class);
    
    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    private List<String> usersList;

    private int partialUserCount;

    private String timestamp;
    
    @Async
    public CompletableFuture<Boolean> runShCmd(String user, String password, String host, String command, String options) throws Exception {
        LOG.log(Level.DEBUG, () -> "start runShCmd method. . .");

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        Session session;

        session = jsch.getSession(user, host, 22);
        session.setPassword(password);
        session.setConfig(config);
        session.connect();

        LOG.log(Level.DEBUG, () -> "Connected to " + host);

        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        ((ChannelExec) channel).setPty(true);
        ((ChannelExec) channel).setErrStream(System.err);
        channel.setInputStream(null);

        InputStream in = channel.getInputStream();
        OutputStream out = channel.getOutputStream();
        channel.connect();

        String outStr;
        int commandCtr = 0;
        
        String[] arrCommands = {environment.getProperty("encrypted.db.revocation.property.informix.password"), "m", "9", "cd security", "perms.ksh -r", "3", options, "userToDelete", "Y", "0", "exit"};
        String[] arrKeywordChecks = {"informix's Password", "menu", "9)", "/home/informix", "/home/informix/security", "DBA Database Authorization", "DBA Database Authorization - EXE", "enter user id(s)", "(Y/N)", "Quit", "/home/informix/security"};

        List<String> commands = Arrays.asList(arrCommands);  
        List<String> keywordChecks = Arrays.asList(arrKeywordChecks);

        commands = new ArrayList<String>(commands);
        keywordChecks = new ArrayList<String>(keywordChecks);

        if (options.equalsIgnoreCase(environment.getProperty("common.db.revocation.other.options"))){ // If OTHER, skip "3" (EXE)
            commands.remove("3");
            keywordChecks.remove("DBA Database Authorization - EXE");
        }

        int userCtr = 0;

        byte[] tmp = new byte[1024];

        try {
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    outStr = new String(tmp,0,i);

                    System.out.print(outStr);

                    if (outStr.contains(keywordChecks.get(commandCtr))){
                        if (keywordChecks.get(commandCtr).equalsIgnoreCase("enter user id(s)") && userCtr < getPartialUserCount()){
                            out.write((usersList.get(userCtr) + "\n").getBytes());
                            out.flush();
                            userCtr++;
                            commandCtr++;
                        }
                        else if (keywordChecks.get(commandCtr).equalsIgnoreCase("(Y/N)") && userCtr < getPartialUserCount()){
                            out.write((commands.get(commandCtr) + "\n").getBytes());
                            out.flush();

                            if (options == environment.getProperty("common.db.revocation.other.options")){ // If OTHER option
                                commandCtr = commandCtr - 2; //go back 2 commands if not the last user to delete
                            }
                            else{
                                commandCtr = commandCtr - 3; //go back 3 commands if not the last user to delete
                            }
                        }
                        else{
                            out.write((commands.get(commandCtr) + "\n").getBytes());
                            out.flush();

                            if (commands.get(commandCtr).equalsIgnoreCase("exit")){
                                break;
                            }

                            commandCtr++;
                        }
                    }
                } 

                if (channel.isClosed()) {
                    if (channel.getExitStatus() != 0) {
                        LOG.log(Level.DEBUG, () -> "Exit status: " + channel.getExitStatus());
                    }    
                    break;
                }
            }

            session.disconnect();
            LOG.log(Level.DEBUG, () -> "end runShCmd method. . .");
            return CompletableFuture.completedFuture(true); // end of async task

        } catch (Exception e){
            session.disconnect();
            LOG.log(Level.INFO, () -> "error in runSchmd. . .");
            LOG.error(e);
            return CompletableFuture.completedFuture(false); // end of async task
        }

    }
    
    @Override
    public void sendDBRevocationEmailSuccess(String startTime, String endTime) {
        LOG.log(Level.DEBUG, () -> "start sendDBRevocationEmailSuccess method. . .");

        String body = "<body>Hello, <br><br>" +
                        "Bot execution successful for the following users: <br>";
        
        for (int i = 0; i < getPartialUserCount(); i++){
            body += getUsersList().get(i) + " ";
        }

        body += "<br><br> Start Time: " + startTime + " <br>" +
                "End Time: " + endTime + " <br><br>" + 
                "Thanks & Regards, <br>" + 
                "AA Bot</body>";

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.db.revocation.from"),
                    environment.getProperty("mail.db.revocation.from.alias"),
                    environment.getProperty("mail.db.revocation.recipients", String[].class),
                    environment.getProperty("mail.db.revocation.cc", String[].class),
                    environment.getProperty("mail.db.revocation.subject.success") + " - "
                            + getTimestamp(),
                    body, NORMAL_PRIORITY, true);

        } catch (ArgusMailException e) {
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end sendDBRevocationEmailSuccess method. . .");
    }

    @Override
    public void sendDBRevocationEmailFailed(String startTime, String endTime) {
        LOG.log(Level.DEBUG, () -> "start sendDBRevocationEmailFailed method. . .");

        String body = "<body>Hello, <br><br>" +
                        "Bot execution failed for the following users: <br>";
        
        for (int i = 0; i < getPartialUserCount(); i++){
            body += getUsersList().get(i) + " ";
        }

        body += "<br><br> Please check logs for more details. <br><br>" +
                "Start Time: " + startTime + " <br>" +
                "End Time: " + endTime + " <br><br>" + 
                "Thanks & Regards, <br>" + 
                "AA Bot</body>";

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.db.revocation.from"),
                    environment.getProperty("mail.db.revocation.from.alias"),
                    environment.getProperty("mail.db.revocation.recipients", String[].class),
                    environment.getProperty("mail.db.revocation.cc", String[].class),
                    environment.getProperty("mail.db.revocation.subject.failed") + " - "
                            + getTimestamp(),
                    body, NORMAL_PRIORITY, true);

        } catch (ArgusMailException e) {
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end sendDBRevocationEmailFailed method. . .");
    }

    @Override
    public void sendDBRevocationEmailStart(String startTime) {
        LOG.log(Level.DEBUG, () -> "start sendDBRevocationEmailStart method. . .");

        AutomationUtil util = new AutomationUtil();

        String body = "<body>Hello, <br><br>" +
                        "Bot execution started for the following users: <br>";
        
        for (int i = 0; i < getPartialUserCount(); i++){
            body += getUsersList().get(i) + " ";
        }

        body += "<br><br> Start Time: " + startTime + " <br><br>" +
                "Thanks & Regards, <br>" + 
                "AA Bot</body>";

        String timestamp = util.toDateString(new Date(), environment.getProperty("domain.util.date.format"),"US/Arizona");//TODO: must trasfer to property files
        setTimestamp(timestamp);

        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.db.revocation.from"),
                    environment.getProperty("mail.db.revocation.from.alias"),
                    environment.getProperty("mail.db.revocation.recipients", String[].class),
                    environment.getProperty("mail.db.revocation.cc", String[].class),
                    environment.getProperty("mail.db.revocation.subject.start") + " - "
                            + getTimestamp(),
                    body, NORMAL_PRIORITY, true);

        } catch (ArgusMailException e) {
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end sendDBRevocationEmailStart method. . .");
    }

    @Override
    public void updateUserList(){
        LOG.log(Level.DEBUG, () -> "start updateUserList method. . .");
        
        Scanner sc = null;
        PrintWriter writer = null;

        try {
            String input = null;
            sc = new Scanner(new File(environment.getProperty("common.db.revocation.in.folder") + environment.getProperty("common.db.revocation.list.txt")));
        
            StringBuffer sb = new StringBuffer();
            while (sc.hasNextLine()) {
                input = sc.nextLine();
                sb.append(input + "\n");
            }

            String result = sb.toString();

            //remove the users successfully revoked
            for (int i = 0; i < getPartialUserCount(); i++){
                result = result.replaceAll(getUsersList().get(0) + "\n", ""); // remove from txt file
                getUsersList().remove(0);
            }

            //Rewriting txt file with remaining users
            writer = new PrintWriter(new File(environment.getProperty("common.db.revocation.in.folder") + environment.getProperty("common.db.revocation.list.txt")));
            writer.append(result);
            writer.flush();

        } catch (Exception e) {
            LOG.error(e);
        } finally {
            writer.close();
            sc.close();
        }

        LOG.log(Level.DEBUG, () -> "end updateUserList method. . .");
    }

    @Override
    public void deleteFile(boolean deleteExcelFile, boolean deleteTxtFile){
        File excelFile = new File(environment.getProperty("common.db.revocation.in.folder") + "\\" + environment.getProperty("common.db.revocation.attachment.file")); 
        File txtFile = new File(environment.getProperty("common.db.revocation.in.folder") + "\\" + environment.getProperty("common.db.revocation.list.txt")); 

        if (deleteExcelFile == true && excelFile.exists()){
            excelFile.delete();
        }

        if (deleteTxtFile == true && txtFile.exists()){
            if (txtFile.delete() == true){
                LOG.log(Level.DEBUG, () -> "Successfully deleted txt file");
            }
            else{
                LOG.log(Level.DEBUG, () -> "Failed to delete txt file");
            }
        }
    }

    @Override
    public void moveLogFile(){
        LOG.log(Level.DEBUG, () -> "start moveLogFile method. . .");

        try {
            Process vbscript = Runtime.getRuntime().exec("wscript " + environment.getProperty("automations.folder") + environment.getProperty("move.log.vbscript") + " " + environment.getProperty("automations.rundb.folder") + " " + " " + environment.getProperty("log.file.name") + " " + environment.getProperty("log.file.name.extension") + " " + environment.getProperty("logging.folder"));
            vbscript.waitFor();

        } catch (Exception e){
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end moveLogFile method. . .");
    }

    @Override
    public boolean getUsersFromFile(){
        LOG.log(Level.DEBUG, () -> "start getUsersFromFile method. . .");

        usersList = new ArrayList<String>();
        Scanner sc = null;

        try {
            sc = new Scanner(new File(environment.getProperty("common.db.revocation.in.folder") + environment.getProperty("common.db.revocation.list.txt")));
        
            while (sc.hasNextLine()) {
                setUsersList(sc.nextLine());
            }

            sc.close();

            if (getUsersList().size() > 0){
                return true;
            }
            else {
                deleteFile(false, true);
                return false;
            }

            
        } catch (Exception e) {
            LOG.log(Level.DEBUG, () -> "Error retrieving " + environment.getProperty("common.db.revocation.list.txt"));
            LOG.error(e);
        } finally {
            sc.close();
        }

        return false;
    }

    @Override
    public boolean openOutlook(){
        LOG.log(Level.DEBUG, () -> "start openOutlook method. . .");

        PrintWriter pw = null;
        
        try{
            Process vbscript = Runtime.getRuntime().exec("wscript " + environment.getProperty("common.db.revocation.in.folder") + environment.getProperty("common.db.revocation.vbscript") + " " + environment.getProperty("mail.db.revocation.outlook.vbs.subject"));
            vbscript.waitFor(20, TimeUnit.SECONDS);
            
            File myFile = new File(environment.getProperty("common.db.revocation.in.folder") + "\\" + environment.getProperty("common.db.revocation.attachment.file")); 
            FileInputStream fis = new FileInputStream(myFile);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            pw = new PrintWriter(environment.getProperty("common.db.revocation.in.folder") + environment.getProperty("common.db.revocation.list.txt"));

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getColumnIndex() == 1 && !cell.getStringCellValue().equalsIgnoreCase("User ID") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){ // 2nd column is the list of users 
                        pw.println(cell.getStringCellValue()); // write to txt file
                    }
                }
            }

            wb.close();
            pw.close();

            return true;
        } 
        catch (Exception e){
            LOG.log(Level.DEBUG, () -> "Error retrieving file from Outlook");
            LOG.error(e);
        } finally {
            pw.close();
        }
        
        return false;
    }

    @Override
    public boolean validateRun(){
        File excelFile = new File(environment.getProperty("common.db.revocation.in.folder") + "\\" + environment.getProperty("common.db.revocation.attachment.file")); 
        File txtFile = new File(environment.getProperty("common.db.revocation.in.folder") + "\\" + environment.getProperty("common.db.revocation.list.txt")); 

        if (excelFile.exists()){ // do not run script
            return false; 
        }
        else{
            if (txtFile.exists()){ // continue users to delete 
                if (getUsersFromFile() == true){
                    return true; 
                }

                return false; 
            }
            else{ // new set of users to delete
                if (openOutlook() == true){
                    if (getUsersFromFile() == true){
                        return true; 
                    }

                    return false;
                }

                return false;
            }
        }

    }

    @Override
    public void setUsersList(String user){
        usersList.add(user);
    }

    @Override
    public List<String> getUsersList(){
        return usersList;
    }

    @Override
    public void setPartialUserCount(int partialUserCount){
        this.partialUserCount = partialUserCount;
    }

    @Override
    public int getPartialUserCount(){
        return partialUserCount;
    }

    @Override
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    @Override
    public String getTimestamp(){
        return timestamp;
    }

}