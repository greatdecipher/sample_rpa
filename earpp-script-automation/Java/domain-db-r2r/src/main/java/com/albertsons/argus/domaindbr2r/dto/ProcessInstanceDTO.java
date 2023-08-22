package com.albertsons.argus.domaindbr2r.dto;

public class ProcessInstanceDTO {
    public Long Instance_Id;
    public String Integration_Name;
    public String Integration_Pattern;
    public String Run_Date;
    public String Start_Time;
    public String End_Time;
    public String Status_Time;
    public String Attribute1;
    public String Attribute2;
    public String Attribute3;
    public String Attribute4;
    public String Attribute5;
    public String Attribute6;
    public String Attribute7;
    public String Attribute8;
    public String Attribute9;
    public String Attribute10;
    public String Attribute11;
    public String Attribute12;
    public String Attribute13;
    public String Attribute14;
    public String Attribute15;
    public String Status;
    public String Aic_Instance_Id;
    public String Transformation_Status;
    public String Start_Batch;
    public String End_Batch;
    public String Interface_Id;
    public String Transformation_Msg;
    public String Batch_Name;
    public String Parent_Instance_Id;
    public String Process_Time;

    public ProcessInstanceDTO(){

    }

    public ProcessInstanceDTO(Long Instance_Id, String Integration_Name, String Integration_Pattern, String Run_Date, String Start_Time, String End_Time, String Status_Time, String Attribute1, String Attribute2, String Attribute3, String Attribute4, String Attribute5, String Attribute6, String Attribute7, String Attribute8, String Attribute9, String Attribute10, String Attribute11, String Attribute12, String Attribute13, String Attribute14, String Attribute15, String Status, String Aic_Instance_Id, String Transformation_Status, String Start_Batch, String End_Batch, String Interface_Id, String Transformation_Msg, String Batch_Name, String Parent_Instance_Id, String Process_Time) {
        this.Instance_Id = Instance_Id;
        this.Integration_Name = Integration_Name;
        this.Integration_Pattern = Integration_Pattern;
        this.Run_Date = Run_Date;
        this.Start_Time = Start_Time;
        this.End_Time = End_Time;
        this.Status_Time = Status_Time;
        this.Attribute1 = Attribute1;
        this.Attribute2 = Attribute2;
        this.Attribute3 = Attribute3;
        this.Attribute4 = Attribute4;
        this.Attribute5 = Attribute5;
        this.Attribute6 = Attribute6;
        this.Attribute7 = Attribute7;
        this.Attribute8 = Attribute8;
        this.Attribute9 = Attribute9;
        this.Attribute10 = Attribute10;
        this.Attribute11 = Attribute11;
        this.Attribute12 = Attribute12;
        this.Attribute13 = Attribute13;
        this.Attribute14 = Attribute14;
        this.Attribute15 = Attribute15;
        this.Status = Status;
        this.Aic_Instance_Id = Aic_Instance_Id;
        this.Transformation_Status = Transformation_Status;
        this.Start_Batch = Start_Batch;
        this.End_Batch = End_Batch;
        this.Interface_Id = Interface_Id;
        this.Transformation_Msg = Transformation_Msg;
        this.Batch_Name = Batch_Name;
        this.Parent_Instance_Id = Parent_Instance_Id;
        this.Process_Time = Process_Time;
    }

    public Long getInstance_Id() {
        return this.Instance_Id;
    }

    public void setInstance_Id(Long Instance_Id) {
        this.Instance_Id = Instance_Id;
    }

    public String getIntegration_Name() {
        return this.Integration_Name;
    }

    public void setIntegration_Name(String Integration_Name) {
        this.Integration_Name = Integration_Name;
    }

    public String getIntegration_Pattern() {
        return this.Integration_Pattern;
    }

    public void setIntegration_Pattern(String Integration_Pattern) {
        this.Integration_Pattern = Integration_Pattern;
    }

    public String getRun_Date() {
        return this.Run_Date;
    }

    public void setRun_Date(String Run_Date) {
        this.Run_Date = Run_Date;
    }

    public String getStart_Time() {
        return this.Start_Time;
    }

    public void setStart_Time(String Start_Time) {
        this.Start_Time = Start_Time;
    }

    public String getEnd_Time() {
        return this.End_Time;
    }

    public void setEnd_Time(String End_Time) {
        this.End_Time = End_Time;
    }

