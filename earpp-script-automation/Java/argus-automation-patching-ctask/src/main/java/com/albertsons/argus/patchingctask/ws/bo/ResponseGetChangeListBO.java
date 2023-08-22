package com.albertsons.argus.patchingctask.ws.bo;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "changeList"
})

@Setter
@Getter
@NoArgsConstructor

public class ResponseGetChangeListBO {
    @JsonProperty("changeList")
    private List<ChangeDetailsBO> changeList;
}
