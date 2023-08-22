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
@Profile({"local","dev","qa","prod"})
@PropertySource(value = "classpath:metrics-ws-app-${spring.profiles.active}.properties",ignoreResourceNotFound = true)
@EncryptablePropertySource(value = "classpath:metrics-ws-cred-${spring.profiles.active}.properties",ignoreResourceNotFound = true)
public class WsAppConfigAll extends WsAppConfig{
   
}
