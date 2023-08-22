package com.albertsons.argus.wcc.service;

import java.util.List;

import com.albertsons.argus.wcc.dto.EmailDetails;

public interface WccService {
    static final Integer NORMAL_PRIORITY = 3;
    
    public void readEmail();
    public void extractRaw();
    public void sendEmail(List<EmailDetails> rows, List<String> managerEmails, List<String> assigneeNames, List<String> assigneeEmails, String directorName);

}
