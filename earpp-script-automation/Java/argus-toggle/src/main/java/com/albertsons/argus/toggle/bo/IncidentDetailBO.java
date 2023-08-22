package com.albertsons.argus.toggle.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"incidentNumber",
"shortDescription",
"state",
"cmdbCI",
"location",
"siteZone",
"longDescription",
"assignmentGroup",
"assignmentTo"
})
@Setter
@Getter

public class IncidentDetailBO {
    @JsonProperty("incidentNumber")
    private String incidentNumber;

    @JsonProperty("shortDescription")
    private String shortDescription;
    
    @JsonProperty("state")
    private String state;

    @JsonProperty("cmdbCI")
    private String cmdbCI;
    
    @JsonProperty("location")
    private String location;
    
    @JsonProperty("siteZone")
    private String siteZone;
    
    @JsonProperty("longDescription")
    private String longDescription;
    
    @JsonProperty("assignmentGroup")
    private String assignmentGroup;
    
    @JsonProperty("assignmentTo")
    private String assignmentTo;
}
