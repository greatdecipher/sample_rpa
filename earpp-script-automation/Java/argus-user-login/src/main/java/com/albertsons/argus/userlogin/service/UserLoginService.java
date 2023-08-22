package com.albertsons.argus.userlogin.service;

import java.util.List;

import com.albertsons.argus.userlogin.model.User;

public interface UserLoginService {
    public List<String> getLoginInfo();
    public void loginMultipleUserRestart(List<String> userInfo) throws Exception;
    public String createMailTemplate(List<User> userProcessed);
    public String sendMail(String msg);
    public List<User> loginMultipleUser(List<String> userInfo) throws Exception;
}
