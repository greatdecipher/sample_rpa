package com.albertsons.argus.wcc.dto;

public class EmailDetails {

    private String number;
    private String type;
    private String changeEnv;
    private String risk;
    private String phase;
    private String phaseState;
    private String psDate;
    private String peDate;
    private String reqBy;
    private String assignName;
    private String changeAnalyst;
    private String aEmail;
    private String aGrp;
    private String managerName;
    private String mEmail;
    private String directorName;
    private String dEmail;
    private String vpName;
    private String vpEmail;

    public EmailDetails(){

    }

    public EmailDetails(String number, String type, String changeEnv, String risk, String phase, String phaseState, String psDate, String peDate, String reqBy, String assignName, String changeAnalyst, String aEmail, String aGrp, String managerName, String mEmail, String directorName, String dEmail, String vpName, String vpEmail){
        this.number = number;
        this.type = type;
        this.changeEnv = changeEnv;
        this.risk = risk;
        this.phase = phase;
        this.phaseState = phaseState;
        this.psDate = psDate;
        this.peDate = peDate;
        this.reqBy = reqBy;
        this.assignName = assignName;
        this.changeAnalyst = changeAnalyst;
        this.aEmail = aEmail;
        this.aGrp = aGrp;
        this.managerName = managerName;
        this.mEmail = mEmail;
        this.directorName = directorName;
        this.dEmail = dEmail;
        this.vpName = vpName;
        this.vpEmail = vpEmail;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getChangeEnv() {
        return changeEnv;
    }

    public void setChangeEnv(String changeEnv) {
        this.changeEnv = changeEnv;
    }
    
    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
    
    public String getPhaseState() {
        return phaseState;
    }

    public void setPhaseState(String phaseState) {
        this.phaseState = phaseState;
    }

    public String getPsDate() {
        return psDate;
    }

    public void setPsDate(String psDate) {
        this.psDate = psDate;
    }

    public String getPeDate() {
        return peDate;
    }

    public void setPeDate(String peDate) {
        this.peDate = peDate;
    }

    public String getReqBy() {
        return reqBy;
    }

    public void setReqBy(String reqBy) {
        this.reqBy = reqBy;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public String getChangeAnalyst() {
        return changeAnalyst;
    }

    public void setChangeAnalyst(String changeAnalyst) {
        this.changeAnalyst = changeAnalyst;
    }

    public String getAEmail() {
        return aEmail;
    }

    public void setAEmail(String aEmail) {
        this.aEmail = aEmail;
    }

    public String getaGrp() {
        return aGrp;
    }

    public void setaGrp(String aGrp) {
        this.aGrp = aGrp;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getdEmail() {
        return dEmail;
    }

    public void setdEmail(String dEmail) {
        this.dEmail = dEmail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getMEmail() {
        return mEmail;
    }

    public void setMEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getVpName() {
        return vpName;
    }

    public void setVpName(String vpName) {
        this.vpName = vpName;
    }
    public String getVpEmail() {
        return vpEmail;
    }

    public void setVpEmail(String vpEmail) {
        this.vpEmail = vpEmail;
    }
}
