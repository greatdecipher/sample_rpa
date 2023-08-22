package com.albertsons.argus.q2c.service.impl;

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
import com.albertsons.argus.domaindbq2c.dto.JobHistoryDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbq2c.exception.OracleServiceException;
import com.albertsons.argus.domaindbq2c.service.DbOracleService;
import com.albertsons.argus.q2c.common.service.Q2CCommonService;
import com.albertsons.argus.q2c.exception.Q2CCommonException;
import com.albertsons.argus.q2c.exception.Q2CEmployeePayrollException;
import com.albertsons.argus.q2c.exception.Q2cInsuranceArException;
import com.albertsons.argus.q2c.inbound.invoices.service.ArInvoicesService;
import com.albertsons.argus.q2c.service.EmployeePayrollService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class EmployeePayrollServiceImpl implements EmployeePayrollService {
    private static final Logger LOG = LogManager.getLogger(EmployeePayrollServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private DbOracleService dbOracleService;

    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;

    @Autowired
    private Q2CCommonService q2cCommonService;

    @Autowired
    private OracleService domainOracleService;

    @Autowired
    private ArInvoicesService arInvoicesService;

    @Override
    public void employeePayroll(String fileName, String execTs) throws Q2CEmployeePayrollException {
        String queryFileName = fileName + "%";
        int intialCount = 0;

        List<ProcessInstanceDTO> getProcessInstanceDTOList = processInstance(queryFileName, intialCount,execTs);
        jobStatusTxn(getProcessInstanceDTOList,execTs);

        // q2cCommonService.

    }

    private void jobStatusTxn(List<ProcessInstanceDTO> getProcessInstanceDTOList, String execTS) throws Q2CEmployeePayrollException {
        List<JobHistoryDTO> jobHistoryDTOList = new ArrayList<>();

        for (ProcessInstanceDTO processInstanceDTO : getProcessInstanceDTOList) {

            try {
                jobHistoryDTOList = dbOracleService.getJobHistoryTrxsDTOs(processInstanceDTO.getBatchName());
            } catch (OracleServiceException e) {
                throw new Q2CEmployeePayrollException(
                        "Retrive Error for getJobHistoryTrxsDTOs service. . .");
            }

            if (!jobHistoryDTOList.isEmpty()) {
                for (JobHistoryDTO jobHistoryDTO : jobHistoryDTOList) {
                    boolean isJobHistory = isJobHistoryTrxtn(jobHistoryDTO);

                    if (isJobHistory) {
                            employeePayrollBrowser(jobHistoryDTO,processInstanceDTO,execTS);
                    } else {
                        sendEmpPayrollVendorFailed("Job History",
                                "File of " + jobHistoryDTO.getAlternateDocumentId()
                                        + " - Maximum count of retry exceeded for jobStatusTxn . . .");
                        throw new Q2CEmployeePayrollException(
                                "Maximum count of retry exceeded for jobStatusTxn service. . .");
                    }
                }
            }

        }
    }

    private List<ProcessInstanceDTO> processInstance(String fileName, int countRetry, String dateStr)
            throws Q2CEmployeePayrollException {
        List<ProcessInstanceDTO> processInstanceDTOList = new ArrayList<>();
        List<ProcessInstanceDTO> newProcessInstanceDTOList = new ArrayList<>();
        try {
            processInstanceDTOList = arInvoicesService.getProcessInstance(fileName,dateStr);
        } catch (Q2cInsuranceArException e) {
            throw new Q2CEmployeePayrollException(e.getMessage());
        }

        int numberOfRetry = noOfRetries();

        for (ProcessInstanceDTO dto : processInstanceDTOList) {
            String interfaceId = StringUtils.isNotBlank(dto.getInterfaceId()) ? dto.getInterfaceId() : "";

            if (!interfaceId.equalsIgnoreCase("INT30")) {
                LOG.log(Level.DEBUG, () -> "processInstance interface id: " + interfaceId);
                continue;
            }

            boolean isProcessInstance = isProcessInstanceTrSuccess(dto);

            if (!isProcessInstance) {
                if (countRetry < numberOfRetry) {
                    try {
                        delayTimer();
                    } catch (Q2cInsuranceArException e) {
                        throw new Q2CEmployeePayrollException(e.getMessage());
                    }
                    processInstance(fileName, ++countRetry, dateStr);
                } else {
                    sendEmpPayrollVendorFailed("Process Instance",
                            "File of " + fileName + " - Maximum count of retry exceeded for processInstance . . .");
                    throw new Q2CEmployeePayrollException(
                            "Maximum count of retry exceeded for processInstance service. . .");
                }
            }
            newProcessInstanceDTOList.add(dto);
            break;
        }

        return newProcessInstanceDTOList;
    }

    public boolean isProcessInstanceTrSuccess(ProcessInstanceDTO processInstanceDTO) {
        if (processInstanceDTO.getTransformationStatus().equalsIgnoreCase(PROCESS_INSTANCE_TRANSFROMATION_STS_VALUE)
                && (StringUtils.isBlank(processInstanceDTO.getStatus())
                        || processInstanceDTO.getStatus().equalsIgnoreCase("SUCCESS")
                        || processInstanceDTO.getStatus().equalsIgnoreCase("PROCESSED")))
            return true;

        return false;
    }

    private int noOfRetries() {
        return Integer.valueOf(environment.getProperty("inbound.employee.payroll.number.retry"));
    }

    private void delayTimer() throws Q2cInsuranceArException {
        try {
            LOG.log(Level.INFO, () -> "delayTimer will commence. . .");
            q2cCommonService
                    .delayTimer(Integer.valueOf(environment.getProperty("inbound.employee.payroll.timeout.seconds")));
        } catch (Q2CCommonException e) {
            LOG.error(e);
            throw new Q2cInsuranceArException("There is something wrong on delayTimer method. " + e.getMessage());
        }
    }

    private void sendEmpPayrollVendorFailed(String subjectType, String message) {
        AutomationUtil util = new AutomationUtil();
        Date dateNow = new Date();

        String execTimestamp = util.toDateString(dateNow, environment.getProperty("domain.util.date.format"),
                environment.getProperty("q2c.timezone"));

        q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class),
                environment.getProperty("mail.argus.cc", String[].class), "Q2C - Employee Payroll Failed -" + subjectType,
                message, execTimestamp);

    }

    private void employeePayrollBrowser(JobHistoryDTO dto, ProcessInstanceDTO processInstanceDTOs, String execTimestamp) throws Q2CEmployeePayrollException {
        Browser browser = null;
        try {
            browser = playwrightAutomationService.openBrowser();
            BrowserContext browserContext = browser
                    .newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));

            Page page = domainOracleService.navigateScheduledProcesses(browser, browserContext);

            TimeUnit.SECONDS.sleep(3);

            boolean isEmpPayroll = isSearchEmployeePayroll(page, dto.getJobId());

            if (!isEmpPayroll) {
                String body = "<body>Hello, <br><br>";

                body += "In AR Invoices, is not SUCCEEDED. See below:";
                body += "<br>";
                body += "Job ID: " + dto.getJobId() + "<br>";
                body += "<br><br>" +
                        "Thanks & Regards, <br>" +
                        "Bot</body>";

                sendEmpPayrollVendorFailed("JobId is not SUCCEEDED", body);
            } else {
                String countProcessInstanceStr = processInstanceDTOs.getAttribute2();
                int countProcessInstance = 0;
                try{
                    countProcessInstance = Integer.valueOf(countProcessInstanceStr); 
                }catch(NumberFormatException nfe){
                    LOG.error(nfe.getMessage());
                    throw new Q2CEmployeePayrollException("count is not a number.");
                }

                String childProcessId = q2cCommonService.getAttachmentContent(page, dto.getJobId(),
                        "ImportExternalTransactions with request ID", dto.getAlternateDocumentId(), execTimestamp);

                if (StringUtils.isNotBlank(childProcessId)) {
                    Page page2 = domainOracleService.navigateScheduledProcesses(browser, browserContext);

                    TimeUnit.SECONDS.sleep(3);

                    if (isSearchEmployeePayroll(page2, childProcessId)) {
                        String rowsSuccessful = q2cCommonService.getAttachmentContent(page2, childProcessId,
                                "rows were successfully imported", dto.getAlternateDocumentId(), execTimestamp);

                        if (StringUtils.isNotBlank(rowsSuccessful)) {
                            int numOfRows = Integer.valueOf(rowsSuccessful);

                            if (numOfRows == countProcessInstance) {
                                // send email to developers that Q2C - EMP Payroll
                                String body = "<body>Hello, <br><br>";
                                body += "Q2C Inbound Employee Payroll process run is complete for Job ID " + dto.getJobId()
                                        + ".";

                                sendEmpPayrollSuccessInternal("- Count and Number of Successful Rows are Equal", body, execTimestamp);
                            } else {
                                String body = "<body>Hello, <br><br>";

                                body += "The number of rows successfully loaded is not equal to the count in the query to staging table. See below:";
                                body += "<br>";
                                body += "Job ID: " + dto.getJobId() + "<br>";

                                body += "Alt Doc ID: " + dto.getAlternateDocumentId() + "<br>";
                                    // subject = subject.replace("Bank Paids", "EAS CB MO ");


                                body += "Count from Query: " + countProcessInstance + "<br>";
                                body += "Child Process ID: " + childProcessId + "<br>";
                                body += "Number of Rows Successfully Loaded: " + numOfRows;

                                sendEmpPayrollVendorFailed("Count and Number of Successful Rows Not Equal", body);
                            }
                        }else{
                            String body = "<body>Hello, <br><br>";
                                    // String subject = environment.getProperty("inbound.bank.paids.download.failure.subject");
        
                                    body += "The number of rows successfully loaded is not indicated in the attachment for child process ID " + childProcessId + " (alt doc ID: " + dto.getAlternateDocumentId() + ").";

                                    sendEmpPayrollVendorFailed("Download Attachment", body);
                        }
                    }
                }
            }
        } catch (DomainException | InterruptedException e) {
            LOG.error("DomainException | InterruptedException : "+e);
            throw new Q2CEmployeePayrollException("arInvoicesBrowser DomainException | InterruptedException: " + e.getMessage());
        } catch (Q2CCommonException e) {
            LOG.error("Q2CCommonException : "+e);
            throw new Q2CEmployeePayrollException("arInvoicesBrowser Q2CCommonException: " + e.getMessage());
        } finally {
            LOG.log(Level.DEBUG, () -> "closed browser. . .");
            // Closed Broswer
            playwrightAutomationService.closedBrowser(browser);
        }

    }

    private void sendEmpPayrollSuccessInternal(String subject, String body, String execTimestamp){
        q2cCommonService.sendEmail(
            environment.getProperty("mail.argus.recipients", String[].class),
            environment.getProperty("mail.argus.cc", String[].class),
            "Q2C - Employee Payroll Suceess -"+subject, body,
            execTimestamp);
    }

    private boolean isSearchEmployeePayroll(Page page, String jobId) {
        try {
            // Expand for Search
            List<ElementHandle> expandSearchButtons = page.querySelectorAll("td.xhs");
            if (!expandSearchButtons.isEmpty()) {
                playwrightAutomationService.pageClick(page, "", "", "td.xhs");
            }
            // Process ID Text Box
            playwrightAutomationService
                    .pageLocatorWait(page, "input", "name", "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:value10'")
                    .fill(jobId);
            // Date Select Box
            playwrightAutomationService.pageLocatorWait(page, "select", "name",
                    "'_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:operator3'").selectOption("BEFORE");

            TimeUnit.SECONDS.sleep(7);

            // Click Search button
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

            return false;
        }
    }

    @Override
    public List<JobHistoryDTO> getJobHistoryTxnDto(String fileName) throws Q2CEmployeePayrollException {
        List<JobHistoryDTO> jobHistoryDTOs = new ArrayList<>();
        try {
            jobHistoryDTOs = dbOracleService.getJobHistoryTrxsDTOs(fileName);
        } catch (OracleServiceException e) {
            LOG.error(e);
            throw new Q2CEmployeePayrollException("getProcessFileDetail excetion: " + e.getMessage());
        }

        return jobHistoryDTOs;
    }

    @Override
    public boolean isJobHistoryTrxtn(JobHistoryDTO jobHistoryDTO) {
        if (jobHistoryDTO.getJobName().equalsIgnoreCase(JOB_NAME_VALUE) &&
                jobHistoryDTO.getJobStatus().equalsIgnoreCase(JOB_STS_VALUE))
            return true;

        return false;
    }

}
