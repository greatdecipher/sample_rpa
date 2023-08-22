package com.albertsons.argus.domain.oracle.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.oracle.service.OracleService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.MfaService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class OracleServiceImpl implements OracleService{
    private static final Logger LOG = LogManager.getLogger(OracleServiceImpl.class);

    @Autowired
    Environment environment;
    
	@Autowired
	private PlaywrightAutomationService playwrightService;

	@Autowired
	private MfaService mfaService;

	@Value("${spring.profiles.active:}")
    private String activeProfiles;

	//TODO: Please don't use this method. Please use oracleLoginPage method.
    @Override
    public Page navigateOracleSaasLogin(Browser browser, BrowserContext browserContext) {
        LOG.log(Level.DEBUG, () -> "start navigateOracleSaasLogin method . . .");

		try {
            browserContext.setDefaultTimeout(120000); //2 minutes max wait

			Page page = browserContext.newPage();

			page = mfaService.loginAlbertsons(page, environment.getProperty("playwright.uri.oracle.saas.uri"));

			for (int i = 0; i <= Integer.valueOf(environment.getProperty("mfa.attempts").trim()); i++){				
				TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("site.load.wait.time.in.seconds")));

				List<ElementHandle> navigatorElements = page.querySelectorAll("svg[aria-label=Navigator]");

				if (!navigatorElements.isEmpty()){ // either it's not requiring MFA code because session is saved or login was successful
					return page;
				}
				else{ // requiring MFA code because session is not saved	
					if ( i == Integer.valueOf(environment.getProperty("mfa.attempts").trim()) ){ // last attempt
						throw new Exception("MFA code not retrieved...");
					}
					else{
						page.navigate(environment.getProperty("playwright.uri.oracle.saas.uri")); //reload page
						TimeUnit.SECONDS.sleep(20);

						List<ElementHandle> otcElements = page.querySelectorAll("input[name=otc]");
						
						if (!otcElements.isEmpty()){
							TimeUnit.SECONDS.sleep(10);

							String mfaCode = mfaService.getMfaCode(environment.getProperty("mfa.python.script"), environment.getProperty("mfa.secret.key"));

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
							mfaService.loginAlbertsons(page, environment.getProperty("playwright.uri.oracle.saas.uri"));
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
	
	@Override
	public Page navigateScheduledProcesses(Browser browser, BrowserContext browserContext){
		LOG.log(Level.DEBUG, () -> "start navigateScheduledProcesses method. . .");

		try {
			Page page = navigateOracleSaasLogin(browser, browserContext);

			page.navigate(environment.getProperty("playwright.uri.oracle.saas.uri") + environment.getProperty("playwright.uri.oracle.saas.scheduled.processes"));

			TimeUnit.SECONDS.sleep(7);

			//make sure correct page
			List<ElementHandle> scheduleButtons = page.querySelectorAll("span:has-text(\"Schedule New Process\")");

			if (scheduleButtons.size() > 0){ // correct page				
				LOG.log(Level.DEBUG, () -> "end navigateScheduledProcesses method. . .");
				return page;
			}
			else{
				return null;
			}

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem navigating Oracle SaaS Scheduled Processes. . .");
            LOG.error(e);
			return null;
		}

	}

    @Override
    public Page navigateOracleOtbiJobHistory(Browser browser, BrowserContext browserContext) {
        LOG.log(Level.DEBUG, () -> "start navigateOracleOtbiJobHistory method . . .");

		try {
            browserContext.setDefaultTimeout(120000); //2 minutes max wait

			Page page = browserContext.newPage();

			page = mfaService.loginAlbertsons(page, environment.getProperty("playwright.uri.oracle.saas.uri") + environment.getProperty("playwright.uri.oracle.otbi.job.history"));

			for (int i = 0; i <= Integer.valueOf(environment.getProperty("mfa.attempts").trim()); i++){				
				TimeUnit.SECONDS.sleep(10);

				List<ElementHandle> jobHistoryTitles = page.querySelectorAll("span:has-text(\"Report Job History\")");

				if (!jobHistoryTitles.isEmpty()){ // either it's not requiring MFA code because session is saved or login was successful
					LOG.log(Level.DEBUG, () -> "Report Job History page loaded successfully . . .");	
					return page;
				}
				else{ // requiring MFA code because session is not saved	
					if ( i == Integer.valueOf(environment.getProperty("mfa.attempts").trim()) ){ // last attempt
						throw new Exception("MFA code not retrieved...");
					}
					else{
						page.navigate(environment.getProperty("playwright.uri.oracle.saas.uri")); //reload page
						TimeUnit.SECONDS.sleep(20);
						
						List<ElementHandle> otcElements = page.querySelectorAll("input[name=otc]");
						
						if (!otcElements.isEmpty()){
							TimeUnit.SECONDS.sleep(10);

							String mfaCode = mfaService.getMfaCode(environment.getProperty("mfa.python.script"), environment.getProperty("mfa.secret.key"));

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
							mfaService.loginAlbertsons(page, environment.getProperty("playwright.uri.oracle.saas.uri") + environment.getProperty("playwright.uri.oracle.otbi.job.history"));
						}
						
					}

				}
			}
									
			LOG.log(Level.DEBUG, () -> "end navigateOracleOtbiJobHistory method . . .");
			return page;
		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem navigating Oracle OTBI Job History . . .");
			LOG.error(e);
			return null;
		}
    }

	@Override
    public void expandSearchButton(Page page){
		LOG.log(Level.DEBUG, () -> "start expandSearchButton method . . .");

		try {
			List<ElementHandle> expandSearchButtons = page.querySelectorAll("td.xhs");
	
			if (!expandSearchButtons.isEmpty()){ 
				playwrightService.pageClick(page, "", "", "td.xhs");
			}

		} catch (Exception e){
			LOG.log(Level.INFO, () -> "problem expanding search button in Oracle ERP . . .");
			LOG.error(e);
		}

		LOG.log(Level.DEBUG, () -> "end expandSearchButton method . . .");
	}

}
