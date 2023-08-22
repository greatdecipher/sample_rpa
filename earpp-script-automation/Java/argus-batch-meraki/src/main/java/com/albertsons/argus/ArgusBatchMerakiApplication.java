package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 
 * @author kbuen03
 * @since 1/17/21
 * @version 1.0
 * 
 */
@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.meraki", "com.albertsons.argus.domain",
		"com.albertsons.argus.mail" })
@EnableSwagger2
public class ArgusBatchMerakiApplication {
	private static final Logger LOG = LogManager.getLogger(ArgusBatchMerakiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArgusBatchMerakiApplication.class, args);
		LOG.info("Springboot Web Started . . .");
	}

}
