package com.albertsons.argus.ip.service.impl;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.ip.service.IPAutomationService;
import com.albertsons.argus.ip.service.IPScheduledTaskService;
import com.albertsons.argus.ip.ws.bo.InvoiceDetailsBO;
import com.albertsons.argus.ip.ws.bo.ResponseGetInvoiceListBO;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

@Service
public class IPScheduledTaskServiceImpl implements IPScheduledTaskService{
    private static final Logger LOG = LogManager.getLogger(IPScheduledTaskServiceImpl.class);
    
    public Browser browser;
	public BrowserContext context;
	public Page page;
    public String automationRunId;

    @Autowired
	public Environment environ;
	
	@Autowired
	private IPAutomationService ipAutomationService;

    @Autowired
    private MetricsWebService metricsWebService;

    @Override
    @Scheduled(cron = "${com.argus.ip.scheduled.task.cron.value}")
    public void runToggleJob() {
        try {
            Page page = null;
            String updateStatus; 
            LocalDateTime mainStartTime; 
            LocalDateTime mainEndTime;
            ResponseGetInvoiceListBO getInvoiceListBO = null;
            

            LOG.info("Invoice Processing - Oracle Extraction Started. . .");
            LOG.info("Main Process Start Time:  " + LocalDateTime.now());
            
            for (int i = 0; i < Integer.parseInt(environ.getProperty("invoice.get.retry")); i++) {
                try {
                    ResponseStartSaveBO bo = metricsWebService.startSave(environ.getProperty("ip.automation.id"), environ.getProperty("ip.automation.version"));
                    if (bo.getResult().contains("SUCCESS")) {
                        automationRunId = bo.getId();
                        break;
                    }
                } catch (Exception e) {
                    LOG.info("Metrics StartSave API call retry: " + Integer.toString(i + 1));
                    LOG.error(e);
                    ipAutomationService.delay(Integer.parseInt(environ.getProperty(DELAY_S)));
                    if (i == Integer.parseInt(environ.getProperty("invoice.get.retry")) - 1) {
                        LOG.info("Metrics StartSave API call failed after " + 
                            Integer.parseInt(environ.getProperty("invoice.get.retry")) + " retries...");
                        LOG.error(e);
                    }
                }
            }

            for (int j = 0; j < Integer.parseInt(environ.getProperty("invoice.get.retry")); j++) {
                try {
                    getInvoiceListBO = ipAutomationService.getInvoiceLists("");
                    LOG.info("getPurchasingByStatus API call success. . .");
                    LOG.info(getInvoiceListBO.getInvoiceDetailsBo().size() + " Records found. . .");
                    
                    if (getInvoiceListBO.getInvoiceDetailsBo() != null) {
                        browser = ipAutomationService.getBrowser();
                        context = ipAutomationService.getBrowserContext(browser);
                        page = ipAutomationService.getPage(context);
        
                        for (InvoiceDetailsBO invoiceDetail : getInvoiceListBO.getInvoiceDetailsBo()) {
                            
                            LOG.info("Process started for Fast ID: " + invoiceDetail.getFastID() + " PO Number: " + invoiceDetail.getPONumber());
                            mainStartTime = LocalDateTime.now();
                            LOG.info("Invoice Processing Start Time:  " + mainStartTime);
                            updateStatus = ipAutomationService.searchInvoiceDetails(page, invoiceDetail.getPONumber(),invoiceDetail.getFastID());
                            try {
                                ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "1");    
                                if (!bo2.getResult().contains("SUCCESS")) {
                                    LOG.info("Metrics transaction increment failed. . .");
                                }
                            } catch (Exception e) {
                                LOG.info("Failed metrics increment transaction api call. . .");
                                LOG.error(e);
                            }
                            LOG.info(updateStatus + ": " + invoiceDetail.getPONumber());
                            LOG.info("Invoice Processing - Oracle Extraction Completed. . .");
                            mainEndTime = LocalDateTime.now();
                            LOG.info("Invoice Processing End Time:  " + mainEndTime);
                            long diff = ChronoUnit.SECONDS.between(mainStartTime, mainEndTime);
                            LOG.info("Processing time: " + diff + "s. . .");
                        }
                    } 
                    else {
                        LOG.info("No NEW Invoices found..");
                        try {
                            ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId, "0");    
                            if (!bo2.getResult().contains("SUCCESS")) {
                                LOG.info("Metrics transaction increment failed. . .");
                            }
                        } catch (Exception e) {
                            LOG.info("Failed metrics increment transaction api call. . .");
                            LOG.error(e);
                        }
                    }
                    break;    
                } catch (Exception e) {
                    LOG.info("getPurchasingByStatus API call retry: " + Integer.toString(j + 1));
                    LOG.error(e);
                    ipAutomationService.delay(Integer.parseInt(environ.getProperty(DELAY_S)));
                    if (j == Integer.parseInt(environ.getProperty("invoice.get.retry")) - 1) {
                        LOG.info("getPurchasingByStatus API call failed after " + 
                            Integer.parseInt(environ.getProperty("invoice.get.retry")) + " retries...");
                        LOG.error(e);
                    }
                }
            }

            for (int k = 0; k < Integer.parseInt(environ.getProperty("invoice.get.retry")); k++) {
                try {
                    ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);
                    if (bo3.getResult().contains("SUCCESS")) {
                        LOG.info("End metrics logged. . .");
                        break;
                    }
                } catch (Exception e) {
                    LOG.info("Metrics EndSave API call retry: " + Integer.toString(k + 1));
                    LOG.error(e);
                    ipAutomationService.delay(Integer.parseInt(environ.getProperty(DELAY_S)));
                    if (k == Integer.parseInt(environ.getProperty("invoice.get.retry")) - 1) {
                        LOG.info("Metrics EndSave API call failed after " + 
                            Integer.parseInt(environ.getProperty("invoice.get.retry")) + " retries...");
                        LOG.error(e);
                    }
                }
            }

        } catch (Exception e) {
            LOG.error("ERROR in runToggleJob");
            LOG.error(e.getMessage());
        }
        finally {
            if (browser != null) {
                browser.close();
            }   
        }
    }
}
