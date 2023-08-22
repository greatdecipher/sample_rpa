package com.albertsons.argus.meraki;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.albertsons.argus.ArgusBatchMerakiApplicationTest;
import com.albertsons.argus.meraki.service.MerakiService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes ={ArgusBatchMerakiApplicationTest.class})
class ArgusBatchMerakiApplicationTests {
	@Autowired
	private MerakiService merakiService;

	@Test
	@Disabled("temp")
	void contextLoads() {
		assertNotNull(merakiService);
	}

}
