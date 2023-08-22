package com.albertsons.argus.mim.service;

import java.util.List;

import com.albertsons.argus.domain.bo.generated.ByProjectBO;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;

public interface MIMAutomationService {
	public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    
    public Page navigateMIMLogin();
    
    public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib);

	public void delay(long seconds);

	public void waitForLoadingFrame(Page mainPage, String waitElem, String frameName, long waitLimit);
    
}
