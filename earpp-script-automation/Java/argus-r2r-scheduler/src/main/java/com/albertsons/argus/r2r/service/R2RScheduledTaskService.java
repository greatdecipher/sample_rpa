package com.albertsons.argus.r2r.service;

public interface R2RScheduledTaskService {
    public static final Integer NORMAL_PRIORITY = 3;
    
    public void runR2RSchedulerEODJob();
    public void runR2RSchedulerJob();
}
