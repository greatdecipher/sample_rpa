package com.albertsons.argus.mim.service.impl;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.mim.service.MIMAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

@Service
public class MIMAutomationServiceImpl implements MIMAutomationService {

	@Autowired
    private Environment environ;
	
	@Override
	public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib) {
		if (mainframe != null) {
    		for (Frame child: mainframe.childFrames()) {
    			child.waitForLoadState(LoadState.LOAD);
         		if (child.name().contains(framename)) {
         			//System.out.println(child.content());
         			if(child.content().contains(selector)) {
         				child.waitForSelector(attrib + "=" + selector);
         				child.click(attrib + "=" + selector);
            			break;
         			}
         		}
         		else {
         			clickElementInFrame(child, selector, framename, attrib);
         		}
        	}	
    	}
	}

	@Override
	public Page navigateMIMLogin() {
		Browser browser = Playwright.create().chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
        Page page = context.newPage();

        page.navigate(environ.getProperty("playwright.uri.mim"));
        
        page.waitForSelector("input[name=userId]");
        page.fill("input[name=userId]", environ.getProperty("encrypted.mim.property.username"));
        page.fill("input[name=password],type[password]", environ.getProperty("encrypted.mim.property.password"));
        
        page.click("input[name=logonButton]");
        
        page.mainFrame().waitForLoadState(LoadState.LOAD);
        
        return page;
	}

	@Override
	public void waitForLoadingFrame(Page mainPage, String waitElem, String frameName, long waitLimit) {
		long lWaitCounter = 0;
		Frame frm = mainPage.frame(frameName);
        
		while (frm.content().contains(waitElem)) {
        	frm = mainPage.frame(frameName);
        	delay(1);
        	lWaitCounter += 1;
        	if (lWaitCounter > waitLimit) {
        		//TODO: Log exceed wait limit
        		break;
        	}
		}
	}

	@Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
