package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.domaindb", "com.albertsons.argus.data"})
public class DomainDBApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomainDBApplication.class, args);
	}

}
