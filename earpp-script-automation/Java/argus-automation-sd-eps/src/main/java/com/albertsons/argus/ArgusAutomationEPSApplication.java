package com.albertsons.argus;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.microsoft.playwright.Page;
import com.albertsons.argus.domain.playwright.service.PlaywrightAutomationService;
import com.albertsons.argus.eps.service.EPSAutomationService;
import com.albertsons.argus.mail.service.EmailService;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.eps","com.albertsons.argus.domain","com.albertsons.argus.mail"})
public class ArgusAutomationEPSApplication implements CommandLineRunner {
    private static final Logger LOG = LogManager.getLogger(ArgusAutomationEPSApplication.class);

	private Browser browser;
	private BrowserContext context;
	private Page page;
    private final String botRunner = System.getProperty("user.name");

    @Autowired
	public Environment environ;
	
	@Autowired
	private EPSAutomationService ePSAutomationService;

	@Autowired
	private PlaywrightAutomationService PlaywrightService;

	@Autowired
	private EmailService emailService;

    public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArgusAutomationEPSApplication.class, args);
		LOG.info("Shutdown Springboot . . .");
		System.exit(SpringApplication.exit(context, () -> 0));
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			LOG.info("Starting eps no task bot. . .");
			String arguments = args[0];
			arguments = arguments.replace("\r", "\\r").replace("\n", "\\n");
			LOG.info("Parsing argument to object. . .");
			
			List<String> argList = Arrays.asList(arguments.split("~"));
			
			String callerId = argList.get(0);
			String incidentNo = argList.get(1);
			String description = argList.get(2);
			String sysUpdatedBy = argList.get(3);
			String sysId = argList.get(4);
			
			String snowUrlGet = URLDecoder.decode(environ.getProperty("eps.web.service.snow.api") + 
				environ.getProperty("eps.web.service.url.validate.get").replace("{SYS_ID}", sysId), StandardCharsets.UTF_8.toString());
			String snowUrlUpdate = URLDecoder.decode(environ.getProperty("eps.web.service.snow.api") + 
				environ.getProperty("eps.web.service.url.update").replace("{SYS_ID}", sysId), StandardCharsets.UTF_8.toString());

			String outputValidateInput = ePSAutomationService.epsValidateInput(sysId,sysUpdatedBy,incidentNo,
				description, snowUrlGet, snowUrlUpdate, callerId);
				
			LOG.info("Finished executing EPS Task");
			
        } catch (Exception e) {
            LOG.error("ERROR in main function");
            LOG.error(e.getMessage());
        }
        finally {
            if (browser != null) {
                browser.close();
            }   
        }
    }  
}
