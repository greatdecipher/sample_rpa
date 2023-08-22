package com.albertsons.argus.datar2r;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;

@Configuration
@EncryptablePropertySource("argus-r2r-cred.properties")
public class DataConfig {
    
}
