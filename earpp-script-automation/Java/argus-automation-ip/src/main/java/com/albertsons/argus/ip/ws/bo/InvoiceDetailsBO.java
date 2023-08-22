package com.albertsons.argus.ip.ws.bo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"result",
"FastID",
"PONumber",
"Status"
})
@Setter
@Getter

public class InvoiceDetailsBO {
    @JsonProperty("result")
    private String result;
    @JsonProperty("FastID")
    private String FastID;
    @JsonProperty("PONumber")
    private String PONumber;
    @JsonProperty("Status")
    private String Status;
}
