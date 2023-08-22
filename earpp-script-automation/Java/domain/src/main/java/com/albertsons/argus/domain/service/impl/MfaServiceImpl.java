package com.albertsons.argus.domain.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.domain.service.MfaService;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

@Service
public class MfaServiceImpl implements MfaService{
	public static final Logger LOG = LogManager.getLogger(MfaServiceImpl.class);
    
	@Autowired
	Environment environment;

	@Autowired
	private PlaywrightAutomationService playwrightService;

    @Override
	public String getMfaCode(String pythonScript, String secretKey){
		LOG.log(Level.DEBUG, () -> "start getMfaCode method. . .");
		
		try {
			String []cmd = {"python", pythonScript, secretKey};
			
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
			LOG.log(Level.DEBUG, () -> "error retrieving mfa code. . .");
			LOG.error(e);
		}

		return null;
	}

	@Override
	public Page loginAlbertsons(Page page, String loginUrl){
		LOG.log(Level.DEBUG, () -> "start loginAlbertsons method. . .");
		
		try {
			page.navigate(loginUrl);
			
			TimeUnit.SECONDS.sleep(10);

			List<ElementHandle> ssoBtnElements = page.querySelectorAll("button[id=ssoBtn]");
			if (!ssoBtnElements.isEmpty()){ 
				playwrightService.pageClick(page, "button", "id", "ssoBtn");
			}
			
			TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("site.load.wait.time.in.seconds")));

			List<ElementHandle> emailElements = page.querySelectorAll("input[type=email]");
	
			if (!emailElements.isEmpty()){ 
				playwrightService.pageFill(page, "input", "type", "email", environment.getProperty("ldap.property.username"));
				
				List<ElementHandle> submitElements = page.querySelectorAll("input[type=submit]"); // check if the initial email entry page is skipped
				if (!submitElements.isEmpty()){ 
					playwrightService.pageClick(page, "input", "type", "submit");
					TimeUnit.SECONDS.sleep(10);
				}
	
				List<ElementHandle> passwordElements = page.querySelectorAll("input[id=passwordInput]");
				if (!passwordElements.isEmpty()){ 
					playwrightService.pageFill(page, "input", "id", "passwordInput", environment.getProperty("encrypted.ldap.property.password"));
					playwrightService.pageClick(page, "span", "id", "submitButton");
				}
			}

			return page;
			
		} catch (Exception e){
			LOG.log(Level.DEBUG, () -> "error in Albertsons login. . .");
			LOG.error(e);
			return null;
		}

	}

	@Override
	public Page loginGenericAlbertsons(Page page, String loginUrl, String userName, String password) {
		LOG.log(Level.DEBUG, () -> "start loginAlbertsons method. . .");
		
		try {
			page.navigate(loginUrl);
			
			TimeUnit.SECONDS.sleep(10);

			List<ElementHandle> ssoBtnElements = page.querySelectorAll("button[id=ssoBtn]");
			if (!ssoBtnElements.isEmpty()){ 
				playwrightService.pageClick(page, "button", "id", "ssoBtn");
			}
			
			TimeUnit.SECONDS.sleep(Integer.valueOf(environment.getProperty("site.load.wait.time.in.seconds")));

			List<ElementHandle> emailElements = page.querySelectorAll("input[type=email]");
	
			if (!emailElements.isEmpty()){ 
				playwrightService.pageFill(page, "input", "type", "email", userName);
				
				List<ElementHandle> submitElements = page.querySelectorAll("input[type=submit]"); // check if the initial email entry page is skipped
				if (!submitElements.isEmpty()){ 
					playwrightService.pageClick(page, "input", "type", "submit");
					TimeUnit.SECONDS.sleep(10);
				}
	
				List<ElementHandle> passwordElements = page.querySelectorAll("input[id=passwordInput]");
				if (!passwordElements.isEmpty()){ 
					playwrightService.pageFill(page, "input", "id", "passwordInput", password);
					playwrightService.pageClick(page, "span", "id", "submitButton");
				}
			}

			return page;
			
		} catch (Exception e){
			LOG.log(Level.DEBUG, () -> "error in Albertsons login. . .");
			LOG.error(e);
			return null;
		}
	}

}
