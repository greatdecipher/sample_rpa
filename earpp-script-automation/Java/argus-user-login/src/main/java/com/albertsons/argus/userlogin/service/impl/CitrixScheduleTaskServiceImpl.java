package com.albertsons.argus.userlogin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.albertsons.argus.userlogin.model.User;
import com.albertsons.argus.userlogin.service.CitrixScheduleTaskService;
import com.albertsons.argus.userlogin.service.UserLoginService;

@Service
public class CitrixScheduleTaskServiceImpl implements CitrixScheduleTaskService{
    private static final Logger LOG = LogManager.getLogger(CitrixScheduleTaskServiceImpl.class);
    @Autowired
	UserLoginService userLoginService;

    @Override
    @Scheduled(cron = "${com.argus.citrix.scheduled.task.cron.value}")
    public void runCitrixLoginJob() {
        LOG.log(Level.INFO, () -> "runCitrixLoginJob started. . . ");
		List<User> userProcessed = new ArrayList<>();
		LOG.log(Level.INFO, () -> "loginMultipleUserRestart started. . . ");
		try {
            userLoginService.loginMultipleUserRestart(userLoginService.getLoginInfo());
            LOG.log(Level.INFO, () -> "loginMultipleUserRestart ended. . . ");
            LOG.log(Level.INFO, () -> "loginMultipleUser started. . . ");
            userProcessed = userLoginService.loginMultipleUser(userLoginService.getLoginInfo());
            userProcessed = userLoginService.loginMultipleUser(userLoginService.getLoginInfo());
            LOG.log(Level.INFO, () -> "loginMultipleUser ended. . . ");
            String msg = userLoginService.createMailTemplate(userProcessed);
            userLoginService.sendMail(msg);	
            LOG.log(Level.INFO, () -> "runCitrixLoginJob ended. . . ");
        } catch (Exception e) {
            userLoginService.sendMail("Bot Failed to restart. . .");	
        }
        
    }
    
}
