package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.data", "com.albertsons.argus.dbcomparebatch", "com.albertsons.argus.domaindb", "com.albertsons.argus.domain", "com.albertsons.argus.mail" })
@EnableScheduling
public class DBCompareApplication{
    private static final Logger LOG = LogManager.getLogger(DBCompareApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DBCompareApplication.class, args);
        LOG.info("Springboot Scheduler job enabled for DB Compare automation. . .");
	}

}
