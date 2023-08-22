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

import com.albertsons.argus.mim.exception.ArgusMimRuntimeException;
import com.albertsons.argus.mim.service.MIMAutomationService;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.mim","com.albertsons.argus.domain","com.albertsons.argus.mail.service"})
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
		String LogFilePath = environ.getProperty("argus.mim.log.folder").replace("userID", System.getProperty("user.name"));
        
		try {
			LOG.log(Level.DEBUG, () -> "Start MIM Label Monitoring tool . . .");
			
        	mimAutomationService.createFolder(LogFilePath);
    		mimAutomationService.deleteFilesByAge(LogFilePath, Integer.parseInt(environ.getProperty("argus.mim.label.days.show")));
    		
    		String[] LabelList = mimAutomationService.getLabelDetails(environ.getProperty("argus.mim.label.list"));
    		
    		String Result = mimAutomationService.mainMimMonitoring(LabelList);
    		
    		LOG.log(Level.DEBUG, () -> "Label List: " + Result);
    		
    		if (Result.length() > 0) {
    			mimAutomationService.mimFormatAndSendEmailBody(Result);
    		}
    		
    		LOG.log(Level.DEBUG, () -> "MIM Label Monitoring Completed . . .");
    		
		} catch (Exception e) {
			LOG.log(Level.DEBUG, () -> "MIM Label Monitoring Completed . . .");
			throw new ArgusMimRuntimeException(e.getMessage());
		}	
		
	}
}
