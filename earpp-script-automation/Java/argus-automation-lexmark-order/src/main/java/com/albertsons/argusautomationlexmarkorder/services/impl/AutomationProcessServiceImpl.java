package com.albertsons.argusautomationlexmarkorder.services.impl;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argusautomationlexmarkorder.dto.OrderSingleton;
import com.albertsons.argusautomationlexmarkorder.dto.Store;
import com.albertsons.argusautomationlexmarkorder.services.AutomationProcessService;
import com.albertsons.argusautomationlexmarkorder.services.LexmarkAutomationService;
import com.albertsons.argusautomationlexmarkorder.utilities.JSONParserUtil;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AutomationProcessServiceImpl implements AutomationProcessService{

     @Autowired
	private LexmarkAutomationService service;

	@Autowired
    private PlaywrightAutomationService playwrightAutomationService;

	private static Logger LOG = LoggerFactory.getLogger(AutomationProcessServiceImpl.class);
    
    @Override
    public void initiatePlaceOrders(OrderSingleton orderSingleton) {
		Page page = null;
		Browser browser = null;
		try(Playwright playwright = Playwright.create()){
			browser = playwrightAutomationService.openBrowser();
			page = service.loginLexmarkSite(browser, page);
			if (page == null){
				playwrightAutomationService.closedBrowser(browser);
				browser = playwrightAutomationService.openBrowser();
				page = service.loginLexmarkSite(browser, page);
			}
			placeOrders(browser, page, orderSingleton.getStore(),orderSingleton);
			service.delay(2);
			JSONParserUtil jsonParserUtil = new JSONParserUtil();
			System.out.println(jsonParserUtil.jsonItemToJson(orderSingleton));
			service.nullifyPageAndBrowser(browser, page);
		}catch(PlaywrightException e){
			String message ="An operation suddenly aborted at initiatePlaceOrders. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
			service.nullifyPageAndBrowser(browser,page);
		}catch(Exception e){
			String message ="Exception occurred at initiatePlaceOrders. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
			service.nullifyPageAndBrowser(browser, page);
		}
    }

    private void placeOrders(Browser browser, Page page, Store store, OrderSingleton orderSingleton){
		try{
			page.waitForLoadState();
			page = service.getSerialNumberOnPage(browser, page, store, orderSingleton);
			page = service.clickSupplyOrders(browser, page, orderSingleton);
			page = service.getAvailableItemsOnPage(page, orderSingleton.getSerialNumber(), orderSingleton, browser);
			page = service.placeOrderOnPage(browser, page, orderSingleton);
			page = service.logoutLexmarkSite(browser, page, orderSingleton);
			page.close();
			service.delay(2);
		}catch(PlaywrightException e){
			String message ="An operation suddenly aborted at placeOrders. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
			service.nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at placeOrders. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
			service.nullifyPageAndBrowser(browser, page);
		}
	}
    
}
