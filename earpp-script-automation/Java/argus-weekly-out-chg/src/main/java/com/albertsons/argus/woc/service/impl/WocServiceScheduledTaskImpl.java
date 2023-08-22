package com.albertsons.argus.woc.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.woc.service.WocService;
import com.albertsons.argus.woc.service.WocServiceScheduledTask;

@Service
public class WocServiceScheduledTaskImpl implements WocServiceScheduledTask{
    private static final Logger LOG = LogManager.getLogger(WocServiceScheduledTaskImpl.class);
    @Autowired
    WocService wocservice;
    
    @Override
	@Scheduled(cron = "${com.albertsons.woc.scheduled.task.cron.value}")
	public void runWocScheduledJob(){
        LOG.log(Level.INFO, () -> "start Weekly Out Change script execution . . .");
        wocservice.login();
        wocservice.extractRaw();
    }

}
