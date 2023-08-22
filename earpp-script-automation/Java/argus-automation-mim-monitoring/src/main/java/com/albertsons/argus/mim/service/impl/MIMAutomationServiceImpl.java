package com.albertsons.argus.mim.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class MIMAutomationServiceImpl implements MIMAutomationService {
	public static final Logger LOG = LogManager.getLogger(MIMAutomationServiceImpl.class);
	@Autowired
    private Environment environ;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public String mainMimMonitoring(String[] labelList) throws ArgusMimException{
		String tableResult = "";
		
		LOG.log(Level.DEBUG, () -> "Start mainMimMonitoring method . . .");
		
		Browser browser = Playwright.create().chromium()
	                .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
			
		BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true));
			
		try {
			
			Page page = navigateMIMLogin(context);
			navigateTransferStatusPage(page);
			
			for (String str: labelList) {
				String[] labelAndThresh = str.split("\\|");        
				waitLoadMainPage(page, "Loading Processes...", 300);
				inputLabelDetails(page, labelAndThresh[0], labelAndThresh[1]);
				waitLoadMainPage(page, "Loading Processes...", 300);
				tableResult = tableResult + getResultTableValues(page, labelAndThresh[1]);	        
			}
			page.close();
			
		} catch (Exception e) {
			LOG.info(e.getMessage());
		} finally {
			context.close();
			browser.close();
		}

		LOG.log(Level.DEBUG, () -> "End mainMimMonitoring method . . .");
		
		return tableResult;
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
	public Page navigateMIMLogin(BrowserContext context) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start navigateMIMLogin method . . .");
		
		try {
			
	        Page page = context.newPage();
			
	        page.navigate(environ.getProperty(MIM_LOGIN_URL));
	        
	        page.waitForSelector("input[name=userId]");
	        page.fill("input[name=userId]", environ.getProperty("encrypted.mim.property.username"));
	        page.fill("input[name=password],type[password]", environ.getProperty("encrypted.mim.property.password"));
	        
	        page.click("input[name=logonButton]");
	        
	        page.mainFrame().waitForLoadState(LoadState.LOAD);
	        
	        LOG.log(Level.DEBUG, () -> "End navigateMIMLogin method . . .");
	        
	        return page;
	        
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}
   
	}
	
	@Override
	public void inputLabelDetails(Page mainPage, String labelName, String labelThresh) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start inputLabelDetails method . . .");
		
		try {
			List<ElementHandle> SelectorColl;
			mainPage.click("text=Edit Unsaved Filter");
			delay(1);
			mainPage.fill("id=descriptionCmpId",labelName);
			delay(1);
			mainPage.click("id=x-form-el-daystoshowcombo >> img");
			delay(1);
			SelectorColl = mainPage.querySelectorAll("div");
			
			for (ElementHandle elemHandle: SelectorColl) {
				if (elemHandle.innerText().length() >= 1 && elemHandle.innerText().length() <= 2) {
					if (elemHandle.getAttribute("class").contains("x-combo-list-item") && elemHandle.innerText().contains(environ.getProperty("argus.mim.label.days.show"))) {
						elemHandle.click();
					}
					
				}
			}
			
			mainPage.uncheck("id=completedCmpId");
			mainPage.check("id=failedCmpId");
			mainPage.check("id=warningCmpId");
			mainPage.check("id=inprogressCmpId");
			mainPage.check("id=fixitCmpId");
			mainPage.click("text=Apply");
			delay(3);
			
			LOG.log(Level.DEBUG, () -> "End inputLabelDetails method . . .");
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}

	}
	
	@Override
	public String getResultTableValues(Page mainPage, String labelThresh) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start getResultTableValues method . . .");
		final String sMessageDebug;
		int iIconStart, iIconEnd, charToAscii, ctr;
		String sProgressIcon = "", sDescription,
				sProcessId, sStartTime, sSource, sDestination, sState, sLine, sPopStr = "", sMessage;
		String sFailedList = "", sClockingList = "", sFailedAndClockingList, filePath;
		long dFileTimestamp,iDateDiff;
		ArrayList<String> sColumnArr = new ArrayList<>();
		char charAt;
		
		try {
			ElementHandle objTable = mainPage.querySelector("id=ext-gen188");
			
			
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
									filePath = environ.getProperty(LOG_FOLDER).replace("userID", System.getProperty("user.name")) + "\\" + sProcessId;
									if (!fileExists(filePath)) {
										writeLog(sDescription + " " + sProcessId + " " + sStartTime + " " + sState, 
												filePath);
									}
									else {
										LocalDateTime ldt;
										dFileTimestamp = getFTimestamp(filePath);
										ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(dFileTimestamp), 
				                                TimeZone.getDefault().toZoneId());
										iDateDiff = ChronoUnit.MINUTES.between(ldt, LocalDateTime.now());
										if (iDateDiff > Integer.parseInt(labelThresh)) {
											sClockingList = sClockingList + "warn_clock_start" + sLine + "warn_clock_end" + "\n";
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
	public String[] getLabelDetails(String labelNames) {
		LOG.log(Level.DEBUG, () -> "Splitting Label to get details . . .");

		return labelNames.split("~");
		
	}
	
	@Override
	public void navigateTransferStatusPage(Page mainPage) {
		LOG.log(Level.DEBUG, () -> "Start navigateTransferStatusPage method . . .");
		
		mainPage.navigate(environ.getProperty(MIM_LOGIN_URL)
	        	+ environ.getProperty("playwright.uri.mim.monitoring"));
		
		LOG.log(Level.DEBUG, () -> "End navigateTransferStatusPage method . . .");
	}
	
	@Override
	public void waitLoadMainPage(Page mainPage, String waitElement, long waitLimit) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start waitLoadMainPage method . . .");
		
		long lWaitCtr = 0;
		try {
			ElementHandle elemHandle = mainPage.querySelector("text=" + waitElement);
			while (elemHandle != null) {
				lWaitCtr += 1;
				elemHandle = mainPage.querySelector("text=" + waitElement);
				delay(1);
				if (lWaitCtr > waitLimit) {
					LOG.log(Level.DEBUG, () -> "waitLoadMainPage Wait Limit Exceeded. . .");
					throw new ArgusMimException("Element: text=" + waitElement);
				}
			}
			
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}
		
		LOG.log(Level.DEBUG, () -> "End waitLoadMainPage method . . .");
		
	}
	
	@Override
	public void waitForLoadingFrame(Page mainPage, String waitElem, String frameName, long waitLimit) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start waitForLoadingFrame method . . .");
		
		long lWaitCounter = 0;
		try {
			Frame frm = mainPage.frame(frameName);
	        
			while (frm.content().contains(waitElem)) {
	        	frm = mainPage.frame(frameName);
	        	delay(1);
	        	lWaitCounter += 1;
	        	if (lWaitCounter > waitLimit) {
	        		LOG.log(Level.DEBUG, () -> "waitLoadMainPage Wait Limit Exceeded. . .");
					throw new ArgusMimException("Element: text=" + waitElem);
	        	}
			}
			
		} catch (PlaywrightException pw) {
			throw new ArgusMimException(pw.getMessage());
		}
		
		LOG.log(Level.DEBUG, () -> "End waitForLoadingFrame method . . .");
	}

	@Override
	public void mimFormatAndSendEmailBody(String emailBody) throws ArgusMimException {
		LOG.log(Level.DEBUG, () -> "Start mimFormatAndSendEmailBody method . . .");
		
		String subjectAddInfo = "";
		
		if (emailBody.contains("warn_clock_start")) {
			subjectAddInfo = subjectAddInfo + " | CLOCKED ";
		}
		if (emailBody.contains("warn_red_start")) {
			subjectAddInfo = subjectAddInfo + " | FAILED ";
		}
		if (emailBody.contains("warn_orange_start")) {
			subjectAddInfo = subjectAddInfo + " | FIXIT ";
		}
		if (emailBody.contains("warn_blue_start")) {
			subjectAddInfo = subjectAddInfo + " | WARNING ";
		}
		
		AutomationUtil util = new AutomationUtil();
		
		emailBody = emailBody.replace("warn_clock_start", "");
		emailBody = emailBody.replace("warn_clock_end", "");
		
		emailBody = emailBody.replace("\n", "<br>");
		emailBody = "<font face='Courier New' color='black' size='1'>" + emailBody + "</font>";
		emailBody = emailBody.replace("warn_red_start", "<font color='red'>");
		emailBody = emailBody.replace("warn_red_end", "</font>");
		emailBody = emailBody.replace("warn_orange_start", "<font color='orange'>");
		emailBody = emailBody.replace("warn_orange_end", "</font>");
		emailBody = emailBody.replace("warn_blue_start", "<font color='blue'>");
		emailBody = emailBody.replace("warn_blue_end", "</font>");
		
		try {
			emailService.sendSimpleMessage(environ.getProperty("mail.mim.monitoring.from"), 
					environ.getProperty("mail.mim.monitoring.from.alias"), 
					environ.getProperty("mail.mim.monitoring.recipients", String[].class), 
					environ.getProperty("mail.mim.monitoring.cc", String[].class), 
					environ.getProperty("mail.mim.monitoring.subject") + " - " 
					+ util.toDateString(new Date(), environ.getProperty("domain.util.date.format"),"")
					+ subjectAddInfo,
					emailBody, NORMAL_PRIORITY, true);
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
	public boolean fileExists(String filePath) {
		LOG.log(Level.DEBUG, () -> "Run fileExists method . . .");
		
		try {
			File fileName = new File(filePath);
			
			if (fileName.exists()) {
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
		
		File fileName = new File(sFile);
		return fileName.lastModified();
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
		
		File folderPath = new File(sFolder);
		Path path; 
		LocalDateTime ldt; 
		BasicFileAttributes bfa;
		
		long iDateDiff;
		
		if (folderPath.isDirectory()) {
			try {
				for (File fName: folderPath.listFiles()) {
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
	public void createFolder(String folderURL) {
		LOG.log(Level.DEBUG, () -> "Start createFolder method . . .");
		try {
			File folderName = new File(folderURL);
			if (!folderName.isDirectory()) {
				folderName.mkdir();
			}
		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "createFolder method failed . . .");
		}
		
		LOG.log(Level.DEBUG, () -> "End createFolder method . . .");
	}
	
	
}
