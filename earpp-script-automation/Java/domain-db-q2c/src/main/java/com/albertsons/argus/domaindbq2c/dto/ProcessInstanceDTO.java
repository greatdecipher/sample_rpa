package com.albertsons.argus.domaindbq2c.dto;

public class ProcessInstanceDTO {
    private Long instanceId;
    private String integrationName;
    private String integrationPattern;
    private String runDate;
    private String startTime;
    private String endTime;
    private String statusTime;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;
    private String attribute8;
    private String attribute9;
    private String attribute10;
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;
    private String status;
    private String aicInstanceId;
    private String transformationStatus;
    private String startBatch;
    private String endBatch;
    private String interfaceId;
    private String transformationMsg;
    private String batchName;
    private String parentInstanceId;
    private String processTime;

    public ProcessInstanceDTO() {
        
    }

    public ProcessInstanceDTO(Long instanceId, String integrationName, String integrationPattern, String runDate, String startTime, String endTime, String statusTime, String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, String attribute6, String attribute7, String attribute8, String attribute9, String attribute10, String attribute11, String attribute12, String attribute13, String attribute14, String attribute15, String status, String aicInstanceId, String transformationStatus, String startBatch, String endBatch, String interfaceId, String transformationMsg, String batchName, String parentInstanceId, String processTime) {
        this.instanceId = instanceId;
        this.integrationName = integrationName;
        this.integrationPattern = integrationPattern;
        this.runDate = runDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.statusTime = statusTime;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.attribute6 = attribute6;
        this.attribute7 = attribute7;
        this.attribute8 = attribute8;
        this.attribute9 = attribute9;
        this.attribute10 = attribute10;
        this.attribute11 = attribute11;
        this.attribute12 = attribute12;
        this.attribute13 = attribute13;
        this.attribute14 = attribute14;
        this.attribute15 = attribute15;
        this.status = status;
        this.aicInstanceId = aicInstanceId;
        this.transformationStatus = transformationStatus;
        this.startBatch = startBatch;
        this.endBatch = endBatch;
        this.interfaceId = interfaceId;
        this.transformationMsg = transformationMsg;
        this.batchName = batchName;
        this.parentInstanceId = parentInstanceId;
        this.processTime = processTime;
    }

    public Long getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
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

    public String getRunDate() {
        return this.runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatusTime() {
        return this.statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
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

    public String getAttribute3() {
        return this.attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return this.attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return this.attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute6() {
        return this.attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute7() {
        return this.attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute8() {
        return this.attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public String getAttribute9() {
        return this.attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute10() {
        return this.attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public String getAttribute11() {
        return this.attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    public String getAttribute12() {
        return this.attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute13() {
        return this.attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    public String getAttribute14() {
        return this.attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    public String getAttribute15() {
        return this.attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAicInstanceId() {
        return this.aicInstanceId;
    }

    public void setAicInstanceId(String aicInstanceId) {
        this.aicInstanceId = aicInstanceId;
    }

    public String getTransformationStatus() {
        return this.transformationStatus;
    }

    public void setTransformationStatus(String transformationStatus) {
        this.transformationStatus = transformationStatus;
    }

    public String getStartBatch() {
        return this.startBatch;
    }

    public void setStartBatch(String startBatch) {
        this.startBatch = startBatch;
    }

    public String getEndBatch() {
        return this.endBatch;
    }

    public void setEndBatch(String endBatch) {
        this.endBatch = endBatch;
    }

    public String getInterfaceId() {
        return this.interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getTransformationMsg() {
        return this.transformationMsg;
    }

    public void setTransformationMsg(String transformationMsg) {
        this.transformationMsg = transformationMsg;
    }

    public String getBatchName() {
        return this.batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getParentInstanceId() {
        return this.parentInstanceId;
    }

    public void setParentInstanceId(String parentInstanceId) {
        this.parentInstanceId = parentInstanceId;
    }

    public String getProcessTime() {
        return this.processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

}
