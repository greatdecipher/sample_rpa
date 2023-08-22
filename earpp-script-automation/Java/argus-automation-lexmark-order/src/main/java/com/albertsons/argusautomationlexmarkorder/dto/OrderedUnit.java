package com.albertsons.argusautomationlexmarkorder.dto;

public class OrderedUnit {
    private String type;
    private String partNumber;
    private int quantity;
    private int orderNumber;
    
    public OrderedUnit(){}

    public OrderedUnit(String type, String partNumber, int quantity, int orderNumber) {
        this.type = type;
        this.partNumber = partNumber;
        this.quantity = quantity;
        this.orderNumber = orderNumber;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPartNumber() {
        return partNumber;
    }
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    
}
