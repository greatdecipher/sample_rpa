package com.albertsons.argusautomationlexmarkorder.services;


import com.albertsons.argusautomationlexmarkorder.dto.OrderSingleton;

public interface AutomationProcessService {
    
    public void initiatePlaceOrders(OrderSingleton orderSingleton);
    
}
