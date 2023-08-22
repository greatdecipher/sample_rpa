package com.albertsons.argus.datar2r.bo.oracle.custom;

public interface RecordBO {
    public Long getGroup_Id();
    public String getBatch_Name();
    public Long getTotal_Rec();
    public Long getConsumed_Rec();
    public Long getRemaining_Rec();
    public String getSum_Entered_Dr();
    public String getSum_Entered_Cr();
    public String getMin_Process_Time();
    public String getMax_Process_Time();
}
