package com.albertsons.argus.hire.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.service.ExcelService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.hire.exception.HireException;
import com.albertsons.argus.hire.exception.HirePlaywrightException;
import com.albertsons.argus.hire.model.Hire;
import com.albertsons.argus.hire.service.HirePlaywrightService;
import com.albertsons.argus.hire.service.HireService;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class HireServiceImpl implements HireService {
    @Autowired
    private HirePlaywrightService hirePlaywrightService;
    @Autowired
    private Environment environment;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private EmailService emailService;

    public String hireRehireDate(String scenario, String requestNumber, String empId, String assignNumber,
            String newHireDt) throws HireException {
        log.debug(() -> "hireRehireDate started . . . . .");
        log.debug(() -> "scenario: " + scenario);
        if (scenario.toLowerCase().contains(SCENERIO_1.toLowerCase())) {
            straightProcessing(scenario, requestNumber, empId, assignNumber, newHireDt);
            return "Scenario_1";
        } else if (scenario.toLowerCase().contains(SCENERIO_2.toLowerCase())) {
            syncPositionRowExistsScenario(scenario, requestNumber, empId, assignNumber, newHireDt);
            return "Scenario_2";
        } else if (scenario.toLowerCase().contains(SCENERIO_3.toLowerCase())) {
            empNewHireDateBeforePendingWorkScenario(scenario, requestNumber, empId, assignNumber, newHireDt);
            return "Scenario_3";
        } else {
            return "error";
        }

    }

    public void straightProcessing(String scenario, String requestNumber, String empId, String assignNumber,
            String newHireDt) throws HireException {
        log.debug(() -> "straightProcessing started . . . . . . . . . . . . . . . . . . . . . . . . . . .");
        try {
            String formatHireDt = getFormatHireDt(newHireDt);
            hirePlaywrightService.browseStraightProcessing(scenario, requestNumber, empId, assignNumber, formatHireDt);
        } catch (ParseException | HirePlaywrightException e) {
            log.error(() -> "straightProcessing() exception: " + e.getMessage());
            throw new HireException(e.getMessage());
        }
        log.debug(() -> "straightProcessing ended . . . . . . . . . . . . . . . . . . . . . . . . . . .");
    }

    public void syncPositionRowExistsScenario(String scenario, String requestNumber, String empId,
            String assignNumber, String newHireDt) throws HireException {

        try {
            String formatHireDt = getFormatHireDt(newHireDt);
            hirePlaywrightService.browseSyncPositionRowExists(scenario, requestNumber, empId, assignNumber,
                    formatHireDt);
        } catch (ParseException | HirePlaywrightException e) {
            log.error(() -> "syncPositionRowExistsScenario() exception: " + e.getMessage());
            throw new HireException(e.getMessage());
        }

    }

    private String getFormatHireDt(String newHireDt) throws ParseException {
        String formatHireDt = "";

        AutomationUtil automationUtil = new AutomationUtil();
        String dateFormat = environment.getProperty("com.argus.domain.util.date.format");
        String timeZone = environment.getProperty("com.argus.domain.util.date.timezone");

        Date hireDt = automationUtil.toDate(newHireDt, "M/dd/yyyy");

        formatHireDt = automationUtil.toDateString(hireDt, dateFormat, timeZone);

        return formatHireDt;
    }

    @Override
    public void empNewHireDateBeforePendingWorkScenario(String scenario, String requestNumber, String empId,
            String assignNumber, String newHireDt) throws HireException {
        try {
            String formatHireDt = getFormatHireDt(newHireDt);
            hirePlaywrightService.browseEmpNewHireDateBeforePendingWork(scenario, requestNumber, empId, assignNumber,
                    formatHireDt);
        } catch (ParseException | HirePlaywrightException e) {
            log.error(() -> "empNewHireDateBeforePendingWorkScenario() exception: " + e.getMessage());
            throw new HireException(e.getMessage());
        }
    }

    @Override
    public List<Hire> getHireContentList(String fileName) {
        List<Hire> getHire = new ArrayList<>();
        try {
            Map<Integer, List<String>> getContentMap = excelService.getExcelContentByRowMap(
                    environment.getProperty("argus.hire.folder.path"), "//"+fileName);

                    // Map<Integer, List<String>> getContentMap = excelService.getExcelContentByRowMap(
                    //     environment.getProperty("argus.hire.folder.path"), environment.getProperty("argus.hire.file.name"));

            getContentMap.entrySet().stream()
                    .filter(entry -> entry.getValue().size() >= 7)
                    .forEach(entry -> {
                        Hire hire = new Hire();

                        hire.setRequestNumber(entry.getValue().get(0));
                        hire.setScenario(entry.getValue().get(1));
                        hire.setEmployeeNumber(entry.getValue().get(2));
                        hire.setAssignmentNumber(entry.getValue().get(3));
                        hire.setNewHireDate(entry.getValue().get(4));
                        hire.setStatus(entry.getValue().get(5));
                        hire.setRemark(entry.getValue().get(6));

                        getHire.add(hire);
                    });

        } catch (DomainException e) {
            log.error(() -> e.getMessage());
        }
        return getHire;
    }

    // public List<String> getAllExcelFileName()

    public void sendReHire(String subject, String message, String pathString) throws ArgusMailException {
        String from = environment.getProperty("mail.argus.from");
        String alias = environment.getProperty("mail.argus.from.alias");
        String[] recipients = environment.getProperty("mail.argus.recipients", String[].class);
        String[] cc = environment.getProperty("mail.argus.cc", String[].class);

        AutomationUtil automationUtil = new AutomationUtil();
        List<String> fileNamesList = new ArrayList<>();

        try {
            fileNamesList = automationUtil.getAllFileNameInDirectory(pathString);
        } catch (SecurityException | IOException e) {
            log.error(() -> "sendReHire() exception: " + e.getMessage());
            throw new ArgusMailException("sendReHire() getAllFileNameInDirectory failed.", e);
        }
        emailService.sendMessageWithMultipleAttachment(from, alias, recipients, cc, subject, message, pathString,
                fileNamesList, 3, true);
        // emailService.sendSimpleMessage(from, alias, recipients, cc, subject, message,
        // 3, true);
    }
}
