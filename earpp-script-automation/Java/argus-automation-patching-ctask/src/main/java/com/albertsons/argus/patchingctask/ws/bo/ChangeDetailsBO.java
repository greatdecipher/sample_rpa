package com.albertsons.argus.patchingctask.ws.bo;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"changeList",
"state",
"shortDescription",
"assignmentGroup",
"assignmentTo",
"attachment",
"changeNumber",
"phase",
"configuration_item",
"phase_state"
})
@Setter
@Getter

public class ChangeDetailsBO {
    @JsonProperty("changeList")
    private String changeList;
    @JsonProperty("state")
    private String state;
    @JsonProperty("shortDescription")
    private String shortDescription;
    @JsonProperty("assignmentGroup")
    private String assignmentGroup;
    @JsonProperty("assignmentTo")
    private String assignmentTo;
    @JsonProperty("attachment")
    private String attachment;
    @JsonProperty("changeNumber")
    private String changeNumber;
    @JsonProperty("phase")
    private String phase;
    @JsonProperty("configuration_item")
    private String configuration_item;
    @JsonProperty("phase_state")
    private String phase_state;
}
