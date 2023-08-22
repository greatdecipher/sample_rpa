package com.albertsons.argus.rundb.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DBRevocationService {
    static final Integer NORMAL_PRIORITY = 3;

    public CompletableFuture<Boolean> runShCmd(String user, String password, String host, String command, String options) throws Exception;

    public void sendDBRevocationEmailSuccess(String startTime, String endTime);

    public void sendDBRevocationEmailFailed(String startTime, String endTime);

    public void sendDBRevocationEmailStart(String startTime);

    public void updateUserList();

    public void deleteFile(boolean excelFile, boolean txtFile);

    public void moveLogFile();

    public boolean getUsersFromFile();

    public boolean openOutlook();

    public boolean validateRun();

    public void setUsersList(String user);

    public List<String> getUsersList();

    public void setPartialUserCount(int partialUserCount);

    public int getPartialUserCount();

    public void setTimestamp(String timestamp);

    public String getTimestamp();

}