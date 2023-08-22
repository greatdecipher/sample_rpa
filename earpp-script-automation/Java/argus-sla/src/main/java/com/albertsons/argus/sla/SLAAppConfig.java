package com.albertsons.argus.sla;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Profile({"local","dev","qa","prod"})
@PropertySource (value = "classpath:argus-app-sla-${spring.profiles.active}.properties")
public class SLAAppConfig {
    
}
