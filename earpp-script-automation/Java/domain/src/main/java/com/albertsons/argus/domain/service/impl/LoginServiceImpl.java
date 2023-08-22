package com.albertsons.argus.domain.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.LoginService;
import com.albertsons.argus.domain.service.MfaService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger LOG = LogManager.getLogger(LoginServiceImpl.class);

    @Autowired
    Environment environment;

    @Autowired
    private PlaywrightAutomationService playwrightService;

    @Autowired
    private MfaService mfaService;

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    @Override
    public Page loginPage(Browser browser, BrowserContext browserContext, String uri, String username, String password,
            String mfaSecret) {
        LOG.log(Level.DEBUG, () -> "start loginPage method . . .");

        try {
            browserContext.setDefaultTimeout(120000); // 2 minutes max wait

            Page page = browserContext.newPage();

            // environment.getProperty("playwright.uri.oracle.saas.uri")
            // page = mfaService.loginAlbertsons(page, uri);
            page = mfaService.loginGenericAlbertsons(page, uri, username, password);

            for (int i = 0; i <= Integer.valueOf(environment.getProperty("mfa.attempts").trim()); i++) {
                TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("site.load.wait.time.in.seconds")));

                List<ElementHandle> navigatorElements = page.querySelectorAll("svg[aria-label=Navigator]");

                if (!navigatorElements.isEmpty()) { // either it's not requiring MFA code because session is saved or
                                                    // login was successful
                    return page;
                } else { // requiring MFA code because session is not saved
                    if (i == Integer.valueOf(environment.getProperty("mfa.attempts").trim())) { // last attempt
                        throw new Exception("MFA code not retrieved...");
                    } else {
                        if (!activeProfiles.contains("dev"))
                            page.navigate(uri); // reload page

                        TimeUnit.SECONDS.sleep(20);

                        List<ElementHandle> otcElements = page.querySelectorAll("input[name=otc]");

                        if (!otcElements.isEmpty()) {
                            TimeUnit.SECONDS.sleep(10);

                            String mfaCode = mfaService.getMfaCode(environment.getProperty("mfa.python.script"),
                                    mfaSecret);

                            if (mfaCode == null || mfaCode.equalsIgnoreCase("") || mfaCode.isEmpty()) {
                                if (i == (Integer.valueOf(environment.getProperty("mfa.attempts").trim()) - 1)) {
                                    throw new Exception("MFA code not retrieved...");
                                }
                            } else {
                                playwrightService.pageFill(page, "input", "name", "otc", mfaCode);
                                playwrightService.pageLocatorWait(page, "input", "name", "rememberMFA").check();
                                playwrightService.pageClick(page, "input", "value", "Verify");
                            }
                        } else {
                            mfaService.loginAlbertsons(page, uri);
                        }

                    }

                }
            }

            LOG.log(Level.DEBUG, () -> "end loginPage method . . .");
            return page;
        } catch (Exception e) {
            LOG.log(Level.INFO, () -> "problem navigating loginPage method . . .");
            LOG.error(e);
            return null;
        }
    }

}
