package com.albertsons.argus.webservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

/**
 * 
 * @author jborj20
 * @since 11/10/22
 * @version 1.0
 */
@Configuration
@Profile("test")
@PropertySource(value = "classpath:metrics-ws-app-test.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource(value = "classpath:metrics-ws-cred-test.properties", ignoreResourceNotFound = true)
public class WsAppConfigTest extends WsAppConfig{
    
}
