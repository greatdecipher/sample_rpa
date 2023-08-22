package com.albertsons.argus.toggle.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "incident",
})

@Getter
@Setter
@NoArgsConstructor
public class ResponseGetIncidentDetailBO {
    @JsonProperty("incident")
    private IncidentDetailBO incident;
}
