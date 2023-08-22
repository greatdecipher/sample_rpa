package com.albertsons.argus.tma.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.albertsons.argus.TmaArgusTestApplication;
import com.albertsons.argus.tma.service.TMAAutomationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes ={TmaArgusTestApplication.class})
class ArgusApplicationTests {
	@Autowired
	private TMAAutomationService automationService;


	@Test
	void contextLoads() {
		assertNotNull(automationService);
	}


}
