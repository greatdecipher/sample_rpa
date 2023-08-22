package com.albertsons.argus.asn;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

@Configuration
@PropertySource("classpath:argus-asn-app.properties")
@PropertySource(value = "classpath:argus-asn-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("classpath:argus-asn-cred.properties")
public class AsnConfig {
    
}
