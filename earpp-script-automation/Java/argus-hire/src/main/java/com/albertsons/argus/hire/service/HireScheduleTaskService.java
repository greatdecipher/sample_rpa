package com.albertsons.argus.hire.service;

public interface HireScheduleTaskService {
    final String EMAIL_SUBJECT_STATUS_STR = "New Hire/Rehire Date Correction Status Report";
    final String EMAIL_SUBJECT_COMPLETE_STR = "New Hire/Rehire Date Correction Status Report ";
    
    // final String EMAIL_BODY_STR = "Hi User, <br> It is partially successfully completed. There are some scenario during process was encountered an error. Please see the details below and reprocess manually.<br><br>";
    public void runHireSchedule();
}
