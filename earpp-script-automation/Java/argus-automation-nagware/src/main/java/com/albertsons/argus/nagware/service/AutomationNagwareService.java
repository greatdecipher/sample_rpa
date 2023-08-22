package com.albertsons.argus.nagware.service;

import java.util.List;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.nagware.exception.NagwareException;
import com.albertsons.argus.nagware.model.User;

public interface AutomationNagwareService {
    public List<User> getProjectCenterDetails() throws NagwareException;
    public List<User> getResourceCenterDetails() throws NagwareException;
    public List<User> getResourceCenterDetailsXLSX() throws NagwareException;
    public List<User> getPortfolioDetails() throws NagwareException;
    public Boolean checkDateValidity(String strDate);
    public List<User> getUserDetails(List<User> projectCenter, List<User> resourceCenter) throws NagwareException;
    public Integer sendMailToList(List<User> users) throws NagwareException;
    public void sendMailHandler(String msg) throws NagwareException;
    public void sendMailError(String msg);
    public String createMailTemplate() throws NagwareException;
    public void archiveFiles();
    public String getExtractDate(String file);
    public void processNotification(String state, String transactionNumber) throws NagwareException;
}
