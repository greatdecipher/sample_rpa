package com.albertsons.argus.q2c.common.service;

import com.albertsons.argus.q2c.exception.Q2CCommonException;
import com.microsoft.playwright.Page;

public interface Q2CCommonService {
    public static final Integer NORMAL_PRIORITY = 3;

    public boolean checkIfFileIsRunning(String folder, String filename) throws Exception;
    public void sendEmail(String[] recipients, String[] cc, String subject, String body, String execTimestamp);
    public void delayTimer(int setTimer) throws Q2CCommonException;
    public String getAttachmentContent(Page page, String filename, String textToLookFor, String altDocId, String execTimestamp) throws Q2CCommonException;
    
}
