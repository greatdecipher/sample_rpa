package com.albertsons.argus.hire;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

@Configuration
@Profile({"local","dev","qa","prod"})
@PropertySource(value = "classpath:argus-hire-app-${spring.profiles.active}.properties")
@EncryptablePropertySource(value = "classpath:argus-hire-cred-${spring.profiles.active}.properties")
public class HireAppConfig {
    
}
