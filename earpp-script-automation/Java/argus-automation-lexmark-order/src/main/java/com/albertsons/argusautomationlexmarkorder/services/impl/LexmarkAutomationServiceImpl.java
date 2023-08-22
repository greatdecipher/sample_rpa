package com.albertsons.argusautomationlexmarkorder.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argusautomationlexmarkorder.dto.ItemType;
import com.albertsons.argusautomationlexmarkorder.dto.OrderSingleton;
import com.albertsons.argusautomationlexmarkorder.dto.OrderedUnit;
import com.albertsons.argusautomationlexmarkorder.dto.Store;
import com.albertsons.argusautomationlexmarkorder.dto.UnitOrder;
import com.albertsons.argusautomationlexmarkorder.services.LexmarkAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LexmarkAutomationServiceImpl implements LexmarkAutomationService {

    @Autowired
    private Environment environ;

    private static Logger LOG = LoggerFactory.getLogger(LexmarkAutomationServiceImpl.class);

    @Autowired
    private PlaywrightAutomationService playwrightAutomationService;
    
    @Override
    public Page loginLexmarkSite(Browser browser, Page page){
        
        try{
            BrowserContext context = browser.newContext(
                new Browser.NewContextOptions()
                .setIgnoreHTTPSErrors(true)
            );

            context.setDefaultTimeout(360000);
            page = context.newPage();
            page.navigate(environ.getProperty("playwright.uri.lexmark"));
            page.waitForLoadState();            
            page.locator("#username").click();
            page.locator("#username").fill(environ.getProperty("lexmark.username"));
            page.locator("#password").click();
            page.locator("#password").fill(environ.getProperty("encrypted.lexmark.property.password"));
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
            page.waitForLoadState();
            
            if(!page.url().equals(environ.getProperty("playwright.url.lexmark.redirection")))
            {
                
            }else {
            	page.close();
                page = null;
                LOG.error("Browser incorrectly redirected to a page..");
            }
            return page;
        }catch(PlaywrightException e){
			String message ="An operation suddenly aborted at loginLexmarkSite. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at loginLexmarkSite. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}
        return null;
    }

    @Override
    public void delay(long seconds) {
        try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
            String message ="Interrupted exception at delay occurred. Please try again.";
			System.out.println(message);
            LOG.error(message);
			LOG.error(e.getMessage());
		}
        
    }

    @Override
    public Page getSerialNumberOnPage(Browser browser,Page page, Store store, OrderSingleton orderSingleton) {
        try{
            page.locator("div").filter(new Locator.FilterOptions()
                .setHasText("Lexmark Managed Services Portal Loading Lexmark Managed Services Portal Menu Alv"))
                .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(" Devices")).click();

            page.locator("div").filter(new Locator.FilterOptions().setHasText(
                Pattern.compile("^All Devices$")))
                .locator("div").click();
            
            page.getByRole(AriaRole.LISTITEM).filter(
                new Locator.FilterOptions().setHasText("All Devices"))
                .click();

            page.locator("div").filter(
                new Locator.FilterOptions().setHasText(Pattern.compile("^Serial Number$")))
                .locator("i").click();

            page.getByRole(AriaRole.LISTITEM).filter(
                new Locator.FilterOptions().setHasText("IPV4 Address"))
                .click();
    
            page.locator("//input[@ng-model='searchByValue']").fill(store.getIpaddress());
            page.locator("button").nth(1).dblclick();
            
            page.waitForLoadState();
            delay(3);
            page.locator("button").nth(1).dblclick();
            int spancount = page.locator("//p//span").count();
            page.locator("(//div[@role='grid'])[2]").scrollIntoViewIfNeeded();
            if(spancount > 0) {
            	page.waitForLoadState();
            	delay(3);
            	String message = page.locator("//p//span").textContent();
                StringBuilder reverseMessage = new StringBuilder();
                reverseMessage.append(message);
                reverseMessage.reverse();
                String reverse=reverseMessage.substring(18,19).toString();
                int conversion = Integer.parseInt(reverse);
                page.waitForLoadState();
                delay(5);
                if(conversion ==1) {
                	int tablecount = page.locator(
                			"//div[@class='ui-grid-contents-wrapper']//div[@role='gridcell']//a[@class='ng-binding ng-scope']"
                			).count();
                	if(tablecount>0) {
                		Locator serialnumber = 
                        page.locator("(//div[@class='ui-grid-contents-wrapper']//div[@role='gridcell']//a[@class='ng-binding ng-scope'])[1]");
                		Locator storenumber = page.locator("div[class='ui-grid-cell-contents ng-binding ng-scope']:right-of(:text('"+ serialnumber.textContent()+ "'))").first();
                        orderSingleton.setSerialNumber(serialnumber.textContent());
                        orderSingleton.getStore().setStorenumber(storenumber.textContent());
                	}else {
                		String messageOut = "Missing table for matching results for serial number for IP Address " + orderSingleton.getStore().getIpaddress() + ". Please try again.";
                        orderSingleton.setMessage(messageOut);
                        orderSingleton.setPrint(false);
                    	System.out.println(messageOut);
                        LOG.error(messageOut);
                    	logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
                	}
                }else if(conversion ==0) {
                	String messageOut = "0 match for the serial number for the IP address " + orderSingleton.getStore().getIpaddress() + ". Please try again.";
                	orderSingleton.setMessage(messageOut);
                    orderSingleton.setPrint(false);
                	System.out.println(messageOut);
                    LOG.error(messageOut);
                	logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
                }
                else if(conversion>1){
                	String messageOut = "Multiple matches for the serial number for the IP address " + orderSingleton.getStore().getIpaddress() + ". Please try again.";
                	orderSingleton.setMessage(messageOut);
                    orderSingleton.setPrint(false);
                	System.out.println(messageOut);
                    LOG.error(messageOut);
                	logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
                }else {
                	String messageOut = "Failed to search the serial number for the IP Address " + orderSingleton.getStore().getIpaddress() + ". Please try again.";
                	orderSingleton.setMessage(messageOut);
                    orderSingleton.setPrint(false);
                	System.out.println(messageOut);
                    LOG.error(messageOut);
                	logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
                }

            }else {
                	String messageOut = "Search for the serial number for " + orderSingleton.getStore().getIpaddress() + " failed. Please try again later.";
                    System.out.println(messageOut);
                    LOG.error(messageOut);
                	orderSingleton.setPrint(false);
                    logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
                }

            return page;

        }catch(PlaywrightException e){
			String message ="An operation suddenly aborted at getSerialNumberOnPage. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at getSerialNumberOnPage. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}
        return null;
    }

    @Override
    public Page placeOrderOnPage(Browser browser, Page page, OrderSingleton orderSingleton) {
        try{
            page.waitForLoadState();
            delay(3);
            int itemscount = page.locator(
                    "//div[@catalog]//div[@ui-grid='catalogOptions']//div[@role='gridcell']//button"
                    ).count();
            if(itemscount <= 0) {
                String warningmessage = "The table for New Order Summary is missing. Please try again later.";
                orderSingleton.setPrint(false);
                System.out.println(warningmessage);
                LOG.error(warningmessage);
                logoutAndSetPageAndBrowserNull(browser, page,orderSingleton);
            }
            List<UnitOrder> listUnitOrder = orderSingleton.getUnitOrderList();
            List<ItemType> listItemTypes = orderSingleton.getListItemType();
            List<OrderedUnit> listOrderedUnits = new ArrayList<OrderedUnit>();
            List<UnitOrder> newListUnitOrders = new ArrayList<UnitOrder>();
            int i=1;
            int unitsForToner =0;
            for(UnitOrder unitOrder: listUnitOrder){
                if(unitOrder.getType().contains("Toner Cartridge")||
                        unitOrder.getType().contains("Toner")||
                        unitOrder.getType().contains("Cartridge")) {
                    for(ItemType itemType: listItemTypes){
                        if(itemType.getType().contains("Cartridge")||
                                itemType.getType().contains("Toner")||
                                itemType.getType().contains("Toner Cartridge")) {
                            OrderedUnit orderedUnit = new OrderedUnit();
                            orderedUnit.setType(itemType.getType());
                            orderedUnit.setPartNumber(itemType.getPartNumber());
                            unitsForToner+=unitOrder.getQuantity();
                            orderedUnit.setQuantity(unitsForToner);
                            Locator button = page.locator(
                                "(//div[@catalog]//div[@ui-grid='catalogOptions']//div[@role='gridcell']//button)"
                                +"["+itemType.getButtonOrderNumber()+"]"
                            );
                            orderedUnit.setOrderNumber(i);
                            button.click();
                            listOrderedUnits.add(orderedUnit);
                            unitOrder.setOrderedUnit(orderedUnit);
                        }
                        
                    }
                    
                }else {
                    for(ItemType itemType: listItemTypes){
                        
                        if(itemType.getType().contains(unitOrder.getType())){
                            OrderedUnit orderedUnit = new OrderedUnit();
                            orderedUnit.setType(itemType.getType());
                            orderedUnit.setPartNumber(itemType.getPartNumber());
                            orderedUnit.setQuantity(unitOrder.getQuantity());
                            Locator button = page.locator(
                                "(//div[@catalog]//div[@ui-grid='catalogOptions']//div[@role='gridcell']//button)"
                                +"["+itemType.getButtonOrderNumber()+"]"
                            );
                            orderedUnit.setOrderNumber(i);
                            button.click();
                            listOrderedUnits.add(orderedUnit);
                            unitOrder.setOrderedUnit(orderedUnit);
                        }
                    }
                    
                }
                
                i++;
                newListUnitOrders.add(unitOrder);
            }

            orderSingleton.setListOrderedUnit(listOrderedUnits);
            orderSingleton.setUnitOrderList(newListUnitOrders);

            //New Order Summary table
            int numberOfNotNulls = 0;
            for(OrderedUnit orderedUnit: listOrderedUnits){
                if(orderedUnit== null) {}
                else {
                    numberOfNotNulls++;
                    Locator input = page.locator(
                        "(//div[@class='ui-grid-render-container ng-isolate-scope ui-grid-render-container-body']//input)"
                        + "["+ orderedUnit.getOrderNumber() +"]"
                    );
                    input.fill(Integer.toString(orderedUnit.getQuantity()));
                }
            }
            
            int countwarning = page.locator(
                    "//span[@translate-values='message']"
                    ).count();
            if(countwarning > 0) {
                String messagewarning = page.locator(
                        "//span[@translate-values='message']"
                        ).textContent();
                System.out.println(messagewarning);
                LOG.error(messagewarning);
                orderSingleton.setPrint(false);
                logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);	
            }
                
            if(numberOfNotNulls >0) {
                Locator submitButton = page.locator(
                        "//button[@translate='DEVICE_MAN.MANAGE_DEVICE_SUPPLIES.BTN_NEW_ORDER_SUBMIT']"
                        );
                    submitButton.click();

                    //Retrieving shipping address
                    page.waitForLoadState();
                    delay(2);
                    Locator shippingAddress = page.locator(
                        "//p[@ng-bind-html='formatedShipToAddress']"
                        );

                    orderSingleton.setShippingAddress(shippingAddress.textContent());
                    orderSingleton.setPrint(true);
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SUBMIT SUPPLIES ORDER")).click();
                    
                    page.waitForLoadState();
                    delay(5);
                    page.waitForLoadState();
                    delay(3);
                    
                    if(page.locator("//div[contains(text(),'Order Number')]/following-sibling::div[@class='ng-binding']").count()>0) {
        
                    String orderNumber = page.locator("//div[contains(text(),'Order Number')]/following-sibling::div[@class='ng-binding']").textContent();
                    if(orderNumber.equals(" generating request number...")) {
                    		page.waitForLoadState();
                     		delay(8);
                     	}
                     	orderSingleton.setOrderNumber(orderNumber);
                        
                     }else {
                     	String messageOut = "Order Processing went too long for " + orderSingleton.getStore().getIpaddress() + ". Please try again.";
                     	orderSingleton.setMessage(messageOut);
                        orderSingleton.setPrint(false);
                     	System.out.println(messageOut);
                        LOG.error(messageOut);
                     	logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
                     }
                    
            }else{
                String messageOut ="Orders are null.";
                System.out.println(messageOut);
                LOG.error(messageOut);
                orderSingleton.setPrint(false);
                logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);	
            }
            
        return page;    
        }catch(PlaywrightException e){
			String message ="An operation suddenly aborted at placeOrderOnPage. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at placeOrderOnPage. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}
    	return null;
    }

    @Override
    public Page clickSupplyOrders(Browser browser, Page page, OrderSingleton orderSingleton){
        try{
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(orderSingleton.getSerialNumber().toString())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Supply orders")).click();
            return page;
        }catch(PlaywrightException e){
			String message ="An operation suddenly aborted at clickSupplyOrders. Please try again.";
			System.out.println(message);
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at clickSupplyOrders. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}
        return null;
    }

    @Override
    public Page logoutLexmarkSite(Browser browser, Page page, OrderSingleton orderSingleton) {
        try{
            Locator logout =  page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Log out"));
            if(logout.count()>0){
                logout.click();
                return page;
            }else{
                String messageOut ="Logout button is missing. Please try again.";
                System.out.println(messageOut);
                LOG.error(messageOut);
                orderSingleton.setPrint(false);
                logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
            }
        }catch(PlaywrightException e){
			String message ="An operation suddenly aborted at logoutLexmarkSite. Please try again.";
			System.out.println(message);
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at logoutLexmarkSite. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}
        return null;
    }

    @Override
    public Page getAvailableItemsOnPage(Page page, String serialNumber, OrderSingleton orderSingleton, Browser browser) {
        try{
            Locator placeNewOrder = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Place a new supplies or service parts order"));
            if(placeNewOrder.count()>0){
                placeNewOrder.click();
                delay(3);
                page.waitForLoadState();
                //page.locator("//span[contains(text(),'Order Number')])[1]").scrollIntoViewIfNeeded();
                int itemcount = page.locator(
                    "//div[@catalog]//div[@ui-grid='catalogOptions']//div[@role='row']//div[@role='gridcell']//div[@class='ui-grid-cell-contents ng-binding ng-scope']"
                    ).count();
        
                if(itemcount > 0) {
                    
                    List<ItemType> listItemType = new ArrayList<ItemType>();
                    int rowscount = itemcount/3;
                    int start = 1;
                    int end = 3;
                    for(int i = 1;i<=rowscount;i++){
                        ItemType itemType = new ItemType();
                        if(start>itemcount){break;}
                        for(int j = start; j<=end; j++){
                            Locator div = page.locator(
                            "(//div[@catalog]//div[@ui-grid='catalogOptions']//div[@role='row']//div[@role='gridcell']//div[@class='ui-grid-cell-contents ng-binding ng-scope'])"
                            + "[" + j+ "]");
                            switch(j%3){
                                case 1:
                                    itemType.setType(div.textContent());
                                    break;
                                case 2:
                                    itemType.setDescription(div.textContent());
                                    break;
                                case 0:
                                    itemType.setPrice(div.textContent());
                                    break; 
                            }
                        }
                        start = end+1;
                        end = start+2;
                        Locator span = page.locator(
                            "(//div[@catalog]//div[@ui-grid='catalogOptions']//div[@role='gridcell']//span[@class='ng-binding ng-scope'])"
                            +"["+ i+"]"
                        );
                        itemType.setPartNumber(span.textContent());
                        itemType.setButtonOrderNumber(i);
                        listItemType.add(itemType);
                    }
                    orderSingleton.setListItemType(listItemType);
                }else {
                        String messageOut = "The table for available order items for IP Address " 
                                + orderSingleton.getStore().getIpaddress() 
                                + " can not be found on the site. Please try again later.";
                        orderSingleton.setPrint(false);
                        System.out.println(messageOut);
                        LOG.error(messageOut);
                        logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
                    }
                return page;

            }else{
                String messageOut = "The button Place New Order did not render. Please try again later.";
                orderSingleton.setPrint(false);
                System.out.println(messageOut);
                LOG.error(messageOut);
                logoutAndSetPageAndBrowserNull(browser, page, orderSingleton);
            }
        }catch(PlaywrightException e){
			String message ="An operation suddenly aborted at getAvailableItemsOnPage. Please try again.";
			System.out.println(message);
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at getAvailableItemsOnPage. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}
        return null;
    }
    
    public void logoutAndSetPageAndBrowserNull(Browser browser, Page page, OrderSingleton orderSingleton) {
        try{
            logoutLexmarkSite(browser,page,orderSingleton);
            nullifyPageAndBrowser(browser, page);

        }catch(PlaywrightException e){
			String message ="An operation suddenly aborted at logoutAndSetPageAndBrowserNull. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}catch(Exception e){
			String message ="Exception occurred at logoutAndSetPageAndBrowserNull. Please try again.";
			LOG.error(message);
			LOG.error(e.getMessage());
            nullifyPageAndBrowser(browser, page);
		}
    }

    @Override
    public void nullifyPageAndBrowser(Browser browser, Page page){
        page.close();
        playwrightAutomationService.closedBrowser(browser);
        page = null;
        browser = null;
    }

}
