package com.albertsons.argus.eps.service;

import java.io.IOException;

import org.springframework.web.client.RestClientException;

import com.albertsons.argus.eps.exception.ArgusEPSException;
import com.albertsons.argus.eps.ws.bo.ResponseGetIncidentDetailBO;
import com.albertsons.argus.eps.ws.bo.ResponseUpdateIncidentDetailsBO;
import com.albertsons.argus.eps.ws.bo.ResponseValidateIncidentBO;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

public interface EPSAutomationService {
    
    public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";
    public static final String USERNAME = System.getProperty("user.name");

    public Browser getBrowser() throws PlaywrightException;

    public BrowserContext getBrowserContext(Browser browser) throws PlaywrightException;

    public Page getPage(BrowserContext context) throws PlaywrightException;

    public Page navigateEPSSite(Page page, String siteUrl) throws ArgusEPSException;
    
	public void delay(long seconds);
    
    public ResponseGetIncidentDetailBO getIncidentDetailFromJson(String jsonString); 

    public ResponseValidateIncidentBO getValidateIncidentDetails(String requestBody, String decodedUrl) throws RestClientException;

    public ResponseUpdateIncidentDetailsBO updateIncidentDetails(String requestBody, String decodedUrl) throws RestClientException;

    public boolean checkElementIsVisible(Page page, String selector, String attrib);

    public Page removePrescriptionEPS(Page page, String firstName, String lastName, String prescription, String sysId, String sysUpdatedBy,
        String incidentNumber, String snowUrlGet, String snowUrlUpdate, String description) throws PlaywrightException;

    public ElementHandle getElementByAttribute(Page page, String attribute, String property, String value);

    public Integer checkSiteAvailability(String stringUrl);

    public Page mainEPSProcess(String sysId, String sysUpdatedBy, String incidentNumber, String callerId, String snowUrlGet, String snowUrlUpdate,String firstName, String lastName, String prescription, String description);

    public void epsFourStoreNotFound(String sysId, String sysUpdatedBy, String incidentNumber,String snowUrlGet, String snowUrlUpdate, String description);
    
    public void epsFivePutBackTask(String sysId, String sysUpdatedBy, String incidentNumber, String snowUrlGet, String snowUrlUpdate, String description, String prescription);

    public String epsValidateInput(String sysId, String sysUpdatedBy, String incidentNumber, String description, String snowUrlGet, String snowUrlUpdate, String callerId);
    
    public void epsSixCheckedOutNotFound(String sysId, String sysUpdatedBy, String incidentNumber, String snowUrlGet, String snowUrlUpdate, String description, String prescription,String firstName, String lastName, String ldapId);

    public String runCheckSiteAvailabilityScript(String arg) throws IOException;
}
