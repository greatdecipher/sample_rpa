package com.albertsons.argus.mim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;


class ArgusAutomationMimApplicationTests {
	private Environment environ;
	@Test
	void contextLoads() {
	}
	
	@Disabled
	public void decryptTest() {
		assertEquals("", environ.getRequiredProperty("encrypted.mim.property"));
	}
	
	public void testEncrypt() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		String privateData = "7uMZu!3WjE";
		encryptor.setPassword("rp@rules");
		encryptor.setAlgorithm("PBEWithMDSANDTripleDES");
		String encryptedText = encryptor.encrypt(privateData);
		System.out.println("Encrypt" + encryptedText);
		assertNotSame(privateData, encryptedText);
		String plainText = encryptor.decrypt(encryptedText);
		assertEquals(plainText, privateData);
	}
}
