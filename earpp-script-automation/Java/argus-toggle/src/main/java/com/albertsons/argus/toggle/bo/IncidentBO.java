package com.albertsons.argus.toggle.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"incidentNumber"
})
@Setter
@Getter

public class IncidentBO {
    @JsonProperty("incidentNumber")
    private String incidentNumber;
}
