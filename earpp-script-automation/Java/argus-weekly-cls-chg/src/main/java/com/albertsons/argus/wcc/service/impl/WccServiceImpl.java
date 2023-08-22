package com.albertsons.argus.wcc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.albertsons.argus.wcc.dto.EmailDetails;
import com.albertsons.argus.wcc.service.WccService;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;


@Service
public class WccServiceImpl implements WccService{
    private static final Logger LOG = LogManager.getLogger(WccServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MetricsWebService metricsWebService;

    String automationRunId;

    @Override
    public void readEmail() {
        LOG.log(Level.DEBUG, () -> "start readEmail method. . .");
        Process vbscript;
        try {
            vbscript = Runtime.getRuntime().exec("wscript " + environment.getProperty("path.to.script")+" "+ environment.getProperty("outlook.email.values"));
            vbscript.waitFor(20, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.log(Level.INFO, () -> "Error at reading email: ");
            LOG.error(e);
        }
    
    }

    @Override
    public void extractRaw(){
        LOG.log(Level.DEBUG, () -> "start excel extracting. . .");
        AutomationUtil util = new AutomationUtil();
        

        try{
            File myFile = new File(environment.getProperty("path.to.file")); 

            if (!myFile.exists()){
                LOG.log(Level.INFO, () -> "no weekly closure file found. . .");
            }
            else{
                try {
                    ResponseStartSaveBO bo = metricsWebService.startSave("A100061", "1.0");
                
                    if (bo.getResult().contains("SUCCESS")){
                        automationRunId = bo.getId();
                    }
                    else{
                        LOG.log(Level.DEBUG, () -> "problem on Start Save. . .");
                    }
                
                } catch (Exception e){
                    LOG.error("error on Start Save. . .");
                    LOG.error(e);
                    e.printStackTrace();
                }

                FileInputStream fis = new FileInputStream(myFile);
                XSSFWorkbook wb = new XSSFWorkbook(fis);
                XSSFSheet sheet = wb.getSheetAt(0);
                wb.close();

                List<EmailDetails> emailDetailsList = new ArrayList<>();

                for (Row row : sheet) {
                    if(row.getRowNum()!= 0){
                        EmailDetails emailDetails = new EmailDetails();

                        for (Cell cell : row) {
                            //number
                            if (cell.getColumnIndex() == 0 && cell != null ){  
                                emailDetails.setNumber(cell.getStringCellValue());
                            }
                            //type
                            if (cell.getColumnIndex() == 1 && cell != null ){  
                                emailDetails.setType(cell.getStringCellValue());
                            }
                            //change env
                            if (cell.getColumnIndex() == 2 && cell != null ){  
                                emailDetails.setChangeEnv(cell.getStringCellValue());
                            }
                            //risk
                            if (cell.getColumnIndex() == 3 && cell != null ){  
                                emailDetails.setRisk(cell.getStringCellValue());
                            }
                            //phase
                            if (cell.getColumnIndex() == 4 && cell != null ){  
                                emailDetails.setPhase(cell.getStringCellValue());
                            }
                            //phase state
                            if (cell.getColumnIndex() == 5 && cell != null ){  
                                emailDetails.setPhaseState(cell.getStringCellValue());
                            }
                            //dates
                                //planned start date
                            if (cell.getColumnIndex() == 6 && cell.getDateCellValue() != null ){  
                                emailDetails.setPsDate(util.toDateString(cell.getDateCellValue(), environment.getProperty("domain.util.date.excelFormat"),""));
                            }
                                //planned end date
                            if (cell.getColumnIndex() == 7 && cell.getDateCellValue() != null ){  
                                emailDetails.setPeDate(util.toDateString(cell.getDateCellValue(), environment.getProperty("domain.util.date.excelFormat"),""));
                            }
                            //requested by
                            if (cell.getColumnIndex() == 8 && !cell.getStringCellValue().equalsIgnoreCase("Requested by") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setReqBy(cell.getStringCellValue());
                            }
                            //assigned to
                            if (cell.getColumnIndex() == 9 && !cell.getStringCellValue().equalsIgnoreCase("Assigned to") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setAssignName(cell.getStringCellValue());
                            }
                            //change analyst
                            if (cell.getColumnIndex() == 10 && !cell.getStringCellValue().equalsIgnoreCase("Change Analyst") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setChangeAnalyst(cell.getStringCellValue());
                            }
                            //assigned to email
                            if (cell.getColumnIndex() == 11 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setAEmail(cell.getStringCellValue());
                            }
                            //assignment group
                            if (cell.getColumnIndex() == 12 && !cell.getStringCellValue().equalsIgnoreCase("Assignment group") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setaGrp(cell.getStringCellValue());
                            }
                            //manager
                            if (cell.getColumnIndex() == 13 && !cell.getStringCellValue().equalsIgnoreCase("Manager") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){ 
                                emailDetails.setManagerName(cell.getStringCellValue()); 
                            }
                            //manager email
                            if (cell.getColumnIndex() == 14 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setMEmail(cell.getStringCellValue());
                            }
                            //director
                            if (cell.getColumnIndex() == 15 && !cell.getStringCellValue().equalsIgnoreCase("Director") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setDirectorName(cell.getStringCellValue());
                            }
                            //director email
                            if (cell.getColumnIndex() == 16 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setdEmail(cell.getStringCellValue());
                            }
                            //vp
                            if (cell.getColumnIndex() == 17 && !cell.getStringCellValue().equalsIgnoreCase("VP") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                                emailDetails.setVpName(cell.getStringCellValue());
                            }
                            // vp email
                            if (cell.getColumnIndex() == 18 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")) {
                                emailDetails.setVpEmail(cell.getStringCellValue());
                            }
                        }
                        emailDetailsList.add(emailDetails);
                    }
                    
                }
                myFile.delete();

                int ctr = 0;
                List<String> directorNames = new ArrayList<>();

                for (int i = 0; i < emailDetailsList.size(); i++){
                    List<String> managerEmails = new ArrayList<>();
                    List<String> assigneeNames = new ArrayList<>();
                    List<String> assigneeEmails = new ArrayList<>();
                    List<EmailDetails> rows = new ArrayList<>();
                    String currentDirector = new String();
                    boolean vpAsDirector = false;

                    if ((emailDetailsList.get(i).getDirectorName() == null || emailDetailsList.get(i).getDirectorName().isBlank()) &&
                        (emailDetailsList.get(i).getVpName() != null || !emailDetailsList.get(i).getVpName().isBlank())) {
                        currentDirector = emailDetailsList.get(i).getVpName();// get VP as director
                        vpAsDirector = true;
                    } else {
                        currentDirector = emailDetailsList.get(i).getDirectorName();
                    }

                    if (!directorNames.contains(currentDirector)){
                        directorNames.add(currentDirector);
                        if (vpAsDirector) {
                            if (emailDetailsList.get(i).getVpEmail() != null || !emailDetailsList.get(i).getVpEmail().isBlank()) {
                                managerEmails.add(emailDetailsList.get(i).getVpEmail());// get VP email
                            }
                        } else {
                            if (emailDetailsList.get(i).getdEmail() != null || !emailDetailsList.get(i).getdEmail().isBlank()) {
                                managerEmails.add(emailDetailsList.get(i).getdEmail());
                            }
                        }

                        for (int j = i; j < emailDetailsList.size(); j++){
                            if ((!vpAsDirector && currentDirector.equalsIgnoreCase(emailDetailsList.get(j).getDirectorName())) ||
                                (currentDirector.equalsIgnoreCase(emailDetailsList.get(j).getVpName()) &&
                                (emailDetailsList.get(j).getDirectorName() == null || emailDetailsList.get(j).getDirectorName().isBlank()))) {
                                
                                rows.add(emailDetailsList.get(j));

                                if (!managerEmails.contains(emailDetailsList.get(j).getMEmail()) && emailDetailsList.get(j).getMEmail() != null){
                                    managerEmails.add(emailDetailsList.get(j).getMEmail());
                                }

                                if (!assigneeNames.contains(emailDetailsList.get(j).getAssignName())){
                                    if (emailDetailsList.get(j).getAssignName() != null) {
                                        assigneeNames.add(emailDetailsList.get(j).getAssignName());
                                    }
                                    if (emailDetailsList.get(j).getAEmail() != null) {
                                        assigneeEmails.add(emailDetailsList.get(j).getAEmail());
                                    }
                                }
                            }
                        }

                        if (rows.size() > 0){
                            sendEmail(rows, managerEmails, assigneeNames, assigneeEmails, currentDirector);
                            ctr =+1;
                        }
                    }
                }

                try {
                    ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, Integer.toString(ctr));

                    if (bo2.getResult().contains("SUCCESS")){
                        LOG.log(Level.DEBUG, () -> "Ending save. . .");
                        ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);

                        if (bo3.getResult().contains("SUCCESS")){
                            LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
                        }
                        else{
                            LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
                        }
                    }
                    else{
                        LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
                    }

                } catch (Exception e){
                    LOG.error("error on Increment Transaction or End Save. . .");
                    LOG.error(e);
                }
            }            

        }catch(Exception e){
            LOG.log(Level.INFO, () -> "Error at excel extracting: ");
            LOG.error(e);
        }
    }

