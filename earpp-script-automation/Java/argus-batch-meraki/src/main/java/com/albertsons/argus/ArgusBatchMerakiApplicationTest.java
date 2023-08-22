package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.meraki",  "com.albertsons.argus.domain",
"com.albertsons.argus.mail" })
public class ArgusBatchMerakiApplicationTest {
    public static void main(String[] args) {
        SpringApplication.run(ArgusBatchMerakiApplicationTest.class, args);
    }
}
