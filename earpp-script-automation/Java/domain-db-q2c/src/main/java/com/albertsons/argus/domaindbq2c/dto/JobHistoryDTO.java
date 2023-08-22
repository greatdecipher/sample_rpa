package com.albertsons.argus.domaindbq2c.dto;

public class JobHistoryDTO {
    private String alternateDocumentId;
    private Long instanceId;
    private String callbackId;
    private String jobId;
    private String jobName;
    private String jobStatus;
    private String processTime;

    public JobHistoryDTO() {
        
    }

    public JobHistoryDTO(String alternateDocumentId, Long instanceId, String callbackId, String jobId, String jobName, String jobStatus, String processTime) {
        this.alternateDocumentId = alternateDocumentId;
        this.instanceId = instanceId;
        this.callbackId = callbackId;
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobStatus = jobStatus;
        this.processTime = processTime;
    }

    public String getAlternateDocumentId() {
        return this.alternateDocumentId;
    }

    public void setAlternateDocumentId(String alternateDocumentId) {
        this.alternateDocumentId = alternateDocumentId;
    }

    public Long getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getCallbackId() {
        return this.callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getJobId() {
        return this.jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobStatus() {
        return this.jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getProcessTime() {
        return this.processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
}