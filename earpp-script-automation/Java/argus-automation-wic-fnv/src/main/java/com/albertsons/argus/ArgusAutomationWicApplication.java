package com.albertsons.argus;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.albertsons.argus.wic.exception.ArgusWicRuntimeException;
import com.albertsons.argus.wic.service.WicAutomationService;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.wic","com.albertsons.argus.domain","com.albertsons.argus.domain.playwright","com.albertsons.argus.mail.service"})
public class ArgusAutomationWicApplication implements CommandLineRunner {
	private static final Logger LOG = LogManager.getLogger(ArgusAutomationWicApplication.class);
	
	@Autowired
	public Environment environ;
	
	@Autowired
	private WicAutomationService WicAutomationService;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArgusAutomationWicApplication.class, args);
		LOG.info("Shutdown Springboot . . .");
		System.exit(SpringApplication.exit(context, () -> 0));
	}
    
	@Override
	public void run(String... args) throws Exception {
		try {
			LOG.log(Level.DEBUG, () -> "Start WIC Mapping tool . . . " + args[0]);
			/*String arg = environ.getProperty("wic.exe.name") + " " + environ.getProperty("wic.outlook.script.path") +
				" \"" + environ.getProperty("wic.outlook.folder.path") + "|" + environ.getProperty("wic.outlook.subject.name") +
				"|" + environ.getProperty("wic.outlook.mail.markread") + "|" + environ.getProperty("wic.outlook.attachment.downloadall") +
				"|" + environ.getProperty("wic.outlook.script.param.detail") + "|" + environ.getProperty("wic.outlook.attachment.save.path") +
				"|" + environ.getProperty("wic.outlook.attachment.type") + "|" + environ.getProperty("wic.outlook.attachment.overwrite") + "\"";	
			
			String extractedEmailSubject = "";
			WicAutomationService.deleteWicFilesInFolder(environ.getProperty("wic.outlook.attachment.save.path"));

			do {
				extractedEmailSubject = WicAutomationService.runOutlookScript(arg);
				//----------------------Log email details------------------------//
				LOG.info("WIC Mapping started for " + extractedEmailSubject);
				
				if(extractedEmailSubject.length() > 0) {
					WicAutomationService.runShCmd(environ.getProperty("encrypted.wic.property.username"), 
						environ.getProperty("encrypted.wic.property.password"), 
						environ.getProperty("jsch.wic.host.name"), extractedEmailSubject);
				} else {
					LOG.info("No unread emails found...");
				}
				
			} while (extractedEmailSubject.length() > 0);*/

    		LOG.log(Level.DEBUG, () -> "WIC Mapping Completed . . .");
    		
		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "WIC Mapping Error . . .");
			throw new ArgusWicRuntimeException(e.getMessage());
		}	
		
	}
	
	
}
