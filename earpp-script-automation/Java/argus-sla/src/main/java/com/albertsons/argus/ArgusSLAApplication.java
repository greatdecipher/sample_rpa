package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.sla", "com.albertsons.argus.domain",
 "com.albertsons.argus.mail", "com.albertsons.argus.webservice"})
@EnableScheduling
public class ArgusSLAApplication {
	private static final Logger LOG = LogManager.getLogger(ArgusSLAApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArgusSLAApplication.class, args);
		LOG.info("Springboot Scheduler job enabled for Out of SLA automation.");
	}
}
