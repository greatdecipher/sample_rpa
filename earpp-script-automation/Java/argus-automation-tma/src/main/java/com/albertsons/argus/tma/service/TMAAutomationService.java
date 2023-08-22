package com.albertsons.argus.tma.service;

import java.util.List;

import com.albertsons.argus.domain.bo.generated.ByProjectBO;
import com.albertsons.argus.domain.bo.generated.ByQueueBO;
import com.albertsons.argus.tma.exception.ArgusTmaException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;

/**
 * @author kbuen03
 * @version 1.0
 * @since 5/18/12
 * 
 */
public interface TMAAutomationService {
    static final Integer HIGH_PRIORITY = 1;
    static final Integer NORMAL_PRIORITY = 3;

    static final String TO_JSON_STR = ":has(body)";
    static final String PLAYWRIGHT_URI = "playwright.uri.tma";

    public Page navigateLoginNonSSL(Browser browser) throws ArgusTmaException;

    public String getContent(Page page, String navigateFilter);

    public List<String> getContentLists(Page page, List<String> qmManagerLists) throws ArgusTmaException;
            
    public String getbyProjectName(Page page) throws ArgusTmaException;

    public List<String> getQMManagers(ByProjectBO project);

    public void sendTmaEmail(List<String> headers, List<ByQueueBO> tmaBOs);

    public List<String> getHeadersList();
   
}
