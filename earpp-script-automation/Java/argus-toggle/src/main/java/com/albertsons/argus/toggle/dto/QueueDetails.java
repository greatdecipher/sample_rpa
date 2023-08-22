package com.albertsons.argus.toggle.dto;

public class QueueDetails {

    private String ticketDesc;
    private int currentQDepth;
    private String incTicket;

    public QueueDetails(){

    }

    public QueueDetails(String incTicket, int currentQDepth, String ticketDesc){
        this.incTicket = incTicket;
        this.currentQDepth = currentQDepth;
        this.ticketDesc = ticketDesc;
    }

    public void setIncTicket(String incTicket){
        this.incTicket = incTicket;
    }

    public String getIncTicket(){
        return incTicket;
    }

    public void setCurrentQDepth(int currentQDepth){
        this.currentQDepth = currentQDepth;
    }

    public int getCurrentQDepth(){
        return currentQDepth;
    }

    public void setTicketDesc(String ticketDesc){
        this.ticketDesc = ticketDesc;
    }

    public String getTicketDesc(){
        return ticketDesc;
    }
}
