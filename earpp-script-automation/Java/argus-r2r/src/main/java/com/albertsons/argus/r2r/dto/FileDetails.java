package com.albertsons.argus.r2r.dto;

public class FileDetails {
    
    private String filename;
    private String filePrefix;
    private String interfaceName;
    private String jobName;
    private String mimInterface;
    private String labelContact;
    private String fileRequirement;
    private String dataSize;
    private int kafkaConsumeTime;
    private int oicRuntime;

    public FileDetails() {
        
    }
    
    public FileDetails(String filename, String filePrefix, String interfaceName, String jobName, String mimInterface, String labelContact, String fileRequirement, String dataSize, int kafkaConsumeTime, int oicRuntime) {
        this.filename = filename;
        this.filePrefix = filePrefix;
        this.interfaceName = interfaceName;
        this.jobName = jobName;
        this.mimInterface = mimInterface;
        this.labelContact = labelContact;
        this.fileRequirement = fileRequirement;
        this.dataSize = dataSize;
        this.kafkaConsumeTime = kafkaConsumeTime;
        this.oicRuntime = oicRuntime;
    }

    public FileDetails(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePrefix() {
        return this.filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public String getInterfaceName() {
        return this.interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;

    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getMimInterface() {
        return this.mimInterface;
    }

    public void setMimInterface(String mimInterface) {
        this.mimInterface = mimInterface;
    }

    public String getLabelContact() {
        return this.labelContact;
    }

    public void setLabelContact(String labelContact) {
        this.labelContact = labelContact;
    }

    public String getFileRequirement() {
        return this.fileRequirement;
    }

    public void setFileRequirement(String fileRequirement) {
        this.fileRequirement = fileRequirement;
    }

    public String getDataSize() {
        return this.dataSize;
    }

    public void setDataSize(String dataSize) {
        this.dataSize = dataSize;
    }

    public int getKafkaConsumeTime() {
        return this.kafkaConsumeTime;
    }

    public void setKafkaConsumeTime(int kafkaConsumeTime) {
        this.kafkaConsumeTime = kafkaConsumeTime;
    }

    public int getOicRuntime() {
        return this.oicRuntime;
    }

    public void setOicRuntime(int oicRuntime) {
        this.oicRuntime = oicRuntime;
    }

}
