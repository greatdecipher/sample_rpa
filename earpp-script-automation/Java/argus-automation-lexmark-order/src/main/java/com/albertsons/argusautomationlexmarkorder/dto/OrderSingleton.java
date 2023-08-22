package com.albertsons.argusautomationlexmarkorder.dto;

import java.util.List;

public class OrderSingleton {
    private String serialNumber;
    private static OrderSingleton instance;
    private List<UnitOrder> unitOrderList;
    private List<ItemType> listItemType;
    private List<OrderedUnit> listOrderedUnit;
    private String shippingAddress;
    private Store store;
    private String message;
    private String orderNumber;
    private boolean print = true;

    private OrderSingleton(){}

    public static OrderSingleton getInstance(){
        if(instance == null){
            return new OrderSingleton(); 
        }
            return instance;  
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<UnitOrder> getUnitOrderList() {
        return unitOrderList;
    }

    public void setUnitOrderList(List<UnitOrder> unitOrderList) {
        this.unitOrderList = unitOrderList;
    }

    public List<ItemType> getListItemType() {
        return listItemType;
    }

    public void setListItemType(List<ItemType> listItemType) {
        this.listItemType = listItemType;
    }

    public List<OrderedUnit> getListOrderedUnit() {
        return listOrderedUnit;
    }

    public void setListOrderedUnit(List<OrderedUnit> listOrderedUnit) {
        this.listOrderedUnit = listOrderedUnit;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    
}
