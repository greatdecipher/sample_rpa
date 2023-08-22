package com.albertsons.argus.toggle;

import com.albertsons.argus.toggle.exception.RestTemplateResponseErrorHandler;
import com.albertsons.argus.webservice.WsAppConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("prod")
@PropertySource("classpath:argus-toggle-app-${spring.profiles.active}.properties")
@EncryptablePropertySource("argus-toggle-cred-${spring.profiles.active}.properties")
public class ProdToggleAppConfig extends WsAppConfig{
    // @Bean
    // public RestTemplate restTemplate(RestTemplateBuilder builder) {
    //     return builder
    //             .setConnectTimeout(Duration.ofMillis(3000))
    //             .setReadTimeout(Duration.ofMillis(3000))
    //             .errorHandler(new RestTemplateResponseErrorHandler())
    //             .build();
    // }
}
