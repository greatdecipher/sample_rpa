package com.albertsons.argusautomationlexmarkorder.utilities;

import java.util.ArrayList;
import java.util.List;

import com.albertsons.argusautomationlexmarkorder.dto.JsonItem;
import com.albertsons.argusautomationlexmarkorder.dto.JsonResult;
import com.albertsons.argusautomationlexmarkorder.dto.OrderSingleton;
import com.albertsons.argusautomationlexmarkorder.dto.UnitOrder;
import com.albertsons.argusautomationlexmarkorder.services.impl.JsonResultAutomationServiceImpl;

public class JSONParserUtil {

    JsonResultAutomationServiceImpl jsonResultAutomationServiceImpl = new JsonResultAutomationServiceImpl();

    public String jsonItemToJson(OrderSingleton orderSingleton){

        JsonResult jsonResult = new JsonResult();
        jsonResult.setIpAddress(orderSingleton.getStore().getIpaddress());
        jsonResult.setStoreNumber(orderSingleton.getStore().getStorenumber());
        jsonResult.setOrderNumber(orderSingleton.getOrderNumber());

        List<JsonItem> listJsonItems = new ArrayList<JsonItem>();
        for(UnitOrder unitOrder: orderSingleton.getUnitOrderList()){
            JsonItem jsonItem = new JsonItem();
            if(unitOrder.getOrderedUnit()!=null){
                jsonItem.setType(unitOrder.getOrderedUnit().getType());
                jsonItem.setQuantity(String.valueOf(unitOrder.getQuantity()));
                jsonItem.setSuccess("true");
            }else{
                jsonItem.setType(unitOrder.getOrderedUnit().getType());
                jsonItem.setQuantity(String.valueOf(unitOrder.getQuantity()));
                jsonItem.setSuccess("false");
            }
            listJsonItems.add(jsonItem);
        }
        jsonResult.setItems(listJsonItems);
        
        return jsonResultAutomationServiceImpl.toJsonString(jsonResult);

    }
}
