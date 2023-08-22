package com.albertsons.argusautomationlexmarkorder.dto;

public class UnitOrder {
    private String type;
    private int quantity;
    private String success;
    private OrderedUnit orderedUnit;

    public UnitOrder(){}

    public UnitOrder(String type, int quantity, String success, OrderedUnit orderedUnit) {
		super();
		this.type = type;
		this.quantity = quantity;
		this.success = success;
		this.orderedUnit = orderedUnit;
	}
	public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public OrderedUnit getOrderedUnit() {
		return orderedUnit;
	}

	public void setOrderedUnit(OrderedUnit orderedUnit) {
		this.orderedUnit = orderedUnit;
	}
    
}
