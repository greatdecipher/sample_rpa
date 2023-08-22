package com.albertsons.argus.dataq2c;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;

@Configuration
@EncryptablePropertySource("argus-q2c-cred.properties")
public class DataConfig {
    
}
