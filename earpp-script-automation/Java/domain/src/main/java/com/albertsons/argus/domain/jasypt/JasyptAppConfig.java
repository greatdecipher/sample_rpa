package com.albertsons.argus.domain.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:domain-app.properties")
@EncryptablePropertySource("encrypted.properties")
public class JasyptAppConfig {
    
}
