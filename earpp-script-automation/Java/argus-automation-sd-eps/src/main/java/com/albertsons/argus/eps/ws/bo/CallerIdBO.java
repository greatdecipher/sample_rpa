package com.albertsons.argus.eps.ws.bo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "display_value",
    "link"
})

@Getter
@Setter
@NoArgsConstructor

public class CallerIdBO {
    @JsonProperty("display_value")
    private String displayValue;
    @JsonProperty("link")
    private String link;
}
