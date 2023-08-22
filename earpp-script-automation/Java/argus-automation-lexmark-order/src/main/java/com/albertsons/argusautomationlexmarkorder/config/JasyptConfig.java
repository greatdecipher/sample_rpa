package com.albertsons.argusautomationlexmarkorder.config;

import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySources;

@Configuration
@EnableEncryptableProperties
@EncryptablePropertySources({@EncryptablePropertySource("classpath:argus-lexmark-cred.properties"),
    @EncryptablePropertySource("classpath:encrypted.properties")})
public class JasyptConfig {
    
}
