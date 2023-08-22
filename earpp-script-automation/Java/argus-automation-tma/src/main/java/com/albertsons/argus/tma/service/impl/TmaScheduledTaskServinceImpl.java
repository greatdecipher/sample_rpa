package com.albertsons.argus.tma.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.domain.bo.generated.ByProjectBO;
import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.domain.exception.DomainException;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.tma.exception.ArgusTmaException;
import com.albertsons.argus.tma.service.TMAAutomationService;
import com.albertsons.argus.tma.service.TmaScheduledTaskService;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

/**
 * 
 * @author kbuen03
 * @since 04/04/22
 * @version
 *      1.1 - 11/17/22 - Add metric component
 *      1.0 - Initial Draft
 * 
 */
@Service
public class TmaScheduledTaskServinceImpl implements TmaScheduledTaskService {
    private static final Logger LOG = LogManager.getLogger(TmaScheduledTaskServinceImpl.class);
    @Autowired
    private TMAAutomationService tmaAutomationService;

    @Autowired
    private AutomationService<ByQueueBO> automationService;

    @Autowired
    private AutomationService<ByProjectBO> tmaProjectService;

    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;

    @Autowired
    private MetricsWebService metricsWebService;

    @Autowired
    private Environment environment;

    @Override
    @Scheduled(cron = "${com.argus.tma.scheduled.task.cron.value}")
    public void runTmaJob() {
        List<ByQueueBO> tmaBOs = new ArrayList<>();
        List<String> headers = tmaAutomationService.getHeadersList();
        List<String> getContentLists = new ArrayList<>();

        ResponseStartSaveBO responseStartSaveBO = new ResponseStartSaveBO();

        try{
            responseStartSaveBO = metricsWebService.startSave(environment.getProperty("com.argus.tma.automation.id"), environment.getProperty("com.argus.tma.version.id"));
        }catch(RestClientException e){
            LOG.log(Level.ERROR, () -> "RestClientException: " + e);
        }
        try {
            Browser browser = playwrightAutomationService.openBrowser();
            try {

                Page pageTMA = tmaAutomationService.navigateLoginNonSSL(browser);

                String pageTMAbyProject = tmaAutomationService.getbyProjectName(pageTMA);

                LOG.log(Level.DEBUG, () -> "pageTMAbyProject: " + pageTMAbyProject);

                ByProjectBO tmaProject = tmaProjectService.toJson(pageTMAbyProject);

                List<String> qManagersLists = tmaAutomationService.getQMManagers(tmaProject);

                getContentLists = tmaAutomationService.getContentLists(pageTMA, qManagersLists);

                playwrightAutomationService.closedBrowser(browser);
            } catch (ArgusTmaException e) {
                LOG.log(Level.ERROR, () -> "ArgusTmaException: " + e);
                playwrightAutomationService.closedBrowser(browser);
            }
            tmaBOs = automationService.getToJsonLists(getContentLists);
            tmaAutomationService.sendTmaEmail(headers, tmaBOs);
            
            try{
                if(StringUtils.isNotBlank(responseStartSaveBO.getId())){}
                    metricsWebService.incrementTransaction(responseStartSaveBO.getId(), "1");
            }catch(RestClientException | NullPointerException e){
                LOG.log(Level.ERROR, () -> "RestClientException: " + e);
            }

        } catch (DomainException de) {
            LOG.log(Level.ERROR, () -> "DomainException: " + de);
        }

        try{
            if(StringUtils.isNotBlank(responseStartSaveBO.getId()))
                metricsWebService.endSave(responseStartSaveBO.getId());
        }catch(RestClientException | NullPointerException e){
            LOG.log(Level.ERROR, () -> "RestClientException: " + e);
        }
    }

}
