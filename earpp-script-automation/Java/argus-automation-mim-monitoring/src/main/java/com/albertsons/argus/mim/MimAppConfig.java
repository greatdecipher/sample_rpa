package com.albertsons.argus.mim;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("argus-app.properties")
@PropertySource(value = "file:${home_deploy_MIM_Monitoring}/config/argus-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("argus-cred.properties")
@EncryptablePropertySource(value = "file:${home_deploy_MIM_Monitoring}/config/argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class MimAppConfig {

}
