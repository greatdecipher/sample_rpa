package com.albertsons.argus.q2c.service;

public interface Q2cScheduledTaskService {

    public static final Integer HIGH_PRIORITY = 1;
    public static final Integer NORMAL_PRIORITY = 3;
    public static final String DELAY_S = "delay.small";
    public static final String DELAY_M = "delay.medium";
    public static final String DELAY_L = "delay.large";
    public static final String OUTBOUND_FILE_PATH = "com.argus.outbound.file.path";

    public void runARQueryOutbound();
    public void runCrosswalkQueryOutbound();
    public void runBalanceQueryOutbound1();
    public void runBalanceQueryOutbound2();
    public void runBalanceQueryOutbound3();
    public void runStatementQueryOutbound1();
    public void runStatementQueryOutbound2();
    public void runStatementQueryOutbound3();
    public void runPRGQueryOutbound();
    public void runStaleDatedQueryOutbound1();
    public void runStaleDatedQueryOutbound2();
    public void runUwareValidationOutbound1();
    public void runPospayVoidsOutbound1();
    public void runPospayVoidsOutbound2();
    public void runPospayVoidsOutbound3();
    public void runPospayVoidsOutbound4();
}
