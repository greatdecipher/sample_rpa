package com.albertsons.argus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.albertsons.argus.userlogin.service.UserLoginService;

@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.userlogin", "com.albertsons.argus.domain",
"com.albertsons.argus.mail" })
@EnableScheduling
public class ArgusUserLoginApplication 
// implements CommandLineRunner 
{
	private static final Logger LOG = LogManager.getLogger(ArgusUserLoginApplication.class);
	@Autowired
	UserLoginService userLoginService;

	public static void main(String[] args) {
		// SpringApplication.run(ArgusUserLoginApplication.class, args);
		SpringApplication.run(ArgusUserLoginApplication.class);

        LOG.info("Springboot Scheduler job enabled. . .");
	}

	// @Override
	// public void run(String... args) throws Exception {
		
	// 	List<User> userProcessed = new ArrayList<>();
		
	// 	userProcessed = userLoginService.loginMultipleUser(userLoginService.getLoginInfo());
	// 	String msg = userLoginService.createMailTemplate(userProcessed);
	// 	userLoginService.sendMail(msg);	
	// }

}
