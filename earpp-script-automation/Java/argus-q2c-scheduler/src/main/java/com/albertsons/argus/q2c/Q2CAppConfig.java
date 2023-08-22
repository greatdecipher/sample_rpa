package com.albertsons.argus.q2c;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;

@Configuration
@Profile({"local","dev","qa","prod"})
@PropertySource(value = "classpath:argus-q2c-app-${spring.profiles.active}.properties")
public class Q2CAppConfig {
   
}