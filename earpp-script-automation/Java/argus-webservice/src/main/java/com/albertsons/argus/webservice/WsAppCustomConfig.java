package com.albertsons.argus.webservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

/**
 * 
 * @author kbuen03
 * @since 11/22/22
 * @version 1.0
 */
@Configuration
@Profile({"esedAll","esedRsil","esedRsil2","so","psac"})
@PropertySource(value = "classpath:metrics-ws-app-prod.properties")
@EncryptablePropertySource(value = "classpath:metrics-ws-cred-prod.properties")
public class WsAppCustomConfig extends WsAppConfigAll{
    
}
