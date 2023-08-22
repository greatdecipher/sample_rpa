package com.albertsons.argus.meraki.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.albertsons.argus.domain.bo.generated.ARPEntries;
import com.albertsons.argus.domain.bo.generated.Internet1;
import com.albertsons.argus.domain.bo.generated.Internet2;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.meraki.exception.MerakiException;
import com.albertsons.argus.meraki.service.MerakiService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 
 * @author kbuen03
 * @since 1/7/22
 * @version 1.1
 * @apiNote - 1.0 kbuen03 1/17/22 - initial draft 
 *            1.1 kbuen03 4/4/22 - call playwright domain
 * 
 */
@Service
public class MerakiServiceImpl implements MerakiService {
    private static final Logger LOG = LogManager.getLogger(MerakiServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;

    @Override
    public Page navigateLoginSSL(Browser browser) throws MerakiException {
        LOG.log(Level.DEBUG, () -> "start navigateLoginSSL method. . .");
        Page page = null;

        String nextButtonProp = environment.getProperty("argus.meraki.navigateLoginSSL.name.next.value");
        String emailProp = environment.getProperty("argus.meraki.navigateLoginSSL.name.email.value");
        String emailUsername = environment.getProperty("encrypted.meraki.property.username");
        String passworProp = environment.getProperty("argus.meraki.navigateLoginSSL.name.password.value");
        String loginButtonProp = environment.getProperty("argus.meraki.navigateLoginSSL.name.login.value");

        try {
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
            
            page = context.newPage();

            page.navigate(
                    environment.getProperty(MERAKI_URI) + environment.getProperty("argus.meraki.batch.url.login"));

            // Click next button
            playwrightAutomationService.pageClick(page, "input", "id", nextButtonProp);

            String pw = environment.getProperty("encrypted.meraki.property.password");
            // LOG.log(Level.DEBUG, () -> ">>>>>> " + pw);

            // Email
            playwrightAutomationService.pageFill(page, "input", "name", emailProp, emailUsername);
            // Password
            playwrightAutomationService.pageFill(page, "input", "name", passworProp, pw);

            // Click Login Button
            playwrightAutomationService.pageClick(page, "input", "id", loginButtonProp);

            LOG.log(Level.DEBUG, () -> "end navigateLoginSSL method. . .");
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        return page;
    }

    /**
     * @param valueProp = IMT-SWE
     */
    @Override
    public Page organizationPage(Page page, String valueProp) throws MerakiException {
        try {
            playwrightAutomationService.pageClick(page, null, "text", valueProp);
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        return page;
    }

    /**
     * @param valueProp = magicSearchQuery
     * @param value     = IMT_0006
     * 
     */
    @Override
    public Page searchDashboard(Page page, String valueProp, String value) throws MerakiException {
        try {
            playwrightAutomationService.pageFill(page, "input", "name", valueProp, value);
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        return page;
    }

    /**
     * @param valueProp = IMT_0006-li
     * 
     */
    @Override
    public Page clickResultDashboard(Page page, String valueProp) throws MerakiException {
        try {
            playwrightAutomationService.pageClick(page, null, null, "data-testid=" + valueProp + "-li");
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        return page;
    }

    /**
     * @param valueProp = Tools
     * 
     */
    @Override
    public Page clickToolsTab(Page page, String valueProp) throws MerakiException {
        try {
            playwrightAutomationService.pageClick(page, null, "text", valueProp);
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        return page;
    }

    @Override
    public Page clickAPRBtn(Page page) throws MerakiException {
        AutomationUtil automationUtil = new AutomationUtil();

        String valueProp = environment.getProperty("argus.meraki.clickAPRBtn.name.text.value");
        String indexVal = environment.getProperty("argus.meraki.clickAPRBtn.name.index.value");
        // Equivalent : :nth-match(:text('Run'), 6)
        String nMatchVal = automationUtil.generateNmatchSelector("text", valueProp, indexVal);
        try {
            // Wait until all three buttons are visible
            playwrightAutomationService.pageLocatorWait(page, null, null, nMatchVal);
            playwrightAutomationService.pageClick(page, null, null, nMatchVal);
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        return page;
    }

    @Override
    public List<Internet1> internetOneOutput(Page page) throws MerakiException {
        LOG.log(Level.DEBUG, () -> "start outputStr. . .");
        AutomationUtil automationUtil = new AutomationUtil();

        boolean isGetAllIntOneResults = Boolean
                .valueOf(environment.getProperty("argus.meraki.int.one.isGetAllResults.value"));
        int i = Integer.valueOf(environment.getProperty("argus.meraki.int.one.results.index.value"));

        Locator locator = null;
        try {
            locator = playwrightAutomationService.pageLocatorWait(page, null, null, automationUtil.generateNmatchSelector(null,
                    environment.getProperty("argus.meraki.internetOneOutput.nmatch.value"),
                    environment.getProperty("argus.meraki.internetOneOutput.name.index.value")));
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        List<String> s = locator.allInnerTexts();
        Internet1 int1 = null;
        List<Internet1> internet1List = new ArrayList<>();
        for (String val : s) {
            String newVal = StringUtils.normalizeSpace(val);

            LOG.log(Level.DEBUG, () -> "newVal: " + newVal);
            List<String> lists = Arrays.asList(StringUtils.split(newVal, " "));
            LOG.log(Level.DEBUG, () -> "lists: " + lists.size());
            int1 = new Internet1();
            if (isGetAllIntOneResults) {
                for (String int1Val : lists) {
                    if (StringUtils.containsIgnoreCase("IP MAC VLAN Age (sec)", int1Val)) {
                        continue;
                    } else if (StringUtils.isBlank(int1.getIp())) {
                        int1.setIp(int1Val);
                    } else if (StringUtils.isBlank(int1.getMac())) {
                        int1.setMac(int1Val);
                    } else if (int1.getVlan() == null) {
                        int1.setVlan(Integer.valueOf(int1Val));
                    } else if (int1.getAgeInSecs() == null) {
                        int1.setAgeInSecs(Integer.valueOf(int1Val));
                        internet1List.add(int1);
                        int1 = new Internet1();
                    }
                }
            } else {
                int max = 0;

                if(lists.size()<i)
                    max = lists.size();
                else
                    max = i;

                for (int index = 0; index < max; index++) {
                    if (StringUtils.containsIgnoreCase("IP MAC VLAN Age (sec)", lists.get(index))) {
                        continue;
                    } else if (StringUtils.isBlank(int1.getIp())) {
                        int1.setIp(lists.get(index));
                    } else if (StringUtils.isBlank(int1.getMac())) {
                        int1.setMac(lists.get(index));
                    } else if (int1.getVlan() == null) {
                        int1.setVlan(Integer.valueOf(lists.get(index)));
                    } else if (int1.getAgeInSecs() == null) {
                        int1.setAgeInSecs(Integer.valueOf(lists.get(index)));
                        internet1List.add(int1);
                        int1 = new Internet1();
                    }
                }
            }
        }

        LOG.log(Level.DEBUG, () -> "end outputStr. . .");

        return internet1List;
    }

    public void sendMerakiEmail(ARPEntries arpEntries, String subject, String[] toArr, String body) {
        LOG.log(Level.DEBUG, () -> "start sendMerakiEmail. . .");
        String from = environment.getProperty("mail.argus.meraki.from");
        String fromAlias = environment.getProperty("mail.argus.meraki.from.alias");
        
        String[] ccArr = environment.getProperty("mail.argus.meraki.cc", String[].class);

        // send to email
        try {
            emailService.sendSimpleMessage(from,
                    fromAlias, toArr, ccArr, subject,
                    body, 3,true);
        } catch (ArgusMailException e) {
            LOG.log(Level.ERROR, () -> "ArgusMailException: " + e);
        }
        LOG.log(Level.DEBUG, () -> "end sendMerakiEmail. . .");
    }

    @Override
    public List<Internet2> internetTwoOutput(Page page) throws MerakiException {
        LOG.log(Level.DEBUG, () -> "start outputStr2. . .");

        boolean isGetAllIntTwoResults = Boolean
                .valueOf(environment.getProperty("argus.meraki.int.two.isGetAllResults.value"));
        int i = Integer.valueOf(environment.getProperty("argus.meraki.int.two.results.index.value"));

        AutomationUtil automationUtil = new AutomationUtil();

        Locator locator = null;
        try {
            locator = playwrightAutomationService.pageLocatorWait(page, null, null, automationUtil.generateNmatchSelector(null,
                    environment.getProperty("argus.meraki.internetOneOutput.nmatch.value"),
                    environment.getProperty("argus.meraki.internetTwoOutput.name.index.value")));
        } catch (PlaywrightException pw) {
            LOG.log(Level.ERROR, () -> "PlaywrightException exception:" + pw);
            throw new MerakiException(pw.getMessage());
        }

        List<String> s = locator.allInnerTexts();
        Internet2 int2 = null;
        List<Internet2> internet2List = new ArrayList<>();
        for (String val : s) {
            String newVal = StringUtils.normalizeSpace(val);

            LOG.log(Level.DEBUG, () -> "newVal: " + newVal);
            List<String> lists = Arrays.asList(StringUtils.split(newVal, " "));
            LOG.log(Level.DEBUG, () -> "lists: " + lists.size());
            int2 = new Internet2();

            if (isGetAllIntTwoResults) {
                for (String int2Val : lists) {
                    if (StringUtils.containsIgnoreCase("IP MAC VLAN Age (sec)", int2Val)) {
                        continue;
                    } else if (StringUtils.isBlank(int2.getIp())) {
                        int2.setIp(int2Val);
                    } else if (StringUtils.isBlank(int2.getMac())) {
                        int2.setMac(int2Val);
                    } else if (int2.getVlan() == null) {
                        int2.setVlan(Integer.valueOf(int2Val));
                    } else if (int2.getAgeInSecs() == null) {
                        int2.setAgeInSecs(Integer.valueOf(int2Val));
                        internet2List.add(int2);
                        int2 = new Internet2();
                    }
                }
            } else {
                int max = 0;
                
                if(lists.size()<i)
                    max = lists.size();
                else
                    max = i;
                    
                for (int index = 0; index < max; index++) {
                    if (StringUtils.containsIgnoreCase("IP MAC VLAN Age (sec)", lists.get(index))) {
                        continue;
                    } else if (StringUtils.isBlank(int2.getIp())) {
                        int2.setIp(lists.get(index));
                    } else if (StringUtils.isBlank(int2.getMac())) {
                        int2.setMac(lists.get(index));
                    } else if (int2.getVlan() == null) {
                        int2.setVlan(Integer.valueOf(lists.get(index)));
                    } else if (int2.getAgeInSecs() == null) {
                        int2.setAgeInSecs(Integer.valueOf(lists.get(index)));
                        internet2List.add(int2);
                        int2 = new Internet2();
                    }
                }
            }

        }
        LOG.log(Level.DEBUG, () -> "end outputStr2. . .");

        return internet2List;
    }

    @Override
    public String runOutlookScript(String arg) throws IOException {
        LOG.log(Level.DEBUG, () -> "Start runOutlookScript. . .");
        LOG.log(Level.DEBUG, () -> "Param: " + arg);
        Process p = Runtime.getRuntime().exec(arg);

        String text = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));

        LOG.log(Level.DEBUG, () -> "Outlook text: " + text);
        LOG.log(Level.DEBUG, () -> "end runOutlookScript. . .");
        return text;
    }

}
