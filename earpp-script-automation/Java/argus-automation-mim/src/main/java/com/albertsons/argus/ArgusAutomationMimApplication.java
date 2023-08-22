package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.albertsons.argus.mim.service.MIMAutomationService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.mim","com.albertsons.argus.domain"})
public class ArgusAutomationMimApplication implements CommandLineRunner {
	private static final Logger LOG = LogManager.getLogger(ArgusAutomationMimApplication.class);
	
	@Autowired
	public Environment environ;
	
	@Autowired
	private MIMAutomationService mimAutomationService;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArgusAutomationMimApplication.class, args);
		LOG.info("Shutdown Springboot . . .");
		System.exit(SpringApplication.exit(context, () -> 0));
	}
	
	@Override
	public void run(String... args) throws Exception {
		LOG.info("Start MIM Resubmit Label");
		String labelName = "SDAV-FTAL-QMERSVC01-IOH-TO-FTPGV011B4F-DC05";
        //Navigate to MIM url login
        Page page = mimAutomationService.navigateMIMLogin();
        
        //Click Managed File Transfer collapse button 
        mimAutomationService.clickElementInFrame(page.mainFrame(), "ext-gen65", "menu", "id");
        page.mainFrame().waitForLoadState(LoadState.LOAD);
        
        //Click Fixit label
        mimAutomationService.clickElementInFrame(page.mainFrame(), "Fixit", "menu", "id=ext-gen69 >> text");
        page.mainFrame().waitForLoadState(LoadState.LOAD);
        
        mimAutomationService.waitForLoadingFrame(page, "Loading...", "text", 300);
        page.mainFrame().waitForLoadState(LoadState.LOAD);
        
        //Select label from table
        //mimAutomationService.ClickElementInFrame(page.mainFrame(), environ.getProperty("playwright.mim.label.name")
        //	, "text", "id=FixitSummaryGridPanel >> id=ext-gen162 >> text");
        
        mimAutomationService.clickElementInFrame(page.mainFrame(), labelName
            	, "text", "id=FixitSummaryGridPanel >> id=ext-gen162 >> text");
        page.mainFrame().waitForLoadState(LoadState.LOAD);
        
        //Click Nextactivity dropdown
        mimAutomationService.clickElementInFrame(page.mainFrame(), "ext-gen48", "text", "id=FixitSummaryGridPanel >> id");
        page.mainFrame().waitForLoadState(LoadState.LOAD);
              
        //Select NextActivity from list
        mimAutomationService.clickElementInFrame(page.mainFrame(), environ.getProperty("playwright.mim.next.activity")
        	, "text", "id=ext-gen6 >> text");
        //FillElementInFrame(page.mainFrame(), NextActivity, "text", "id=FixitSummaryGridPanel >> id", "ext-gen272");
        
        //Click Submit
        //mimAutomationService.ClickElementInFrame(page.mainFrame(), "id=submitButton_FixitSummaryGridPanel >> text"
        //	, "text", "Submit");
        
        LOG.info("MIM Resubmit Label complete");
	}
}
