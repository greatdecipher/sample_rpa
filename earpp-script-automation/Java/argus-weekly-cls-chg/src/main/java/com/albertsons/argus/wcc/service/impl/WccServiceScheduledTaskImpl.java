package com.albertsons.argus.wcc.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.wcc.service.WccService;
import com.albertsons.argus.wcc.service.WccServiceScheduledTask;

@Service
public class WccServiceScheduledTaskImpl implements WccServiceScheduledTask{
    private static final Logger LOG = LogManager.getLogger(WccServiceScheduledTaskImpl.class);
    @Autowired
    private WccService wccservice;
    
    @Override
	@Scheduled(cron = "${com.albertsons.wcc.scheduled.task.cron.value}")
	public void runWccScheduledJob(){
        LOG.log(Level.INFO, () -> "start Weekly Closure Change script execution . . .");
        wccservice.readEmail();
        wccservice.extractRaw();
    }

}
