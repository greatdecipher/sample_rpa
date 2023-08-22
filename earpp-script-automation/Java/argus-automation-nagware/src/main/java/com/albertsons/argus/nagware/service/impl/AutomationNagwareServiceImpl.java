package com.albertsons.argus.nagware.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.nagware.exception.NagwareException;
import com.albertsons.argus.nagware.model.User;
import com.albertsons.argus.nagware.service.AutomationNagwareService;

@Service
public class AutomationNagwareServiceImpl implements AutomationNagwareService {
    private static final Logger LOG = LogManager.getLogger(AutomationNagwareServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Override
    public List<User> getProjectCenterDetails() throws NagwareException {
        LOG.info("Getting Project Center Details . . .");
        List<User> projectCenterList = new ArrayList<>();
        try {
            File filePath = new File(environment.getProperty("argus.nagware.folder.path") + environment.getProperty("argus.nagware.project.file"));
            if (filePath.exists()) {
            FileInputStream file = new FileInputStream(
                filePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for(Row row: sheet) {
                if(i > 0) {
                    if(checkDateValidity(row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue())) {
                        User projectCenter = new User();
                        projectCenter.setOwner(row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                        projectCenter.setProjectName(row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                        projectCenter.setPortfolio(row.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                        if(!projectCenter.getOwner().equals("")) {
                            projectCenterList.add(projectCenter);
                        }    
                    }
                }
                i++;
            }
            workbook.close();
        } else {
            sendMailHandler(environment.getProperty("argus.nagware.project.file") + " was not found.");
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NagwareException(e.getMessage());
        }
        LOG.info("Storing Project Center Details . . .");
        return projectCenterList;
    }

    @Override
    public Boolean checkDateValidity(String strDate) {
        if(strDate.equals("") || strDate == null) {
            return Boolean.TRUE;
        } else {
            
            LocalDate previousMonday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.US);
            LocalDate date = LocalDate.parse(strDate.trim(), formatter);
            if(previousMonday.equals(date))
                return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public List<User> getResourceCenterDetails() throws NagwareException {
        LOG.info("Getting Resource Center Details . . .");
        List<User> resourceCenterList = new ArrayList<>();
        
        try {
            File file = new File(environment.getProperty("argus.nagware.folder.path") + environment.getProperty("argus.nagware.resource.file"));
            if(file.exists()) {
                Scanner scanner = new Scanner(file);
                int i = 0;
                while(scanner.hasNextLine()) {
                    User resourceCenter = new User();
                    String[] resource = scanner.nextLine().split(",");
                    if(i > 0) {
                        if(resource[0].equals("")) {
                            resourceCenter.setEmailAddress("");
                        } else {
                            String[] emailCol = resource[2].split("|");
                            resourceCenter.setEmailAddress(emailCol[2]);
                        }
                        resourceCenter.setOwner(resource[0]);
                        resourceCenterList.add(resourceCenter);
                    }
                    i++;
                }
                scanner.close();
            } else { 
                sendMailHandler(environment.getProperty("argus.nagware.resource.file") + " was not found.");
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
            throw new NagwareException(e.getMessage());
        }
        LOG.info("Storing Resource Center Details . . .");
        return resourceCenterList;
    }

    @Override
    public List<User> getPortfolioDetails() throws NagwareException {

        List<User> portfolioList = new ArrayList<>();
        try {
            File filePath = new File(environment.getProperty("argus.nagware.folder.path") + environment.getProperty("argus.nagware.portfolio.file"));
            if (filePath.exists()) {
            FileInputStream file = new FileInputStream(
                filePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for(Row row: sheet) {
                if(i > 0) {
                    User portfolio = new User();
                    portfolio.setPortfolio(row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                    portfolio.setPoc(row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                    portfolio.setPocEmail(row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                    if(!row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equals("")) {
                        portfolioList.add(portfolio);
                    }
                }
                i++;
            }
            workbook.close();
            return portfolioList;
            } else {
                sendMailHandler(environment.getProperty("argus.nagware.portfolio.file") + " was not found.");
            }
        } catch(Exception e) {

        }
        return null;
    }

    @Override
    public List<User> getUserDetails(List<User> projectCenter, List<User> resourceCenter) throws NagwareException {
        LOG.info("Getting User Details . . .");
        List<User> users = new ArrayList<>();
        List<User> portfolioList = getPortfolioDetails();
        for(User user: projectCenter) {
            user.setEmailAddress("");
            user.setPoc("");
            user.setPocEmail("");

            for(User resource: resourceCenter) {
                if(user.getOwner().equals(resource.getOwner())) {
                    user.setEmailAddress(resource.getEmailAddress());
                    user.setEmailFound(Boolean.TRUE);
                    break;
                }
            }
            if(user.getEmailAddress().equals("")) {
                user.setEmailFound(Boolean.FALSE);
            }
            for(User portfolio: portfolioList) {
                if(user.getPortfolio().equals(portfolio.getPortfolio())) {
                    user.setPocEmail(portfolio.getPocEmail());
                    user.setPoc(portfolio.getPoc());
                    break;
                }
            }
            users.add(user);
        }
        LOG.info(users.toString());
        return users;
    }

    @Override
    public Integer sendMailToList(List<User> users) throws NagwareException{
        
        Integer ctr = 0;
        String htmlTemplate = createMailTemplate();
        String from = environment.getProperty("nagware.mail.pmo");
        String imageFile = environment.getProperty("argus.nagware.folder.path")+environment.getProperty("argus.nagware.image.file");
        String imageCID = "<image002>";
        if(htmlTemplate.equals("")) {
            throw new NagwareException("Html Template Was not Created Properly");
        }
        for(User user: users) {
            
            List<String> ccRecipients = new ArrayList<>();
            List<String> bccRecipients = new ArrayList<>();
            List<String> toRecipients = new ArrayList<>();
            String newIntroductionString = "";
            String subject = "";
            try {
                bccRecipients.add(environment.getProperty("nagware.mail.bcc1"));
                bccRecipients.add(environment.getProperty("nagware.mail.bcc2"));
                ccRecipients.add(environment.getProperty("nagware.mail.cc"));

                if(user.isEmailFound()) {
                    newIntroductionString = "Hello " + user.getOwner() + ",";
                    subject = environment.getProperty("nagware.mail.subject") + " - " + user.getProjectName();
                    toRecipients.add(user.getEmailAddress());

                    if(!user.getPocEmail().equals("")) {
                        String[] pocEmails = user.getPocEmail().split(";");
                        for(int i = 0; i < pocEmails.length; i++) {
                            ccRecipients.add(pocEmails[i].trim());
                        }
                    }
                    
                } else {
                    newIntroductionString = environment.getProperty("nagware.string.handler.info").replace("-Owner-", user.getOwner()).replace("-Project-", user.getProjectName());
                    subject = environment.getProperty("nagware.mail.subject") + " - Not Delivered Successfully";
                    if(!user.getPocEmail().equals("")) {
                        String[] pocEmails = user.getPocEmail().split(";");
                        for(int i = 0; i < pocEmails.length; i++) {
                            toRecipients.add(pocEmails[i].trim());
                        }
                    }
                }
                
                String msg = "<br/>" + htmlTemplate.toString().replace(environment.getProperty("nagware.string.intro"), newIntroductionString)
                        .replace(environment.getProperty("nagware.string.project.name"), user.getProjectName())
                        .replace("(DateTime)", getExtractDate(environment.getProperty("argus.nagware.folder.path") + environment.getProperty("argus.nagware.project.file")) + " Manila")
                        .replaceAll(environment.getProperty("nagware.string.image.code.start") + ".*" + environment.getProperty("nagware.string.image.code.end"), environment.getProperty("nagware.string.image.code"));
                
                emailService.sendMessageWithInlineImage(from, from, toRecipients.toArray(new String[0]), ccRecipients.toArray(new String[0]), bccRecipients.toArray(new String[0]), subject, msg, imageFile, imageCID);
                ctr++;
            } catch(Exception e) {
                throw new NagwareException(e.getMessage());
            } 
        }
        return ctr;
    }

    @Override
    public void sendMailHandler(String msg) throws NagwareException{ 
        try{
            emailService.sendSimpleMessage(environment.getProperty("nagware.mail.pmo"), 
                environment.getProperty("nagware.mail.pmo"), 
                environment.getProperty("nagware.default.mail.handler", String[].class), 
                environment.getProperty("nagware.mail.handler.cc", String[].class),
                environment.getProperty("nagware.mail.subject"), 
                environment.getProperty("nagware.default.mail.handler.message") + msg, 
                3, true);
        } catch(ArgusMailException e) { 
            throw new NagwareException(e.getMessage());
        }
    }

    @Override
    public void sendMailError(String msg) {
        LOG.info("Sending Mail Error . . .");
        try{ 
            emailService.sendSimpleMessage(environment.getProperty("nagware.mail.error.sender"), 
                environment.getProperty("nagware.mail.error.sender"), 
                environment.getProperty("nagware.mail.error.recipients", String[].class), 
                environment.getProperty("nagware.mail.error.cc", String[].class),
                environment.getProperty("nagware.mail.error.subject"), 
                environment.getProperty("nagware.mail.error.message") + msg, 
                3, true);
        } catch(ArgusMailException e) {
            LOG.info(e.getMessage());
        }
    }

    @Override
    public String createMailTemplate() throws NagwareException {
        StringBuilder sb = new StringBuilder();
        File file = new File(environment.getProperty("argus.nagware.folder.path")
            + environment.getProperty("argus.nagware.html.file"));
        try{
            if(file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str;
                while((str = br.readLine()) != null) {
                    sb.append(str).append(" ");
                }
                
                br.close();
                String mailTemplate = sb.toString().replace("’", "'").replace("FTEs%20Time%20Tracking%20Retirement%20-%20Enterprise%20Agile%20PMO%20-%20Safeway%20Confluence", environment.getProperty("nagware.string.link"));
                return mailTemplate;
            } else { 
                sendMailHandler(environment.getProperty("argus.nagware.html.file") + " was not found.");
            }
        } catch (Exception e) {
            throw new NagwareException(e.getMessage());
        }

        return "";
    }

    @Override
    public void archiveFiles() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);
        LocalDate date = LocalDate.now();
        String target = environment.getProperty("argus.nagware.folder.path") + environment.getProperty("argus.nagware.folder.archive") 
           + "//" + date.format(formatter).toString() + "_ProjectCenter.xlsx";
        try{
            File projectFile = new File(environment.getProperty("argus.nagware.folder.path") + environment.getProperty("argus.nagware.project.file"));
            InputStream inputStream = new FileInputStream(projectFile);
            Path targetPath = Paths.get(target);
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
            projectFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getResourceCenterDetailsXLSX() throws NagwareException {
        LOG.info("Getting Resource Center Details . . .");
        List<User> resourceCenterList = new ArrayList<>();
        try {
            File filePath = new File(environment.getProperty("argus.nagware.folder.path") + environment.getProperty("argus.nagware.resource.file"));
            if (filePath.exists()) {
            FileInputStream file = new FileInputStream(
                filePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for(Row row: sheet) {
                
                User resource = new User();
                if(i > 0) {
                    
                    String emailCol = row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if(emailCol.equals("")) {
                        resource.setEmailAddress("");
                    } else {
                        resource.setEmailAddress(emailCol);
                    }
                    
                    resource.setOwner(row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
                    resourceCenterList.add(resource);
                }
                i++;

            }
            workbook.close();
        } else {
            sendMailHandler(environment.getProperty("argus.nagware.resource.file") + " was not found.");
        }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NagwareException(e.getMessage());
        }
        LOG.info("Storing Resource Center Details . . .");
        return resourceCenterList;
    }

    @Override
    public String getExtractDate(String file) {
        Path pathFile = Paths.get(file);
        try {
            BasicFileAttributes fileAttr = Files.readAttributes(pathFile, BasicFileAttributes.class);
            LocalDateTime dateTime = fileAttr.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY hh:ss a");
            return dateTime.format(dtf);
        } catch(Exception e) {
            LOG.error(e);
        }
        return null;
    }

    @Override
    public void processNotification(String state, String transactionNumber) throws NagwareException {
        try{    
            if(state.equals("START")) {
                emailService.sendSimpleMessage(environment.getProperty("nagware.mail.rpa"),
                    environment.getProperty("nagware.mail.rpa"), 
                    environment.getProperty("nagware.mail.rpa", String[].class), 
                    environment.getProperty("nagware.mail.handler.cc", String[].class), 
                    "PMO Time Tracking Reminder Automation - Started", "Time Tracking Reminder Automation Process Started", 3, true);
                return;
            } else if (state.equals("END")) {
                StringBuilder sb = new StringBuilder();
                sb.append("Time Tracking Reminder Automation Process Completed");
                sb.append(System.getProperty("line.separator"));
                sb.append("Total Transaction: " + transactionNumber);
                emailService.sendSimpleMessage(environment.getProperty("nagware.mail.rpa"),
                    environment.getProperty("nagware.mail.rpa"), 
                    environment.getProperty("nagware.mail.rpa", String[].class), 
                    environment.getProperty("nagware.mail.handler.cc", String[].class), 
                    "PMO Time Tracking Reminder Automation - Finished", sb.toString(), 3, true);
                return;
            } else {
                return;
            }
        } catch (Exception e) {
            throw new NagwareException(e.getMessage());
        }
    }

    
}
