package com.albertsons.argus.meraki.service.impl;

import java.util.Arrays;

import com.albertsons.argus.meraki.exception.MerakiException;
import com.albertsons.argus.meraki.service.MerakiWebService;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
/**
 * 
 * @author kbuen03
 * @since 1/17/21
 * @version 1.0
 * 
 */
@Service
public class MerakiWebServiceImpl implements MerakiWebService {
    private static final Logger LOG = LogManager.getLogger(MerakiWebServiceImpl.class);
    @Autowired
    private Environment environment;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String sendARPRequest(String request) throws MerakiException{
        LOG.log(Level.DEBUG, () -> "start sendARPRequest method. . .");
        LOG.log(Level.DEBUG, () -> "request body: "+request);

        String responseBodyStr = "";
        String tokenProp = environment.getProperty("argus.meraki.webservice.arp.request.token.property");
        String tokenValue = environment.getProperty("argus.meraki.webservice.arp.request.token.value");
        String aprWebserviceAPI = environment.getProperty("argus.meraki.webservice.arp.uri") 
                                + environment.getProperty("argus.meraki.webservice.arp.uri.path") ;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenProp, tokenValue);
        LOG.log(Level.DEBUG, () -> "tN -> "+tokenValue);
        try{
            responseBodyStr = getRestResponseBodyString(aprWebserviceAPI, headers, request, HttpMethod.POST);
        }catch(RestClientException re){
            LOG.log(Level.ERROR, () -> "RestClientException: "+re);
            throw new MerakiException(re.getMessage());
        }

        return responseBodyStr;
    }

    private String getRestResponseBodyString(String uri, HttpHeaders headers, String requestBody,
            HttpMethod httpMethod) throws RestClientException{
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate
                .exchange(uri, httpMethod, entity, String.class)
                .getBody();
    }

    
}
