package com.albertsons.argus.nagware.model;

public class User {
    String owner;
    String projectName;
    String emailAddress;
    String portfolio;
    String poc;
    String pocEmail;
    boolean isEmailFound;

    
    public boolean isEmailFound() {
        return isEmailFound;
    }
    public void setEmailFound(boolean isEmailFound) {
        this.isEmailFound = isEmailFound;
    }
    public String getPortfolio() {
        return portfolio;
    }
    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }
    public String getPoc() {
        return poc;
    }
    public void setPoc(String poc) {
        this.poc = poc;
    }
    public String getPocEmail() {
        return pocEmail;
    }
    public void setPocEmail(String pocEmail) {
        this.pocEmail = pocEmail;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    @Override
    public String toString() {
        return "User [emailAddress=" + emailAddress + ", owner=" + owner + ", poc=" + poc + ", pocEmail=" + pocEmail
                + ", portfolio=" + portfolio + ", projectName=" + projectName + "]";
    }
}
