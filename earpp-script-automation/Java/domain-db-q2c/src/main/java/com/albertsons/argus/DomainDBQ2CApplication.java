package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.domaindbq2c", "com.albertsons.argus.dataq2c"})
public class DomainDBQ2CApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomainDBQ2CApplication.class, args);
	}

}
