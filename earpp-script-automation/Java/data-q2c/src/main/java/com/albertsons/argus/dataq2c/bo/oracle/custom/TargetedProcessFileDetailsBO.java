package com.albertsons.argus.dataq2c.bo.oracle.custom;

public interface TargetedProcessFileDetailsBO {
    public String getFile_Name();
    public String getLoad_Status();
    public String getLoad_Date();
    public Integer getExpectedMessageCnt();
    public Integer getConsumed_Rec();
    public String getStart_Process();
    public String getEnd_Process();
    public String getBox_Name();
    public String getSource_Application_Code();
    public String getBatch_Name();
    public String getRemittance_Amount();
    public Long getInstance_Id();
    public String getProcess_Time();
    public String getStatus();
    public String getTransformation_Status();
    public String getTransformation_Msg();
}
