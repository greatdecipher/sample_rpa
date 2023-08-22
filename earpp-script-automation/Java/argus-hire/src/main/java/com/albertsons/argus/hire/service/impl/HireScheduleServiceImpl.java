package com.albertsons.argus.hire.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.hire.exception.HireException;
import com.albertsons.argus.hire.model.Hire;
import com.albertsons.argus.hire.model.HireError;
import com.albertsons.argus.hire.service.HireScheduleTaskService;
import com.albertsons.argus.hire.service.HireService;
import com.albertsons.argus.hire.util.HireUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class HireScheduleServiceImpl implements HireScheduleTaskService {

    @Autowired
    private HireService hireService;
    @Autowired
    private Environment environment;
    @Autowired
    private MetricsWebService metricsWebService;

    @Override
    @Scheduled(cron = "${com.argus.hire.scheduled.task.cron.value}")
    public void runHireSchedule() {
        log.debug(() -> "runHireSchedule() started. . . . . . . . . .");
        AutomationUtil automationUtil = new AutomationUtil();

        List<String> getAllFilesExcel = new ArrayList<>();
        try {
            getAllFilesExcel = automationUtil
                    .getAllFileNameInDirectory(environment.getProperty("argus.hire.folder.path"));

            if (getAllFilesExcel.isEmpty()) {
                log.info("No file in a directory during this time . . . .");
            } else {
                String automationId = environment.getProperty("com.argus.hire.automation.id");
                String[] metricsArr = { "" };
                try {
                    ResponseStartSaveBO responseStartSaveBO = metricsWebService.startSave(automationId,
                            environment.getProperty("com.argus.hire.version.id"));

                    metricsArr[0] = responseStartSaveBO.getId();
                } catch (RestClientException e) {
                    log.error("Metrics Exception: " + e);
                }

                getAllFilesExcel.stream()
                        .filter(fileName -> fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))
                        .forEach(fileName -> {

                            processHireByExcel(fileName, metricsArr[0]);
                        });

                automationUtil.moveAllFiles(environment.getProperty("argus.hire.folder.path"),
                        environment.getProperty("argus.hire.folder.archive"));

                if (StringUtils.isNotBlank(metricsArr[0])) {
                    try {
                        metricsWebService.endSave(metricsArr[0]);
                    } catch (RestClientException e) {
                        log.error("Metrics Exception: " + e);
                    }
                }

            }
        } catch (SecurityException | IOException e) {
            log.error("runHireSchedule() exception: " + e);
        }
        log.info("HireRehire Application completed.");
        log.debug(() -> "runHireSchedule() end. . . . . . . . . .");
    }

    private void processHireByExcel(String fileName, String metircsId) {
        List<HireError> errorList = new ArrayList<>();
        List<Hire> getHireList = new ArrayList<>();
        List<String> empNoList = new ArrayList<>();

        HireError hireError = null;

        log.debug("Metircs id: " + metircsId);
        log.info("processHireByExcel() Filename: " + fileName);

        // Get Content from excel file
        try {
            getHireList = hireService.getHireContentList(fileName);
        } catch (HireException e) {
            log.error(e.getMessage());
            hireError = new HireError();
            hireError.setErrorMessage(e.getMessage());

            errorList.add(hireError);
        }

        if (errorList.isEmpty()) {
            // Skip the first row
            // Remove the blank rows
            getHireList.stream()
                    .skip(1)
                    .filter(hire -> StringUtils.isNotBlank(hire.getScenario()))
                    .forEach(hire -> {
                        log.debug(() -> "Scenario: " + hire.getScenario());

                        if (StringUtils.isNotBlank(metircsId)) {
                            try {
                                metricsWebService.incrementTransaction(metircsId, "1");
                            } catch (RestClientException e) {
                                log.error("processHireByExcel() inner Metric exception: " + e);
                            }
                        }

                        try {
                            hireService.hireRehireDate(hire.getScenario(), hire.getRequestNumber(),
                                    hire.getEmployeeNumber(),
                                    hire.getAssignmentNumber(), hire.getNewHireDate());

                            empNoList.add(hire.getEmployeeNumber());
                        } catch (HireException e) {
                            log.error(() -> "runHireSchedule exception: " + e.getMessage());
                            HireError hireError2 = new HireError();

                            hireError2.setEmployeeNumber(hire.getEmployeeNumber());
                            hireError2.setFileName(fileName);
                            hireError2.setScenario(hire.getScenario());
                            hireError2.setErrorMessage(e.getMessage());

                            errorList.add(hireError2);
                        }
                    });
        }

        if (!errorList.isEmpty()) {
            AutomationUtil automationUtil = new AutomationUtil();

            sendEmailError(errorList);

            // Cleanup in local the ss files
            try {
                automationUtil.deleteAllFileInFolder(environment.getProperty("argus.hire.folder.ss"));
            } catch (IOException e) {
                log.error(() -> "processHireByExcel exception: " + e.getMessage());
            }
        }

        // Send email this file was successfully processed.
        sendAcknowledge(empNoList, fileName);
    }

    private void sendAcknowledge(List<String> employeeList, String fileName) {
        HireUtil hireUtil = new HireUtil();
        String emailContent = hireUtil.getAcknowledgeHTML(employeeList);

        try {
            hireService.sendReHire(EMAIL_SUBJECT_COMPLETE_STR + fileName, emailContent,
                    environment.getProperty("argus.hire.folder.ss"));
        } catch (ArgusMailException e) {
            log.error(() -> "sendEmailError exception: " + e.getMessage());
        }
    }

    private void sendEmailError(List<HireError> errorList) {
        log.info("sendEmailError() -> count: " + errorList.size());
        HireUtil hireUtil = new HireUtil();
        String emailContent = hireUtil.getHireErrorHTML(errorList);
        log.debug("emailContent: " + emailContent);
        try {
            hireService.sendReHire(EMAIL_SUBJECT_STATUS_STR, emailContent,
                    environment.getProperty("argus.hire.folder.ss"));
        } catch (ArgusMailException e) {
            log.error(() -> "sendEmailError exception: " + e.getMessage());
        }

    }

}
