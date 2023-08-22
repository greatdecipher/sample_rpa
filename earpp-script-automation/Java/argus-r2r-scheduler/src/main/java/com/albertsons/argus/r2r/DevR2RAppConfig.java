package com.albertsons.argus.r2r;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("dev")
@PropertySource("classpath:argus-r2r-app-${spring.profiles.active}.properties")
public class DevR2RAppConfig {

}
