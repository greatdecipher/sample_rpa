package com.albertsons.argus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author kbuen03
 * @since 5/19/21
 * @version 1.0
 * 
 */
@SpringBootApplication(scanBasePackages = { "com.albertsons.argus.domain", "com.albertsons.argus.servicenow.web" })
public class ArgusServicenowWebApplication {
	// private static final Logger LOG = LogManager.getLogger(ArgusServicenowWebApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ArgusServicenowWebApplication.class, args);
	}

	/*
	 * @Bean public RestTemplate restTemplate(RestTemplateBuilder builder) { return
	 * builder.basicAuthentication("auto.anywhere.api","autoanywhere01").build(); }
	 * 
	 * @Bean public CommandLineRunner run(RestTemplate restTemplate) throws
	 * Exception { return args -> { Table table = restTemplate.getForObject( //
	 * "https://safewayqa1.service-now.com/api/now/table/incident?sysparm_query=assigned_to%3D14c9581bdbc8f740154c24684b961983%5Estate",
	 * "https://safewayqa1.service-now.com/api/now/actsub/activities", Table.class);
	 * LOG.info(table.toString()); }; }
	 */
}
