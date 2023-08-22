package com.albertsons.argus.asn.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.asn.service.AsnService;
import com.albertsons.argus.mail.service.EmailService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class AsnServiceImpl implements AsnService {
    private static final Logger LOG = LogManager.getLogger(AsnServiceImpl.class);
    
    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Override
    public String createFile(String text, String datetime) {
        String[] dateDetails = datetime.split(" ");
        String date = dateDetails[0];
        String time = dateDetails[1];
        String[] timeDetails = time.split(":");
        String hour = timeDetails[0];
        String fileSuffix = "0";
        LOG.info("Creating File . . .");
        if(Integer.parseInt(hour) >= 0 && Integer.parseInt(hour) < 4) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate prevDay = LocalDate.parse(date, dtf).minusDays(1);
            fileSuffix = ".6.txt";
            date = prevDay.toString();
        } else if(Integer.parseInt(hour) >= 4 && Integer.parseInt(hour) < 8) {
            fileSuffix = ".1.txt";
        } else if(Integer.parseInt(hour) >= 8 && Integer.parseInt(hour) < 12) {
            fileSuffix = ".2.txt";
        } else if(Integer.parseInt(hour) >= 12 && Integer.parseInt(hour) < 16) {
            fileSuffix = ".3.txt";
        } else if(Integer.parseInt(hour) >= 16 && Integer.parseInt(hour) < 20) {
            fileSuffix = ".4.txt";
        } else if(Integer.parseInt(hour) >= 20 && Integer.parseInt(hour) < 24) {
            fileSuffix = ".5.txt";
        }

        //String filename = date.replace("-","") + fileSuffix;
        String filename = "test_data1" + fileSuffix;
        try {
            FileWriter fileWriter = new FileWriter(environment.getProperty("asn.file.folder") + "\\" + filename);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            printWriter.print(text);
            printWriter.close();
        } catch (IOException e) {
            LOG.error(e);
        }
        LOG.info("File Created . . .");
        return filename;
    }

    @Override
    public void saveFileToServer(String filename) {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        Session session;
        try {
            LOG.info("Connecting to Server . . .");
            //CREATE SESSION
            session = jsch.getSession(environment.getProperty("asn.ssh.login.user"), environment.getProperty("asn.ssh.host.name"), 22);
            session.setPassword(environment.getProperty("asn.ssh.login.pass"));
            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            ChannelSftp channelSftp = (ChannelSftp) channel;
            String source = (environment.getProperty("asn.file.folder") + "\\" + filename);
            channelSftp.connect();
            channelSftp.put(source, environment.getProperty("asn.ssh.server.temp"));

            channelSftp.exit();
            LOG.info("Saving File to Server . . .");
            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public Boolean getEmails(String datetime){
        LOG.log(Level.DEBUG, () -> "start openOutlook method. . .");
        LOG.info("Getting FAR Missing ASN Emails . . .");
        String[] dateDetails = datetime.split(" ");
        String date = dateDetails[0];
        String time = dateDetails[1];
        String[] timeDetails = time.split(":");
        String hour = timeDetails[0];
        String hr = "00";

        if(Integer.parseInt(hour) >= 0 && Integer.parseInt(hour) < 4) {
            hr = "20";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate prevDay = LocalDate.parse(date, dtf).minusDays(1);
            date = prevDay.toString();
        } else if(Integer.parseInt(hour) >= 4 && Integer.parseInt(hour) < 8) {
            hr = "00";
        } else if(Integer.parseInt(hour) >= 8 && Integer.parseInt(hour) < 12) {
            hr = "04";
        } else if(Integer.parseInt(hour) >= 12 && Integer.parseInt(hour) < 16) {
            hr = "08";
        } else if(Integer.parseInt(hour) >= 16 && Integer.parseInt(hour) < 20) {
            hr = "12";
        } else if(Integer.parseInt(hour) >= 20 && Integer.parseInt(hour) < 24) {
            hr = "16";
        }

        String vbsParam = environment.getProperty("asn.outlook.vbs.parameter1").replace("(SUBJECT)", environment.getProperty("asn.outlook.vbs.subject") + date + "_" + hr);

        try{
            Process vbscript = Runtime.getRuntime().exec("wscript " + environment.getProperty("asn.file.folder") + environment.getProperty("asn.file.vb.script") + " " + vbsParam);
            vbscript.waitFor(20, TimeUnit.SECONDS);

            InputStream input = vbscript.getInputStream();
            String output = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            LOG.info(output);
            return Boolean.parseBoolean(output);
        }   
        catch (Exception e){
            LOG.log(Level.DEBUG, () -> "Error retrieving file from Outlook");
            LOG.error(e);
            return Boolean.FALSE;
        }
    }

    @Override
    public String mergeEmailMessage() {
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i < 20; i++){
            try {
                String filePath = environment.getProperty("asn.file.email.folder") + environment.getProperty("asn.file.email.name")
                    + (i+1) + environment.getProperty("asn.file.email.ext");
                File file = new File(filePath);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                file.delete();
                br.close();
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String getServerDate() {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        Session session;

        try {

            //CREATE SESSION
            session = jsch.getSession(environment.getProperty("asn.ssh.login.user"), environment.getProperty("asn.ssh.host.name"), 22);
            session.setPassword(environment.getProperty("asn.ssh.login.pass"));
            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("date +\"%Y-%m-%d %T\"");
            ((ChannelExec) channel).setPty(true);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.setInputStream(null);

            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] tmp = new byte[1024];
            StringBuilder sb = new StringBuilder();

            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String outputDate = new String(tmp,0,i);

                    sb.append(outputDate);
                }

                if (channel.isClosed()) {
                    if (channel.getExitStatus() != 0) {
                        LOG.log(Level.DEBUG, () -> "Exit status: " + channel.getExitStatus());
                    }    
                    break;
                }
            }
            channel.disconnect();
            session.disconnect();
            return sb.toString();

        } catch (Exception e) {
            LOG.error(e);
        }
        return "";
    }

    @Override
    public void sendMail(String filename) {
        emailService.sendMessageWithAttachment("RPA.CoE@safeway.com", "RPA.CoE@safeway.com",
        environment.getProperty("asn.email.to", String[].class), 
        environment.getProperty("asn.email.cc", String[].class), "FAR Missing ASN Automation - Success", "FAR Missing ASN process is complete, please see attached file for reference.", 
        environment.getProperty("asn.file.folder") + "//" + filename , 3, true);

        LOG.info("Email Sent . . .");
        LOG.info("FAR Missing ASN Process Finished . . .");
    }

    @Override
    public void sudoTransferFile(String filename) {
        
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        Session session;        
	    try {
            session = jsch.getSession(environment.getProperty("asn.ssh.login.user"), environment.getProperty("asn.ssh.host.name"), 22);
            session.setPassword(environment.getProperty("asn.ssh.login.pass"));
            session.setConfig(config);
            session.connect();

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            
            ((ChannelExec) channelExec).setCommand(environment.getProperty("jsch.asn.sudo"));
            ((ChannelExec) channelExec).setErrStream(System.err);
            
            channelExec.connect(5000);
            channelExec.setInputStream(null);
            List<String> cmdMulti = new ArrayList<>();
            cmdMulti.add("scp /tmp/" + filename + " " + environment.getProperty("asn.ssh.server.path"));

            for (String cmd : cmdMulti) {
                LOG.info("Executing command: " + cmd);
                
                OutputStream out = channelExec.getOutputStream();
                out.write((cmd + "\n").getBytes());
                out.flush();

                
		    }
            LOG.info("Command Executed!");
            channelExec.disconnect();
            session.disconnect();

        } catch (Exception e) {
            LOG.error(e);
        }
    }

    @Override
    public void deleteFile(String filename) {
        File file = new File(environment.getProperty("asn.file.folder") + "\\" + filename);
        file.delete();
    }

}
