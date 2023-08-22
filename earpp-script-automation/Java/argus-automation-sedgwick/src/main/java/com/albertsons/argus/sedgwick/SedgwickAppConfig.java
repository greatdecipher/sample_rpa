package com.albertsons.argus.sedgwick;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.albertsons.argus.webservice.WsAppConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

@Configuration
@PropertySource(value = "classpath:argus-sedgwick-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("argus-cred.properties")
@EncryptablePropertySource(value = "classpath:argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)

public class SedgwickAppConfig extends WsAppConfig {
    
}
