package com.albertsons.argus.domain.mim.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.mim.service.MIMService;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;

@Service
public class MIMServiceImpl implements MIMService{
    private static final Logger LOG = LogManager.getLogger(MIMServiceImpl.class);

    @Autowired
    Environment environment;
    
	@Autowired
	private PlaywrightAutomationService playwrightService;
    
    @Override
	public Page navigateMimLogin(Browser browser, BrowserContext browserContext, String loginBase) {
		LOG.log(Level.DEBUG, () -> "start navigateMimLogin method . . .");

		try {
			Page page = browserContext.newPage();

			page.navigate(environment.getProperty("playwright.uri.mim." + loginBase + ".login")); //prd3 or prd10
			
			page.waitForSelector("input[name=userId]");
			playwrightService.pageFill(page, "", "", "input[name=userId]", environment.getProperty("encrypted.mim.property.username"));
			playwrightService.pageFill(page, "", "", "input[name=password],type[password]", environment.getProperty("encrypted.mim.property.password"));
			
			playwrightService.pageClick(page, "", "", "input[name=logonButton]");
			
			page.mainFrame().waitForLoadState(LoadState.LOAD);
			
			LOG.log(Level.DEBUG, () -> "end navigateMimLogin method . . .");
			return page;
		} catch (PlaywrightException e){
			LOG.log(Level.INFO, () -> "problem navigating MIM login . . .");
			return null;
		}
		
	}
    
}
