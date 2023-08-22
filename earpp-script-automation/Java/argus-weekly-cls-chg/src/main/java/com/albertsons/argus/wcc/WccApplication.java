package com.albertsons.argus.wcc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.wcc", "com.albertsons.argus.domain", "com.albertsons.argus.mail", "com.albertsons.argus.webservice"})
@EnableScheduling
public class WccApplication{
	private static final Logger LOG = LogManager.getLogger(WccApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(WccApplication.class, args);
        LOG.info("Springboot Scheduler job enabled for Weekly Closure Change automation. . .");
	}

}
