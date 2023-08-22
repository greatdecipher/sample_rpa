package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.wic","com.albertsons.argus.domain","com.albertsons.argus.domain.playwright","com.albertsons.argus.mail","com.albertsons.argus.webservice"})
@EnableScheduling
public class ArgusAutomationWicApplication {
	private static final Logger LOG = LogManager.getLogger(ArgusAutomationWicApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArgusAutomationWicApplication.class, args);
		LOG.info("Springboot Scheduler job enabled for WIC Fruit & Veg Mapping. . .");
	}
}
