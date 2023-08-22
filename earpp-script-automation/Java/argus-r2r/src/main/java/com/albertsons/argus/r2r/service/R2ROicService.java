package com.albertsons.argus.r2r.service;

import java.util.List;

import com.albertsons.argus.domaindbr2r.dto.ProcessInstanceDTO;
import com.albertsons.argus.domaindbr2r.dto.RecordDTO;
import com.albertsons.argus.r2r.dto.FileDetails;
import com.albertsons.argus.r2r.exception.ArgusR2RException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public interface R2ROicService {
    public static final Integer NORMAL_PRIORITY = 3;
    
    public List<Object> mainOicTask(FileDetails fileDetails, String execTimestamp) throws ArgusR2RException;

    public String checkFileReceived(FileDetails fileDetails);

    public RecordDTO checkFileConsumed(FileDetails fileDetails, int greaterConsumedFlag);

    public ProcessInstanceDTO queryTransformStatus(FileDetails fileDetails);

    public boolean checkTransformStatus(FileDetails fileDetails);

    public String checkOicUi(FileDetails fileDetails, ProcessInstanceDTO processInstanceDTO);

    public Page navigateOicLogin(Browser browser, BrowserContext browserContext);

    public String checkTransformStatusOIC(FileDetails fileDetails, Page page, ProcessInstanceDTO processInstanceDTO);

    public String generateUpdateQuery(FileDetails fileDetails, ProcessInstanceDTO processInstanceDTO);

    public void sendOicEmail(String[] recipients, String[] cc, String subject, String body);

}
