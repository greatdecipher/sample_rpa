package com.albertsons.argus.tma;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
/**
 * 
 * @author kbuen03
 * @since 5/21/21
 * @version 1.0
 * 
 */
@Configuration
@PropertySource("classpath:argus-app.properties")
@PropertySource(value = "file:${home_deploy}/config/argus-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("classpath:argus-cred.properties")
@EncryptablePropertySource(value = "file:${home_deploy}/config/argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class TmaAppConfig {
    
}
