package com.albertsons.argus.r2r;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("prod")
@PropertySource("classpath:argus-r2r-app-${spring.profiles.active}.properties")
public class ProdR2RAppConfig {

}
