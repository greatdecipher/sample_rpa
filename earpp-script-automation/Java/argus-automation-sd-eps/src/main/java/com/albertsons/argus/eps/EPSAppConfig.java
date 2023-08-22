package com.albertsons.argus.eps;

import java.time.Duration;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.albertsons.argus.eps.exception.RestTemplateResponseErrorHandler;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

@Configuration
@PropertySource("argus-eps-app.properties")
@PropertySource(value = "classpath:argus-eps-app-${spring.profiles.active}.properties")
@EncryptablePropertySource("argus-cred.properties")
@EncryptablePropertySource(value = "classpath:argus-cred-${spring.profiles.active}.properties")

public class EPSAppConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }
}
