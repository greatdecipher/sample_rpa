package com.albertsons.argusautomationlexmarkorder.dto;

import java.util.List;

//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"ipAddress",
"storeNumber",
"shippingAddress",
"items",
"orderNumber"
})
@Data
public class JsonResult {
    @JsonProperty("ipAddress")
    private String ipAddress;
    @JsonProperty("storeNumber")
    private String storeNumber;
    @JsonProperty("shippingAddress")
    private String shippingAddress;
    @JsonProperty("items")
    private List<JsonItem>items;
    @JsonProperty("orderNumber")
    private String orderNumber;
}
