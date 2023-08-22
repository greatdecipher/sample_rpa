package com.albertsons.argus.domain.oracle;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

@Configuration
@Profile({"local","dev","qa","prod"})
@PropertySource(value = "classpath:domain-app-oracle-${spring.profiles.active}.properties")
@EncryptablePropertySource(value = "classpath:domain-cred-oracle-${spring.profiles.active}.properties")
public class OracleAppConfig {
   
}