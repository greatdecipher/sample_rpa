package com.albertsons.argus.eps.ws.bo;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"result",
"number",
"sys_updated_by",
"close_code",
"assignment_group",
"state",
"work_notes",
"close_notes",
"assigned_to"
})
@Setter
@Getter

public class UpdateIncidentBO {
    @JsonProperty("result")
    private String result;
    @JsonProperty("number")
    private String number;
    @JsonProperty("sys_updated_by")
    private String sysUpdatedBy;
    @JsonProperty("close_code")
    private String closeCode;
    @JsonProperty("assignment_group")
    private AssignmentGroupBO assignmentGroup;
    @JsonProperty("state")
    private String state;
    @JsonProperty("work_notes")
    private String workNotes;
    @JsonProperty("close_notes")
    private String closeNotes;
    @JsonProperty("assigned_to")
    private String assignedTo;
}