    @Override
    public void sendEmail(List<EmailDetails> rows, List<String> managerEmails, List<String> assigneeNames, List<String> assigneeEmails, String directorName) {
        LOG.log(Level.INFO, () -> "start sending email. . .");

        String body = "<p class='MsoNormal'>Hi ";
            for (int i = 0; i < assigneeEmails.size(); i++){
                body += "<a href='mailto:" + assigneeEmails.get(i) + "'><span style='font-family:&quot;Calibri&quot;,sans-serif;text-decoration:none;text-underline:none'>" + assigneeNames.get(i) + "</span></a>, ";
            }
        body += "</p>"+
            
        //email header
        environment.getProperty("email.header");
        
        //table start
        body += environment.getProperty("table.header");
        //end of header table

        //start of table entry
        for(EmailDetails row:rows){

            body+="<tr style='mso-yfti-irow:1;mso-yfti-lastrow:yes;height:14.5pt'>"+

            //number
            "<td width='98' nowrap='' valign='top' style='width:47.65pt;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getNumber()+"<o:p></o:p></span></p>"+
            "</td>" +

            //type
            "<td width='64' nowrap='' valign='top' style='width:47.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getType()+"<o:p></o:p></span></p>"+
            "</td>" +

            //change env
            "<td width='64' nowrap='' valign='top' style='width:47.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getChangeEnv()+"<o:p></o:p></span></p>"+
            "</td>" +

            //risk
            "<td width='64' nowrap='' valign='top' style='width:47.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getRisk()+"<o:p></o:p></span></p>"+
            "</td>" +

            //phase
            "<td width='64' nowrap='' valign='top' style='width:47.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getPhase()+"<o:p></o:p></span></p>"+
            "</td>" +

            //phase state
            "<td width='64' nowrap='' valign='top' style='width:47.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getPhaseState()+"<o:p></o:p></span></p>"+
            "</td>" +

            //planned start date
            "<td width='90' nowrap='' valign='top' style='width:85.55pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal' align='left' style='text-align:left'><span style='font-size:10.0pt;color:black'>"+row.getPsDate()+"<o:p></o:p></span></p>"+
            "</td>" +

            //planned end date
            "<td width='90' nowrap='' valign='top' style='width:85.55pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal' align='left' style='text-align:left'><span style='font-size:10.0pt;color:black'>"+row.getPeDate()+"<o:p></o:p></span></p>"+
            "</td>"+

            //assigned to
            "<td width='93' nowrap='' valign='top' style='width:69.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getAssignName()+"<o:p></o:p></span></p>"+
            "</td>"+

            //assignment group
            "<td width='101' nowrap='' valign='top' style='width:76.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getaGrp()+"<o:p></o:p></span></p>"+
            "</td>";

            //assignment's group manager
            if (row.getManagerName() != null){
                body+="<td width='119' nowrap='' valign='top' style='width:89.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getManagerName()+"<o:p></o:p></span></p>"+
                "</td>";
            }
            else{
                body+="<td width='119' nowrap='' valign='top' style='width:89.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'><o:p></o:p></span></p>"+
                "</td>";
            }

            //director name
            if (row.getDirectorName() != null){
                body+="<td width='119' nowrap='' valign='top' style='width:89.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+row.getDirectorName()+"<o:p></o:p></span></p>"+
                "</td>";
            } else{
                body+="<td width='119' nowrap='' valign='top' style='width:89.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
                "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'><o:p></o:p></span></p>"+
                "</td>";
            }

            body+="</tr>";
        }
        body += "</tbody></table>";
        //table end

        //email footer
        body+= environment.getProperty("email.footer");

        body+= "<img src=\"cid:image\">";

        body+= "</body>";

        if(environment.getProperty("spring.profiles.active").equalsIgnoreCase("dev")){
            String[] toEmails = {"RPA-DEV@safeway.com"};
            String[] ccEmails = { "alexander.robes@safeway.com"}; 
            String[] bccEmails = {"cpo0000@safeway.com"};

            try {
                sendMessageWithInlineImage(environment.getProperty("mail.argus.from"), "Change Management", toEmails, ccEmails, bccEmails, "Possible Infraction: Production Changes for Urgent Closure – Tower of " + directorName, body, "C:\\Automations\\Weekly_Closure_Chg\\signature2.png", "<image>");
            } catch (ArgusMailException e) {
                LOG.log(Level.INFO, () -> "Error at email sending: ");
                LOG.error(e);
            }
        }else if(environment.getProperty("spring.profiles.active").equalsIgnoreCase("qa")){
            String[] toEmails = assigneeEmails.toArray(new String[assigneeEmails.size()]);

            //managerEmails.add("change.mgmt@safeway.com");
            String[] ccEmails = managerEmails.toArray(new String[managerEmails.size()]);

            String[] bccEmails = {"cpo0000@safeway.com", "alexander.robes@safeway.com"};

            try {
                sendMessageWithInlineImage(environment.getProperty("mail.argus.from"), "Change Management", toEmails, ccEmails, bccEmails, "Possible Infraction: Production Changes for Urgent Closure – Tower of " + directorName, body, "C:\\Automations\\Weekly_Closure_Chg\\signature2.png", "<image>");
            } catch (ArgusMailException e) {
                LOG.log(Level.INFO, () -> "Error at email sending: ");
                LOG.error(e);
            }
        }else if(environment.getProperty("spring.profiles.active").equalsIgnoreCase("prod")){
            String[] toEmails = assigneeEmails.toArray(new String[assigneeEmails.size()]);

            managerEmails.add("change.mgmt@safeway.com");
            String[] ccEmails = managerEmails.toArray(new String[managerEmails.size()]);

            String[] bccEmails = {"cpo0000@safeway.com", "alexander.robes@safeway.com"};

            try {
                sendMessageWithInlineImage(environment.getProperty("mail.argus.from"), "Change Management", toEmails, ccEmails, bccEmails, "Possible Infraction: Production Changes for Urgent Closure – Tower of " + directorName, body, "C:\\Automations\\Weekly_Closure_Chg\\signature2.png", "<image>");
            } catch (ArgusMailException e) {
                LOG.log(Level.INFO, () -> "Error at email sending: ");
                LOG.error(e);
            }
        }

    }

