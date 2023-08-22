package com.albertsons.argus.servicenow.web.service;

import com.albertsons.argus.domain.bo.generated.Error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public interface RestTemplateService {

    public String getRestResponseBodyString(String uri, HttpHeaders headers, String requestBody, HttpMethod httpMethod);
    public Error getObjectTemplate();
    public Integer getExchangeTemplate();
}
