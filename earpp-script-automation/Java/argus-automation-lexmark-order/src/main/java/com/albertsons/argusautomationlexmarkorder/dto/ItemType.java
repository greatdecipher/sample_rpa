package com.albertsons.argusautomationlexmarkorder.dto;

public class ItemType {
    private String type;
    private String partNumber;
    private String description;
    private String price;
    private int buttonOrderNumber;

    public ItemType(){}

    public ItemType(String type, String partNumber, String description, String price, int buttonOrderNumber) {
        this.type = type;
        this.partNumber = partNumber;
        this.description = description;
        this.price = price;
        this.buttonOrderNumber = buttonOrderNumber;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public int getButtonOrderNumber() {
        return buttonOrderNumber;
    }

    public void setButtonOrderNumber(int buttonOrderNumber) {
        this.buttonOrderNumber = buttonOrderNumber;
    }
    
}
