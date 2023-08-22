package com.albertsons.argus.sedgwick.service;

public interface SedgwickScheduledTaskService {
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";
    public static final String ATTACH_PATH = "sedgwick.outlook.attachment.save.path";
    public static final String ATTACH_NAME = "sedgwick.outlook.attachment.file.name";
    
    public void runSedgwickJob();
}
