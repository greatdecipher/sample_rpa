package com.albertsons.argus.data;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;

@Configuration
@EncryptablePropertySource("argus-cred.properties")
public class DataConfig {
    
}
