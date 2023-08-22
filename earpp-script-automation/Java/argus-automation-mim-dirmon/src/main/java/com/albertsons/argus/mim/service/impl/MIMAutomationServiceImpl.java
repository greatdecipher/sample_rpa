package com.albertsons.argus.mim.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.mim.exception.ArgusMimException;
import com.albertsons.argus.mim.service.MIMAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class MIMAutomationServiceImpl implements MIMAutomationService {
	public static final Logger LOG = LogManager.getLogger(MIMAutomationServiceImpl.class);
	@Autowired
    private Environment environ;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public void startMimDirmon(Page page) throws ArgusMimException{
		//String tableResult = "";
		
		LOG.log(Level.DEBUG, () -> "Start mainMimMonitoring method . . .");
		
		clickElementInFrame(page.mainFrame(), "Managed File Transfer", "menu", "text");
        page.mainFrame().waitForLoadState(LoadState.LOAD);
        
        clickElementInFrame(page.mainFrame(), "Directory Monitor", "menu", "text");
        page.mainFrame().waitForLoadState(LoadState.LOAD);
        
        waitForLoadingFrame(page, "Loading", "text", 300);
		LOG.log(Level.DEBUG, () -> "End mainMimMonitoring method . . .");

	} 
	
	@Override
	public void clickElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		
		try {
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
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	}
	
	@Override
	public void checkUncheckElementInFrame(Frame mainframe, String selector, String framename, String attrib, String check) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		
		try {
			if (mainframe != null) {
	    		for (Frame child: mainframe.childFrames()) {
	    			child.waitForLoadState(LoadState.LOAD);
	         		if (child.name().contains(framename)) {
	         			if(child.content().contains(selector)) {
	         				List<ElementHandle> inputList = child.querySelectorAll("input");
	         				for (ElementHandle elemHandle : inputList) {
	         					if (elemHandle != null && elemHandle.getAttribute("type").contains("checkbox")) {
	         						//System.out.print(elemHandle.getAttribute("name"));
	         						if (elemHandle.getAttribute("name").contains("isEnabled")) {
	         							if (elemHandle.getAttribute(attrib).contains(selector)) {
		         							if (check == "check") {
		        								elemHandle.check();
		        							}
		        	         				else {
		        	         					elemHandle.uncheck();
		        							}
		        	         				//child.click(attrib + "=" + selector);
		        	            			break;
										
	         							}	
									}
	         						
								}
							}
	         				
	         			}
	         		}
	         		else {
	         			clickElementInFrame(child, selector, framename, attrib);
	         		}
	        	}	
	    	}
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	}
	
	@Override
	public boolean checkElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		boolean bool = false;
		try {
			if (mainframe != null) {
	    		for (Frame child: mainframe.childFrames()) {
	    			child.waitForLoadState(LoadState.LOAD);
	         		if (child.name().contains(framename)) {
	         			if(child.content().contains(selector)) {
	         				bool = child.waitForSelector(attrib + "=" + selector).isVisible();
	         				//System.out.print(bool);
	         				//child.click(attrib + "=" + selector);
	         				
	            			break;
	         			}
	         		}
	         		else {
	         			checkElementInFrame(child, selector, framename, attrib);
	         		}
	        	}	
	    	}
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		return bool;
		//return false;
	}
	
	@Override
	public String getTextElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		
		try {
			if (mainframe != null) {
	    		for (Frame child: mainframe.childFrames()) {
	    			child.waitForLoadState(LoadState.LOAD);
	         		if (child.name().contains(framename)) {
	         			if(child.content().contains(selector)) {
	         				child.waitForSelector(attrib + "=" + selector);
	         				LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	         				return child.getAttribute(attrib + "=" + selector, "value");
	         			}
	         		}
	         		else {
	         			clickElementInFrame(child, selector, framename, attrib);
	         		}
	        	}	
	    	}
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

		
		return "";
	}
	
	@Override
	public void dblClickElementInFrame(Frame mainframe, String selector, String framename, String attrib) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		
		try {
			if (mainframe != null) {
	    		for (Frame child: mainframe.childFrames()) {
	    			child.waitForLoadState(LoadState.LOAD);
	         		if (child.name().contains(framename)) {
	         			if(child.content().contains(selector)) {
	         				child.waitForSelector(attrib + "=" + selector);
	         				child.dblclick(attrib + "=" + selector);
	            			break;
	         			}
	         		}
	         		else {
	         			clickElementInFrame(child, selector, framename, attrib);
	         		}
	        	}	
	    	}
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	}
	
	@Override
	public void clickElementInFrameByTagList(Frame mainframe, String selector, String framename, String attrib, String tag) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		
		try {
			if (mainframe != null) {
	    		for (Frame child: mainframe.childFrames()) {
	    			child.waitForLoadState(LoadState.LOAD);
	         		if (child.name().contains(framename)) {
	         			if (child.name().length() == framename.length()) {
		         			if(child.content().contains(selector)) {
		         				if (selector.contains("deletedirmonentry")) {
		         					selector = selector.replace("&amp;", "&");
								}
		         				List<ElementHandle> tagList = child.querySelectorAll(tag);
		         				for (ElementHandle elemHandle : tagList) {
		         					if (elemHandle.getAttribute(attrib) != null) {
		         						if (selector == "Save") {
		         							if (elemHandle.getAttribute(attrib).length() == 4 && 
		         							elemHandle.getAttribute(attrib).contains(selector) == true) {
		         								elemHandle.click();
		    	         						break;
											}
										}
		         						else {
											if (elemHandle.getAttribute(attrib).contains(selector) == true) {
												elemHandle.click();
				         						break;
											}
										}
		         						
		         					}
								}
		         			}
						}
	         		}
	         		else {
	         			clickElementInFrameByTagList(child, selector, framename, attrib, tag);
	         		}
	        	}	
	    	}
		} catch (PlaywrightException pw) {
			LOG.log(Level.DEBUG, () -> "clickElementInFrame Error: " + pw.getMessage());
			throw new ArgusMimException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	}
	
	@Override
	public void inputElementInFrame(Frame mainframe, String selector, String framename, String attrib, String inputValue) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End inputElementInFrame method . . .");
		try {
			if (mainframe != null) {
	    		for (Frame child: mainframe.childFrames()) {
	    			child.waitForLoadState(LoadState.LOAD);
	         		if (child.name().contains(framename)) {
	         			if(child.content().contains(selector)) {
	         				List<ElementHandle> inputList = child.querySelectorAll("input");
	         				for (ElementHandle elemHandle : inputList) {
	         					if (elemHandle != null) {
	         						if (elemHandle.getAttribute(attrib).contains(selector) == true) {
										if (selector == "queueName" || selector == "recoveryQueueName") {
											elemHandle.fill(elemHandle.getAttribute("value") + "." + inputValue);
											break;
										}
										elemHandle.fill(inputValue);
										break;
									}
								}
							}
	         			}
	         		}
	         		else {
	         			inputElementInFrame(child, selector, framename, attrib, inputValue);
	         		}
	        	}	
	    	}
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	}
	
	@Override
	public void selectOptionElementInFrame(Frame mainframe, String selector, String framename, String attrib, String labelValue) throws ArgusMimException{
		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
		
		try {
			if (mainframe != null) {
	    		for (Frame child: mainframe.childFrames()) {
	    			child.waitForLoadState(LoadState.LOAD);
	         		if (child.name().contains(framename)) {
	         			if(child.content().contains(selector)) {
	         				List<ElementHandle> elemHandle = child.querySelectorAll("select");
	         				for (ElementHandle elem: elemHandle) {
								if (elem.getAttribute(attrib).contains(selector)) {
									elem.selectOption(new SelectOption().setLabel(labelValue));
									break;
								}
							}
	         				//child.waitForSelector(attrib + "=" + selector);
	         				//child.selectOption(attrib + "=" + selector,labelValue);
	            			
	         			}
	         		}
	         		else {
	         			selectOptionElementInFrame(child, selector, framename, attrib, labelValue);
	         		}
	        	}	
	    	}
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

		LOG.log(Level.DEBUG, () -> "End clickElementInFrame method . . .");
	}

	@Override
	public Page navigateMIMLogin() throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start navigateMIMLogin method . . .");
		
		try {
			Browser browser = Playwright.create().chromium()
	                .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
			
	        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
	        Page page = context.newPage();
	        
	        page.navigate(environ.getProperty(MIM_LOGIN_URL));
	        
	        page.waitForSelector("input[name=userId]");
	        page.fill("input[name=userId]", environ.getProperty("encrypted.mim.property.username"));
	        //System.out.print(environ.getProperty("encrypted.mim.property.password"));
	        page.fill("input[name=password],type[password]", environ.getRequiredProperty("encrypted.mim.property.password"));
	        
	        page.click("input[name=logonButton]");
	        
	        page.mainFrame().waitForLoadState(LoadState.LOAD);
	        
	        LOG.log(Level.DEBUG, () -> "End navigateMIMLogin method . . .");
	        
	        return page;
	        
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}
   
	}
	
	@Override
	public void inputLabelDetails(Page MainPage, String LabelName, String LabelThresh) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start inputLabelDetails method . . .");
		
		try {
			List<ElementHandle> SelectorColl;
			MainPage.click("text=Edit Unsaved Filter");
			delay(1);
			MainPage.fill("id=descriptionCmpId",LabelName);
			delay(1);
			MainPage.click("id=x-form-el-daystoshowcombo >> img");
			delay(1);
			SelectorColl = MainPage.querySelectorAll("div");
			
			for (ElementHandle EH: SelectorColl) {
				if (EH.innerText().length() >= 1 && EH.innerText().length() <= 2) {
					if (EH.getAttribute("class").contains("x-combo-list-item") && EH.innerText().contains(environ.getProperty("argus.mim.label.days.show"))) {
						EH.click();
					}
					
				}
			}
			
			MainPage.uncheck("id=completedCmpId");
			MainPage.check("id=failedCmpId");
			MainPage.check("id=warningCmpId");
			MainPage.check("id=inprogressCmpId");
			MainPage.check("id=fixitCmpId");
			MainPage.click("text=Apply");
			delay(3);
			
			LOG.log(Level.DEBUG, () -> "End inputLabelDetails method . . .");
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

	}
	
	@Override
	public String getResultTableValues(Page MainPage, String LabelThresh) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start getResultTableValues method . . .");
		final String sMessageDebug;
		int iIconStart, iIconEnd, charToAscii, ctr;
		String sProgressIcon = "", sDescription,
				sProcessId, sStartTime, sSource, sDestination, sState, sLine, sPopStr = "", sMessage;
		String sFailedList = "", sClockingList = "", sFailedAndClockingList, FilePath;
		long dFileTimestamp,iDateDiff;
		ArrayList<String> sColumnArr = new ArrayList<>();
		char charAt;
		
		try {
			ElementHandle objTable = MainPage.querySelector("id=ext-gen188");
			
			if (objTable != null) {
				for (ElementHandle oCrElemTR: objTable.querySelectorAll("tr")) {
					sColumnArr.clear();
					ctr=0;
					iIconStart = oCrElemTR.innerHTML().indexOf("td-statusimg ") + 13;
					iIconEnd = oCrElemTR.innerHTML().indexOf("\" style=");
					
					if (iIconEnd - iIconStart > 0) {
						sProgressIcon = oCrElemTR.innerHTML().substring(iIconStart,iIconEnd);
					}	
					
					for (ElementHandle oCrElemTD: oCrElemTR.querySelectorAll("td")) {
						ctr+=1;
						if (ctr == 2 | ctr == 4 | ctr == 5 | ctr == 7 | ctr == 8 | ctr == 9) {
							charAt = oCrElemTD.innerText().charAt(0);
							charToAscii = charAt;
							if (charToAscii == 160) {
								sColumnArr.add("");
							}
							else {
								sColumnArr.add(oCrElemTD.innerText());
							}
						}
					}

					if (sColumnArr.size() > 0) {
					
						sDescription = sColumnArr.get(0);
						sProcessId = sColumnArr.get(1);
						sStartTime = sColumnArr.get(2);
						sSource = sColumnArr.get(3);
						sDestination = sColumnArr.get(4);
						sState = sColumnArr.get(5);
						
						sFailedAndClockingList = sFailedList + sClockingList;
						
						if (sFailedAndClockingList.indexOf(sProcessId) < 1) {
							sLine = padRight(sDescription, 45) + " " + sProcessId + " " + sStartTime + " " + sSource
								+ " " + sDestination + " " + sState;
							
							if (sProgressIcon.toUpperCase().indexOf("SUCESS") < 1) {
								if ((sDescription + " " + sProgressIcon.replace("summary", "").toUpperCase()).length() < 28) {
									sPopStr = sPopStr + padRight(sDescription, 27) + ": " + sProgressIcon.replace("summary", "").toUpperCase().substring(0,5) + "\n";
								}
								else {
									sPopStr = sPopStr + padRight(sDescription.substring(0, 14) + " " + sDescription.substring(sDescription.length() - 12, sDescription.length()) ,27)
										+ ": " + sProgressIcon.replace("summary", "").toUpperCase().substring(0,5) + "\n";
								}
							}
							
							switch (sProgressIcon) {
							case "error":
							case "errorsummary":
								sFailedList = sFailedList + "warn_red_start" + sLine + "warn_red_end" + "\n";
								break;
							case "fixit":
							case "fixitsummary":
								sFailedList = sFailedList + "warn_orange_start" + sLine + "warn_orange_end" + "\n";
								break;
							case "warning":
							case "warningsummary":
								sFailedList = sFailedList + "warn_blue_start" + sLine + "warn_blue_end" + "\n";
								break;
							case "progress":
							case "progresssummary":
								if (sProcessId.length() > 0) {
									FilePath = environ.getProperty(LOG_FOLDER).replace("userID", System.getProperty("user.name")) + sProcessId;
									if (!fileExists(FilePath)) {
										writeLog(sDescription + " " + sProcessId + " " + sStartTime + " " + sState, 
												FilePath);
									}
									else {
										LocalDateTime ldt;
										dFileTimestamp = getFTimestamp(FilePath);
										ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(dFileTimestamp), 
				                                TimeZone.getDefault().toZoneId());
										iDateDiff = ChronoUnit.MINUTES.between(ldt, LocalDateTime.now());
										if (iDateDiff > Integer.parseInt(LabelThresh)) {
											sClockingList = sClockingList + sLine + "\n";
										}
									}
								}
								break;
							default:
								break;
							}
						}
					}
					else {
						LOG.log(Level.DEBUG, () -> "Unable to get table info in Function ScanLabel . . .");
					}
				}
			}
			else {
				LOG.log(Level.DEBUG, () -> "Unable to get table info in Function ScanLabel . . .");
			}
			
			sFailedAndClockingList = sFailedList + sClockingList;
			
			if (sFailedAndClockingList.length() > 0) {
				sMessage = sFailedList + sClockingList;
				sMessage = sMessage.replace("warn_red_start", "").replace("warn_red_end", "");
				sMessage = sMessage.replace("warn_orange_start", "").replace("warn_orange_end", "");
				sMessage = sMessage.replace("warn_blue_start", "").replace("warn_blue_end", "");
				
				sPopStr = sPopStr.replace("PROG", "Clock");
				sPopStr = sPopStr.replace("FIXIT", "Fixit");
				sPopStr = sPopStr.replace("ERROR", "Error");
				sPopStr = sPopStr.replace("WARNI", "Warn");
				
				sMessageDebug = sMessage;
				LOG.log(Level.DEBUG, () -> "Failed Labels found: " + sMessageDebug);
			}
	
		} catch (Exception e) {
			throw new ArgusMimException(e.getMessage());
		}
		
		LOG.log(Level.DEBUG, () -> "End getResultTableValues method . . .");
		return sFailedList + sClockingList;
		
	}
	
	@Override
	public String[] getLabelDetails(String LabelNames) {
		LOG.log(Level.DEBUG, () -> "Splitting Label to get details . . .");

		return LabelNames.split("~");
		
	}
	
	@Override
	public void navigateTransferStatusPage(Page MainPage) {
		LOG.log(Level.DEBUG, () -> "Start navigateTransferStatusPage method . . .");
		
		MainPage.navigate(environ.getProperty(MIM_LOGIN_URL)
	        	+ environ.getProperty("playwright.uri.mim.monitoring"));
		
		LOG.log(Level.DEBUG, () -> "End navigateTransferStatusPage method . . .");
	}
	
	@Override
	public void waitLoadMainPage(Page MainPage, String WaitElement, long WaitLimit) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start waitLoadMainPage method . . .");
		
		long lWaitCtr = 0;
		try {
			ElementHandle EH = MainPage.querySelector("text=" + WaitElement);
			while (EH != null) {
				lWaitCtr += 1;
				EH = MainPage.querySelector("text=" + WaitElement);
				delay(1);
				if (lWaitCtr > WaitLimit) {
					LOG.log(Level.DEBUG, () -> "waitLoadMainPage Wait Limit Exceeded. . .");
					throw new ArgusMimException("Element: text=" + WaitElement);
				}
			}
			
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}
		
		LOG.log(Level.DEBUG, () -> "End waitLoadMainPage method . . .");
		
	}
	
	@Override
	public void waitForLoadingFrame(Page MainPage, String WaitElem, String FrameName, long WaitLimit) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start waitForLoadingFrame method . . .");
		
		long lWaitCounter = 0;
		try {
			Frame frm = MainPage.frame(FrameName);
	        
			while (frm.content().contains(WaitElem)) {
	        	frm = MainPage.frame(FrameName);
	        	delay(1);
	        	lWaitCounter += 1;
	        	if (lWaitCounter > WaitLimit) {
	        		LOG.log(Level.DEBUG, () -> "waitLoadMainPage Wait Limit Exceeded. . .");
					throw new ArgusMimException("Element: text=" + WaitElem);
	        	}
			}
			
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}
		
		LOG.log(Level.DEBUG, () -> "End waitForLoadingFrame method . . .");
	}

	@Override
	public void mimFormatAndSendEmailBody(String EmailBody) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start mimFormatAndSendEmailBody method . . .");
		
		AutomationUtil util = new AutomationUtil();
		
		EmailBody = EmailBody.replace("\n", "<br>");
		EmailBody = "<font face='Courier New' color='black' size='1'>" + EmailBody + "</font>";
		EmailBody = EmailBody.replace("warn_red_start", "<font color='red'>");
		EmailBody = EmailBody.replace("warn_red_end", "</font>");
		EmailBody = EmailBody.replace("warn_orange_start", "<font color='orange'>");
		EmailBody = EmailBody.replace("warn_orange_end", "</font>");
		EmailBody = EmailBody.replace("warn_blue_start", "<font color='blue'>");
		EmailBody = EmailBody.replace("warn_blue_end", "</font>");
		
		try {
			emailService.sendSimpleMessage(environ.getProperty("mail.mim.monitoring.from"), 
					environ.getProperty("mail.mim.monitoring.from.alias"), 
					environ.getProperty("mail.mim.monitoring.recipients", String[].class), 
					environ.getProperty("mail.mim.monitoring.cc", String[].class), 
					environ.getProperty("mail.mim.monitoring.subject") + " - " 
					+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"US/Arizona"), //TODO: must trasfer to property files
					EmailBody, NORMAL_PRIORITY, true);
		} catch (ArgusMailException e) {
			LOG.log(Level.DEBUG, () -> "Error in mimFormatAndSendEmailBody method . . .");
			//TODO: Implement
		}
		
		LOG.log(Level.DEBUG, () -> "End mimFormatAndSendEmailBody method . . .");
	}
	
	@Override
	public void delay(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String padRight(String sInStr, int iLength) {
		LOG.log(Level.DEBUG, () -> "Run padRight method . . .");
		
		return String.format("%1$-" + iLength + "s", sInStr).substring(0,iLength);
	}
	
	@Override
	public boolean fileExists(String FilePath) {
		LOG.log(Level.DEBUG, () -> "Run fileExists method . . .");
		
		try {
			File FileName = new File(FilePath);
			
			if (FileName.exists()) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		
	}
	
	@Override
	public long getFTimestamp(String sFile) {
		LOG.log(Level.DEBUG, () -> "Run getFTimestamp method . . .");
		
		File FileName = new File(sFile);
		return FileName.lastModified();
	}
	
	@Override
	public void writeLog(String strMessage, String sFile) {
		LOG.log(Level.DEBUG, () -> "Start writeLog method . . .");
		
		try {
			
			if (!fileExists(sFile)) {
				File myObj = new File(sFile);
				myObj.createNewFile();	
			}
			
			FileWriter fw = new FileWriter(sFile,true);
		    fw.write(strMessage + "\n");
		    fw.close();
		    
		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "writeLog method failed . . .");
		}
		
		LOG.log(Level.DEBUG, () -> "End writeLog method . . .");
	
	}
	
	
	@Override
	public void deleteFilesByAge(String sFolder,Integer iMaxAge) {
		LOG.log(Level.DEBUG, () -> "Start deleteFilesByAge method . . .");
		
		File FolderPath = new File(sFolder);
		Path path; 
		LocalDateTime ldt; 
		BasicFileAttributes bfa;
		
		long iDateDiff;
		
		if (FolderPath.isDirectory()) {
			try {
				for (File fName: FolderPath.listFiles()) {
					path = Paths.get(fName.getPath());
					bfa = Files.readAttributes(path, BasicFileAttributes.class);
					ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(bfa.creationTime().toMillis()), 
			                TimeZone.getDefault().toZoneId());
					iDateDiff = ChronoUnit.DAYS.between(ldt, LocalDateTime.now());
					if (iDateDiff > iMaxAge) {
						fName.delete();
					}
				}
			} catch (Exception e) {
				LOG.log(Level.DEBUG, () -> "deleteFilesByAge method failed . . .");
			}
			
		}
		
		LOG.log(Level.DEBUG, () -> "End deleteFilesByAge method . . .");
	}
	
	@Override
	public void createFolder(String FolderURL) {
		LOG.log(Level.DEBUG, () -> "Start createFolder method . . .");
		try {
			File FolderName = new File(FolderURL);
			if (!FolderName.isDirectory()) {
				FolderName.mkdir();
			}
		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "createFolder method failed . . .");
		}
		
		LOG.log(Level.DEBUG, () -> "End createFolder method . . .");
	}
	
	@Override
	public void createDirmonInstance(Page page, String uniqueNodeName, String registryLabel, String queueManager) throws ArgusMimException{
		
		clickElementInFrame(page.mainFrame(), "New", "text", "text");
    	page.mainFrame().waitForLoadState(LoadState.LOAD);
    	
    	selectOptionElementInFrame(page.mainFrame(), "dirmonNode", "text", "id", uniqueNodeName);
    	selectOptionElementInFrame(page.mainFrame(), "regEle", "text", "id", registryLabel);
    	inputElementInFrame(page.mainFrame(), "queueManager", "text", "name", queueManager);
    	inputElementInFrame(page.mainFrame(), "queueName", "text", "name", uniqueNodeName);
    	inputElementInFrame(page.mainFrame(), "recoveryQueueName", "text", "name", uniqueNodeName);
    	
    	// Save dirmon
    	clickElementInFrameByTagList(page.mainFrame(), "Save", "text", "value", "input");
    	
	}
	
	@Override
	public void createDirmonTemplate(Page page, String requestName, String label, String transferType, String writeMode, String ioAccess, String userArgs, 
			String exitRoutine, String sqm, String dqm, String destFileName, String organization, String recordFormat, String lrecl, String priQuantity, 
			String secQuantity, String dirBlocks, String spaceUnits, Boolean isMultipleDestination) throws ArgusMimException {
		
		if (!checkElementInFrame(page.mainFrame(), "Requests", "menu", "text")) {
			dblClickElementInFrame(page.mainFrame(), "Directory Monitor", "menu", "text");
			clickElementInFrame(page.mainFrame(), "Templates", "menu", "text");
		}
		
		clickElementInFrame(page.mainFrame(), "Requests", "menu", "text");
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		clickElementInFrame(page.mainFrame(), "New", "text", "text");
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		inputElementInFrame(page.mainFrame(), "reqName", "text", "id", requestName);
		inputElementInFrame(page.mainFrame(), "label", "text", "name", label);
		clickElementInFrameByTagList(page.mainFrame(), "toggle1", "text", "id", "img");
		selectOptionElementInFrame(page.mainFrame(), "ttype", "text", "name", transferType);
		selectOptionElementInFrame(page.mainFrame(), "mode", "text", "name", writeMode);
		selectOptionElementInFrame(page.mainFrame(), "ioAccess", "text", "name", ioAccess);
		inputElementInFrame(page.mainFrame(), "userArgs", "text", "name", userArgs);
		
		/*if (exitRoutine != "") {
			clickElementInFrameByTagList(page.mainFrame(), "toggle7", "text", "id", "img");
			selectOptionElementInFrame(page.mainFrame(), "exitType", "text", "name", exitRoutine);
			clickElementInFrameByTagList(page.mainFrame(), "New", "text", "name", "input");
			// TODO: Enter exit routine details
		}*/
		
		if (sqm != "") {
			inputElementInFrame(page.mainFrame(), "sqm", "text", "name", sqm);
		}
		
		if (dqm != "") {
			inputElementInFrame(page.mainFrame(), "dqm", "text", "name", dqm);
			inputElementInFrame(page.mainFrame(), "dpath", "text", "name", destFileName);
		}
		
		if (organization != "") {
			clickElementInFrameByTagList(page.mainFrame(), "toggle4", "text", "id", "img");
			selectOptionElementInFrame(page.mainFrame(), "dsorg", "text", "name", organization);
			selectOptionElementInFrame(page.mainFrame(), "recfm", "text", "name", recordFormat);
			inputElementInFrame(page.mainFrame(), "lrecl", "text", "name", lrecl);
			inputElementInFrame(page.mainFrame(), "primary", "text", "name", priQuantity);
			inputElementInFrame(page.mainFrame(), "secondary", "text", "name", secQuantity);
			inputElementInFrame(page.mainFrame(), "dirblks", "text", "name", dirBlocks);
			selectOptionElementInFrame(page.mainFrame(), "alcunit", "text", "name", spaceUnits);
		}
		
		if (isMultipleDestination) {
			clickElementInFrameByTagList(page.mainFrame(), "toggle8", "text", "id", "img");
			selectOptionElementInFrame(page.mainFrame(), "destinationListName", "text", "name", label);
		}
		
		clickElementInFrameByTagList(page.mainFrame(), "GenerateXML_Button", "text", "name", "input");	
		// Save template
    	clickElementInFrameByTagList(page.mainFrame(), "Save", "text", "value", "input");
		
	}
	
	@Override
	public void createDirmonEntry(Page page, String uniqueNodeName, String requestTemplate, String monitoredDir,
			String include, String queueManager) throws ArgusMimException {
		
		inputElementInFrame(page.mainFrame(), "ext-comp-1001", "text", "id", uniqueNodeName);
		clickElementInFrame(page.mainFrame(), "Apply", "text", "text");
		waitForLoadingFrame(page, "Loading", "text", 300);
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		dblClickElementInFrame(page.mainFrame(), uniqueNodeName, "text", "text");
		clickElementInFrameByTagList(page.mainFrame(), "Create Entry", "text", "value", "input");
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		inputElementInFrame(page.mainFrame(), "name", "text", "name", requestTemplate);
		inputElementInFrame(page.mainFrame(), "dir", "text", "name", monitoredDir);
		inputElementInFrame(page.mainFrame(), "include", "text", "name", include);
		selectOptionElementInFrame(page.mainFrame(), "requestTemplateName", "text", "name", requestTemplate);
		inputElementInFrame(page.mainFrame(), "destQueueManager", "text", "name", queueManager);
		
		clickElementInFrameByTagList(page.mainFrame(), "Save", "text", "value", "input");
		clickElementInFrameByTagList(page.mainFrame(), "Refresh", "text", "value", "input");
	}
	
	@Override
	public void createDestinationTemplate(Page page, String destName, String dqm, String destFileName, 
			String userArgs, String destLabel, String[] destinations, String requestName, String label, 
			String transferType, String writeMode, String ioAccess, String exitRoutine, String sqm, 
			String organization, String recordFormat, String lrecl, String priQuantity, String secQuantity, 
			String dirBlocks, String spaceUnits, String templatePath) throws ArgusMimException, IOException {
		
		dblClickElementInFrame(page.mainFrame(), "Directory Monitor", "menu", "text");
		clickElementInFrame(page.mainFrame(), "Templates", "menu", "text");
		
		Map<Integer, List<String>> templateValues = readExcelValues(environ.getProperty("argus.mim.destination.template.path"));
		
		String listName = inputMultipleDestinationDetails(page, spaceUnits, templateValues);
		
		selectMultipleDestination(page, templateValues, listName);
		
		createDirmonTemplate(page, requestName, listName, transferType, writeMode, ioAccess, userArgs, 
				exitRoutine, sqm, dqm, destFileName, organization, recordFormat, lrecl, priQuantity, 
				secQuantity, dirBlocks, spaceUnits, true);
	}
	
	@Override
	public String[] getDestinationName(String destLabel, String strdqm) {
		String[] dqmList = strdqm.split(",");
		
		List<String> finaldqmList = new ArrayList<>();
		for (String dqm: dqmList) {
			finaldqmList.add(destLabel + "_" + dqm);
		}
		
		String[] arr = new String[finaldqmList.size()];
		arr = finaldqmList.toArray(arr);
		return arr;
	}
	
	@Override
	public void setupExitRoutine(Page page, String exitRoutine, String scriptName, String exitMethod, String queueManager,
			String userData, String label, String targetUid, String uniqueNodeName) throws ArgusMimException {
		dblClickElementInFrame(page.mainFrame(), "Directory Monitor", "menu", "text");
		clickElementInFrame(page.mainFrame(), "Templates", "menu", "text");
		clickElementInFrame(page.mainFrame(), "Requests", "menu", "text");
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		waitForLoadingFrame(page, "Loading", "text", 300);
		dblClickElementInFrame(page.mainFrame(), label, "text", "text");
		waitForLoadingFrame(page, "Loading", "text", 300);
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		clickElementInFrameByTagList(page.mainFrame(), "Exits", "text", "text", "img");
		selectOptionElementInFrame(page.mainFrame(), "exitType", "text", "name", exitRoutine);
		clickElementInFrameByTagList(page.mainFrame(), "New", "text", "name", "input");
		inputElementInFrame(page.mainFrame(), "exitdll8_0", "text", "name", scriptName);
		inputElementInFrame(page.mainFrame(), "exitentry8_0", "text", "name", exitMethod);
		inputElementInFrame(page.mainFrame(), "exitqm8_0", "text", "name", queueManager);
		inputElementInFrame(page.mainFrame(), "exitdata8_0", "text", "name", userData);
		inputElementInFrame(page.mainFrame(), "exitlabel8_0", "text", "name", label);
		
		clickElementInFrameByTagList(page.mainFrame(), "toggle1", "text", "id", "img");
		//mimAutomationService.inputElementInFrame(page.mainFrame(), "userArgs", "text", "name", userArgs);
		String userArgsVal = getTextElementInFrame(page.mainFrame(), "userArgs", "text", "id");
		
		userArgsVal = userArgsVal.replaceFirst("-eid(.*)", "-eid " + targetUid);
		inputElementInFrame(page.mainFrame(), "userArgs", "text", "name", userArgsVal);
		clickElementInFrameByTagList(page.mainFrame(), "GenerateXML_Button", "text", "name", "input");	
		// Save template
    	clickElementInFrameByTagList(page.mainFrame(), "Save", "text", "value", "input");
    	
    	clickElementInFrame(page.mainFrame(), "Directory Monitor", "menu", "text");
    	inputElementInFrame(page.mainFrame(), "ext-comp-1001", "text", "id", uniqueNodeName);
		clickElementInFrame(page.mainFrame(), "Apply", "text", "text");
		waitForLoadingFrame(page, "Loading", "text", 300);
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		clickElementInFrame(page.mainFrame(), uniqueNodeName, "text", "text");
		clickElementInFrameByTagList(page.mainFrame(), "Refresh Instance", "text", "text", "button");
	}
	
	@Override
	public void disableDirmonLabel(Page page, String uniqueNodeName, String label) throws ArgusMimException {
		inputElementInFrame(page.mainFrame(), "ext-comp-1001", "text", "id", uniqueNodeName);
		clickElementInFrame(page.mainFrame(), "Apply", "text", "text");
		waitForLoadingFrame(page, "Loading", "text", 300);
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		dblClickElementInFrame(page.mainFrame(), uniqueNodeName, "text", "text");
		checkUncheckElementInFrame(page.mainFrame(), label, "text", "value", "uncheck");
		
		clickElementInFrameByTagList(page.mainFrame(), "Refresh", "text", "value", "input");
		
	}
	
	@Override
	public void deleteDirmonEntry(Page page, String uniqueNodeName, String label) throws ArgusMimException {
		inputElementInFrame(page.mainFrame(), "ext-comp-1001", "text", "id", uniqueNodeName);
		clickElementInFrame(page.mainFrame(), "Apply", "text", "text");
		waitForLoadingFrame(page, "Loading", "text", 300);
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		dblClickElementInFrame(page.mainFrame(), uniqueNodeName, "text", "text");
		page.onDialog(dialog -> {dialog.accept();});
		clickElementInFrameByTagList(page.mainFrame(), "deletedirmonentry&amp;entryName=" + label, "text", "href", "a");
		//checkUncheckElementInFrame(page.mainFrame(), label, "text", "value", "uncheck");
		
		clickElementInFrameByTagList(page.mainFrame(), "Refresh", "text", "value", "input");
		
		dblClickElementInFrame(page.mainFrame(), "Directory Monitor", "menu", "text");
		inputElementInFrame(page.mainFrame(), "ext-comp-1001", "text", "id", uniqueNodeName);
		clickElementInFrame(page.mainFrame(), "Apply", "text", "text");
		waitForLoadingFrame(page, "Loading", "text", 300);
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		clickElementInFrame(page.mainFrame(), uniqueNodeName, "text", "text");
		clickElementInFrameByTagList(page.mainFrame(), "x-btn-text refresh_dirmon", "text", "class", "button");
	}
	
	@Override
	public void verifyRunningMIM(Page page, String sourceServer, String destServer) throws ArgusMimException {
		clickElementInFrame(page.mainFrame(), "Ping", "menu", "text");
		inputElementInFrame(page.mainFrame(), "sqm", "text", "name", sourceServer);
		clickElementInFrameByTagList(page.mainFrame(), "Submit_Button", "text", "name", "input");
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		
		if (!checkElementInFrame(page.mainFrame(), "Ping Failed", "text", "text")) {
			//TODO: if ping failed
		}
		
		clickElementInFrame(page.mainFrame(), "OK", "text", "text");
		
		inputElementInFrame(page.mainFrame(), "dqm", "text", "name", destServer);
		clickElementInFrameByTagList(page.mainFrame(), "Submit_Button", "text", "name", "input");
		
		if (!checkElementInFrame(page.mainFrame(), "Ping Failed", "text", "text")) {
			//TODO: if ping failed
		}
		
		clickElementInFrame(page.mainFrame(), "OK", "text", "text");
	}
	
	@Override
	public void verifyCommunicationMIM(Page page, String sourceServer, String destServer) throws ArgusMimException {
		clickElementInFrame(page.mainFrame(), "Ping", "menu", "text");
		inputElementInFrame(page.mainFrame(), "sqm", "text", "name", sourceServer);
		inputElementInFrame(page.mainFrame(), "dqm", "text", "name", destServer);
		clickElementInFrameByTagList(page.mainFrame(), "Submit_Button", "text", "name", "input");
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		
		if (!checkElementInFrame(page.mainFrame(), "Ping Failed", "text", "text")) {
			//TODO: if ping failed
		}
		
		clickElementInFrame(page.mainFrame(), "OK", "text", "text");
	}
	
	@Override
	public void verifyExistingDirmonEntry(Page page, String uniqueNodeName) throws ArgusMimException {
		inputElementInFrame(page.mainFrame(), "ext-comp-1001", "text", "id", uniqueNodeName);
		clickElementInFrame(page.mainFrame(), "Apply", "text", "text");
		waitForLoadingFrame(page, "Loading", "text", 300);
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		dblClickElementInFrame(page.mainFrame(), uniqueNodeName, "text", "text");
		clickElementInFrameByTagList(page.mainFrame(), "deletedirmonentry&amp;entryName=", "text", "href", "a");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Map<Integer, List<String>> readExcelValues(String fileLocation) throws IOException{
		
		FileInputStream file = new FileInputStream(new File(fileLocation));
		
		Workbook workbook = new XSSFWorkbook(file);
		
		Sheet sheet = workbook.getSheetAt(0);

		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		for (Row row : sheet) {
		    data.put(i, new ArrayList<String>());
		    for (Cell cell : row) {
		        switch (cell.getCellTypeEnum()) {
		            case STRING: 
		            	data.get(new Integer(i)).add(cell.getRichStringCellValue().getString()); 
		            break;
		            case NUMERIC: 
		            	if (DateUtil.isCellDateFormatted(cell)) {
		                data.get(i).add(cell.getDateCellValue() + "");
			            } else {
			                data.get(i).add(cell.getNumericCellValue() + "");
			            } 
		            break;
		            case BOOLEAN: 
		            	data.get(i).add(cell.getBooleanCellValue() + ""); 
		            break;
		            case FORMULA: 
		            	data.get(i).add(cell.getCellFormula() + ""); 
		            break;
		            default: data.get(new Integer(i)).add(" ");
		        }
		    }
		    i++;
		}
		
		workbook.close();
		return data;
	}
	
	@Override
	public String inputMultipleDestinationDetails(Page page, String uniqueNodeName, Map<Integer, List<String>> map) throws ArgusMimException {
		List<String> valueList = null;
		Integer i = 0;
		for (Entry<Integer, List<String>> entry: map.entrySet()) {
			//System.out.print(entry.getKey() + " ");
			if (i==0) {
				i++;
				continue;
			}
			
			valueList = entry.getValue();
			
			clickElementInFrameByTagList(page.mainFrame(), "jsp/mft/destination.jsp?isTemplate=true&folderPath=", "menu", "href", "a");
			//mimAutomationService.clickElementInFrame(page.mainFrame(), "ext-gen77", "menu", "id");
			page.mainFrame().waitForLoadState(LoadState.LOAD);
			clickElementInFrame(page.mainFrame(), "New", "text", "text");
			waitForLoadingFrame(page, "Loading", "text", 300);
			page.mainFrame().waitForLoadState(LoadState.LOAD);
			valueList = entry.getValue();
			inputElementInFrame(page.mainFrame(), "destinationName", "text", "name", valueList.get(0) + "_" + valueList.get(1));
			inputElementInFrame(page.mainFrame(), "dqm", "text", "name", valueList.get(1));
			inputElementInFrame(page.mainFrame(), "dpath", "text", "name", valueList.get(2));
			inputElementInFrame(page.mainFrame(), "userArgs", "text", "name", valueList.get(3));
			
			// Save Destination Template
        	clickElementInFrameByTagList(page.mainFrame(), "Save", "text", "value", "input");
			i++;
		}
		return valueList.get(0);
	}
	
	@Override
	public void selectMultipleDestination(Page page, Map<Integer, List<String>> map, String listName) throws ArgusMimException {
		
		clickElementInFrameByTagList(page.mainFrame(), "/jsp/mft/destinationlist.jsp?isTemplate=true&folderPath=", "menu", "href", "a");
    	page.mainFrame().waitForLoadState(LoadState.LOAD);
		clickElementInFrame(page.mainFrame(), "New", "text", "text");
		page.mainFrame().waitForLoadState(LoadState.LOAD);
		inputElementInFrame(page.mainFrame(), "destListName", "text", "id", listName);
		inputElementInFrame(page.mainFrame(), "description", "text", "name", listName);
		
		List<String> valueList;
		Integer i = 0;
		for (Entry<Integer, List<String>> entry: map.entrySet()) {
			//System.out.print(entry.getKey() + " ");
			if (i==0) {
				i++;
				continue;
			}
			
			valueList = entry.getValue();
			
			clickElementInFrame(page.mainFrame(), valueList.get(0) + "_" + valueList.get(1), "text", "text");
			clickElementInFrameByTagList(page.mainFrame(), "AddDest", "text", "name", "input");
			i++;
		}
		
		clickElementInFrameByTagList(page.mainFrame(), "Save", "text", "value", "input");
	}
	
}
