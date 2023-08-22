package com.albertsons.argus.meraki.service;

import java.io.IOException;
import java.util.List;

import com.albertsons.argus.domain.bo.generated.ARPEntries;
import com.albertsons.argus.domain.bo.generated.Internet1;
import com.albertsons.argus.domain.bo.generated.Internet2;
import com.albertsons.argus.meraki.exception.MerakiException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
/**
 * 
 * @author kbuen03
 * @since 1/17/21
 * @version 1.0
 * 
 */
public interface MerakiService {
    static final String MERAKI_URI = "argus.meraki.batch.uri";

    public Page navigateLoginSSL(Browser browser) throws MerakiException;
    public Page organizationPage(Page page, String valueProp) throws MerakiException;
    public Page searchDashboard(Page page, String valueProp, String value) throws MerakiException;
    public Page clickResultDashboard(Page page, String valueProp) throws MerakiException;
    public Page clickToolsTab(Page page, String valueProp) throws MerakiException;
    public void sendMerakiEmail(ARPEntries arpEntries, String subject, String[] toArr, String body);
    public Page clickAPRBtn(Page page) throws MerakiException;
    public List<Internet1> internetOneOutput(Page page) throws MerakiException;
    public List<Internet2> internetTwoOutput(Page page) throws MerakiException;
    public String runOutlookScript(String arg) throws IOException;
}
