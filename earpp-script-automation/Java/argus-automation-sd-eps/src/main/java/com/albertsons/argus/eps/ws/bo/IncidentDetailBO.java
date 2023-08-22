package com.albertsons.argus.eps.ws.bo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"result",
"number",
"sys_id",
"sys_updated_by",
"caller_id",
"description",
"state"
})
@Setter
@Getter

public class IncidentDetailBO {
    @JsonProperty("result")
    private String result;
    @JsonProperty("number")
    private String number;
    @JsonProperty("sys_id")
    private String sysId;
    @JsonProperty("sys_updated_by")
    private String sysUpdatedBy;
    @JsonProperty("caller_id")
    private CallerIdBO callerId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("state")
    private String state;
}
