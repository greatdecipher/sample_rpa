package com.albertsons.argus.domaindbq2c.dto;

public class ProcessFileDetailsFilteredDTO {
    private String fileName;
    private String attribute2;
    private String attribute4;
    private String loadStatus;
    private String processTime;

    public ProcessFileDetailsFilteredDTO() {
        
    }

    public ProcessFileDetailsFilteredDTO(String fileName, String attribute2, String attribute4, String loadStatus, String processTime) {
        this.fileName = fileName;
        this.attribute2 = attribute2;
        this.attribute4 = attribute4;
        this.loadStatus = loadStatus;
        this.processTime = processTime;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAttribute2() {
        return this.attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute4() {
        return this.attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getLoadStatus() {
        return this.loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
        this.loadStatus = loadStatus;
    }

    public String getProcessTime() {
        return this.processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

}