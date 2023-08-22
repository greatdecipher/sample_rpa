package com.albertsons.argus.wic;

import java.time.Duration;

import com.albertsons.argus.webservice.WsAppConfig;
import com.albertsons.argus.wic.exception.RestTemplateResponseErrorHandler;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
//@PropertySource("argus-wic-app.properties")
//@PropertySource(value = "file:${WIC_home_deploy}/config/argus-wic-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
//@EncryptablePropertySource("argus-cred.properties")
//@EncryptablePropertySource(value = "file:${WIC_home_deploy}/config/argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource(value = "classpath:argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@PropertySource(value = "classpath:argus-wic-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class WicAppConfig extends WsAppConfig{
    /*@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();     
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);    
        RestTemplate restTemplate = builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }*/
}