    public void sendMessageWithInlineImage(String from, String aliasFrom, String[] to, String[] cc, String[] bcc,
            String subject, String msg, String imageFile, String imageCID) throws ArgusMailException {
        
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            mailMessage.setFrom(from);
            mailMessage.setSubject(subject, "UTF-8");
            
            for(String bccString: bcc){
                Address bccAddress = new InternetAddress(bccString);
                mailMessage.addRecipient(Message.RecipientType.BCC, bccAddress);
            }
            
            for(String ccString: cc){
                Address ccAddress = new InternetAddress(ccString);
                mailMessage.addRecipient(Message.RecipientType.CC, ccAddress);
            }

            for(String toString: to){
                Address toAddress = new InternetAddress(toString);
                mailMessage.addRecipient(Message.RecipientType.TO, toAddress);
            }

            MimeMultipart multipart = new MimeMultipart("related");

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent(msg, "text/html");

            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart imageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(new File(imageFile));

            imageBodyPart.setDataHandler(new DataHandler(fds));
            imageBodyPart.setHeader("Content-ID", imageCID);
            
            multipart.addBodyPart(imageBodyPart);

            mailMessage.setContent(multipart);
        } catch (MessagingException e) {
            LOG.error(e);
        }
        try{
            javaMailSender.send(mailMessage);
        }catch(MailException me){
            throw new ArgusMailException("Message failed. ",me);
        }
        
    }

}