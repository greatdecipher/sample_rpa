package com.albertsons.argus.domain.bo.generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "u_test_field", "number", "u_caller_name", "short_description", "description", "opened_by",
        "state", "assignment_group", "assigned_to", "comments", })
public class IncidentBO {
    @JsonProperty("u_test_field")
    private String uTestField;
    @JsonProperty("number")
    private String number;
    @JsonProperty("u_caller_name")
    private String uCallerName;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("description")
    private String description;
    @JsonProperty("opened_by")
    private OpenedBy openedBy;
    @JsonProperty("state")
    private String state;
    @JsonProperty("assignment_group")
    private AssignmentGroup assignmentGroup;
    @JsonProperty("assigned_to")
    private AssignedTo assignedTo;
    @JsonProperty("comments")
    private String comments;

    @JsonProperty("comments")
    public String getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(String comments) {
        this.comments = comments;
    }

    @JsonProperty("assigned_to")
    public AssignedTo getAssignedTo() {
        return assignedTo;
    }

    @JsonProperty("assigned_to")
    public void setAssignedTo(AssignedTo assignedTo) {
        this.assignedTo = assignedTo;
    }

    @JsonProperty("assignment_group")
    public AssignmentGroup getAssignmentGroup() {
        return assignmentGroup;
    }

    @JsonProperty("assignment_group")
    public void setAssignmentGroup(AssignmentGroup assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("opened_by")
    public OpenedBy getOpenedBy() {
        return openedBy;
    }

    @JsonProperty("opened_by")
    public void setOpenedBy(OpenedBy openedBy) {
        this.openedBy = openedBy;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("u_caller_name")
    public String getuCallerName() {
        return uCallerName;
    }

    @JsonProperty("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @JsonProperty("short_description")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @JsonProperty("u_caller_name")
    public void setuCallerName(String uCallerName) {
        this.uCallerName = uCallerName;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("u_test_field")
    public String getuTestField() {
        return uTestField;
    }

    @JsonProperty("u_test_field")
    public void setuTestField(String uTestField) {
        this.uTestField = uTestField;
    }

}
