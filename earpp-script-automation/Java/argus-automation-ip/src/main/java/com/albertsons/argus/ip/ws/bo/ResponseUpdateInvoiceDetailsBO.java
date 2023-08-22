package com.albertsons.argus.ip.ws.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
})

@Getter
@Setter
@NoArgsConstructor

public class ResponseUpdateInvoiceDetailsBO {
    private String result;
}
