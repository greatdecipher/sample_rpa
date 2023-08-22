package com.albertsons.argus.domaindbq2c.dto;

public class TargetedProcessFileDetailsDTO {
    private String fileName;
    private String loadStatus;
    private String loadDate;
    private Integer expectedMessageCnt;
    private Integer consumedRec;
    private String startProcess;
    private String endProcess;
    private String boxName;
    private String sourceApplicationCode;
    private String batchName;
    private String remittanceAmount;
    private Long instanceId;
    private String processTime;
    private String status;
    private String transformationStatus;
    private String transformationMsg;

    public TargetedProcessFileDetailsDTO() {

    }

    public TargetedProcessFileDetailsDTO(String fileName, String loadStatus, String loadDate, Integer expectedMessageCnt, Integer consumedRec, String startProcess, String endProcess, String boxName, String sourceApplicationCode, String batchName, String remittanceAmount, Long instanceId, String processTime, String status, String transformationStatus, String transformationMsg) {
        this.fileName = fileName;
        this.loadStatus = loadStatus;
        this.loadDate = loadDate;
        this.expectedMessageCnt = expectedMessageCnt;
        this.consumedRec = consumedRec;
        this.startProcess = startProcess;
        this.endProcess = endProcess;
        this.boxName = boxName;
        this.sourceApplicationCode = sourceApplicationCode;
        this.batchName = batchName;
        this.remittanceAmount = remittanceAmount;
        this.instanceId = instanceId;
        this.processTime = processTime;
        this.status = status;
        this.transformationStatus = transformationStatus;
        this.transformationMsg = transformationMsg;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLoadStatus() {
        return this.loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getLoadDate() {
        return this.loadDate;
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public Integer getExpectedMessageCnt() {
        return this.expectedMessageCnt;
    }

    public void setExpectedMessageCnt(Integer expectedMessageCnt) {
        this.expectedMessageCnt = expectedMessageCnt;
    }

    public Integer getConsumedRec() {
        return this.consumedRec;
    }

    public void setConsumedRec(Integer consumedRec) {
        this.consumedRec = consumedRec;
    }

    public String getStartProcess() {
        return this.startProcess;
    }

    public void setStartProcess(String startProcess) {
        this.startProcess = startProcess;
    }

    public String getEndProcess() {
        return this.endProcess;
    }

    public void setEndProcess(String endProcess) {
        this.endProcess = endProcess;
    }

    public String getBoxName() {
        return this.boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public String getSourceApplicationCode() {
        return this.sourceApplicationCode;
    }

    public void setSourceApplicationCode(String sourceApplicationCode) {
        this.sourceApplicationCode = sourceApplicationCode;
    }

    public String getBatchName() {
        return this.batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getRemittanceAmount() {
        return this.remittanceAmount;
    }

    public void setRemittanceAmount(String remittanceAmount) {
        this.remittanceAmount = remittanceAmount;
    }

    public Long getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public String getProcessTime() {
        return this.processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
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

    public String getTransformationMsg() {
        return this.transformationMsg;
    }

    public void setTransformationMsg(String transformationMsg) {
        this.transformationMsg = transformationMsg;
    }

}
