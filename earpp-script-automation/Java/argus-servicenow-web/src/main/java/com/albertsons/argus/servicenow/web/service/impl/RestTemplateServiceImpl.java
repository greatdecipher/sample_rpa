package com.albertsons.argus.servicenow.web.service.impl;

import java.util.Arrays;

import com.albertsons.argus.domain.bo.generated.Error;
import com.albertsons.argus.servicenow.web.exception.RestTemplateResponseErrorHandler;
import com.albertsons.argus.servicenow.web.service.RestTemplateService;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author kbuen03 
 * @since 05/26/2021
 */
@Service
public class RestTemplateServiceImpl implements RestTemplateService {
    private static final Logger LOG = LogManager.getLogger(RestTemplateServiceImpl.class);
    private RestTemplate restTemplate;

    private Environment environment;

    @Autowired
    public RestTemplateServiceImpl(RestTemplateBuilder restTemplateBuilder, Environment env) {
        LOG.log(Level.DEBUG, "Begin constructor . . .");
        this.environment = env;
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(env.getProperty("encrypted.web.ws.property.username"),
                        env.getProperty("encrypted.web.ws.property.password"))
                .errorHandler(new RestTemplateResponseErrorHandler()).build();
        LOG.log(Level.DEBUG, "End constructor . . .");
    }

    @Override
    public String getRestResponseBodyString(String uri, HttpHeaders headers, String requestBody,
            HttpMethod httpMethod) {
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
       /* List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();        
         //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        
        // Note: here we are making this converter to process any kind of response, 
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));        
        messageConverters.add(converter);  
        restTemplate.setMessageConverters(messageConverters); 
 */
        return restTemplate
                .exchange(environment.getProperty("argus.web.ws.uri.host") + uri, httpMethod, entity, String.class)
                .getBody();
    }

    // ---------------------------------------------------------------------------------------------------------------------
    @Override
    public Error getObjectTemplate() {
        return restTemplate.getForObject("https://safewayqa1.service-now.com/api/now/actsub/activities", Error.class);
    }

    @Override
    public Integer getExchangeTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange("https://safewayqa1.service-now.com/api/now/stats/incident", HttpMethod.GET,
                entity, String.class).getStatusCode().value();

    }
}
