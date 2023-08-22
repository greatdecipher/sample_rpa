package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.patchingctask","com.albertsons.argus.domain","com.albertsons.argus.mail.service","com.albertsons.argus.webservice"})
@EnableScheduling
public class ArgusAutomationPatchingCtaskApplication{
	private static final Logger LOG = LogManager.getLogger(ArgusAutomationPatchingCtaskApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArgusAutomationPatchingCtaskApplication.class, args);
		LOG.info("Springboot Scheduler job enabled for Patching Ctask Creation Automation. . .");
	}
	
}
