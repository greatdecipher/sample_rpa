package com.albertsons.argus.rundb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.rundb", "com.albertsons.argus.mail", "com.albertsons.argus.domain", "com.albertsons.argus.webservice"})
@EnableScheduling
public class ArgusRunDBApplication {
    private static final Logger LOG = LogManager.getLogger(ArgusRunDBApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArgusRunDBApplication.class, args);
        LOG.info("Springboot Scheduler job enabled for Run DB automation. . .");
	}

}
