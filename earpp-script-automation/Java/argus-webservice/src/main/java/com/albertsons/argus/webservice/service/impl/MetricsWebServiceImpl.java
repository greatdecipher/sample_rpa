package com.albertsons.argus.webservice.service.impl;

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

import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.exception.JsonException;
import com.albertsons.argus.webservice.service.JsonService;
import com.albertsons.argus.webservice.service.MetricsWebService;

@Service
public class MetricsWebServiceImpl implements MetricsWebService{
    private static final Logger LOG = LogManager.getLogger(MetricsWebServiceImpl.class);
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonService<ResponseStartSaveBO> jsonResponseStartSaveBO;
    
    @Autowired
    private JsonService<ResponseEndSaveBO> jsonResponseEndSaveBO;
    
    @Autowired
    private JsonService<ResponseIncrementTransactionBO> jsonResponseIncrementTransactionBO;

    @Override
    public ResponseStartSaveBO startSave(String automationId, String versionId) throws RestClientException {
        LOG.log(Level.DEBUG, () -> "start method startSave(). . .");

        try {
            return jsonResponseStartSaveBO.jsonToObjMapper(restTemplate.exchange(
                    environment.getProperty("argus.metrics.webservice.uri") +
                            environment.getProperty("argus.metrics.webservice.uri.root") +
                            environment.getProperty("argus.metrics.webservice.uri.path.start.save"),
                    HttpMethod.PUT, getHttpEntityStartSave(automationId, versionId), String.class).getBody());
        } catch (JsonException e) {
            LOG.log(Level.ERROR, () -> "JSON Exception: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEndSaveBO endSave(String automationRunId) throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method endSave(). . .");

        try {
            return jsonResponseEndSaveBO.jsonToObjMapper(restTemplate.exchange(
                    environment.getProperty("argus.metrics.webservice.uri") +
                            environment.getProperty("argus.metrics.webservice.uri.root") +
                            environment.getProperty("argus.metrics.webservice.uri.path.end.save"),
                    HttpMethod.PUT, getHttpEntityEndSave(automationRunId), String.class).getBody());
        } catch (JsonException e) {
            LOG.log(Level.ERROR, () -> "JSON Exception: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseIncrementTransactionBO incrementTransaction(String automationRunId, String transactionIncrement)throws RestClientException{
        LOG.log(Level.DEBUG, () -> "start method incrementTransaction(). . .");

        try {
            return jsonResponseIncrementTransactionBO.jsonToObjMapper(restTemplate.exchange(
                    environment.getProperty("argus.metrics.webservice.uri") +
                            environment.getProperty("argus.metrics.webservice.uri.root") +
                            environment.getProperty("argus.metrics.webservice.uri.path.increment.transaction"),
                    HttpMethod.PUT, getHttpEntityIncrementTransaction(automationRunId, transactionIncrement), String.class).getBody());
        } catch (JsonException e) {
            LOG.log(Level.ERROR, () -> "JSON Exception: " + e.getMessage());
            return null;
        }
    }

    private HttpEntity<Object> getHttpEntityStartSave(String automationId, String versionId) {
        String token = environment.getProperty("encrypted.argus.metrics.token");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("AutomationID", automationId);
        headers.set("VersionID", versionId);

        return new HttpEntity<>(headers);
    }

    private HttpEntity<Object> getHttpEntityEndSave(String automationRunId) {
        String token = environment.getProperty("encrypted.argus.metrics.token");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("AutomationRunID", automationRunId);

        return new HttpEntity<>(headers);
    }

    private HttpEntity<Object> getHttpEntityIncrementTransaction(String automationRunId, String transactionIncrement) {
        String token = environment.getProperty("encrypted.argus.metrics.token");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", token);
        headers.set("AutomationRunID", automationRunId);
        headers.set("TransactionIncrement", transactionIncrement);

        return new HttpEntity<>(headers);
    }
}
