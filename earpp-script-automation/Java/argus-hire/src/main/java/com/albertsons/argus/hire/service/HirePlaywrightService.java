package com.albertsons.argus.hire.service;

import com.albertsons.argus.hire.exception.HirePlaywrightException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;

public interface HirePlaywrightService {
    final String  CLIENTGRP_SHOWMORE = "SHOW_MORE";
    final String  CLIENTGRP_PERSON_MANAGEMENT = "PERSON_MANAGEMENT";
    final String EMAIL_SCENARIO_2_SUBJECT = "[Hire/Rehire] Synchronization From Position row exists - Error";
    final String EMAIL_SCENARIO_3_SUBJECT = "[Hire/Rehire] Employee's new hire date is before the pending worker's termination date - Error";
    final int SET_SHORTER_SEC_VALUE = 3;
    final int SET_LONGER_SEC_VALUE = 10;
    public void browseStraightProcessing(String scenario, String requestNumber, String empId, String assignNumber, String newHireDt) throws HirePlaywrightException;
    public void browseSyncPositionRowExists(String scenario, String requestNumber, String empId, String assignNumber, String newHireDt) throws HirePlaywrightException;
    public void browseEmpNewHireDateBeforePendingWork(String scenario, String requestNumber, String empId, String assignNumber, String newHireDt) throws HirePlaywrightException;
    public Browser openBrowser()throws HirePlaywrightException;
    public Page navigateMyClientGroup(Browser browser) throws InterruptedException;
}
