package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.domaindbr2r", "com.albertsons.argus.datar2r"})
public class DomainDBR2RApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomainDBR2RApplication.class, args);
	}

}
