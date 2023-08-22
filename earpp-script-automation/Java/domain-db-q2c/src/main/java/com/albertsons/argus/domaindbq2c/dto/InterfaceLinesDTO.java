package com.albertsons.argus.domaindbq2c.dto;

public class InterfaceLinesDTO {
    private Long instanceId;
    private String alternateDocumentId;
    private String status;
    private String errorMessage;
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public InterfaceLinesDTO() {
        
    }

    public InterfaceLinesDTO(Long instanceId, String alternateDocumentId, String status, String errorMessage, String count) {
        this.instanceId = instanceId;
        this.alternateDocumentId = alternateDocumentId;
        this.status = status;
        this.errorMessage = errorMessage;
        this.count = count;
    }

    public Long getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getAlternateDocumentId() {
        return this.alternateDocumentId;
    }

    public void setAlternateDocumentId(String alternateDocumentId) {
        this.alternateDocumentId = alternateDocumentId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    
}