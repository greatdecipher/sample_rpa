package com.albertsons.argus.q2c.dto;

public class InboundMask {
    
    private String filePrefix;
    private String interfaceName;
    private String inboundProcessType;
    private String fileRequirement;

    public InboundMask() {
        
    }
    
    public InboundMask(String filePrefix, String interfaceName, String inboundProcessType, String fileRequirement) {
        this.filePrefix = filePrefix;
        this.interfaceName = interfaceName;
        this.inboundProcessType = inboundProcessType;
        this.fileRequirement = fileRequirement;
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

    public String getInboundProcessType() {
        return this.inboundProcessType;
    }

    public void setInboundProcessType(String inboundProcessType) {
        this.inboundProcessType = inboundProcessType;
    }

    public String getFileRequirement() {
        return this.fileRequirement;
    }

    public void setFileRequirement(String fileRequirement) {
        this.fileRequirement = fileRequirement;
    }
}
