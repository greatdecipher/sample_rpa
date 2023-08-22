package com.albertsons.argus.sla.dto;

import java.util.Date;

public class SLAReport {
    
    private String number;
    private String priority;
    private String group;
    private String state;
    private String description;
    private String manager;
    private String managerEmail;
    private String director;
    private String directorEmail;  
    private Date opened; 
    
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getManagerEmail() {
        return managerEmail;
    }
    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getDirectorEmail() {
        return directorEmail;
    }
    public void setDirectorEmail(String directorEmail) {
        this.directorEmail = directorEmail;
    }
    public Date getOpened() {
        return opened;
    }
    public void setOpened(Date opened) {
        this.opened = opened;
    } 
}

