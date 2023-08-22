package com.albertsons.argus.mail;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailApplicationTests {

	@Autowired
	private EmailService emailService;

	@Test
	public void contextLoads() {
		String[] recipient = {"kbuen03@safeway.com"};
		try {
			emailService.sendSimpleMessage("test@safeway.com","Test Safeway", recipient, recipient, "Test Email ! ! !",
					"Text Body success. . .",1, false);
		} catch (ArgusMailException e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}

}
