package com.albertsons.argus.dataq2c.bo.oracle.custom;

public interface BankProcessInstanceBO {
    public Long getInstance_Id();
    public String getBatch_Name();
    public String getIntegration_Name();
    public String getIntegration_Pattern();
    public String getStatus();
    public String getTransformation_Status();
    public String getInterface_Id();
    public String getRun_Date();
    public String getAttribute1();
    public String getAttribute2();
    public String getProcess_Time();
}