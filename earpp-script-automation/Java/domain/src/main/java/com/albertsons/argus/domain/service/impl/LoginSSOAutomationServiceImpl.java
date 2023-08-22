package com.albertsons.argus.domain.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.LoginSSOAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@Service
public class LoginSSOAutomationServiceImpl implements LoginSSOAutomationService{
    private static final Logger LOG = LogManager.getLogger(LoginSSOAutomationServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    PlaywrightAutomationService playwrightAutomationService;

    @Override
    public Page openBrowser() {
        Browser browser = Playwright.create().chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
        Page page = context.newPage();

        return page;
    }

    @Override
    public Page loginSSO(Page page, String url, String username, String password, String secretKey) throws Exception {
        page.navigate(url);
        timeout(5);
        List<ElementHandle> emailElements = page.querySelectorAll("input[type=email]");
        if (!emailElements.isEmpty()){ 
            playwrightAutomationService.pageFill(page, "input", "type", "email", username + "@safeway.com");
            playwrightAutomationService.pageClick(page, "input", "type", "submit");
        
            timeout(10);
        
            List<ElementHandle> passwordElements = page.querySelectorAll("input[id=passwordInput]");
            if (!passwordElements.isEmpty()){ 
                playwrightAutomationService.pageFill(page, "input", "id", "passwordInput", password);
                playwrightAutomationService.pageClick(page, "span", "id", "submitButton");
            }
        }
        
        for (int i = 0; i <= Integer.valueOf(environment.getProperty("mfa.attempts")); i++){				
            timeout(5);
        
            if ( i == Integer.valueOf(environment.getProperty("mfa.attempts")) ){ // last attempt
                   throw new Exception("MFA code not retrieved...");
            }
            else {
                
                List<ElementHandle> otcElements = page.querySelectorAll("input[name=otc]");
                
                if (!otcElements.isEmpty()){
                    String mfaCode = getMfaCode(environment.getProperty("mfa.python.script"), secretKey);
                    LOG.info(mfaCode);
                    timeout(5);
    
                    if (mfaCode == null || mfaCode.equalsIgnoreCase("") || mfaCode.isEmpty()){
                        if (i == (Integer.valueOf(environment.getProperty("mfa.attempts")) - 1)){
                            throw new Exception("MFA code not retrieved...");
                        }
                    }
                    else{
                        playwrightAutomationService.pageFill(page, "input", "name", "otc", mfaCode);
                        playwrightAutomationService.pageLocatorWait(page, "input", "name", "rememberMFA").check();
                        playwrightAutomationService.pageClick(page, "input", "value", "Verify");
                    }
                } else {
                    break;
                }
            }
        }

        return page;
    }

    private void timeout(Integer seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getMfaCode(String pyScript, String secretKey) {

	try {

		String []cmd = {"python", pyScript, secretKey};
		
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(cmd);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String s = "", mfaCode = "";
		
		while((s = in.readLine()) != null){
			mfaCode = s.trim();

			if (mfaCode.length() == 6){ // make sure it's 6 digits
				return mfaCode;
			}
		}

	} catch (Exception e){
		LOG.error("getMfaCode Error");
        e.printStackTrace();
	}

	return null;
    }
    
}
