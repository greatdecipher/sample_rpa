package com.albertsons.argus.ip;

import java.time.Duration;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.albertsons.argus.ip.exception.RestTemplateResponseErrorHandler;
import com.albertsons.argus.webservice.WsAppConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

@Configuration
@PropertySource(value = "classpath:argus-ip-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("argus-cred.properties")
@EncryptablePropertySource(value = "classpath:argus-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)

public class IPAppConfig extends WsAppConfig {
    
}
