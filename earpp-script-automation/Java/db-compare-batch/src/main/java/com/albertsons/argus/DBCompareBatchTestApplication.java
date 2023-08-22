package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.data", "com.albertsons.argus.dbcomparebatch", "com.albertsons.argus.domain", "com.albertsons.argus.domaindb",
        "com.albertsons.argus.mail"})
public class DBCompareBatchTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(DBCompareBatchTestApplication.class, args);
    }
}