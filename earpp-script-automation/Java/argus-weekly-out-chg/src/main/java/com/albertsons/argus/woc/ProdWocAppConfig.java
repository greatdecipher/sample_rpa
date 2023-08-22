package com.albertsons.argus.woc;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("prod")
@PropertySource("classpath:argus-woc-app-${spring.profiles.active}.properties")
public class ProdWocAppConfig {
    
}