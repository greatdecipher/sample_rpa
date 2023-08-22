package com.albertsons.argus.ip.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.albertsons.argus.ip.service.PlaywrightService;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;

@Service
public class PlaywrightServiceImpl implements PlaywrightService{
    
    @Autowired 
    private IPAutomationServiceImpl ipAutomationServiceImpl;

    @Override
	public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib) {
		if (mainframe != null) {
    		for (Frame child: mainframe.childFrames()) {
    			child.waitForLoadState(LoadState.LOAD);
         		if (child.name().contains(framename)) {
         			
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
	public void waitForLoadingFrame(Page mainPage, String waitElem, String frameName, long waitLimit) {
		long lWaitCounter = 0;
		Frame frm = mainPage.frame(frameName);
        
		while (frm.content().contains(waitElem)) {
        	frm = mainPage.frame(frameName);
        	ipAutomationServiceImpl.delay(1);
        	lWaitCounter += 1;
        	if (lWaitCounter > waitLimit) {
        		break;
        	}
		}
	}

    public Locator pageLocatorWait(Page page, String attribute, String prop, String value) throws PlaywrightException {
        Locator loc = null;
        if (StringUtils.isNotBlank(attribute)) {
            loc = page.locator(attribute + "[" + prop + " = " + value + "]");
            loc.waitFor();
        } else if (StringUtils.isNotBlank(prop)) {
            loc = page.locator(prop + " = " + value);
            loc.waitFor();
        } else { 
            loc = page.locator(value);
            loc.waitFor();
        }

        return loc;
    }
}
