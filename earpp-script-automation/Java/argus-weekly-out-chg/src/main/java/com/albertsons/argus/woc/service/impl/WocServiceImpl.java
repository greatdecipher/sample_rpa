package com.albertsons.argus.woc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.albertsons.argus.woc.dto.EmailDetails;
import com.albertsons.argus.woc.enums.StatusNameEnum;
import com.albertsons.argus.woc.service.WocService;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;


@Service
public class WocServiceImpl implements WocService{
    private static final Logger LOG = LogManager.getLogger(WocServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    EmailService emailService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MetricsWebService metricsWebService;

    String automationRunId;

    @Override
    public void login() {
        LOG.log(Level.INFO, () -> "start Login method. . .");

        

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
        LOG.log(Level.INFO, () -> "start excel extracting. . .");
        AutomationUtil util = new AutomationUtil();
        try {
            ResponseStartSaveBO bo = metricsWebService.startSave("A100062", "1.0");
        
            if (bo.getResult().contains("SUCCESS")){
                automationRunId = bo.getId();
            }
            else{
                LOG.log(Level.DEBUG, () -> "problem on Start Save. . .");
            }
        
        } catch (Exception e){
            LOG.error("error on Start Save. . .");
            LOG.error(e);
        }

        FileInputStream fis = null;
        XSSFWorkbook wb = null;
        try{
            File myFile = new File(environment.getProperty("path.to.file")); 
            fis = new FileInputStream(myFile);
            wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

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
                        //short descr
                        if (cell.getColumnIndex() == 2 && cell != null ){  
                            emailDetails.setShortDecr(cell.getStringCellValue());
                        }
                        //dates
                            //planned start date
                        if (cell.getColumnIndex() == 3 && cell.getDateCellValue() != null ){  
                            emailDetails.setPsDate(util.toDateString(cell.getDateCellValue(), environment.getProperty("domain.util.date.excelFormat"),""));
                        }
                            //planned end date
                        if (cell.getColumnIndex() == 4 && cell.getDateCellValue() != null ){  
                            emailDetails.setPeDate(util.toDateString(cell.getDateCellValue(), environment.getProperty("domain.util.date.excelFormat"),""));
                        }
                            //work start
                        if (cell.getColumnIndex() == 5 && cell.getDateCellValue() != null ){  
                            emailDetails.setWsDate(util.toDateString(cell.getDateCellValue(), environment.getProperty("domain.util.date.excelFormat"),""));
                        }
                            //work end
                        if (cell.getColumnIndex() == 6 && cell.getDateCellValue() != null ){  
                            emailDetails.setWeDate(util.toDateString(cell.getDateCellValue(), environment.getProperty("domain.util.date.excelFormat"),""));
                        }

                        //other details
                            //requested by
                        if (cell.getColumnIndex() == 7 && !cell.getStringCellValue().equalsIgnoreCase("Requested by") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setReqBy(cell.getStringCellValue());
                        }
                            //assignment group
                        if (cell.getColumnIndex() == 10 && !cell.getStringCellValue().equalsIgnoreCase("Assignment group") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setaGrp(cell.getStringCellValue());
                        }
                            //vp
                        if (cell.getColumnIndex() == 15 && !cell.getStringCellValue().equalsIgnoreCase("VP") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setVp(cell.getStringCellValue());
                        }
                            // vp email
                        if (cell.getColumnIndex() == 16 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")) {
                            emailDetails.setVpEmail(cell.getStringCellValue());
                        }

                        //names and emails
                            //assigned to
                        if (cell.getColumnIndex() == 8 && !cell.getStringCellValue().equalsIgnoreCase("Assigned to") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setAssignName(cell.getStringCellValue());
                        }
                        if (cell.getColumnIndex() == 9 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setAEmail(cell.getStringCellValue());
                        }
                        
                            //manager
                        if (cell.getColumnIndex() == 11 && !cell.getStringCellValue().equalsIgnoreCase("Manager") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){ 
                            emailDetails.setManagerName(cell.getStringCellValue()); 
                        }
    
                        if (cell.getColumnIndex() == 12 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setMEmail(cell.getStringCellValue());
                        }
    
                            //director
                        if (cell.getColumnIndex() == 13 && !cell.getStringCellValue().equalsIgnoreCase("Director") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setDirectorName(cell.getStringCellValue());
                        }
    
                        if (cell.getColumnIndex() == 14 && !cell.getStringCellValue().equalsIgnoreCase("Email") && cell != null && !cell.getStringCellValue().equalsIgnoreCase("")){  
                            emailDetails.setdEmail(cell.getStringCellValue());
                        }

                    }
                    emailDetailsList.add(emailDetails);
                }
                 
            }
            myFile.delete();

