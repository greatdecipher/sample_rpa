package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.webservice"})
public class ArgusWSApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArgusWSApplication.class, args);
	}

}
