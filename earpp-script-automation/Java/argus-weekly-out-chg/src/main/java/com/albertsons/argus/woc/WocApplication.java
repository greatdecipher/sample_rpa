package com.albertsons.argus.woc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.albertsons.argus.woc.service.WocService;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.woc", "com.albertsons.argus.domain", "com.albertsons.argus.mail", "com.albertsons.argus.webservice"})
@EnableScheduling
public class WocApplication{
	private static final Logger LOG = LogManager.getLogger(WocApplication.class);
	
	// @Autowired
	// private WocService wocservice;

    public static void main(String[] args) {
		SpringApplication.run(WocApplication.class, args);
        LOG.info("Springboot Scheduler job enabled for Weekly Out Change automation. . .");
	}
	
    // public static void main(String[] args) {

    //     ConfigurableApplicationContext context = SpringApplication.run(WocApplication.class, args);
    //     LOG.debug("Shutdown Springboot . . .");
    //     System.exit(SpringApplication.exit(context, () -> 0));
    // }

    // @Override
    // public void run(String... args) throws Exception {
	// 	wocservice.login();
    //     wocservice.extractRaw();
    //     //wocservice.sendEmail();
	// }

}