            int ctr = 0;
            List<String> checkedNames = new ArrayList<>();
                for(int i=0;i<emailDetailsList.size();i++){
                    List<EmailDetails> nameList = new ArrayList<>();
                    String managerEmail = new String();
                    String assigneeName = new String();
                    String assigneeEmail = new String();

                    if(!checkedNames.contains(emailDetailsList.get(i).getAssignName())){
                        String name = emailDetailsList.get(i).getAssignName();
                        checkedNames.add(name);

                        for(int j=i;j<emailDetailsList.size();j++){
                            if(emailDetailsList.get(j).getAssignName() == name){
                                nameList.add(emailDetailsList.get(j));
                                //set assignee email
                                if (assigneeEmail.isBlank() && emailDetailsList.get(j).getAEmail() != null && !emailDetailsList.get(j).getAEmail().isBlank()) {
                                    assigneeEmail = emailDetailsList.get(j).getAEmail();
                                    assigneeName = emailDetailsList.get(j).getAssignName();
                                } 
                                //set manager email
                                if (managerEmail.isBlank() && emailDetailsList.get(j).getMEmail() != null && !emailDetailsList.get(j).getMEmail().isBlank()) {
                                    managerEmail = emailDetailsList.get(j).getMEmail();
                                }
                            }
                        }

                        if (assigneeEmail.isBlank()) {
                            if (emailDetailsList.get(i).getMEmail() != null && !emailDetailsList.get(i).getMEmail().isBlank()) {
                                assigneeEmail = emailDetailsList.get(i).getMEmail();
                                assigneeName = emailDetailsList.get(i).getManagerName();
                            } else if (emailDetailsList.get(i).getdEmail() != null && !emailDetailsList.get(i).getdEmail().isBlank()) {
                                assigneeEmail = emailDetailsList.get(i).getdEmail();
                                assigneeName = emailDetailsList.get(i).getDirectorName();
                            } else if (emailDetailsList.get(i).getVpEmail() != null && !emailDetailsList.get(i).getVpEmail().isBlank()) {
                                assigneeEmail = emailDetailsList.get(i).getVpEmail();
                                assigneeName = emailDetailsList.get(i).getVp();
                            }
                        }

                        if (managerEmail.isBlank()) {
                            if (emailDetailsList.get(i).getdEmail() != null && !emailDetailsList.get(i).getdEmail().isBlank()) {
                                managerEmail = emailDetailsList.get(i).getdEmail();
                            } else if (emailDetailsList.get(i).getVpEmail() != null && !emailDetailsList.get(i).getVpEmail().isBlank()) {
                                managerEmail = emailDetailsList.get(i).getVpEmail();
                            }
                        }

                        if(nameList.size()>0){
                            sendEmail(nameList, assigneeName, assigneeEmail, managerEmail);
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

        } catch (Exception e){
            LOG.log(Level.INFO, () -> "Error at excel extracting: ");
            LOG.error(e);
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
				LOG.error(e);
				LOG.log(Level.INFO, () -> "Error at closing FileInputStream or FileOutputStream . . . ");
            }
        }
    }

