package com.albertsons.argus.wic;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("argus-app.properties")
//@PropertySource(value = "file:${WIC_home_deploy}/config/argus-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("argus-cred.properties")
//@EncryptablePropertySource(value = "file:${WIC_home_deploy}/config/argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class WicAppConfig {

}
