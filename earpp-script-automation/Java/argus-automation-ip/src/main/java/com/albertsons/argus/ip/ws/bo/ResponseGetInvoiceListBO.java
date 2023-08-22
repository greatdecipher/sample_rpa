package com.albertsons.argus.ip.ws.bo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
})

@Setter
@Getter
@NoArgsConstructor

public class ResponseGetInvoiceListBO {
    @JsonProperty("result")
    private List<InvoiceDetailsBO> invoiceDetailsBo;
}
