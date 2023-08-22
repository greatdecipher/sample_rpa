package com.albertsons.argus.q2c.inbound.bankpaids.service.impl;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.oracle.service.OracleService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.domaindbq2c.dto.BankStatementsDTO;
import com.albertsons.argus.q2c.common.service.Q2CCommonService;
import com.albertsons.argus.q2c.inbound.bankpaids.service.BankPaidsErpService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class BankPaidsErpServiceImpl implements BankPaidsErpService{
    private static final Logger LOG = LogManager.getLogger(BankPaidsErpServiceImpl.class);

    @Autowired
    OracleService oracleService;

    @Autowired
    OracleService domainOracleService;

    @Autowired
    PlaywrightAutomationService playwrightService;

    @Autowired
    Q2CCommonService q2cCommonService;

    @Autowired
    Environment environment;

    List<String> bankStatementsAccountsFound = new ArrayList<>();

    String[] tokenizedAccounts = {};

    private String execTimestamp;

    @Override
    public void bankPaidsErpReadBankMain(String dateToCheck, List<BankStatementsDTO> bankStatementsDTOs, String execTimestamp){
        LOG.log(Level.DEBUG, () -> "start bankPaidsErpReadBankMain method. . .");

        AutomationUtil util = new AutomationUtil();
        this.execTimestamp = execTimestamp;

        try {
            Browser browser = playwrightService.openBrowser();
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
    
            Page page = domainOracleService.navigateScheduledProcesses(browser, browserContext);

            TimeUnit.SECONDS.sleep(3);

            if (bankStatementsDTOs.get(0).getAlternateDocumentId().contains("EAS_CB_MO_")){
                //this is a special case, so don't include it
            }
            else{
                List<ElementHandle> searchResults = searchAutoReconcileBankStatements(page);

                int ctrResults = 0; // ctr for later on to compare that the number of results is actually the same as the result from the query 

                if (searchResults != null || searchResults.size() > 0 || !searchResults.isEmpty()){
                    //get only results for the current date
                    List<ElementHandle> tableResults = page.querySelectorAll("table.x1he.x1i2");
                    if (!tableResults.isEmpty()){ 
                        
                        int firstVisitFlag = 0;

                        for (ElementHandle tableResult : tableResults){
                            List<ElementHandle> rowResults = tableResult.querySelectorAll("tbody >> tr >> tbody >> tr");
                            
                            if (!rowResults.isEmpty()){ 

                                for (ElementHandle rowResult : rowResults){
                                    List<ElementHandle> columnResults = rowResult.querySelectorAll("td.xen");
                                    
                                    if (!columnResults.isEmpty()){ 

                                        for (ElementHandle columnResult : columnResults){
                                            if (columnResult.innerText().contains("MST")){

                                                String timeOfResult = columnResult.innerText();
                                                
                                                //convert string to date
                                                Date rowDate = new SimpleDateFormat("MM/dd/yyyy").parse(timeOfResult);
                                                Date dateToCheckDate = new SimpleDateFormat(environment.getProperty("domain.util.date.only.format")).parse(dateToCheck);

                                                //LOG.log(Level.DEBUG, () -> "rowDate: " + rowDate + " | dateToCheck: " + dateToCheck);

                                                //get month, day, year of row and the dateToCheck then check if equal
                                                Calendar rowDateCalendar = util.dateToCalendar(rowDate, "");
                                                Calendar dateToCheckCalendar = util.dateToCalendar(dateToCheckDate, "");

                                                int rowYear = rowDateCalendar.get(Calendar.YEAR);
                                                int rowMonth = rowDateCalendar.get(Calendar.MONTH);
                                                int rowDayOfMonth = rowDateCalendar.get(Calendar.DAY_OF_MONTH);
                                                
                                                int yestYear = dateToCheckCalendar.get(Calendar.YEAR);
                                                int yestMonth = dateToCheckCalendar.get(Calendar.MONTH);
                                                int yestDayOfMonth = dateToCheckCalendar.get(Calendar.DAY_OF_MONTH);
                                                
                                                //LOG.log(Level.DEBUG, () -> "row: " + rowYear + "-" + rowMonth + "-" + rowDayOfMonth + " | yest: " + yestYear + "-" + yestMonth + "-" + yestDayOfMonth);

                                                if ((rowYear == yestYear) && (rowMonth == yestMonth) && (rowDayOfMonth == yestDayOfMonth)){

                                                    if (ctrResults >= 1){
                                                        firstVisitFlag = 1;
                                                    }

                                                    columnResult.click();

                                                    if (getArgument1(page, searchResults, firstVisitFlag) == true){
                                                        ctrResults++;
                                                        //proceed to next
                                                    }

                                                }
                                                else{
                                                    //skip row
                                                }
                                            }
                                        }
                                        
                                    }
                                    else{
                                        LOG.log(Level.DEBUG, () -> "column results are empty. . .");
                                    }
                                }

                            }
                            else{
                                LOG.log(Level.DEBUG, () -> "row results are empty. . .");
                            }

                        }

                    }
                    else{
                        LOG.log(Level.DEBUG, () -> "table results are empty. . .");
                    }

                }
                else{
                    LOG.log(Level.DEBUG, () -> "search results are empty. . .");
                }

                if (ctrResults != bankStatementsDTOs.size()){
                    List<String> tokenizedAccounts = Arrays.asList(environment.getProperty("bank.paids.tokenized.accounts", String[].class));  
                    tokenizedAccounts = new ArrayList<String>(tokenizedAccounts);

                    //get only expected bank accounts based on the query
                    List<String> accountsFromQuery = new ArrayList<>();
                    for (BankStatementsDTO bankStatementsDTO : bankStatementsDTOs){
                        String bankAccount = StringUtils.substringBetween(bankStatementsDTO.getFileName(), "#", "*");
                        accountsFromQuery.add(bankAccount);
                    }

                    //compare lists
                    accountsFromQuery.removeAll(bankStatementsAccountsFound); //what will remain will be the bank accounts not found

                    if (accountsFromQuery.size() > 0){
                        String body = "<body>Hello, <br><br>";
                                    
                        body += "FYI, the Autoreconcile job has not been triggered for the following statements:<br>";
                        body += "<ul style='list-style-type:disc'>";

                            for (String tokenizedAccount: accountsFromQuery){

                                String detokenizedAccount = environment.getProperty("token." + tokenizedAccount);
                                body += "<li>" + detokenizedAccount + "</li>";
                            }

                        body += "</ul>";

                        body += "<br><br>" +
                                "Thanks, <br>" + 
                                "OrCa IT Team</body>";

                        String dayOnlyTimestamp = util.toDateString(new Date(), environment.getProperty("domain.util.date.month.date.only.format"), environment.getProperty("q2c.timezone"));

                        q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                                    environment.getProperty("mail.argus.cc", String[].class), 
                                    environment.getProperty("inbound.bank.paids.bank.accounts.not.found.subject"),
                                    body, dayOnlyTimestamp);
                    }
                }
                else{
                    // send email to developers that Q2C bank paids autoreconcile bank statements is done
                    String body = "<body>Hello, <br><br>";
                                
                    body += "Q2C Inbound Bank Paids (Autoreconcile Bank Statements) process run is complete.";

                    body += "<br><br>" +
                            "Thanks & Regards, <br>" + 
                            "Bot</body>";

                    q2cCommonService.sendEmail(environment.getProperty("mail.argus.recipients", String[].class), environment.getProperty("mail.argus.cc", String[].class), environment.getProperty("inbound.bank.paids.bank.paids.autoreconcile.complete"), body, execTimestamp);

                }
            }

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in bankPaidsErpReadBankMain method. . .");
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end bankPaidsErpReadBankMain method. . .");
    }

    @Override
    public List<ElementHandle> searchAutoReconcileBankStatements(Page page){
        LOG.log(Level.DEBUG, () -> "start searchAutoReconcileBankStatements method. . .");

        try {
            domainOracleService.expandSearchButton(page);

            playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:value00'").fill("Autoreconcile Bank Statements");
            playwrightService.pageLocatorWait(page, "select", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:operator3'").selectOption("BEFORE");
            playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:value50'").fill("oe00fi");
            TimeUnit.SECONDS.sleep(7);

            playwrightService.pageLocatorWait(page, "", "", "button:has-text(\"Search\")").click();
            TimeUnit.SECONDS.sleep(60);

            List<ElementHandle> finalSearchResults = new ArrayList<>();

            List<ElementHandle> searchResults = page.querySelectorAll("span.x2hg");
            if (!searchResults.isEmpty()){ 
                for (ElementHandle searchResult : searchResults){

                    if (searchResult.innerText().contains("Autoreconcile Bank Statements")){
                        finalSearchResults.add(searchResult);
                    }
                }

                return finalSearchResults;
            }
            else{
                LOG.log(Level.INFO, () -> "No results found after searching for Autoreconcile Bank Statements in Oracle ERP . . .");
                return null;
            }

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in searchAutoReconcileBankStatements method. . .");
            LOG.error(e);
            return null;
        }
    }

    @Override
    public boolean getArgument1(Page page, List<ElementHandle> searchResults, int firstVisitFlag){
        LOG.log(Level.DEBUG, () -> "start getArgument1 method. . .");

        try {
            TimeUnit.SECONDS.sleep(3);

            if (firstVisitFlag == 0){ // open up parameters part of screen
                playwrightService.pageClick(page, "a", "id", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:processDetails:processDetails:phprmsid2::_afrDscl'");
                TimeUnit.SECONDS.sleep(60);
                playwrightService.pageClick(page, "a", "id", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:processDetails:processDetails:phprmsid3::_afrDscl'");   
                TimeUnit.SECONDS.sleep(60);
            }

            List<ElementHandle> argument1RowResults = page.querySelectorAll("tr");
            if (!argument1RowResults.isEmpty()){ 
                for (ElementHandle argument1RowResult : argument1RowResults){
                    
                    List<ElementHandle> argument1LabelResults = argument1RowResult.querySelectorAll("td.x15.x4z");
                    if (!argument1LabelResults.isEmpty()){ 
                        for (ElementHandle argument1LabelResult : argument1LabelResults){
                            
                            if (argument1LabelResult.innerText().contains("argument1")){
                                List<ElementHandle> argument1Results = argument1RowResult.querySelectorAll("td.x18u.xk7");
                                
                                if (!argument1Results.isEmpty()){ 
                                    for (ElementHandle argument1Result : argument1Results){

                                        String bankNum = "";
                                        bankNum = argument1Result.innerText().replaceAll("[^0-9]", "");

                                        if (bankNum.length() == 10){ //bank account number is 10 digits
                                            String[] tokenizedAccounts = environment.getProperty("bank.paids.tokenized.accounts", String[].class);

                                            for (String tokenizedAccount: tokenizedAccounts){
                                                if (bankNum.equalsIgnoreCase(environment.getProperty("token." + tokenizedAccount))){ //check if bank account is in list
                                                    bankStatementsAccountsFound.add(tokenizedAccount);
                                                    return true;
                                                }
                                            }
                                        }
                                        else{
                                            // argument1 is not 10 digits
                                        }
                                    }
                                }
                                else{
                                    // argument1 not found
                                    String body = "<body>Hello, <br><br>";
                        
                                    body += "Bank account number cannot be found upon checking Autoreconcile Bank Statements in Oracle ERP.";
                    
                                    body += "<br><br>" +
                                            "Thanks & Regards, <br>" + 
                                            "Bot</body>";
                    
                                    q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                                                environment.getProperty("mail.argus.cc", String[].class), 
                                                environment.getProperty("inbound.bank.paids.argument1.not.found.subject"),
                                                body, execTimestamp);
                                }
                                
                            }
                        }
                    }
                }
            }

            

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in getArgument1 method. . .");
            LOG.error(e);
            return false;
        }

        return false;
    }

    @Override
    public void bankPaidsErpMain(List<BankStatementsDTO> bankStatementsDTOs, String execTimestamp){
        LOG.log(Level.DEBUG, () -> "start bankPaidsErpMain method. . .");
        
        this.execTimestamp = execTimestamp;
        
        final List<BankStatementsDTO> finalBankStatementsDTOs = bankStatementsDTOs;

        // From this point, files to process will run in parallel
        for (int j = 0; j < bankStatementsDTOs.size();){

            IntStream.range(j, bankStatementsDTOs.size()).parallel().limit(2).forEach(i->{
                
                Browser browser = null;
                BrowserContext browserContext = null;

                try {
                    browser = playwrightService.openBrowser();
                    browserContext = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
            
                    Page page = domainOracleService.navigateScheduledProcesses(browser, browserContext);

                    TimeUnit.SECONDS.sleep(3);

                    String detokenizedAccount = environment.getProperty("token." + StringUtils.substringBetween(finalBankStatementsDTOs.get(i).getFileName(), "#", "*"));

                    if (searchIdInScheduledProcesses(page, finalBankStatementsDTOs.get(i).getJobId()) == true){
                        String childProcessId = getAttachmentContent(page, finalBankStatementsDTOs.get(i).getJobId(), "waiting on child process with request id", finalBankStatementsDTOs.get(i).getAlternateDocumentId());

                        if (StringUtils.isNotBlank(childProcessId)){
                            Page page2 = domainOracleService.navigateScheduledProcesses(browser, browserContext);
                            
                            TimeUnit.SECONDS.sleep(3);

                            if (searchIdInScheduledProcesses(page2, childProcessId) == true){
                                String rowsSuccessful = getAttachmentContent(page2, childProcessId, "successfully loaded", finalBankStatementsDTOs.get(i).getAlternateDocumentId());

                                if (StringUtils.isNotBlank(rowsSuccessful)){
                                    int numOfRows = Integer.valueOf(rowsSuccessful);

                                    if (numOfRows == finalBankStatementsDTOs.get(i).getCount()){
                                        // Successful, no extra notification needed    

                                        // send email to developers that Q2C bank paids autoreconcile bank statements is done
                                        String body = "<body>Hello, <br><br>";
                                    
                                        body += "Q2C Inbound Bank Paids process run is complete for Job ID " + finalBankStatementsDTOs.get(i).getJobId() + ".";

                                        body += "<br><br>" +
                                                "Thanks & Regards, <br>" + 
                                                "Bot</body>";

                                        q2cCommonService.sendEmail(environment.getProperty("mail.argus.recipients", String[].class), environment.getProperty("mail.argus.cc", String[].class), environment.getProperty("inbound.bank.paids.bank.paids.job.complete"), body, execTimestamp);

                                    }
                                    else{
                                        String body = "<body>Hello, <br><br>";
                                        String subject = environment.getProperty("inbound.bank.paids.count.not.equal.subject");
            
                                        body += "The number of rows successfully loaded is not equal to the count in the query to staging table. See below:";
                                        body += "<br>";
                                        body += "Job ID: " + finalBankStatementsDTOs.get(i).getJobId() + "<br>";

                                        if (finalBankStatementsDTOs.get(i).getAlternateDocumentId().contains("EAS_CB_MO_")){
                                            body += "Alt Doc ID: " + finalBankStatementsDTOs.get(i).getAlternateDocumentId() + "<br>";
                                            subject = subject.replace("Bank Paids", "EAS CB MO ");
                                        }
                                        else{
                                            body += "Bank Account No.: " + detokenizedAccount + "<br>";
                                        }

                                        body += "Count from Query: " + finalBankStatementsDTOs.get(i).getCount() + "<br>";
                                        body += "Child Process ID: " + childProcessId + "<br>";
                                        body += "Number of Rows Successfully Loaded: " + numOfRows;
        
                                        body += "<br><br>" +
                                                "Thanks & Regards, <br>" + 
                                                "Bot</body>";
        
                                        q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                                                    environment.getProperty("mail.argus.cc", String[].class), 
                                                    subject, body, execTimestamp);
                                    }
                                }
                                else{
                                    String body = "<body>Hello, <br><br>";
                                    String subject = environment.getProperty("inbound.bank.paids.download.failure.subject");
        
                                    if (finalBankStatementsDTOs.get(i).getAlternateDocumentId().contains("EAS_CB_MO_")){
                                        body += "The number of rows successfully loaded is not indicated in the attachment for child process ID " + childProcessId + " (alt doc ID: " + finalBankStatementsDTOs.get(i).getAlternateDocumentId() + ").";
                                        subject = subject.replace("Bank Paids", "EAS CB MO");
                                    }
                                    else{
                                        body += "The number of rows successfully loaded is not indicated in the attachment for child process ID " + childProcessId + " (bank account number: " + detokenizedAccount + ").";

                                    }

                                    body += "<br><br>" +
                                            "Thanks & Regards, <br>" + 
                                            "Bot</body>";

                                    q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                                                environment.getProperty("mail.argus.cc", String[].class), 
                                                subject, body, execTimestamp);
                                }
                                
                            }

                        }
                        else{
                            String body = "<body>Hello, <br><br>";
                            String subject = environment.getProperty("inbound.bank.paids.child.process.id.not.found.subject");
        
                            if (finalBankStatementsDTOs.get(i).getAlternateDocumentId().contains("EAS_CB_MO_")){
                                body += "Child process ID was not found for job ID " + finalBankStatementsDTOs.get(i).getJobId() + " (alt doc ID: " + finalBankStatementsDTOs.get(i).getAlternateDocumentId() + ").";
                                subject = subject.replace("Bank Paids", "EAS CB MO");
                            }
                            else{
                                body += "Child process ID was not found for job ID " + finalBankStatementsDTOs.get(i).getJobId() + " (bank account number: " + detokenizedAccount + ").";
                            }

                            body += "<br><br>" +
                                    "Thanks & Regards, <br>" + 
                                    "Bot</body>";

                            q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                                        environment.getProperty("mail.argus.cc", String[].class), 
                                        subject, body, execTimestamp);
                        }

                    }
                    else{
                        String body = "<body>Hello, <br><br>";
                        String subject = environment.getProperty("inbound.bank.paids.job.id.not.found.subject");
                        
                        if (finalBankStatementsDTOs.get(i).getAlternateDocumentId().contains("EAS_CB_MO_")){
                            body += "Job ID " + finalBankStatementsDTOs.get(i).getJobId() + " (alt doc ID: " + finalBankStatementsDTOs.get(i).getAlternateDocumentId() + ") was not found in Oracle ERP.";
                            subject = subject.replace("Bank Paids", "EAS CB MO");
                        }
                        else{
                            body += "Job ID " + finalBankStatementsDTOs.get(i).getJobId() + " (bank account number: " + detokenizedAccount + ") was not found in Oracle ERP.";
                        }

                        body += "<br><br>" +
                                "Thanks & Regards, <br>" + 
                                "Bot</body>";

                        q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                                    environment.getProperty("mail.argus.cc", String[].class), 
                                    subject, body, execTimestamp);
                    }
        
                } catch (Exception e){
                    LOG.log(Level.DEBUG, () -> "error in bankPaidsErpMain method. . .");
                    LOG.error(e);
                } finally {
                    browserContext.close();
                    browser.close();
                }
                    
            });

            //adjust the concurrent executions (limited to 2 at a time)
            if ((bankStatementsDTOs.size() - j) % 2 == 0){ 
                j += 2;
            }
            else{
                j++;
            }

        }   

        LOG.log(Level.DEBUG, () -> "end bankPaidsErpMain method. . .");
    }

    @Override
    public boolean searchIdInScheduledProcesses(Page page, String jobId){
        LOG.log(Level.DEBUG, () -> "start searchIdInScheduledProcesses method. . .");

        try {
            domainOracleService.expandSearchButton(page);

            playwrightService.pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:value10'").fill(jobId);
            playwrightService.pageLocatorWait(page, "select", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:operator3'").selectOption("BEFORE");
            
            TimeUnit.SECONDS.sleep(7);

            playwrightService.pageLocatorWait(page, "button", "id", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl::search'").click();

            TimeUnit.SECONDS.sleep(10);

            List<ElementHandle> searchResults = page.querySelectorAll("span:has-text(\"" + jobId + "\")");

            if (!searchResults.isEmpty()){ 
                return true;
            }
            else{
                LOG.log(Level.INFO, () -> "No results found after searching Job ID " + jobId + " in Oracle ERP. . .");
                return false;
            }

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in searchIdInScheduledProcesses method. . .");
            LOG.error(e);
            return false;
        }
    }

    @Override
    public String getAttachmentContent(Page page, String filename, String textToLookFor, String altDocId){
        LOG.log(Level.DEBUG, () -> "start getAttachmentContent method. . .");

        try {                           
            playwrightService.pageClick(page, "", "", "span:has-text(\"" + filename + "\")");
            TimeUnit.SECONDS.sleep(10);

            Download download = null;

            if (textToLookFor.contains("waiting on child process with request id")){
                playwrightService.pageLocatorWait(page, "span", "id", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:processDetails:processDetails:attachment1:pglMore'").click();

                download = page.waitForDownload(() -> {
                    page.click("text=" + filename + ".log");
                });
            }
            else if (textToLookFor.contains("successfully loaded")){            
                download = page.waitForDownload(() -> {
                    page.click("text=ESS_L_" + filename);
                });
            }

            download.saveAs(Paths.get(environment.getProperty("q2c.downloads.folder") + filename + ".log"));
            TimeUnit.SECONDS.sleep(5);
            return readAttachment(page, environment.getProperty("q2c.downloads.folder") + filename + ".log", filename, textToLookFor, altDocId);
            
        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in getAttachmentContent method. . .");
            LOG.error(e);
            return null;
        }

    }

    @Override
    public String readAttachment(Page page, String filePath, String filename, String textToLookFor, String altDocId){
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
    
                    if (text.toLowerCase().contains(textToLookFor)){

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

                q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
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
