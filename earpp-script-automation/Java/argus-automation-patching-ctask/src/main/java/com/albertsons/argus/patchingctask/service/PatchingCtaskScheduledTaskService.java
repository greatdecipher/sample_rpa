package com.albertsons.argus.patchingctask.service;

public interface PatchingCtaskScheduledTaskService {
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";

    public void runPatchingCtaskJob();

}
