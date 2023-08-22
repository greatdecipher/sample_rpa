package com.albertsons.argus.ip.service;

public interface IPScheduledTaskService {
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";
    
    public void runToggleJob();
}
