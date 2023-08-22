package com.albertsons.argus.webservice.service;

import org.springframework.web.client.RestClientException;

import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;

public interface MetricsWebService {
    public ResponseStartSaveBO startSave(String automationId, String versionId) throws RestClientException;
    public ResponseEndSaveBO endSave(String automationRunId) throws RestClientException;
    public ResponseIncrementTransactionBO incrementTransaction(String automationRunId, String transactionIncrement) throws RestClientException;
}
