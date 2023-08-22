package com.albertsons.argus.webservice;

import java.time.Duration;

import com.albertsons.argus.webservice.exception.RestTemplateResponseErrorHandler;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author jborj20
 * @since 11/10/22
 * @version 1.0
 */
public class WsAppConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }
}
