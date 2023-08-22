package com.albertsons.argus.meraki;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 
 * @author kbuen03
 * @since 1/3/21
 * @version 1.0
 * 
 */

@Configuration
@PropertySource("classpath:argus-meraki-app.properties")
@PropertySource(value = "classpath:argus-meraki-app-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
@EncryptablePropertySource("classpath:argus-meraki-cred.properties")
@EncryptablePropertySource(value = "classpath:argus-meraki-cred-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class MerakiAppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

    @Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.albertsons.argus.meraki")).build()
				.apiInfo(new ApiInfoBuilder().title("Meraki API")
						.description("Meraki APIs").version("0.0.1").build())
                .protocols(new HashSet<>(Arrays.asList("http", "https")));
	}
}
