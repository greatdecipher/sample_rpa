package com.albertsons.argus.webservice.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author jborj20
 * @since 11/10/22
 * @version 1.0
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
    "id"
})
@Getter
@Setter
@NoArgsConstructor
public class ResponseIncrementTransactionBO {
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("id")
    private String id;
}