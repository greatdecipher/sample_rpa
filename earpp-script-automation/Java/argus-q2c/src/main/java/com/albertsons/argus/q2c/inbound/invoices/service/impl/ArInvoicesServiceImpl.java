package com.albertsons.argus.q2c.inbound.invoices.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.oracle.service.OracleService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.domaindbq2c.dto.InterfaceLinesDTO;
import com.albertsons.argus.domaindbq2c.dto.JobHistoryDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbq2c.exception.OracleServiceException;
import com.albertsons.argus.domaindbq2c.service.DbOracleService;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.q2c.common.service.Q2CCommonService;
import com.albertsons.argus.q2c.exception.Q2CCommonException;
import com.albertsons.argus.q2c.exception.Q2cInsuranceArException;
import com.albertsons.argus.q2c.inbound.invoices.service.ArInvoicesService;
import com.albertsons.argus.q2c.util.EmailContentTemplateUtil;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

/**
 * @author kbuen03
 * @since 01/25/23
 * @version 1.0
 * 
 */
@Service
public class ArInvoicesServiceImpl implements ArInvoicesService {
    private static final Logger LOG = LogManager.getLogger(ArInvoicesServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private DbOracleService dbOracleService;

    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OracleService domainOracleService;

    @Autowired
    private Q2CCommonService q2cCommonService;

    @Override
    public void processArInvoices(String fileName, String dateStr) throws Q2cInsuranceArException {
        String queryFileName = fileName + dateStr + "%";
        int intialCount = 0;

        List<ProcessInstanceDTO> getProcessInstanceDTOList = processInstance(queryFileName, intialCount,dateStr);
        List<String> batchNameList = getBatchNameList(getProcessInstanceDTOList);

        processFileDetails(queryFileName, intialCount);
        interfaceLines(batchNameList, intialCount);
        List<JobHistoryDTO> jobHistoryDTOList = jobHistory(batchNameList, intialCount);
        LOG.log(Level.INFO, () -> "jobHistoryDTOList no. records - "+jobHistoryDTOList.size());
        if(jobHistoryDTOList.size()!=0){
            arInvoicesBrowser(jobHistoryDTOList);
            //Get CMM count and compare - Disable as per Corpserv Team
            // compareCMMCounts(getProcessInstanceDTOList, queryFileName, dateStr);
    
            sendTeamEmailArInvoicesSuccess(fileName+": Completed", "");
        }else{
            //Get CMM count and compare - Disable as per Corpserv Team
            // compareCMMCounts(getProcessInstanceDTOList, queryFileName, dateStr);
                
            sendVendorEmailArInvoicesSuccess(fileName+": Completed", "AutoCompleteInvoiceImportEss not found on Jobhistory table.");
        }
    }

    @Override
    public List<ProcessInstanceDTO> getProcessInstance(String fileName, String dateStr) throws Q2cInsuranceArException {
        List<ProcessInstanceDTO> instanceDTOList = new ArrayList<>();

        try {
            instanceDTOList = dbOracleService.getProcessInstanceByDescDTOs(fileName);
        } catch (OracleServiceException e) {
            throw new Q2cInsuranceArException("getProcessInstanceDTOs excetion: " + e.getMessage());
        }

        if (instanceDTOList.isEmpty() && instanceDTOList.size() == 0) {
            AutomationUtil util = new AutomationUtil();
            EmailContentTemplateUtil emailContentTemplateUtil = new EmailContentTemplateUtil();
            Date dateNow = new Date();
            
            String execTimestamp = util.toDateString(dateNow, environment.getProperty("domain.util.date.format"), environment.getProperty("q2c.timezone"));
            
            LOG.error("getProcessInstance exception - No batch file found.");
           
            sendVendorEmailArInvoicesCustom("No File Found - "+execTimestamp, emailContentTemplateUtil.noBatchFileContentArInv(fileName, dateStr));
            throw new Q2cInsuranceArException("No batch file found.");
        }
        return instanceDTOList;
    }

    @Override
    public boolean isProcessInstanceTrSuccess(ProcessInstanceDTO processInstanceDTO) {
        if (processInstanceDTO.getTransformationStatus().equalsIgnoreCase(PROCESS_INSTANCE_TRANSFROMATION_STS_VALUE)
                && processInstanceDTO.getStatus().equalsIgnoreCase(PROCESS_INSTACE_STS_VALUE))
            return true;

        return false;
    }

    @Override
    public List<ProcessFileDetailsDTO> getProcessFileDetail(String fileName) throws Q2cInsuranceArException {
        List<ProcessFileDetailsDTO> fileDetailsDtoList = new ArrayList<>();

        try {
            fileDetailsDtoList = dbOracleService.getProcessFileDetailsDTOs(fileName);
        } catch (OracleServiceException e) {
            LOG.error(e);
            throw new Q2cInsuranceArException("getProcessFileDetail excetion: " + e.getMessage());
        }

        return fileDetailsDtoList;
    }

    @Override
    public boolean isProcessFileDetailsSuccess(ProcessFileDetailsDTO processFileDetailsDTO) {
        if (processFileDetailsDTO.getLoadStatus().equalsIgnoreCase(PROCSS_FILE_DTLS_LOAD_STS))
            return true;

        return false;
    }

    @Override
    public List<InterfaceLinesDTO> getInterfaceLine(List<String> fileNames) throws Q2cInsuranceArException {
        List<InterfaceLinesDTO> interfaceLinesDTOList = new ArrayList<>();

        try {
            interfaceLinesDTOList = dbOracleService.getInterfaceLinesDTOs(fileNames);
        } catch (OracleServiceException e) {
            LOG.error("getInterfaceLine exception: "+e);
            throw new Q2cInsuranceArException("getInterfaceLine exception: " + e.getMessage());
        }

        return interfaceLinesDTOList;
    }

    @Override
    public List<JobHistoryDTO> getJobHistoryInLines(List<String> filename) throws Q2cInsuranceArException {
        List<JobHistoryDTO> jobHistoryDTOs = new ArrayList<>();
        try {
            jobHistoryDTOs = dbOracleService.getJobHistoryInLinesDTOs(filename);
        } catch (OracleServiceException e) {
            LOG.error("getJobHistoryInLines exception: "+e);
            throw new Q2cInsuranceArException("getJobHistoryInLines exception: " + e.getMessage());
        }
        return jobHistoryDTOs;
    }

    @Override
    public boolean isInterfaceLineSuccess(InterfaceLinesDTO interfaceLinesDTO) {
        if (interfaceLinesDTO.getStatus().equalsIgnoreCase(INTERFACE_LNE_STS))
            return true;

        return false;
    }

    @Override
    public boolean isJobHistoryInLineSuccess(JobHistoryDTO jobHistoryDTO) {
        if (jobHistoryDTO.getJobStatus().equalsIgnoreCase(JOB_HSTY_LNE_JOB_STS))
            return true;

        return false;
    }

    private void compareCMMCounts(List<ProcessInstanceDTO> getProcessInstanceDTOList, String queryFileName, String dateStr) throws Q2cInsuranceArException{
        int cmmCountProcessInstanceDTOList = getCountProcessInstanceDTOList(getProcessInstanceDTOList);
        List<InterfaceLinesDTO> getCMMCountInterfaceList =  new ArrayList<>();
        
        try {
            getCMMCountInterfaceList = dbOracleService.getInterfaceLineCountDTOs(queryFileName);
        } catch (OracleServiceException e) {
            throw new Q2cInsuranceArException(e.getMessage());
        }

        int cmmCountInterface = getCountInterfaceDTO(getCMMCountInterfaceList);


        if(cmmCountProcessInstanceDTOList == cmmCountInterface){
            sendTeamEmailArInvoicesSuccess("CMM Count - "+queryFileName, "Cmm count is equal.");
        }else{
            AutomationUtil util = new AutomationUtil();
            EmailContentTemplateUtil emailContentTemplateUtil = new EmailContentTemplateUtil();
            Date dateNow = new Date();
            
            String execTimestamp = util.toDateString(dateNow, environment.getProperty("domain.util.date.format"), environment.getProperty("q2c.timezone"));

            // sendVendorEmailArInvoicesFailed("CMM count - "+queryFileName, "The cmm count is not equal. ");
            sendVendorEmailArInvoicesCustom("CMM Count Not Equal - "+execTimestamp, emailContentTemplateUtil.cmmNotEqualContentArInv(queryFileName, dateStr, cmmCountProcessInstanceDTOList, cmmCountInterface));
        }
    }

    private int getCountInterfaceDTO(List<InterfaceLinesDTO> getCMMCountInterfaceList) throws Q2cInsuranceArException{
        String countStr = "";
        int cmmCount = 0;

        for(InterfaceLinesDTO dto : getCMMCountInterfaceList){
            countStr = dto.getCount();

            break;
        }

        if(StringUtils.isNotBlank(countStr)){
            try{
                cmmCount = Integer.valueOf(countStr);
            }catch(NumberFormatException e){
                LOG.error("getCountInterfaceDTO exception: "+e);
                throw new Q2cInsuranceArException("InterfaceLines - count is not a number: "+e.getMessage());
            }
        }else{
            LOG.error("InterfaceLines Table count is blank.");
            throw new Q2cInsuranceArException("InterfaceLines Table count is blank.");
        }

        return cmmCount;
    }

    private int getCountProcessInstanceDTOList(List<ProcessInstanceDTO> getProcessInstanceDTOList) throws Q2cInsuranceArException{
        String attribute2Str = "";
        int cmmCount = 0;

        for(ProcessInstanceDTO dto : getProcessInstanceDTOList){
            attribute2Str = dto.getAttribute2();

            break;
        }

        if(StringUtils.isNotBlank(attribute2Str)){
            try{
                cmmCount = Integer.valueOf(attribute2Str);
            }catch(NumberFormatException e){
                LOG.error("getCountProcessInstanceDTOList exception: "+e);
                throw new Q2cInsuranceArException("ProcessInstance Table - attribute2Str is not a number: "+e.getMessage());
            }
        }else{
            LOG.error("ProcessInstance Table - attribute2Str is blank");
            throw new Q2cInsuranceArException("attribute2Str is blank");
        }

        return cmmCount;
    }

    private int noOfRetries(){
        return Integer.valueOf(environment.getProperty("inbound.invoice.ar.number.retry"));
    }

    private List<ProcessInstanceDTO> processInstance(String fileName, int countRetry, String dateStr) throws Q2cInsuranceArException {
        List<ProcessInstanceDTO> processInstanceDTOList = getProcessInstance(fileName,dateStr);
        int numberOfRetry = noOfRetries();
        
        for (ProcessInstanceDTO dto : processInstanceDTOList) {
            boolean isProcessInstance = isProcessInstanceTrSuccess(dto);

            if (!isProcessInstance) {
                if (countRetry < numberOfRetry) {
                    delayTimer();
                    processInstance(fileName, countRetry++,dateStr);
                } else {
                    LOG.error("Maximum count of retry exceeded for processInstance . . .");
                    sendVendorEmailArInvoicesFailed("Process Instance",
                            "File of " + fileName + " - Maximum count of retry exceeded for processInstance . . .");
                    throw new Q2cInsuranceArException(
                            "Maximum count of retry exceeded for processInstance service. . .");
                }
            }
        }

        return processInstanceDTOList;
    }

    private void processFileDetails(String fileName, int countRetry) throws Q2cInsuranceArException {
        List<ProcessFileDetailsDTO> processFileDetailsDTOList = getProcessFileDetail(fileName);
        int numberOfRetry = noOfRetries();

        for (ProcessFileDetailsDTO dto : processFileDetailsDTOList) {
            boolean isProcessFileDetail = isProcessFileDetailsSuccess(dto);

            if (!isProcessFileDetail) {
                if (countRetry < numberOfRetry) {
                    delayTimer();
                    processFileDetails(fileName, ++countRetry);
                } else {
                    LOG.error("Maximum count of retry exceeded for processFileDetails . . .");
                    sendVendorEmailArInvoicesFailed("Process File Details",
                            "File of " + fileName + " - Maximum count of retry exceeded for processFileDetails . . .");
                    throw new Q2cInsuranceArException(
                            "Maximum count of retry exceeded for processFileDetails service. . .");
                }
            }
        }
    }

    private void interfaceLines(List<String> fileNames, int countRetry) throws Q2cInsuranceArException {
        List<InterfaceLinesDTO> interfaceLinesDTOList = getInterfaceLine(fileNames);
        int numberOfRetry = noOfRetries();

        for (InterfaceLinesDTO dto : interfaceLinesDTOList) {
            boolean isInterfaceLineSuccess = isInterfaceLineSuccess(dto);

            if (!isInterfaceLineSuccess) {
                if (countRetry < numberOfRetry) {
                    delayTimer();
                    interfaceLines(fileNames, ++countRetry);
                } else {
                    LOG.error("Maximum count of retry exceeded for interfaceLines . . .");
                    sendVendorEmailArInvoicesFailed("Interface Line",
                            "Maximum count of retry exceeded for interfaceLines . . .");
                    throw new Q2cInsuranceArException(
                            "Maximum count of retry exceeded for interfaceLines service. . .");
                }
            }
        }
    }

    private List<JobHistoryDTO> jobHistory(List<String> fileNames, int countRetry) throws Q2cInsuranceArException {
        List<JobHistoryDTO> jobHistoryDTOList = getJobHistoryInLines(fileNames);
        List<JobHistoryDTO> newJobHistoryDTOList = jobHistoryFilterByAutoInvoices(jobHistoryDTOList);
        int numberOfRetry = noOfRetries();

        for (JobHistoryDTO dto : newJobHistoryDTOList) {
            boolean isJobHistory = isJobHistoryInLineSuccess(dto);
            
            if (!isJobHistory) {
                if (countRetry < numberOfRetry) {
                    delayTimer();
                    interfaceLines(fileNames, ++countRetry);
                } else {
                    LOG.error("Maximum count of retry exceeded for jobHistory . . .");
                    sendVendorEmailArInvoicesFailed("Job History", "Maximum count of retry exceeded for jobHistory . . .");
                    throw new Q2cInsuranceArException("Maximum count of retry exceeded for jobHistory service. . .");
                }
            }
        }

        return newJobHistoryDTOList;
    }

    private List<JobHistoryDTO> jobHistoryFilterByAutoInvoices(List<JobHistoryDTO> jobHistoryDTOList) throws Q2cInsuranceArException {
        List<JobHistoryDTO> newJobHistoryDTOList = new ArrayList<>();

        for(JobHistoryDTO dto : jobHistoryDTOList){
            if(dto.getJobName().toLowerCase().contains(AUTOINVOICEIMPORTS_CONST.toLowerCase())){
                newJobHistoryDTOList.add(dto);
            }
        }

        return newJobHistoryDTOList;
    }

    private void delayTimer() throws Q2cInsuranceArException {
        try {
            LOG.log(Level.INFO, () -> "delayTimer will commence. . .");
            q2cCommonService.delayTimer(Integer.valueOf(environment.getProperty("inbound.invoice.ar.timeout.seconds")));
        } catch (Q2CCommonException e) {
            LOG.error(e);
            throw new Q2cInsuranceArException("There is something wrong on delayTimer method. " + e.getMessage());
        }
    }

    private void sendTeamEmailArInvoicesFailed(String subjectType, String message) {
        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"),
                    environment.getProperty("mail.argus.recipients", String[].class),
                    environment.getProperty("mail.argus.cc", String[].class), "Q2C AR Invoices Failed - " + subjectType, message, 2,
                    false);
        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending ar invoices email. . .");
            LOG.error(e);
        }
    }

    private void sendVendorEmailArInvoicesFailed(String subjectType, String message) {
        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"),
                    environment.getProperty("mail.argus.orca.email", String[].class),
                    environment.getProperty("mail.argus.cc", String[].class), "Q2C - AR Invoices Failed - " + subjectType, message, 2,
                    true);
        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending ar invoices email. . .");
            LOG.error(e);
        }
    }

    private void sendVendorEmailArInvoicesCustom(String subjectType, String message) {
        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"),
                    environment.getProperty("mail.argus.orca.email", String[].class),
                    environment.getProperty("mail.argus.cc", String[].class), "Q2C - AR Invoices - " + subjectType, message, 2,
                    true);
        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending ar invoices email. . .");
            LOG.error(e);
        }
    }

    private void sendVendorEmailArInvoicesSuccess(String subjectType, String message) {
        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"),
                    environment.getProperty("mail.argus.orca.email", String[].class),
                    environment.getProperty("mail.argus.cc", String[].class), "Q2C - AR Invoices Success - " + subjectType, message, 2,
                    true);
        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending ar invoices email. . .");
            LOG.error(e);
        }
    }

    private void sendTeamEmailArInvoicesSuccess(String subjectType, String message) {
        try {
            emailService.sendSimpleMessage(environment.getProperty("mail.argus.from"),
                    environment.getProperty("mail.argus.from.alias"),
                    environment.getProperty("mail.argus.recipients", String[].class),
                    environment.getProperty("mail.argus.cc", String[].class), "Q2C AR Invoices Success - " + subjectType, message, 2,
                    false);
        } catch (ArgusMailException e) {
            LOG.log(Level.INFO, () -> "error sending ar invoices email. . .");
            LOG.error(e);
        }
    }

    private List<String> getBatchNameList(List<ProcessInstanceDTO> processInstanceDTOList) {
        List<String> batchNameList = new ArrayList<>();
        for (ProcessInstanceDTO dto : processInstanceDTOList) {
            batchNameList.add(dto.getBatchName());
        }

        return batchNameList;
    }

    private void arInvoicesBrowser(List<JobHistoryDTO> jobHistoryDTOList) throws Q2cInsuranceArException {
        Browser browser = null;
        EmailContentTemplateUtil emailContentTemplateUtil = new EmailContentTemplateUtil();
        try {
            browser = playwrightAutomationService.openBrowser();
            BrowserContext browserContext = browser
                    .newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));

            for (JobHistoryDTO dto : jobHistoryDTOList) {
                Page page = domainOracleService.navigateScheduledProcesses(browser, browserContext);

                TimeUnit.SECONDS.sleep(3);

                boolean isSearchProcessArInvoices = isSearchProcessArInvoices(page, dto.getJobId());

                if (!isSearchProcessArInvoices) {
                    String body = emailContentTemplateUtil.notSucceededArInv(dto.getJobId());

                    sendVendorEmailArInvoicesFailed(dto.getAlternateDocumentId()+" - JobId is not SUCCEEDED", body);
                }

            }
        } catch (DomainException | InterruptedException e) {
            LOG.error(e);
            throw new Q2cInsuranceArException("arInvoicesBrowser exception: " + e.getMessage());
        } finally {
            LOG.log(Level.DEBUG, () -> "closed browser. . .");
            // Closed Broswer
            playwrightAutomationService.closedBrowser(browser);
        }

    }

    private boolean isSearchProcessArInvoices(Page page, String jobId) throws Q2cInsuranceArException {
        try {
            // Expand for Search
            List<ElementHandle> expandSearchButtons = page.querySelectorAll("td.xhs");
            if (!expandSearchButtons.isEmpty()) {
                playwrightAutomationService.pageClick(page, "", "", "td.xhs");
            }
            //Process ID Text Box
            playwrightAutomationService
                    .pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:value10'")
                    .fill(jobId);
            //Date Select Box        
            playwrightAutomationService.pageLocatorWait(page, "select", "name",
                    "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:operator3'").selectOption("BEFORE");

            TimeUnit.SECONDS.sleep(7);
            
            //Click Search button
            playwrightAutomationService
                    .pageLocatorWait(page, "button", "id", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl::search'")
                    .click();

            TimeUnit.SECONDS.sleep(10);

            List<ElementHandle> searchResults = page.querySelectorAll("span:has-text(\"" + jobId + "\")");
            List<ElementHandle> succeeded = page.querySelectorAll("span:has-text(\"Succeeded\")");

            if (!searchResults.isEmpty() && !succeeded.isEmpty()) {
                return true;
            } else {
                LOG.log(Level.INFO, () -> "No results found after searching Job ID " + jobId + " in Oracle ERP. . .");
                return false;
            }

        } catch (Exception e) {
            LOG.log(Level.DEBUG, () -> "error in isSearchProcessArInvoices method. . .");
            LOG.error(e);

           throw new Q2cInsuranceArException("isSearchProcessArInvoices exception: "+e.getMessage());
        }
    }

}
