package com.albertsons.argus.domaindbq2c.dto;

public class BankProcessInstanceDTO {
    private Long instanceId;
    private String batchName;
    private String integrationName;
    private String integrationPattern;
    private String status;
    private String transformationStatus;
    private String interfaceId;
    private String runDate;
    private String attribute1;
    private String attribute2;
    private String processTime;

    public BankProcessInstanceDTO() {

    }
    
    public BankProcessInstanceDTO(Long instanceId, String batchName, String integrationName, String integrationPattern, String status, String transformationStatus, String interfaceId, String runDate, String attribute1, String attribute2, String processTime) {
        this.instanceId = instanceId;
        this.batchName = batchName;
        this.integrationName = integrationName;
        this.integrationPattern = integrationPattern;
        this.status = status;
        this.transformationStatus = transformationStatus;
        this.interfaceId = interfaceId;
        this.runDate = runDate;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.processTime = processTime;
    }

    public Long getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getBatchName() {
        return this.batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getIntegrationName() {
        return this.integrationName;
    }

    public void setIntegrationName(String integrationName) {
        this.integrationName = integrationName;
    }

    public String getIntegrationPattern() {
        return this.integrationPattern;
    }

    public void setIntegrationPattern(String integrationPattern) {
        this.integrationPattern = integrationPattern;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransformationStatus() {
        return this.transformationStatus;
    }

    public void setTransformationStatus(String transformationStatus) {
        this.transformationStatus = transformationStatus;
    }

    public String getInterfaceId() {
        return this.interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getRunDate() {
        return this.runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public String getAttribute1() {
        return this.attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return this.attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getProcessTime() {
        return this.processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
    
}