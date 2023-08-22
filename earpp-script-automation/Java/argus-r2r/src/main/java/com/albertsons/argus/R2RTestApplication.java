package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.albertsons.argus.r2r", "com.albertsons.argus.datar2r", "com.albertsons.argus.domain", "com.albertsons.argus.domaindbr2r", "com.albertsons.argus.mail"})
public class R2RTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(R2RTestApplication.class, args);
    }
}