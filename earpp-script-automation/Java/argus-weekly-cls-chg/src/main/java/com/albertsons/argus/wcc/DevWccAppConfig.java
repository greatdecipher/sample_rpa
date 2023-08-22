package com.albertsons.argus.wcc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("dev")
@PropertySource("classpath:argus-wcc-app-${spring.profiles.active}.properties")
public class DevWccAppConfig {

}
