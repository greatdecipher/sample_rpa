package com.albertsons.argus.patchingctask;

import com.albertsons.argus.webservice.WsAppConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:argus-ctask-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("argus-cred.properties")
@EncryptablePropertySource(value = "classpath:argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)

public class PatchingCtaskAppConfig extends WsAppConfig {
    
}

