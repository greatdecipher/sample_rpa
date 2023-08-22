package com.albertsons.argusautomationlexmarkorder.dto;

//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"type",
"quantity",
"success"
})
@Data
public class JsonItem {
    private String type;
    private String quantity;
    private String success;
}
