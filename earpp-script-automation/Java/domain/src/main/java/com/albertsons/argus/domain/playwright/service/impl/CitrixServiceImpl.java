package com.albertsons.argus.domain.playwright.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.playwright.service.CitrixService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.MfaService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class CitrixServiceImpl implements CitrixService{
    private static final Logger LOG = LogManager.getLogger(CitrixServiceImpl.class);
    
    @Autowired
    Environment environment;
    
	@Autowired
	private PlaywrightAutomationService playwrightService;

	@Autowired
	private MfaService mfaService;
    
    @Override
    public Page navigateCitrixSaasLogin(Browser browser, BrowserContext browserContext, String secret, String userName, String password) {
        try {
            browserContext.setDefaultTimeout(120000); //2 minutes max wait

			Page page = browserContext.newPage();

			page = mfaService.loginGenericAlbertsons(page, environment.getProperty("playwright.uri.citrix.login"), userName, password);

			for (int i = 0; i <= Integer.valueOf(environment.getProperty("mfa.attempts").trim()); i++){				
				TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("site.load.wait.time.in.seconds")));

				List<ElementHandle> navigatorElements = page.querySelectorAll("button[data-testid=detection-use_browser]");

				if (!navigatorElements.isEmpty()){ // either it's not requiring MFA code because session is saved or login was successful
					return page;
				}
				else{ // requiring MFA code because session is not saved	
					if ( i == Integer.valueOf(environment.getProperty("mfa.attempts").trim()) ){ // last attempt
						throw new Exception("MFA code not retrieved...");
					}
					else{
						// page.navigate(environment.getProperty("playwright.uri.citrix.login")); //reload page
						// TimeUnit.SECONDS.sleep(20);

						List<ElementHandle> otcElements = page.querySelectorAll("input[name=otc]");
						
						if (!otcElements.isEmpty()){
							TimeUnit.SECONDS.sleep(10);

							String mfaCode = mfaService.getMfaCode(environment.getProperty("mfa.python.script"), secret);

							if (mfaCode == null || mfaCode.equalsIgnoreCase("") || mfaCode.isEmpty()){
								if (i == (Integer.valueOf(environment.getProperty("mfa.attempts").trim()) - 1)){
									throw new Exception("MFA code not retrieved...");
								}
							}
							else{
								playwrightService.pageFill(page, "input", "name", "otc", mfaCode);
								playwrightService.pageLocatorWait(page, "input", "name", "rememberMFA").check();
								playwrightService.pageClick(page, "input", "value", "Verify");
							}
						}
						else{
							mfaService.loginAlbertsons(page, environment.getProperty("playwright.uri.citrix.login"));
						}
						
					}

				}
			}					
			LOG.log(Level.DEBUG, () -> "end navigateOracleSaasLogin method . . .");
			return page;
		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem navigating Oracle SaaS login . . .");
			LOG.error(e);
			return null;
		}
    }
    
}
