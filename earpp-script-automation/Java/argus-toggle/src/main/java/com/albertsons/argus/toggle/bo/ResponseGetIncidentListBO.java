package com.albertsons.argus.toggle.bo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "incidentList",
})

@Getter
@Setter
@NoArgsConstructor
public class ResponseGetIncidentListBO {
    @JsonProperty("incidentList")
    private List<IncidentBO> incidentList;
}
