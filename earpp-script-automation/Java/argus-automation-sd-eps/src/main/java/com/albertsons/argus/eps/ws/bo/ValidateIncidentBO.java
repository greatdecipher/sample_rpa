package com.albertsons.argus.eps.ws.bo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"result",
"number",
"sys_id",
"sys_updated_by",
"close_code",
"assignment_group",
"caller_id",
"description",
"state",
"work_notes",
"close_notes"
})
@Setter
@Getter
public class ValidateIncidentBO {
    @JsonProperty("result")
    private String result;
    @JsonProperty("number")
    private String number;
    @JsonProperty("sys_id")
    private String sysId;
    @JsonProperty("sys_updated_by")
    private String sysUpdatedBy;
    @JsonProperty("close_code")
    private String closeCode;
    @JsonProperty("assignment_group")
    private AssignmentGroupBO assignmentGroup;
    @JsonProperty("caller_id")
    private CallerIdBO callerId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("state")
    private String state;
    @JsonProperty("work_notes")
    private String workNotes;
    @JsonProperty("close_notes")
    private String closeNotes;
}
