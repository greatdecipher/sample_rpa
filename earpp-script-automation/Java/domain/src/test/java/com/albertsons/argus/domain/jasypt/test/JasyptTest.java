package com.albertsons.argus.domain.jasypt.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import com.albertsons.argus.DomainApplication;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest(classes = DomainApplication.class)
public class JasyptTest {
    @Autowired
    private Environment environment;
    
    @Test
    @Disabled("Please add the decrypted password if you want to test it and enable the @disabled afterwards.")
    public void decryptTest(){
        String val = environment.getRequiredProperty("encrypted.tma.property");
        assertEquals("", val);

    }
    @Test
    // @Disabled("Enable this disable function after using this encrypting tool. . .")
    public void testencrypt() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //insert the decrypt test data
        String privateData = "karlo";

        encryptor.setPassword("rp@rules");
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");

        String encryptedText = encryptor.encrypt(privateData);

        System.out.println("Encrypt: " + encryptedText);

        assertNotSame(privateData, encryptedText);

        String plainText = encryptor.decrypt(encryptedText);
        assertEquals(plainText, privateData);
    }
}
