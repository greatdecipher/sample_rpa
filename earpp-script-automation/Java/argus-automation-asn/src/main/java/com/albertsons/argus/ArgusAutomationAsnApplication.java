package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.asn", "com.albertsons.argus.domain",
"com.albertsons.argus.mail", "com.albertsons.argus.webservice" })
@EnableScheduling
public class ArgusAutomationAsnApplication{

	public static void main(String[] args) {
		SpringApplication.run(ArgusAutomationAsnApplication.class, args);
	}


}
