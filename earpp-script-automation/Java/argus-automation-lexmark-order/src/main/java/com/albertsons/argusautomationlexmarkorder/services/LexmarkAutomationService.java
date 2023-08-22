package com.albertsons.argusautomationlexmarkorder.services;

import org.springframework.stereotype.Service;

import com.albertsons.argusautomationlexmarkorder.dto.OrderSingleton;
import com.albertsons.argusautomationlexmarkorder.dto.Store;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;

@Service
public interface LexmarkAutomationService {
    
    public Page loginLexmarkSite(Browser browser, Page page);

    public void delay(long seconds);

    public Page getSerialNumberOnPage(Browser browser, Page page, Store store, OrderSingleton orderSingleton);

    public Page getAvailableItemsOnPage(Page page, String serialNumber, OrderSingleton orderSingleton, Browser browser);

    public Page placeOrderOnPage(Browser browser, Page page, OrderSingleton orderSingleton);

    public Page clickSupplyOrders(Browser browser, Page page, OrderSingleton orderSingleton);

    public Page logoutLexmarkSite(Browser browser, Page page, OrderSingleton orderSingleton);

    public void nullifyPageAndBrowser(Browser browser, Page page);
    
}