    public String getStatus_Time() {
        return this.Status_Time;
    }

    public void setStatus_Time(String Status_Time) {
        this.Status_Time = Status_Time;
    }

    public String getAttribute1() {
        return this.Attribute1;
    }

    public void setAttribute1(String Attribute1) {
        this.Attribute1 = Attribute1;
    }

    public String getAttribute2() {
        return this.Attribute2;
    }

    public void setAttribute2(String Attribute2) {
        this.Attribute2 = Attribute2;
    }

    public String getAttribute3() {
        return this.Attribute3;
    }

    public void setAttribute3(String Attribute3) {
        this.Attribute3 = Attribute3;
    }

    public String getAttribute4() {
        return this.Attribute4;
    }

    public void setAttribute4(String Attribute4) {
        this.Attribute4 = Attribute4;
    }

    public String getAttribute5() {
        return this.Attribute5;
    }

    public void setAttribute5(String Attribute5) {
        this.Attribute5 = Attribute5;
    }

    public String getAttribute6() {
        return this.Attribute6;
    }

    public void setAttribute6(String Attribute6) {
        this.Attribute6 = Attribute6;
    }

    public String getAttribute7() {
        return this.Attribute7;
    }

    public void setAttribute7(String Attribute7) {
        this.Attribute7 = Attribute7;
    }

    public String getAttribute8() {
        return this.Attribute8;
    }

    public void setAttribute8(String Attribute8) {
        this.Attribute8 = Attribute8;
    }

    public String getAttribute9() {
        return this.Attribute9;
    }

    public void setAttribute9(String Attribute9) {
        this.Attribute9 = Attribute9;
    }

    public String getAttribute10() {
        return this.Attribute10;
    }

    public void setAttribute10(String Attribute10) {
        this.Attribute10 = Attribute10;
    }

    public String getAttribute11() {
        return this.Attribute11;
    }

    public void setAttribute11(String Attribute11) {
        this.Attribute11 = Attribute11;
    }

    public String getAttribute12() {
        return this.Attribute12;
    }

    public void setAttribute12(String Attribute12) {
        this.Attribute12 = Attribute12;
    }

    public String getAttribute13() {
        return this.Attribute13;
    }

    public void setAttribute13(String Attribute13) {
        this.Attribute13 = Attribute13;
    }

    public String getAttribute14() {
        return this.Attribute14;
    }

    public void setAttribute14(String Attribute14) {
        this.Attribute14 = Attribute14;
    }

    public String getAttribute15() {
        return this.Attribute15;
    }

    public void setAttribute15(String Attribute15) {
        this.Attribute15 = Attribute15;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getAic_Instance_Id() {
        return this.Aic_Instance_Id;
    }

    public void setAic_Instance_Id(String Aic_Instance_Id) {
        this.Aic_Instance_Id = Aic_Instance_Id;
    }

    public String getTransformation_Status() {
        return this.Transformation_Status;
    }

    public void setTransformation_Status(String Transformation_Status) {
        this.Transformation_Status = Transformation_Status;
    }

    public String getStart_Batch() {
        return this.Start_Batch;
    }

    public void setStart_Batch(String Start_Batch) {
        this.Start_Batch = Start_Batch;
    }

    public String getEnd_Batch() {
        return this.End_Batch;
    }

    public void setEnd_Batch(String End_Batch) {
        this.End_Batch = End_Batch;
    }

    public String getInterface_Id() {
        return this.Interface_Id;
    }

    public void setInterface_Id(String Interface_Id) {
        this.Interface_Id = Interface_Id;
    }

    public String getTransformation_Msg() {
        return this.Transformation_Msg;
    }

    public void setTransformation_Msg(String Transformation_Msg) {
        this.Transformation_Msg = Transformation_Msg;
    }

    public String getBatch_Name() {
        return this.Batch_Name;
    }

    public void setBatch_Name(String Batch_Name) {
        this.Batch_Name = Batch_Name;
    }

    public String getParent_Instance_Id() {
        return this.Parent_Instance_Id;
    }

    public void setParent_Instance_Id(String Parent_Instance_Id) {
        this.Parent_Instance_Id = Parent_Instance_Id;
    }

    public String getProcess_Time() {
        return this.Process_Time;
    }

    public void setProcess_Time(String Process_Time) {
        this.Process_Time = Process_Time;
    }

}
