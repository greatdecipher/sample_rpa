package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.nagware", "com.albertsons.argus.domain",
"com.albertsons.argus.mail", "com.albertsons.argus.webservice" }) 
@EnableScheduling
public class ArgusAutomationNagwareApplication{

	public static void main(String[] args) {
		SpringApplication.run(ArgusAutomationNagwareApplication.class, args);
	}


}

