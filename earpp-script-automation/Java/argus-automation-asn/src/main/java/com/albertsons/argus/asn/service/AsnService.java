package com.albertsons.argus.asn.service;

public interface AsnService {
    public Boolean getEmails(String datetime);
    public String createFile(String text, String datetime);
    public String mergeEmailMessage();
    public void saveFileToServer(String filename);
    public String getServerDate();
    public void sendMail(String filename);
    public void sudoTransferFile(String filename);
    public void deleteFile(String filename);
}
