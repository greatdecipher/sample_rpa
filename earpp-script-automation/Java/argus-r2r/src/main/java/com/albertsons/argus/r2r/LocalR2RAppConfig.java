package com.albertsons.argus.r2r;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("local")
@PropertySource("classpath:argus-r2r-app-${spring.profiles.active}.properties")
@EncryptablePropertySource("classpath:argus-r2r-cred-${spring.profiles.active}.properties")
public class LocalR2RAppConfig {

}
