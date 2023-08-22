package com.albertsons.argus.ip.service;

import org.springframework.web.client.RestClientException;

import com.albertsons.argus.ip.exception.ArgusIpException;
import com.albertsons.argus.ip.ws.bo.ResponseGetInvoiceListBO;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

public interface IPAutomationService {
    
    public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";

    public Browser getBrowser() throws PlaywrightException;

    public BrowserContext getBrowserContext(Browser browser) throws PlaywrightException;

    public Page getPage(BrowserContext context) throws PlaywrightException;

    public Page navigateIPOracle(Page page) throws ArgusIpException;
    
	public void delay(long seconds);
    
    public String getMfaCode(String pythonScript, String secretKey);
    
    public Page loginMFA(Page page);

    public String searchInvoiceDetails(Page page, String poNumber, String fastId);

    public String updateInvoiceDetails(String poLine, String noteToSupplier, String noteToReceiver,
    String approver, String accountingGroup, String projectNo, String lawsonReceiver, String fastId, String status);

    public String updateInvoice(String requestBody, String poLine, String noteToSupplier, String noteToReceiver,
    String approver, String accountingGroup, String projectNo, String lawsonReceiver, String fastId, String status) throws RestClientException;

    public ResponseGetInvoiceListBO getInvoiceLists(String requestBody) throws RestClientException;

    public void closePlaywright();

    public boolean checkElementIsVisible(Page page, String selector, String attrib);

}
