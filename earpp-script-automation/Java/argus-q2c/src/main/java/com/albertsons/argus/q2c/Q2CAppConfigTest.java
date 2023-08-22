package com.albertsons.argus.q2c;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile({"test"})
@PropertySource(value = "classpath:argus-q2c-app-test.properties")
public class Q2CAppConfigTest {
    
}
