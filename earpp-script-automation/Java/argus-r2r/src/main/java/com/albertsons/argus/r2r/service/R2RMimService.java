package com.albertsons.argus.r2r.service;

import java.util.List;

import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

public interface R2RMimService {
    public static final String LOG_FOLDER = "mim.log.folder";
    public static final Integer NORMAL_PRIORITY = 3;
    
    public List<FileDetails> mainMimTask(FileDetails file, String execTimestamp, String execCompleteTime) throws ArgusR2RException;

    public FileDetails getFileDetailsFromRefFile(String filePrefix);

    public void inputLabelDetails(Page mainPage, String labelName) throws ArgusR2RException;

    public void navigateTransferStatusPage(Page mainPage, FileDetails fileDetails);

    public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit) throws ArgusR2RException;
    
    public List<String> getResultTableValues(Page mainPage, FileDetails file);

    public String checkUnsuccessType(Page page, ElementHandle row, String label);

    public void closeUnsuccessTab(Page page);

    public void sendMimEmail(String[] recipients, String[] cc, String subject, String body);

}
