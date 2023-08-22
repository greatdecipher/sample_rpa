package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.tma", "com.albertsons.argus.domain.service",
        "com.albertsons.argus.domain.playwright",
        "com.albertsons.argus.mail.service","com.albertsons.argus.webservice"  })
public class TmaArgusTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmaArgusTestApplication.class, args);
    }
}