    @Override
    public void sendEmail(List<EmailDetails> list, String assigneeName, String assigneeEmail, String managerEmail) {
        LOG.log(Level.INFO, () -> "start sending email. . .");

        AutomationUtil util = new AutomationUtil();

        String body = "<p class='MsoNormal'>Hi <a href='mailto:"+assigneeEmail+"'><span style='font-family:&quot;Calibri&quot;,sans-serif;text-decoration:none;text-underline:none'>"+assigneeName+"</span></a>,</p>"+
            //email header
            environment.getProperty("email.header");
       //table start
       body += environment.getProperty("table.header");
        //end of header table

        //start of table entry
    for(EmailDetails item:list){
        body+="<tr style='mso-yfti-irow:1;mso-yfti-lastrow:yes;height:14.5pt'>"+

        //number
        "<td width='98' nowrap='' valign='top' style='width:73.7pt;border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+item.getNumber()+"<o:p></o:p></span></p>"+
        "</td>" +

        //type
        "<td width='64' nowrap='' valign='top' style='width:47.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+item.getType()+"<o:p></o:p></span></p>"+
        "</td>" +

        //short description
        "<td width='238' nowrap='' valign='top' style='width:178.7pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+item.getShortDecr()+"<o:p></o:p></span></p>"+
        "</td>" +

        //planned start date
        "<td width='114' nowrap='' valign='top' style='width:85.55pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal' align='left' style='text-align:left'><span style='font-size:10.0pt;color:black'>"+item.getPsDate()+"<o:p></o:p></span></p>"+
        "</td>" +

        //planned end date
        "<td width='114' nowrap='' valign='top' style='width:85.55pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal' align='left' style='text-align:left'><span style='font-size:10.0pt;color:black'>"+item.getPeDate()+"<o:p></o:p></span></p>"+
        "</td>"+

        //work start
         "<td width='114' nowrap='' valign='top' style='width:85.55pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
             "<p class='MsoNormal' align='left' style='text-align:left'><span style='font-size:10.0pt;color:black'>"+item.getWsDate()+"<o:p></o:p></span></p>"+
         "</td>" +

        //work end
         "<td width='114' nowrap='' valign='top' style='width:85.55pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
             "<p class='MsoNormal' align='left' style='text-align:left'><span style='font-size:10.0pt;color:black'>"+item.getWeDate()+"<o:p></o:p></span></p>"+
         "</td>" +

        //status
        "<td width='225' nowrap='' style='width:169.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;background:#F4B084;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+statStatus(statStart(item.getPsDate(), item.getWsDate(), item.getPeDate()), statEnd(item.getPsDate(), item.getWeDate(), item.getPeDate()))+"<o:p></o:p></span></p>"+
        "</td>"+

        //assigned to
        "<td width='93' nowrap='' valign='top' style='width:69.65pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+item.getAssignName()+"<o:p></o:p></span></p>"+
        "</td>"+

        //assignment group
        "<td width='101' nowrap='' valign='top' style='width:76.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+item.getaGrp()+"<o:p></o:p></span></p>"+
        "</td>"+

        //assignment's group manager
        "<td width='119' nowrap='' valign='top' style='width:89.1pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+item.getManagerName()+"<o:p></o:p></span></p>"+
        "</td>"+

        //assignment's group director
        "<td width='90' nowrap='' valign='top' style='width:67.45pt;border-top:none;border-left:none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;padding:0in 5.4pt 0in 5.4pt;height:14.5pt'>"+
            "<p class='MsoNormal'><span style='font-size:10.0pt;color:black'>"+item.getDirectorName()+"<o:p></o:p></span></p>"+
        "</td>"+

    "</tr>";
    }
    body += "</tbody></table>";
        
    //table end

    //email footer
    body+= environment.getProperty("email.footer");


body+= "<img src=\"cid:image\">";

body+= "</body>";

        if(environment.getProperty("spring.profiles.active").equalsIgnoreCase("dev")){
            String[] aemail = {assigneeEmail};
            //String[] memail = { "alexander.robes@safeway.com"};
            String[] memail = {managerEmail};
            String[] bccemail = environment.getProperty("mail.argus.cc", String[].class);
            String timestamp = util.toDateString(new Date(), environment.getProperty("domain.util.date.tsFormat"),"");

            try {
                sendMessageWithInlineImage(environment.getProperty("mail.argus.from"), "Change Management", aemail, memail, bccemail, "Change Implemented Outside of the Approved Schedule - " + getmulNumber(list) +" " + timestamp, body, "C:\\Automations\\Weekly_Outside_Chg\\signature2.png", "<image>");
            } catch (ArgusMailException e) {
                LOG.log(Level.INFO, () -> "Error at email sending: ");
                LOG.error(e);
            }
        }else if(environment.getProperty("spring.profiles.active").equalsIgnoreCase("qa")){
            String[] aemail = {assigneeEmail};
            String[] memail = {managerEmail, "change.mgmt@safeway.com"}; 
            String[] bccemail = environment.getProperty("mail.argus.cc", String[].class);
            String timestamp = util.toDateString(new Date(), environment.getProperty("domain.util.date.tsFormat"),"");

            try {
                sendMessageWithInlineImage(environment.getProperty("mail.argus.from"), "Change Management", aemail, memail, bccemail, "Change Implemented Outside of the Approved Schedule - " + getmulNumber(list) +" " + timestamp, body, "C:\\Automations\\Weekly_Outside_Chg\\signature2.png", "<image>");
            } catch (ArgusMailException e) {
                LOG.log(Level.INFO, () -> "Error at email sending: ");
                LOG.error(e);
            }
        }else if(environment.getProperty("spring.profiles.active").equalsIgnoreCase("prod")){
            String[] aemail = {assigneeEmail};
            String[] memail = {managerEmail, "change.mgmt@safeway.com"}; 
            String[] bccemail = environment.getProperty("mail.argus.cc", String[].class);
            String timestamp = util.toDateString(new Date(), environment.getProperty("domain.util.date.tsFormat"),"");

            try {
                sendMessageWithInlineImage(environment.getProperty("mail.argus.from"), "Change Management", aemail, memail, bccemail, "Change Implemented Outside of the Approved Schedule - " + getmulNumber(list) +" " + timestamp, body, "C:\\Automations\\Weekly_Outside_Chg\\signature2.png", "<image>");
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

    private String getmulNumber(List<EmailDetails> list){
            String numTemp = "";
            for(int i=0;i<list.size();i++){
                if(i!=list.size()-1){
                    numTemp = numTemp + list.get(i).getNumber();
                    numTemp = numTemp + ", ";
                }else{
                    numTemp = numTemp + list.get(i).getNumber();
                }
            }return numTemp;

    }

    private String statStart(String pStartdate, String wStartdate, String pEnddate) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

            if(pStartdate == null || wStartdate == null){
                return StatusNameEnum.BEFORE.getValue();
            }else{
                Date newdate1 = sdf.parse(pStartdate);
                Date newdate2 = sdf.parse(wStartdate);
                Date newdate3 = sdf.parse(pEnddate);

                int result = newdate1.compareTo(newdate2);
                int result2 = newdate3.compareTo(newdate2);
                
                if(result == 0){
                    sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    newdate1 = sdf.parse(pStartdate);
                    newdate2 = sdf.parse(wStartdate);
                    result = newdate1.compareTo(newdate2);

                    if(result < 0 || result == 0){
                        return StatusNameEnum.OK.getValue();
                    }else{
                        return StatusNameEnum.BEFORE.getValue();
                    }
                }
                else if (result > 0){
                    return StatusNameEnum.BEFORE.getValue();
                }
                
                else if(result2 == 0){
                    sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    newdate2 = sdf.parse(wStartdate);
                    newdate3 = sdf.parse(pEnddate);

                    result2 = newdate3.compareTo(newdate2);
                    if(result2 > 0 || result2 == 0){
                        return StatusNameEnum.OK.getValue();
                    }else{
                        return StatusNameEnum.AFTER.getValue();
                    }
                }
                
                else if(result2 < 0){
                    return StatusNameEnum.AFTER.getValue();
                } else{
                    return StatusNameEnum.OK.getValue();
                }
                
            }

        }catch(java.text.ParseException e){
            LOG.error(e);
            return "error";
        }

    }

    private String statEnd(String pStartdate, String wEnddate, String pEnddate) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");

            if(pStartdate == null || wEnddate == null){
                return StatusNameEnum.BEFORE.getValue();
            }else{
                Date newdate1 = sdf.parse(pStartdate);
                Date newdate2 = sdf.parse(wEnddate);
                Date newdate3 = sdf.parse(pEnddate);

                int result = newdate1.compareTo(newdate2);
                int result2 = newdate3.compareTo(newdate2);

                if(result == 0){
                    sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    newdate1 = sdf.parse(pStartdate);
                    newdate2 = sdf.parse(wEnddate);
                    result = newdate1.compareTo(newdate2);

                    if(result > 0 || result == 0){
                        return StatusNameEnum.OK.getValue();
                    }else{
                        return StatusNameEnum.BEFORE.getValue();
                    }
                }
                if (result > 0) {
                    return StatusNameEnum.BEFORE.getValue();
                } 

                else if(result2 == 0){
                    sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    newdate2 = sdf.parse(wEnddate);
                    newdate3 = sdf.parse(pEnddate);

                    result2 = newdate3.compareTo(newdate2);
                    if(result2 > 0 || result2 == 0){
                        return StatusNameEnum.OK.getValue();
                    }else{
                        return StatusNameEnum.AFTER.getValue();
                    }
                }

                else if(result2 < 0){
                    return StatusNameEnum.AFTER.getValue();
                }else{
                    return StatusNameEnum.OK.getValue();
                }
                
            }

        }catch(java.text.ParseException e){
            LOG.log(Level.INFO, () -> "Error at status function: ");
            LOG.error(e);
            return "error";
        }

    }
    
    private String statStatus(String start, String end){
        if(start == StatusNameEnum.BEFORE.getValue() && end == StatusNameEnum.AFTER.getValue()){
            return StatusNameEnum.BEFOREAFTER.getValue();
        }else{
            return end;
        }
    }

}