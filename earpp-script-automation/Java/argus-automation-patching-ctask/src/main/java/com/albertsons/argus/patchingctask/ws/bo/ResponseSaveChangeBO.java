package com.albertsons.argus.patchingctask.ws.bo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
    "id"
})

@Getter
@Setter
@NoArgsConstructor

public class ResponseSaveChangeBO {
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("id")
    private String id;
}
