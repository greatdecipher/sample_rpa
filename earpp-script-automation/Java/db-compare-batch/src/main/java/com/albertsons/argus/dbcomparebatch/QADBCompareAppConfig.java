package com.albertsons.argus.dbcomparebatch;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("qa")
@PropertySource("classpath:argus-app-${spring.profiles.active}.properties")
@EncryptablePropertySource("argus-cred.properties")
public class QADBCompareAppConfig {
    
}
