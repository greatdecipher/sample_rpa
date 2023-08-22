package com.albertsons.argus.domaindbr2r.dto;

public class RecordDTO {
    public Long Group_Id;
    public String Batch_Name;
    public Long Total_Rec;
    public Long Consumed_Rec;
    public Long Remaining_Rec;
    public String Sum_Entered_Dr;
    public String Sum_Entered_Cr;
    public String Min_Process_Time;
    public String Max_Process_Time;

    public RecordDTO(){

    }

    public RecordDTO(Long Group_Id, String Batch_Name, Long Total_Rec, Long Consumed_Rec, Long Remaining_Rec, String Sum_Entered_Dr, String Sum_Entered_Cr, String Min_Process_Time, String Max_Process_Time) {
        this.Group_Id = Group_Id;
        this.Batch_Name = Batch_Name;
        this.Total_Rec = Total_Rec;
        this.Consumed_Rec = Consumed_Rec;
        this.Remaining_Rec = Remaining_Rec;
        this.Sum_Entered_Dr = Sum_Entered_Dr;
        this.Sum_Entered_Cr = Sum_Entered_Cr;
        this.Min_Process_Time = Min_Process_Time;
        this.Max_Process_Time = Max_Process_Time;
    }

    public Long getGroup_Id() {
        return this.Group_Id;
    }

    public void setGroup_Id(Long getGroup_Id) {
        this.Group_Id = getGroup_Id;
    }

    public String getBatch_Name() {
        return this.Batch_Name;
    }

    public void setBatch_Name(String getBatch_Name) {
        this.Batch_Name = getBatch_Name;
    }

    public Long getTotal_Rec() {
        return this.Total_Rec;
    }

    public void setTotal_Rec(Long getTotal_Rec) {
        this.Total_Rec = getTotal_Rec;
    }

    public Long getConsumed_Rec() {
        return this.Consumed_Rec;
    }

    public void setConsumed_Rec(Long getConsumed_Rec) {
        this.Consumed_Rec = getConsumed_Rec;
    }

    public Long getRemaining_Rec() {
        return this.Remaining_Rec;
    }

    public void setRemaining_Rec(Long getRemaining_Rec) {
        this.Remaining_Rec = getRemaining_Rec;
    }

    public String getSum_Entered_Dr() {
        return this.Sum_Entered_Dr;
    }

    public void setSum_Entered_Dr(String getSum_Entered_Dr) {
        this.Sum_Entered_Dr = getSum_Entered_Dr;
    }

    public String getSum_Entered_Cr() {
        return this.Sum_Entered_Cr;
    }

    public void setSum_Entered_Cr(String getSum_Entered_Cr) {
        this.Sum_Entered_Cr = getSum_Entered_Cr;
    }

    public String getMin_Process_Time() {
        return this.Min_Process_Time;
    }

    public void setMin_Process_Time(String getMin_Process_Time) {
        this.Min_Process_Time = getMin_Process_Time;
    }

    public String getMax_Process_Time() {
        return this.Max_Process_Time;
    }

    public void setMax_Process_Time(String getMax_Process_Time) {
        this.Max_Process_Time = getMax_Process_Time;
    }

}
