package com.albertsons.argus.wic.ws.bo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author damis01
 * @since 5/1/22
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"taskList",
"taskNumber",
"rit",
"storeNumberList",
"requestType",
"emaillAdress",
"attachment"
})
@Setter
@Getter
public class TaskBO {
    @JsonProperty("taskList")
    private String taskList;
    @JsonProperty("taskNumber")
    private String taskNumber;
    @JsonProperty("rit")
    private String rit;
    @JsonProperty("storeNumberList")
    private String storeNumberList;
    @JsonProperty("requestType")
    private String requestType;
    @JsonProperty("emaillAdress")
    private String emaillAdress;
    @JsonProperty("attachment")
    private String attachment;
}