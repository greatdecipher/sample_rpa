package com.albertsons.argus.toggle.service;

import java.util.List;

import org.springframework.web.client.RestClientException;

import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.toggle.bo.IncidentDetailBO;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentDetailBO;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentListBO;
import com.albertsons.argus.toggle.bo.ResponseUpdateIncidentBO;
import com.albertsons.argus.toggle.dto.QueueDetails;
import com.albertsons.argus.toggle.exception.ArgusTmaException;
import com.microsoft.playwright.Page;

public interface ToggleAutomationService {
    static final Integer HIGH_PRIORITY = 1;
    static final Integer NORMAL_PRIORITY = 3;

    static final String TO_JSON_STR = ":has(body)";
    static final String PLAYWRIGHT_URI = "playwright.uri.tma";

    public ResponseUpdateIncidentBO updateIncident(String requestBody) throws RestClientException;

    public ResponseGetIncidentListBO getIncidentList(String requestBody) throws RestClientException;

    public ResponseGetIncidentDetailBO getIncidentDetail(String requestBody) throws RestClientException;
    
    public void sendTmaEmail(String emailMessage, QueueDetails queueDetails);

    public void sendIssueEmail(String emailMessage, QueueDetails queueDetails);

    public void toggleQueue(Page pageTMA) throws ArgusTmaException;

    public boolean checkCurrentQDepth(List<ByQueueBO> tmaBOs, QueueDetails queueDetails);

    public List<String> getContentLists(String projectName, String storeNumber)  throws ArgusTmaException;

    public boolean goToQueue(String projectName, String storeNumber, String localQueue, Page pageTMA, QueueDetails queueDetails) throws ArgusTmaException;

    public Page navigateLoginNonSSL() throws ArgusTmaException;

    public QueueDetails readIncident(IncidentDetailBO incidentDetailBO);
    
    public void deleteFile();

    public void moveLogFile();
}
