package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.toggle", "com.albertsons.argus.domain.service",
        "com.albertsons.argus.mail.service" })
public class ArgusToggleTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArgusToggleTestApplication.class, args);
    }
}
