package com.albertsons.argus;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.q2c", "com.albertsons.argus.dataq2c", "com.albertsons.argus.domain", "com.albertsons.argus.domaindbq2c", "com.albertsons.argus.mail", "com.albertsons.argus.webservice"})
@EnableScheduling
public class ArgusQ2CApplication implements CommandLineRunner {
	private static final Logger LOG = LogManager.getLogger(ArgusQ2CApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArgusQ2CApplication.class, args);
		LOG.debug("Start Springboot Q2C Outbound scheduler job. . .");
	}
	
	@Override
	public void run(String... args) throws Exception {     
		LOG.log(Level.INFO, () -> "start Q2C script execution . . .");
	}

}
