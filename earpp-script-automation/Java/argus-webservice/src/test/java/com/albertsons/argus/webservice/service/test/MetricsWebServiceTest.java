package com.albertsons.argus.webservice.service.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.exception.JsonException;
import com.albertsons.argus.webservice.service.JsonService;
import com.albertsons.argus.webservice.service.MetricsWebService;

@SpringBootTest
@ActiveProfiles("test")
class MetricsWebServiceTest {
    @MockBean
    private RestTemplate restTemplateMock;

    @MockBean
    private JsonService<ResponseStartSaveBO> jsonResponseStartSaveBoMock;

    @MockBean
    private JsonService<ResponseIncrementTransactionBO> jsonResponseIncrementTransactionBoMock;

    @MockBean
    private JsonService<ResponseEndSaveBO> jsonResponseEndSaveBoMock;

    @Autowired
    private MetricsWebService metricsWebService;

    @BeforeEach
    void init(){

    }

    @Test
    void startSaveTest(){
        ResponseStartSaveBO bo = new ResponseStartSaveBO();
        bo.setResult("SUCCESS");
        bo.setId("12");

        try {
            when(jsonResponseStartSaveBoMock.jsonToObjMapper(anyString())).thenReturn(bo);
        } catch (JsonException e) {
            e.printStackTrace();
        }

        when(restTemplateMock
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), Mockito.<Class<String>>any()))
        .thenReturn(
                new ResponseEntity<String>("{\"result\": \"SUCCESS\", \"id\": \"12\"}", HttpStatus.OK));

        ResponseStartSaveBO bo2 = metricsWebService.startSave("A000000", "1.0");

        assertTrue(bo2!=null);
        assertTrue(bo2.getResult().contains("SUCCESS"));
        assertTrue(bo2.getId().equals("12"));
    }

    @Test
    void incrementTransactionTest(){
        ResponseIncrementTransactionBO bo = new ResponseIncrementTransactionBO();
        bo.setResult("SUCCESS");
        bo.setId("12");

        try {
            when(jsonResponseIncrementTransactionBoMock.jsonToObjMapper(anyString())).thenReturn(bo);
        } catch (JsonException e) {
            e.printStackTrace();
        }

        when(restTemplateMock
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), Mockito.<Class<String>>any()))
        .thenReturn(
                new ResponseEntity<String>("{\"result\": \"SUCCESS\", \"id\": \"12\"}", HttpStatus.OK));

        ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction("12", "3");

        assertTrue(bo2!=null);
        assertTrue(bo2.getResult().contains("SUCCESS"));
        assertTrue(bo2.getId().equals("12"));
    }

    @Test
    void endSaveTest(){
        ResponseEndSaveBO bo = new ResponseEndSaveBO();
        bo.setResult("SUCCESS");
        bo.setId("12");

        try {
            when(jsonResponseEndSaveBoMock.jsonToObjMapper(anyString())).thenReturn(bo);
        } catch (JsonException e) {
            e.printStackTrace();
        }

        when(restTemplateMock
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), Mockito.<Class<String>>any()))
        .thenReturn(
                new ResponseEntity<String>("{\"result\": \"SUCCESS\", \"id\": \"12\"}", HttpStatus.OK));

        ResponseEndSaveBO bo2 = metricsWebService.endSave("12");

        assertTrue(bo2!=null);
        assertTrue(bo2.getResult().contains("SUCCESS"));
        assertTrue(bo2.getId().equals("12"));
    }

    @Test
    void startSaveTest_jsonException(){
        try {
            when(jsonResponseStartSaveBoMock.jsonToObjMapper(anyString())).thenThrow(new JsonException("Json Exception Test."));
        } catch (JsonException e) {
            e.printStackTrace();
        }

        when(restTemplateMock
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), Mockito.<Class<String>>any()))
        .thenReturn(
                new ResponseEntity<String>("{\"result\": \"Error on Start Save\", \"id\": \"NULL\"}", HttpStatus.OK));
        
        ResponseStartSaveBO bo =  metricsWebService.startSave("A000000", "1.0");

        assertTrue(bo==null);
    }

    @Test
    void endSaveTest_jsonException(){
        try {
            when(jsonResponseEndSaveBoMock.jsonToObjMapper(anyString())).thenThrow(new JsonException("Json Exception Test."));
        } catch (JsonException e) {
            e.printStackTrace();
        }

        when(restTemplateMock
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), Mockito.<Class<String>>any()))
        .thenReturn(
                new ResponseEntity<String>("{\"result\": \"Error on End Save\", \"id\": \"NULL\"}", HttpStatus.OK));
        
        ResponseEndSaveBO bo =  metricsWebService.endSave("12");

        assertTrue(bo==null);
    }
    
    @Test
    void incrementTransactionTest_jsonException(){
        try {
            when(jsonResponseStartSaveBoMock.jsonToObjMapper(anyString())).thenThrow(new JsonException("Json Exception Test."));
        } catch (JsonException e) {
            e.printStackTrace();
        }

        when(restTemplateMock
        .exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), Mockito.<Class<String>>any()))
        .thenReturn(
                new ResponseEntity<String>("{\"result\": \"Error incrementing transaction\", \"id\": \"NULL\"}", HttpStatus.OK));
        
        ResponseIncrementTransactionBO bo =  metricsWebService.incrementTransaction("A000000", "1.0");

        assertTrue(bo==null);
    }
}
