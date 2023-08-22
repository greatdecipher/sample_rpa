package com.albertsons.argusautomationlexmarkorder.dto;

public class Store {
    private String ipaddress;
    private String storenumber;

    public Store(){}
    
    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getStorenumber() {
        return storenumber;
    }

    public void setStorenumber(String storenumber) {
        this.storenumber = storenumber;
    }

    public Store(String ipaddress, String storenumber) {
        this.ipaddress = ipaddress;
        this.storenumber = storenumber;
    }
    
}
