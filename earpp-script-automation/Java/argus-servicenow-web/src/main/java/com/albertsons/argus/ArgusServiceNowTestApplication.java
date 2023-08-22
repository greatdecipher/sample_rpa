package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.servicenow.web", "com.albertsons.argus.domain.service" })
public class ArgusServiceNowTestApplication {
    public static void main(String[] args) {
		SpringApplication.run(ArgusServiceNowTestApplication.class, args);
	}
}
