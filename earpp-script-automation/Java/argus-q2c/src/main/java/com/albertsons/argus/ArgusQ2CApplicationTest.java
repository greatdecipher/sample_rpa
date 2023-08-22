package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.q2c", "com.albertsons.argus.dataq2c", "com.albertsons.argus.domain", "com.albertsons.argus.domaindbq2c", "com.albertsons.argus.mail"})
public class ArgusQ2CApplicationTest {
    private static final Logger LOG = LogManager.getLogger(ArgusQ2CApplicationTest.class);
    
    public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArgusQ2CApplicationTest.class, args);
		LOG.debug("Shutdown Springboot . . .");
		System.exit(SpringApplication.exit(context, () -> 0));
	}
}
