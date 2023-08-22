package com.albertsons.argus.woc.service;

import java.util.List;

import com.albertsons.argus.woc.dto.EmailDetails;

public interface WocService {
    static final Integer NORMAL_PRIORITY = 3;
    
    public void login();
    public void extractRaw();
    public void sendEmail(List<EmailDetails> list, String assigneeName, String assigneeEmail, String managerEmail);

}
