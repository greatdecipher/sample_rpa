package com.albertsons.argus.nagware;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("argus-app-nagware.properties")
@PropertySource(value = "classpath:argus-app-nagware-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class NagwareConfig {
    
}
