
package com.albertsons.argus.domain.bo.generated;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "parent",
    "u_reassignment_reason",
    "caused_by",
    "watch_list",
    "u_bmcps_app_tier",
    "sys_updated_on",
    "u_verizon_customer_location",
    "skills",
    "u_ponca_city",
    "u_cancellation_reason",
    "u_sundries_boise",
    "lessons_learned",
    "u_kb_relate_xml",
    "u_mc_parameter",
    "u_parameter",
    "state",
    "u_portland",
    "u_southwest_las_vegas",
    "sys_created_by",
    "knowledge",
    "impact",
    "active",
    "u_vendor",
    "group_list",
    "u_pharmacy",
    "major_incident_state",
    "u_denver",
    "correlation_display",
    "u_caller_name",
    "vz_release_for_test",
    "follow_up",
    "parent_incident",
    "reopened_by",
    "u_reporting",
    "u_target_date",
    "u_recycle_required",
    "u_site_zone",
    "u_impacted_business_department",
    "escalation",
    "u_socal",
    "correlation_id",
    "timeline",
    "u_verizon_product_type",
    "u_conversion_activity",
    "u_related_hr_case",
    "u_merger_activity",
    "u_change",
    "u_vendor_hardware_asset",
    "user_input",
    "sys_created_on",
    "route_reason",
    "u_support_level",
    "calendar_stc",
    "closed_at",
    "u_jewel",
    "u_soft",
    "u_consolidated_event_data",
    "u_first_notified_at",
    "u_last_outage_at",
    "u_mc_long_msg",
    "u_alert_source",
    "u_vendor_task_status",
    "business_impact",
    "u_source_ref",
    "u_test_field",
    "rfc",
    "time_worked",
    "u_event_handle",
    "u_verizon_severity",
    "work_end",
    "u_related_chat_queue",
    "subcategory",
    "u_current_date",
    "work_notes",
    "close_code",
    "assignment_group",
    "business_stc",
    "description",
    "u_pause_date_time",
    "sys_id",
    "u_additional_tracks_affected",
    "u_first_outage_at",
    "u_mc_ueid",
    "u_bmcps_message",
    "urgency",
    "company",
    "severity",
    "overview",
    "u_reported_ci",
    "u_source_uid",
    "approval",
    "u_mc_object_uri",
    "reopen_count",
    "u_dev_qa_environment",
    "u_houston",
    "sys_tags",
    "u_knowledge_article",
    "location",
    "promoted_by",
    "x_avne2_bmc_to_sno_source_uid",
    "u_supply_chain",
    "u_hpi",
    "upon_reject",
    "u_best_callback",
    "u_mc_object_class",
    "approval_history",
    "number",
    "proposed_by",
    "u_instance",
    "u_adapter_host",
    "order",
    "u_hr",
    "u_missing_category",
    "cmdb_ci",
    "u_track_name",
    "u_vendor_type_model",
    "priority",
    "u_all_divisions",
    "u_reason_for_sla_exception",
    "business_duration",
    "approval_set",
    "u_vendor_sent",
    "universal_request",
    "short_description",
    "u_circuit_id",
    "work_start",
    "u_intermountain",
    "u_alert_ref",
    "additional_assignee_list",
    "u_vendor_status",
    "notify",
    "sys_class_name",
    "closed_by",
    "u_related_call",
    "u_vendor_serial_number",
    "reassignment_count",
    "u_business_third_party",
    "assigned_to",
    "u_assigned_to",
    "u_breach_time",
    "sla_due",
    "u_bmcps_dedup_id",
    "upon_approval",
    "u_response_sla",
    "u_restoration_manager",
    "u_data_migration",
    "u_first_incident_at",
    "promoted_on",
    "child_incidents",
    "task_effective_number",
    "resolved_by",
    "sys_updated_by",
    "opened_by",
    "sys_domain",
    "proposed_on",
    "u_ci_details",
    "u_mm",
    "u_software",
    "business_service",
    "u_retail",
    "u_symptom",
    "expected_start",
    "opened_at",
    "u_impacted_business_area",
    "u_accounting",
    "vz_release_for_test_datetime",
    "u_awaiting_reason",
    "u_caused_by_story",
    "caller_id",
    "reopened_time",
    "resolved_at",
    "u_problem",
    "u_symptoms",
    "u_vendor_warranty_expiration",
    "x_avne2_bmc_to_sno_source_ref",
    "cause",
    "u_vendor_ticket",
    "calendar_duration",
    "u_acme",
    "u_turnover_category",
    "close_notes",
    "u_mc_object",
    "contact_type",
    "incident_state",
    "u_southern",
    "problem_id",
    "u_seattle",
    "u_problem_created_at",
    "activity_due",
    "u_problem_related_by",
    "comments",
    "u_shaws",
    "u_contact_phone",
    "u_mc_object_uri_link",
    "due_date",
    "sys_mod_count",
    "u_ncpdp_rejects",
    "u_verizon_customer_name",
    "u_other_specify",
    "u_mc_modhist",
    "x_avne2_bmc_to_sno_source_modified",
    "category",
    "u_it_shared_services",
    "u_turnover_review"
})
@Generated("jsonschema2pojo")
public class ResultTableInc {

    @JsonProperty("parent")
    private String parent;
    @JsonProperty("u_reassignment_reason")
    private String uReassignmentReason;
    @JsonProperty("caused_by")
    private String causedBy;
    @JsonProperty("watch_list")
    private String watchList;
    @JsonProperty("u_bmcps_app_tier")
    private String uBmcpsAppTier;
    @JsonProperty("sys_updated_on")
    private String sysUpdatedOn;
    @JsonProperty("u_verizon_customer_location")
    private String uVerizonCustomerLocation;
    @JsonProperty("skills")
    private String skills;
    @JsonProperty("u_ponca_city")
    private String uPoncaCity;
    @JsonProperty("u_cancellation_reason")
    private String uCancellationReason;
    @JsonProperty("u_sundries_boise")
    private String uSundriesBoise;
    @JsonProperty("lessons_learned")
    private String lessonsLearned;
    @JsonProperty("u_kb_relate_xml")
    private String uKbRelateXml;
    @JsonProperty("u_mc_parameter")
    private String uMcParameter;
    @JsonProperty("u_parameter")
    private String uParameter;
    @JsonProperty("state")
    private String state;
    @JsonProperty("u_portland")
    private String uPortland;
    @JsonProperty("u_southwest_las_vegas")
    private String uSouthwestLasVegas;
    @JsonProperty("sys_created_by")
    private String sysCreatedBy;
    @JsonProperty("knowledge")
    private String knowledge;
    @JsonProperty("impact")
    private String impact;
    @JsonProperty("active")
    private String active;
    @JsonProperty("u_vendor")
    private String uVendor;
    @JsonProperty("group_list")
    private String groupList;
    @JsonProperty("u_pharmacy")
    private String uPharmacy;
    @JsonProperty("major_incident_state")
    private String majorIncidentState;
    @JsonProperty("u_denver")
    private String uDenver;
    @JsonProperty("correlation_display")
    private String correlationDisplay;
    @JsonProperty("u_caller_name")
    private String uCallerName;
    @JsonProperty("vz_release_for_test")
    private String vzReleaseForTest;
    @JsonProperty("follow_up")
    private String followUp;
    @JsonProperty("parent_incident")
    private String parentIncident;
    @JsonProperty("reopened_by")
    private String reopenedBy;
    @JsonProperty("u_reporting")
    private String uReporting;
    @JsonProperty("u_target_date")
    private String uTargetDate;
    @JsonProperty("u_recycle_required")
    private String uRecycleRequired;
    @JsonProperty("u_site_zone")
    private String uSiteZone;
    @JsonProperty("u_impacted_business_department")
    private String uImpactedBusinessDepartment;
    @JsonProperty("escalation")
    private String escalation;
    @JsonProperty("u_socal")
    private String uSocal;
    @JsonProperty("correlation_id")
    private String correlationId;
    @JsonProperty("timeline")
    private String timeline;
    @JsonProperty("u_verizon_product_type")
    private String uVerizonProductType;
    @JsonProperty("u_conversion_activity")
    private String uConversionActivity;
    @JsonProperty("u_related_hr_case")
    private String uRelatedHrCase;
    @JsonProperty("u_merger_activity")
    private String uMergerActivity;
    @JsonProperty("u_change")
    private String uChange;
    @JsonProperty("u_vendor_hardware_asset")
    private String uVendorHardwareAsset;
    @JsonProperty("user_input")
    private String userInput;
    @JsonProperty("sys_created_on")
    private String sysCreatedOn;
    @JsonProperty("route_reason")
    private String routeReason;
    @JsonProperty("u_support_level")
    private String uSupportLevel;
    @JsonProperty("calendar_stc")
    private String calendarStc;
    @JsonProperty("closed_at")
    private String closedAt;
    @JsonProperty("u_jewel")
    private String uJewel;
    @JsonProperty("u_soft")
    private String uSoft;
    @JsonProperty("u_consolidated_event_data")
    private String uConsolidatedEventData;
    @JsonProperty("u_first_notified_at")
    private String uFirstNotifiedAt;
    @JsonProperty("u_last_outage_at")
    private String uLastOutageAt;
    @JsonProperty("u_mc_long_msg")
    private String uMcLongMsg;
    @JsonProperty("u_alert_source")
    private String uAlertSource;
    @JsonProperty("u_vendor_task_status")
    private String uVendorTaskStatus;
    @JsonProperty("business_impact")
    private String businessImpact;
    @JsonProperty("u_source_ref")
    private String uSourceRef;
    @JsonProperty("u_test_field")
    private String uTestField;
    @JsonProperty("rfc")
    private String rfc;
    @JsonProperty("time_worked")
    private String timeWorked;
    @JsonProperty("u_event_handle")
    private String uEventHandle;
    @JsonProperty("u_verizon_severity")
    private String uVerizonSeverity;
    @JsonProperty("work_end")
    private String workEnd;
    @JsonProperty("u_related_chat_queue")
    private String uRelatedChatQueue;
    @JsonProperty("subcategory")
    private String subcategory;
    @JsonProperty("u_current_date")
    private String uCurrentDate;
    @JsonProperty("work_notes")
    private String workNotes;
    @JsonProperty("close_code")
    private String closeCode;
    @JsonProperty("assignment_group")
    private AssignmentGroup assignmentGroup;
    @JsonProperty("business_stc")
    private String businessStc;
    @JsonProperty("description")
    private String description;
    @JsonProperty("u_pause_date_time")
    private String uPauseDateTime;
    @JsonProperty("sys_id")
    private String sysId;
    @JsonProperty("u_additional_tracks_affected")
    private String uAdditionalTracksAffected;
    @JsonProperty("u_first_outage_at")
    private String uFirstOutageAt;
    @JsonProperty("u_mc_ueid")
    private String uMcUeid;
    @JsonProperty("u_bmcps_message")
    private String uBmcpsMessage;
    @JsonProperty("urgency")
    private String urgency;
    @JsonProperty("company")
    private Company company;
    @JsonProperty("severity")
    private String severity;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("u_reported_ci")
    private UReportedCi uReportedCi;
    @JsonProperty("u_source_uid")
    private String uSourceUid;
    @JsonProperty("approval")
    private String approval;
    @JsonProperty("u_mc_object_uri")
    private String uMcObjectUri;
    @JsonProperty("reopen_count")
    private String reopenCount;
    @JsonProperty("u_dev_qa_environment")
    private String uDevQaEnvironment;
    @JsonProperty("u_houston")
    private String uHouston;
    @JsonProperty("sys_tags")
    private String sysTags;
    @JsonProperty("u_knowledge_article")
    private UKnowledgeArticle uKnowledgeArticle;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("promoted_by")
    private String promotedBy;
    @JsonProperty("x_avne2_bmc_to_sno_source_uid")
    private String xAvne2BmcToSnoSourceUid;
    @JsonProperty("u_supply_chain")
    private String uSupplyChain;
    @JsonProperty("u_hpi")
    private String uHpi;
    @JsonProperty("upon_reject")
    private String uponReject;
    @JsonProperty("u_best_callback")
    private String uBestCallback;
    @JsonProperty("u_mc_object_class")
    private String uMcObjectClass;
    @JsonProperty("approval_history")
    private String approvalHistory;
    @JsonProperty("number")
    private String number;
    @JsonProperty("proposed_by")
    private String proposedBy;
    @JsonProperty("u_instance")
    private String uInstance;
    @JsonProperty("u_adapter_host")
    private String uAdapterHost;
    @JsonProperty("order")
    private String order;
    @JsonProperty("u_hr")
    private String uHr;
    @JsonProperty("u_missing_category")
    private String uMissingCategory;
    @JsonProperty("cmdb_ci")
    private CmdbCi cmdbCi;
    @JsonProperty("u_track_name")
    private String uTrackName;
    @JsonProperty("u_vendor_type_model")
    private String uVendorTypeModel;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("u_all_divisions")
    private String uAllDivisions;
    @JsonProperty("u_reason_for_sla_exception")
    private String uReasonForSlaException;
    @JsonProperty("business_duration")
    private String businessDuration;
    @JsonProperty("approval_set")
    private String approvalSet;
    @JsonProperty("u_vendor_sent")
    private String uVendorSent;
    @JsonProperty("universal_request")
    private String universalRequest;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("u_circuit_id")
    private String uCircuitId;
    @JsonProperty("work_start")
    private String workStart;
    @JsonProperty("u_intermountain")
    private String uIntermountain;
    @JsonProperty("u_alert_ref")
    private String uAlertRef;
    @JsonProperty("additional_assignee_list")
    private String additionalAssigneeList;
    @JsonProperty("u_vendor_status")
    private String uVendorStatus;
    @JsonProperty("notify")
    private String notify;
    @JsonProperty("sys_class_name")
    private String sysClassName;
    @JsonProperty("closed_by")
    private ClosedBy closedBy;
    @JsonProperty("u_related_call")
    private URelatedCall uRelatedCall;
    @JsonProperty("u_vendor_serial_number")
    private String uVendorSerialNumber;
    @JsonProperty("reassignment_count")
    private String reassignmentCount;
    @JsonProperty("u_business_third_party")
    private String uBusinessThirdParty;
    @JsonProperty("assigned_to")
    private AssignedTo assignedTo;
    @JsonProperty("u_assigned_to")
    private String uAssignedTo;
    @JsonProperty("u_breach_time")
    private String uBreachTime;
    @JsonProperty("sla_due")
    private String slaDue;
    @JsonProperty("u_bmcps_dedup_id")
    private String uBmcpsDedupId;
    @JsonProperty("upon_approval")
    private String uponApproval;
    @JsonProperty("u_response_sla")
    private String uResponseSla;
    @JsonProperty("u_restoration_manager")
    private String uRestorationManager;
    @JsonProperty("u_data_migration")
    private String uDataMigration;
    @JsonProperty("u_first_incident_at")
    private String uFirstIncidentAt;
    @JsonProperty("promoted_on")
    private String promotedOn;
    @JsonProperty("child_incidents")
    private String childIncidents;
    @JsonProperty("task_effective_number")
    private String taskEffectiveNumber;
    @JsonProperty("resolved_by")
    private String resolvedBy;
    @JsonProperty("sys_updated_by")
    private String sysUpdatedBy;
    @JsonProperty("opened_by")
    private OpenedBy openedBy;
    @JsonProperty("sys_domain")
    private SysDomain sysDomain;
    @JsonProperty("proposed_on")
    private String proposedOn;
    @JsonProperty("u_ci_details")
    private String uCiDetails;
    @JsonProperty("u_mm")
    private String uMm;
    @JsonProperty("u_software")
    private String uSoftware;
    @JsonProperty("business_service")
    private String businessService;
    @JsonProperty("u_retail")
    private String uRetail;
    @JsonProperty("u_symptom")
    private String uSymptom;
    @JsonProperty("expected_start")
    private String expectedStart;
    @JsonProperty("opened_at")
    private String openedAt;
    @JsonProperty("u_impacted_business_area")
    private String uImpactedBusinessArea;
    @JsonProperty("u_accounting")
    private String uAccounting;
    @JsonProperty("vz_release_for_test_datetime")
    private String vzReleaseForTestDatetime;
    @JsonProperty("u_awaiting_reason")
    private String uAwaitingReason;
    @JsonProperty("u_caused_by_story")
    private String uCausedByStory;
    @JsonProperty("caller_id")
    private CallerId callerId;
    @JsonProperty("reopened_time")
    private String reopenedTime;
    @JsonProperty("resolved_at")
    private String resolvedAt;
    @JsonProperty("u_problem")
    private String uProblem;
    @JsonProperty("u_symptoms")
    private String uSymptoms;
    @JsonProperty("u_vendor_warranty_expiration")
    private String uVendorWarrantyExpiration;
    @JsonProperty("x_avne2_bmc_to_sno_source_ref")
    private String xAvne2BmcToSnoSourceRef;
    @JsonProperty("cause")
    private String cause;
    @JsonProperty("u_vendor_ticket")
    private String uVendorTicket;
    @JsonProperty("calendar_duration")
    private String calendarDuration;
    @JsonProperty("u_acme")
    private String uAcme;
    @JsonProperty("u_turnover_category")
    private String uTurnoverCategory;
    @JsonProperty("close_notes")
    private String closeNotes;
    @JsonProperty("u_mc_object")
    private String uMcObject;
    @JsonProperty("contact_type")
    private String contactType;
    @JsonProperty("incident_state")
    private String incidentState;
    @JsonProperty("u_southern")
    private String uSouthern;
    @JsonProperty("problem_id")
    private String problemId;
    @JsonProperty("u_seattle")
    private String uSeattle;
    @JsonProperty("u_problem_created_at")
    private String uProblemCreatedAt;
    @JsonProperty("activity_due")
    private String activityDue;
    @JsonProperty("u_problem_related_by")
    private String uProblemRelatedBy;
    @JsonProperty("comments")
    private String comments;
    @JsonProperty("u_shaws")
    private String uShaws;
    @JsonProperty("u_contact_phone")
    private String uContactPhone;
    @JsonProperty("u_mc_object_uri_link")
    private String uMcObjectUriLink;
    @JsonProperty("due_date")
    private String dueDate;
    @JsonProperty("sys_mod_count")
    private String sysModCount;
    @JsonProperty("u_ncpdp_rejects")
    private String uNcpdpRejects;
    @JsonProperty("u_verizon_customer_name")
    private String uVerizonCustomerName;
    @JsonProperty("u_other_specify")
    private String uOtherSpecify;
    @JsonProperty("u_mc_modhist")
    private String uMcModhist;
    @JsonProperty("x_avne2_bmc_to_sno_source_modified")
    private String xAvne2BmcToSnoSourceModified;
    @JsonProperty("category")
    private String category;
    @JsonProperty("u_it_shared_services")
    private String uItSharedServices;
    @JsonProperty("u_turnover_review")
    private String uTurnoverReview;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("parent")
    public String getParent() {
        return parent;
    }

    @JsonProperty("parent")
    public void setParent(String parent) {
        this.parent = parent;
    }

    @JsonProperty("u_reassignment_reason")
    public String getuReassignmentReason() {
        return uReassignmentReason;
    }

    @JsonProperty("u_reassignment_reason")
    public void setuReassignmentReason(String uReassignmentReason) {
        this.uReassignmentReason = uReassignmentReason;
    }

    @JsonProperty("caused_by")
    public String getCausedBy() {
        return causedBy;
    }

    @JsonProperty("caused_by")
    public void setCausedBy(String causedBy) {
        this.causedBy = causedBy;
    }

    @JsonProperty("watch_list")
    public String getWatchList() {
        return watchList;
    }

    @JsonProperty("watch_list")
    public void setWatchList(String watchList) {
        this.watchList = watchList;
    }

    @JsonProperty("u_bmcps_app_tier")
    public String getuBmcpsAppTier() {
        return uBmcpsAppTier;
    }

    @JsonProperty("u_bmcps_app_tier")
    public void setuBmcpsAppTier(String uBmcpsAppTier) {
        this.uBmcpsAppTier = uBmcpsAppTier;
    }

    @JsonProperty("sys_updated_on")
    public String getSysUpdatedOn() {
        return sysUpdatedOn;
    }

    @JsonProperty("sys_updated_on")
    public void setSysUpdatedOn(String sysUpdatedOn) {
        this.sysUpdatedOn = sysUpdatedOn;
    }

    @JsonProperty("u_verizon_customer_location")
    public String getuVerizonCustomerLocation() {
        return uVerizonCustomerLocation;
    }

    @JsonProperty("u_verizon_customer_location")
    public void setuVerizonCustomerLocation(String uVerizonCustomerLocation) {
        this.uVerizonCustomerLocation = uVerizonCustomerLocation;
    }

    @JsonProperty("skills")
    public String getSkills() {
        return skills;
    }

    @JsonProperty("skills")
    public void setSkills(String skills) {
        this.skills = skills;
    }

    @JsonProperty("u_ponca_city")
    public String getuPoncaCity() {
        return uPoncaCity;
    }

    @JsonProperty("u_ponca_city")
    public void setuPoncaCity(String uPoncaCity) {
        this.uPoncaCity = uPoncaCity;
    }

    @JsonProperty("u_cancellation_reason")
    public String getuCancellationReason() {
        return uCancellationReason;
    }

    @JsonProperty("u_cancellation_reason")
    public void setuCancellationReason(String uCancellationReason) {
        this.uCancellationReason = uCancellationReason;
    }

    @JsonProperty("u_sundries_boise")
    public String getuSundriesBoise() {
        return uSundriesBoise;
    }

    @JsonProperty("u_sundries_boise")
    public void setuSundriesBoise(String uSundriesBoise) {
        this.uSundriesBoise = uSundriesBoise;
    }

    @JsonProperty("lessons_learned")
    public String getLessonsLearned() {
        return lessonsLearned;
    }

    @JsonProperty("lessons_learned")
    public void setLessonsLearned(String lessonsLearned) {
        this.lessonsLearned = lessonsLearned;
    }

    @JsonProperty("u_kb_relate_xml")
    public String getuKbRelateXml() {
        return uKbRelateXml;
    }

    @JsonProperty("u_kb_relate_xml")
    public void setuKbRelateXml(String uKbRelateXml) {
        this.uKbRelateXml = uKbRelateXml;
    }

    @JsonProperty("u_mc_parameter")
    public String getuMcParameter() {
        return uMcParameter;
    }

    @JsonProperty("u_mc_parameter")
    public void setuMcParameter(String uMcParameter) {
        this.uMcParameter = uMcParameter;
    }

    @JsonProperty("u_parameter")
    public String getuParameter() {
        return uParameter;
    }

    @JsonProperty("u_parameter")
    public void setuParameter(String uParameter) {
        this.uParameter = uParameter;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("u_portland")
    public String getuPortland() {
        return uPortland;
    }

    @JsonProperty("u_portland")
    public void setuPortland(String uPortland) {
        this.uPortland = uPortland;
    }

    @JsonProperty("u_southwest_las_vegas")
    public String getuSouthwestLasVegas() {
        return uSouthwestLasVegas;
    }

    @JsonProperty("u_southwest_las_vegas")
    public void setuSouthwestLasVegas(String uSouthwestLasVegas) {
        this.uSouthwestLasVegas = uSouthwestLasVegas;
    }

    @JsonProperty("sys_created_by")
    public String getSysCreatedBy() {
        return sysCreatedBy;
    }

    @JsonProperty("sys_created_by")
    public void setSysCreatedBy(String sysCreatedBy) {
        this.sysCreatedBy = sysCreatedBy;
    }

    @JsonProperty("knowledge")
    public String getKnowledge() {
        return knowledge;
    }

    @JsonProperty("knowledge")
    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    @JsonProperty("impact")
    public String getImpact() {
        return impact;
    }

    @JsonProperty("impact")
    public void setImpact(String impact) {
        this.impact = impact;
    }

    @JsonProperty("active")
    public String getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(String active) {
        this.active = active;
    }

    @JsonProperty("u_vendor")
    public String getuVendor() {
        return uVendor;
    }

    @JsonProperty("u_vendor")
    public void setuVendor(String uVendor) {
        this.uVendor = uVendor;
    }

    @JsonProperty("group_list")
    public String getGroupList() {
        return groupList;
    }

    @JsonProperty("group_list")
    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }

    @JsonProperty("u_pharmacy")
    public String getuPharmacy() {
        return uPharmacy;
    }

    @JsonProperty("u_pharmacy")
    public void setuPharmacy(String uPharmacy) {
        this.uPharmacy = uPharmacy;
    }

    @JsonProperty("major_incident_state")
    public String getMajorIncidentState() {
        return majorIncidentState;
    }

    @JsonProperty("major_incident_state")
    public void setMajorIncidentState(String majorIncidentState) {
        this.majorIncidentState = majorIncidentState;
    }

    @JsonProperty("u_denver")
    public String getuDenver() {
        return uDenver;
    }

    @JsonProperty("u_denver")
    public void setuDenver(String uDenver) {
        this.uDenver = uDenver;
    }

    @JsonProperty("correlation_display")
    public String getCorrelationDisplay() {
        return correlationDisplay;
    }

    @JsonProperty("correlation_display")
    public void setCorrelationDisplay(String correlationDisplay) {
        this.correlationDisplay = correlationDisplay;
    }

    @JsonProperty("u_caller_name")
    public String getuCallerName() {
        return uCallerName;
    }

    @JsonProperty("u_caller_name")
    public void setuCallerName(String uCallerName) {
        this.uCallerName = uCallerName;
    }

    @JsonProperty("vz_release_for_test")
    public String getVzReleaseForTest() {
        return vzReleaseForTest;
    }

    @JsonProperty("vz_release_for_test")
    public void setVzReleaseForTest(String vzReleaseForTest) {
        this.vzReleaseForTest = vzReleaseForTest;
    }

    @JsonProperty("follow_up")
    public String getFollowUp() {
        return followUp;
    }

    @JsonProperty("follow_up")
    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    @JsonProperty("parent_incident")
    public String getParentIncident() {
        return parentIncident;
    }

    @JsonProperty("parent_incident")
    public void setParentIncident(String parentIncident) {
        this.parentIncident = parentIncident;
    }

    @JsonProperty("reopened_by")
    public String getReopenedBy() {
        return reopenedBy;
    }

    @JsonProperty("reopened_by")
    public void setReopenedBy(String reopenedBy) {
        this.reopenedBy = reopenedBy;
    }

    @JsonProperty("u_reporting")
    public String getuReporting() {
        return uReporting;
    }

    @JsonProperty("u_reporting")
    public void setuReporting(String uReporting) {
        this.uReporting = uReporting;
    }

    @JsonProperty("u_target_date")
    public String getuTargetDate() {
        return uTargetDate;
    }

    @JsonProperty("u_target_date")
    public void setuTargetDate(String uTargetDate) {
        this.uTargetDate = uTargetDate;
    }

    @JsonProperty("u_recycle_required")
    public String getuRecycleRequired() {
        return uRecycleRequired;
    }

    @JsonProperty("u_recycle_required")
    public void setuRecycleRequired(String uRecycleRequired) {
        this.uRecycleRequired = uRecycleRequired;
    }

    @JsonProperty("u_site_zone")
    public String getuSiteZone() {
        return uSiteZone;
    }

    @JsonProperty("u_site_zone")
    public void setuSiteZone(String uSiteZone) {
        this.uSiteZone = uSiteZone;
    }

    @JsonProperty("u_impacted_business_department")
    public String getuImpactedBusinessDepartment() {
        return uImpactedBusinessDepartment;
    }

    @JsonProperty("u_impacted_business_department")
    public void setuImpactedBusinessDepartment(String uImpactedBusinessDepartment) {
        this.uImpactedBusinessDepartment = uImpactedBusinessDepartment;
    }

    @JsonProperty("escalation")
    public String getEscalation() {
        return escalation;
    }

    @JsonProperty("escalation")
    public void setEscalation(String escalation) {
        this.escalation = escalation;
    }

    @JsonProperty("u_socal")
    public String getuSocal() {
        return uSocal;
    }

    @JsonProperty("u_socal")
    public void setuSocal(String uSocal) {
        this.uSocal = uSocal;
    }

    @JsonProperty("correlation_id")
    public String getCorrelationId() {
        return correlationId;
    }

    @JsonProperty("correlation_id")
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @JsonProperty("timeline")
    public String getTimeline() {
        return timeline;
    }

    @JsonProperty("timeline")
    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    @JsonProperty("u_verizon_product_type")
    public String getuVerizonProductType() {
        return uVerizonProductType;
    }

    @JsonProperty("u_verizon_product_type")
    public void setuVerizonProductType(String uVerizonProductType) {
        this.uVerizonProductType = uVerizonProductType;
    }

    @JsonProperty("u_conversion_activity")
    public String getuConversionActivity() {
        return uConversionActivity;
    }

    @JsonProperty("u_conversion_activity")
    public void setuConversionActivity(String uConversionActivity) {
        this.uConversionActivity = uConversionActivity;
    }

    @JsonProperty("u_related_hr_case")
    public String getuRelatedHrCase() {
        return uRelatedHrCase;
    }

    @JsonProperty("u_related_hr_case")
    public void setuRelatedHrCase(String uRelatedHrCase) {
        this.uRelatedHrCase = uRelatedHrCase;
    }

    @JsonProperty("u_merger_activity")
    public String getuMergerActivity() {
        return uMergerActivity;
    }

    @JsonProperty("u_merger_activity")
    public void setuMergerActivity(String uMergerActivity) {
        this.uMergerActivity = uMergerActivity;
    }

    @JsonProperty("u_change")
    public String getuChange() {
        return uChange;
    }

    @JsonProperty("u_change")
    public void setuChange(String uChange) {
        this.uChange = uChange;
    }

    @JsonProperty("u_vendor_hardware_asset")
    public String getuVendorHardwareAsset() {
        return uVendorHardwareAsset;
    }

    @JsonProperty("u_vendor_hardware_asset")
    public void setuVendorHardwareAsset(String uVendorHardwareAsset) {
        this.uVendorHardwareAsset = uVendorHardwareAsset;
    }

    @JsonProperty("user_input")
    public String getUserInput() {
        return userInput;
    }

    @JsonProperty("user_input")
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    @JsonProperty("sys_created_on")
    public String getSysCreatedOn() {
        return sysCreatedOn;
    }

    @JsonProperty("sys_created_on")
    public void setSysCreatedOn(String sysCreatedOn) {
        this.sysCreatedOn = sysCreatedOn;
    }

    @JsonProperty("route_reason")
    public String getRouteReason() {
        return routeReason;
    }

    @JsonProperty("route_reason")
    public void setRouteReason(String routeReason) {
        this.routeReason = routeReason;
    }

    @JsonProperty("u_support_level")
    public String getuSupportLevel() {
        return uSupportLevel;
    }

    @JsonProperty("u_support_level")
    public void setuSupportLevel(String uSupportLevel) {
        this.uSupportLevel = uSupportLevel;
    }

    @JsonProperty("calendar_stc")
    public String getCalendarStc() {
        return calendarStc;
    }

    @JsonProperty("calendar_stc")
    public void setCalendarStc(String calendarStc) {
        this.calendarStc = calendarStc;
    }

    @JsonProperty("closed_at")
    public String getClosedAt() {
        return closedAt;
    }

    @JsonProperty("closed_at")
    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    @JsonProperty("u_jewel")
    public String getuJewel() {
        return uJewel;
    }

    @JsonProperty("u_jewel")
    public void setuJewel(String uJewel) {
        this.uJewel = uJewel;
    }

    @JsonProperty("u_soft")
    public String getuSoft() {
        return uSoft;
    }

    @JsonProperty("u_soft")
    public void setuSoft(String uSoft) {
        this.uSoft = uSoft;
    }

    @JsonProperty("u_consolidated_event_data")
    public String getuConsolidatedEventData() {
        return uConsolidatedEventData;
    }

    @JsonProperty("u_consolidated_event_data")
    public void setuConsolidatedEventData(String uConsolidatedEventData) {
        this.uConsolidatedEventData = uConsolidatedEventData;
    }

    @JsonProperty("u_first_notified_at")
    public String getuFirstNotifiedAt() {
        return uFirstNotifiedAt;
    }

    @JsonProperty("u_first_notified_at")
    public void setuFirstNotifiedAt(String uFirstNotifiedAt) {
        this.uFirstNotifiedAt = uFirstNotifiedAt;
    }

    @JsonProperty("u_last_outage_at")
    public String getuLastOutageAt() {
        return uLastOutageAt;
    }

    @JsonProperty("u_last_outage_at")
    public void setuLastOutageAt(String uLastOutageAt) {
        this.uLastOutageAt = uLastOutageAt;
    }

    @JsonProperty("u_mc_long_msg")
    public String getuMcLongMsg() {
        return uMcLongMsg;
    }

    @JsonProperty("u_mc_long_msg")
    public void setuMcLongMsg(String uMcLongMsg) {
        this.uMcLongMsg = uMcLongMsg;
    }

    @JsonProperty("u_alert_source")
    public String getuAlertSource() {
        return uAlertSource;
    }

    @JsonProperty("u_alert_source")
    public void setuAlertSource(String uAlertSource) {
        this.uAlertSource = uAlertSource;
    }

    @JsonProperty("u_vendor_task_status")
    public String getuVendorTaskStatus() {
        return uVendorTaskStatus;
    }

    @JsonProperty("u_vendor_task_status")
    public void setuVendorTaskStatus(String uVendorTaskStatus) {
        this.uVendorTaskStatus = uVendorTaskStatus;
    }

    @JsonProperty("business_impact")
    public String getBusinessImpact() {
        return businessImpact;
    }

    @JsonProperty("business_impact")
    public void setBusinessImpact(String businessImpact) {
        this.businessImpact = businessImpact;
    }

    @JsonProperty("u_source_ref")
    public String getuSourceRef() {
        return uSourceRef;
    }

    @JsonProperty("u_source_ref")
    public void setuSourceRef(String uSourceRef) {
        this.uSourceRef = uSourceRef;
    }

    @JsonProperty("u_test_field")
    public String getuTestField() {
        return uTestField;
    }

    @JsonProperty("u_test_field")
    public void setuTestField(String uTestField) {
        this.uTestField = uTestField;
    }

    @JsonProperty("rfc")
    public String getRfc() {
        return rfc;
    }

    @JsonProperty("rfc")
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @JsonProperty("time_worked")
    public String getTimeWorked() {
        return timeWorked;
    }

    @JsonProperty("time_worked")
    public void setTimeWorked(String timeWorked) {
        this.timeWorked = timeWorked;
    }

    @JsonProperty("u_event_handle")
    public String getuEventHandle() {
        return uEventHandle;
    }

    @JsonProperty("u_event_handle")
    public void setuEventHandle(String uEventHandle) {
        this.uEventHandle = uEventHandle;
    }

    @JsonProperty("u_verizon_severity")
    public String getuVerizonSeverity() {
        return uVerizonSeverity;
    }

    @JsonProperty("u_verizon_severity")
    public void setuVerizonSeverity(String uVerizonSeverity) {
        this.uVerizonSeverity = uVerizonSeverity;
    }

    @JsonProperty("work_end")
    public String getWorkEnd() {
        return workEnd;
    }

    @JsonProperty("work_end")
    public void setWorkEnd(String workEnd) {
        this.workEnd = workEnd;
    }

    @JsonProperty("u_related_chat_queue")
    public String getuRelatedChatQueue() {
        return uRelatedChatQueue;
    }

    @JsonProperty("u_related_chat_queue")
    public void setuRelatedChatQueue(String uRelatedChatQueue) {
        this.uRelatedChatQueue = uRelatedChatQueue;
    }

    @JsonProperty("subcategory")
    public String getSubcategory() {
        return subcategory;
    }

    @JsonProperty("subcategory")
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @JsonProperty("u_current_date")
    public String getuCurrentDate() {
        return uCurrentDate;
    }

    @JsonProperty("u_current_date")
    public void setuCurrentDate(String uCurrentDate) {
        this.uCurrentDate = uCurrentDate;
    }

    @JsonProperty("work_notes")
    public String getWorkNotes() {
        return workNotes;
    }

    @JsonProperty("work_notes")
    public void setWorkNotes(String workNotes) {
        this.workNotes = workNotes;
    }

    @JsonProperty("close_code")
    public String getCloseCode() {
        return closeCode;
    }

    @JsonProperty("close_code")
    public void setCloseCode(String closeCode) {
        this.closeCode = closeCode;
    }

    @JsonProperty("assignment_group")
    public AssignmentGroup getAssignmentGroup() {
        return assignmentGroup;
    }

    @JsonProperty("assignment_group")
    public void setAssignmentGroup(AssignmentGroup assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }

    @JsonProperty("business_stc")
    public String getBusinessStc() {
        return businessStc;
    }

    @JsonProperty("business_stc")
    public void setBusinessStc(String businessStc) {
        this.businessStc = businessStc;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("u_pause_date_time")
    public String getuPauseDateTime() {
        return uPauseDateTime;
    }

    @JsonProperty("u_pause_date_time")
    public void setuPauseDateTime(String uPauseDateTime) {
        this.uPauseDateTime = uPauseDateTime;
    }

    @JsonProperty("sys_id")
    public String getSysId() {
        return sysId;
    }

    @JsonProperty("sys_id")
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    @JsonProperty("u_additional_tracks_affected")
    public String getuAdditionalTracksAffected() {
        return uAdditionalTracksAffected;
    }

    @JsonProperty("u_additional_tracks_affected")
    public void setuAdditionalTracksAffected(String uAdditionalTracksAffected) {
        this.uAdditionalTracksAffected = uAdditionalTracksAffected;
    }

    @JsonProperty("u_first_outage_at")
    public String getuFirstOutageAt() {
        return uFirstOutageAt;
    }

    @JsonProperty("u_first_outage_at")
    public void setuFirstOutageAt(String uFirstOutageAt) {
        this.uFirstOutageAt = uFirstOutageAt;
    }

    @JsonProperty("u_mc_ueid")
    public String getuMcUeid() {
        return uMcUeid;
    }

    @JsonProperty("u_mc_ueid")
    public void setuMcUeid(String uMcUeid) {
        this.uMcUeid = uMcUeid;
    }

    @JsonProperty("u_bmcps_message")
    public String getuBmcpsMessage() {
        return uBmcpsMessage;
    }

    @JsonProperty("u_bmcps_message")
    public void setuBmcpsMessage(String uBmcpsMessage) {
        this.uBmcpsMessage = uBmcpsMessage;
    }

    @JsonProperty("urgency")
    public String getUrgency() {
        return urgency;
    }

    @JsonProperty("urgency")
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    @JsonProperty("company")
    public Company getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(Company company) {
        this.company = company;
    }

    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    @JsonProperty("severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @JsonProperty("u_reported_ci")
    public UReportedCi getuReportedCi() {
        return uReportedCi;
    }

    @JsonProperty("u_reported_ci")
    public void setuReportedCi(UReportedCi uReportedCi) {
        this.uReportedCi = uReportedCi;
    }

    @JsonProperty("u_source_uid")
    public String getuSourceUid() {
        return uSourceUid;
    }

    @JsonProperty("u_source_uid")
    public void setuSourceUid(String uSourceUid) {
        this.uSourceUid = uSourceUid;
    }

    @JsonProperty("approval")
    public String getApproval() {
        return approval;
    }

    @JsonProperty("approval")
    public void setApproval(String approval) {
        this.approval = approval;
    }

    @JsonProperty("u_mc_object_uri")
    public String getuMcObjectUri() {
        return uMcObjectUri;
    }

    @JsonProperty("u_mc_object_uri")
    public void setuMcObjectUri(String uMcObjectUri) {
        this.uMcObjectUri = uMcObjectUri;
    }

    @JsonProperty("reopen_count")
    public String getReopenCount() {
        return reopenCount;
    }

    @JsonProperty("reopen_count")
    public void setReopenCount(String reopenCount) {
        this.reopenCount = reopenCount;
    }

    @JsonProperty("u_dev_qa_environment")
    public String getuDevQaEnvironment() {
        return uDevQaEnvironment;
    }

    @JsonProperty("u_dev_qa_environment")
    public void setuDevQaEnvironment(String uDevQaEnvironment) {
        this.uDevQaEnvironment = uDevQaEnvironment;
    }

    @JsonProperty("u_houston")
    public String getuHouston() {
        return uHouston;
    }

    @JsonProperty("u_houston")
    public void setuHouston(String uHouston) {
        this.uHouston = uHouston;
    }

    @JsonProperty("sys_tags")
    public String getSysTags() {
        return sysTags;
    }

    @JsonProperty("sys_tags")
    public void setSysTags(String sysTags) {
        this.sysTags = sysTags;
    }

    @JsonProperty("u_knowledge_article")
    public UKnowledgeArticle getuKnowledgeArticle() {
        return uKnowledgeArticle;
    }

    @JsonProperty("u_knowledge_article")
    public void setuKnowledgeArticle(UKnowledgeArticle uKnowledgeArticle) {
        this.uKnowledgeArticle = uKnowledgeArticle;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("promoted_by")
    public String getPromotedBy() {
        return promotedBy;
    }

    @JsonProperty("promoted_by")
    public void setPromotedBy(String promotedBy) {
        this.promotedBy = promotedBy;
    }

    @JsonProperty("x_avne2_bmc_to_sno_source_uid")
    public String getxAvne2BmcToSnoSourceUid() {
        return xAvne2BmcToSnoSourceUid;
    }

    @JsonProperty("x_avne2_bmc_to_sno_source_uid")
    public void setxAvne2BmcToSnoSourceUid(String xAvne2BmcToSnoSourceUid) {
        this.xAvne2BmcToSnoSourceUid = xAvne2BmcToSnoSourceUid;
    }

    @JsonProperty("u_supply_chain")
    public String getuSupplyChain() {
        return uSupplyChain;
    }

    @JsonProperty("u_supply_chain")
    public void setuSupplyChain(String uSupplyChain) {
        this.uSupplyChain = uSupplyChain;
    }

    @JsonProperty("u_hpi")
    public String getuHpi() {
        return uHpi;
    }

    @JsonProperty("u_hpi")
    public void setuHpi(String uHpi) {
        this.uHpi = uHpi;
    }

    @JsonProperty("upon_reject")
    public String getUponReject() {
        return uponReject;
    }

    @JsonProperty("upon_reject")
    public void setUponReject(String uponReject) {
        this.uponReject = uponReject;
    }

    @JsonProperty("u_best_callback")
    public String getuBestCallback() {
        return uBestCallback;
    }

    @JsonProperty("u_best_callback")
    public void setuBestCallback(String uBestCallback) {
        this.uBestCallback = uBestCallback;
    }

    @JsonProperty("u_mc_object_class")
    public String getuMcObjectClass() {
        return uMcObjectClass;
    }

    @JsonProperty("u_mc_object_class")
    public void setuMcObjectClass(String uMcObjectClass) {
        this.uMcObjectClass = uMcObjectClass;
    }

    @JsonProperty("approval_history")
    public String getApprovalHistory() {
        return approvalHistory;
    }

    @JsonProperty("approval_history")
    public void setApprovalHistory(String approvalHistory) {
        this.approvalHistory = approvalHistory;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("proposed_by")
    public String getProposedBy() {
        return proposedBy;
    }

    @JsonProperty("proposed_by")
    public void setProposedBy(String proposedBy) {
        this.proposedBy = proposedBy;
    }

    @JsonProperty("u_instance")
    public String getuInstance() {
        return uInstance;
    }

    @JsonProperty("u_instance")
    public void setuInstance(String uInstance) {
        this.uInstance = uInstance;
    }

    @JsonProperty("u_adapter_host")
    public String getuAdapterHost() {
        return uAdapterHost;
    }

    @JsonProperty("u_adapter_host")
    public void setuAdapterHost(String uAdapterHost) {
        this.uAdapterHost = uAdapterHost;
    }

    @JsonProperty("order")
    public String getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(String order) {
        this.order = order;
    }

    @JsonProperty("u_hr")
    public String getuHr() {
        return uHr;
    }

    @JsonProperty("u_hr")
    public void setuHr(String uHr) {
        this.uHr = uHr;
    }

    @JsonProperty("u_missing_category")
    public String getuMissingCategory() {
        return uMissingCategory;
    }

    @JsonProperty("u_missing_category")
    public void setuMissingCategory(String uMissingCategory) {
        this.uMissingCategory = uMissingCategory;
    }

    @JsonProperty("cmdb_ci")
    public CmdbCi getCmdbCi() {
        return cmdbCi;
    }

    @JsonProperty("cmdb_ci")
    public void setCmdbCi(CmdbCi cmdbCi) {
        this.cmdbCi = cmdbCi;
    }

    @JsonProperty("u_track_name")
    public String getuTrackName() {
        return uTrackName;
    }

    @JsonProperty("u_track_name")
    public void setuTrackName(String uTrackName) {
        this.uTrackName = uTrackName;
    }

    @JsonProperty("u_vendor_type_model")
    public String getuVendorTypeModel() {
        return uVendorTypeModel;
    }

    @JsonProperty("u_vendor_type_model")
    public void setuVendorTypeModel(String uVendorTypeModel) {
        this.uVendorTypeModel = uVendorTypeModel;
    }

    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @JsonProperty("u_all_divisions")
    public String getuAllDivisions() {
        return uAllDivisions;
    }

    @JsonProperty("u_all_divisions")
    public void setuAllDivisions(String uAllDivisions) {
        this.uAllDivisions = uAllDivisions;
    }

    @JsonProperty("u_reason_for_sla_exception")
    public String getuReasonForSlaException() {
        return uReasonForSlaException;
    }

    @JsonProperty("u_reason_for_sla_exception")
    public void setuReasonForSlaException(String uReasonForSlaException) {
        this.uReasonForSlaException = uReasonForSlaException;
    }

    @JsonProperty("business_duration")
    public String getBusinessDuration() {
        return businessDuration;
    }

    @JsonProperty("business_duration")
    public void setBusinessDuration(String businessDuration) {
        this.businessDuration = businessDuration;
    }

    @JsonProperty("approval_set")
    public String getApprovalSet() {
        return approvalSet;
    }

    @JsonProperty("approval_set")
    public void setApprovalSet(String approvalSet) {
        this.approvalSet = approvalSet;
    }

    @JsonProperty("u_vendor_sent")
    public String getuVendorSent() {
        return uVendorSent;
    }

    @JsonProperty("u_vendor_sent")
    public void setuVendorSent(String uVendorSent) {
        this.uVendorSent = uVendorSent;
    }

    @JsonProperty("universal_request")
    public String getUniversalRequest() {
        return universalRequest;
    }

    @JsonProperty("universal_request")
    public void setUniversalRequest(String universalRequest) {
        this.universalRequest = universalRequest;
    }

    @JsonProperty("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @JsonProperty("short_description")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @JsonProperty("u_circuit_id")
    public String getuCircuitId() {
        return uCircuitId;
    }

    @JsonProperty("u_circuit_id")
    public void setuCircuitId(String uCircuitId) {
        this.uCircuitId = uCircuitId;
    }

    @JsonProperty("work_start")
    public String getWorkStart() {
        return workStart;
    }

    @JsonProperty("work_start")
    public void setWorkStart(String workStart) {
        this.workStart = workStart;
    }

    @JsonProperty("u_intermountain")
    public String getuIntermountain() {
        return uIntermountain;
    }

    @JsonProperty("u_intermountain")
    public void setuIntermountain(String uIntermountain) {
        this.uIntermountain = uIntermountain;
    }

    @JsonProperty("u_alert_ref")
    public String getuAlertRef() {
        return uAlertRef;
    }

    @JsonProperty("u_alert_ref")
    public void setuAlertRef(String uAlertRef) {
        this.uAlertRef = uAlertRef;
    }

    @JsonProperty("additional_assignee_list")
    public String getAdditionalAssigneeList() {
        return additionalAssigneeList;
    }

    @JsonProperty("additional_assignee_list")
    public void setAdditionalAssigneeList(String additionalAssigneeList) {
        this.additionalAssigneeList = additionalAssigneeList;
    }

    @JsonProperty("u_vendor_status")
    public String getuVendorStatus() {
        return uVendorStatus;
    }

    @JsonProperty("u_vendor_status")
    public void setuVendorStatus(String uVendorStatus) {
        this.uVendorStatus = uVendorStatus;
    }

    @JsonProperty("notify")
    public String getNotify() {
        return notify;
    }

    @JsonProperty("notify")
    public void setNotify(String notify) {
        this.notify = notify;
    }

    @JsonProperty("sys_class_name")
    public String getSysClassName() {
        return sysClassName;
    }

    @JsonProperty("sys_class_name")
    public void setSysClassName(String sysClassName) {
        this.sysClassName = sysClassName;
    }

    @JsonProperty("closed_by")
    public ClosedBy getClosedBy() {
        return closedBy;
    }

    @JsonProperty("closed_by")
    public void setClosedBy(ClosedBy closedBy) {
        this.closedBy = closedBy;
    }

    @JsonProperty("u_related_call")
    public URelatedCall getuRelatedCall() {
        return uRelatedCall;
    }

    @JsonProperty("u_related_call")
    public void setuRelatedCall(URelatedCall uRelatedCall) {
        this.uRelatedCall = uRelatedCall;
    }

    @JsonProperty("u_vendor_serial_number")
    public String getuVendorSerialNumber() {
        return uVendorSerialNumber;
    }

    @JsonProperty("u_vendor_serial_number")
    public void setuVendorSerialNumber(String uVendorSerialNumber) {
        this.uVendorSerialNumber = uVendorSerialNumber;
    }

    @JsonProperty("reassignment_count")
    public String getReassignmentCount() {
        return reassignmentCount;
    }

    @JsonProperty("reassignment_count")
    public void setReassignmentCount(String reassignmentCount) {
        this.reassignmentCount = reassignmentCount;
    }

    @JsonProperty("u_business_third_party")
    public String getuBusinessThirdParty() {
        return uBusinessThirdParty;
    }

    @JsonProperty("u_business_third_party")
    public void setuBusinessThirdParty(String uBusinessThirdParty) {
        this.uBusinessThirdParty = uBusinessThirdParty;
    }

    @JsonProperty("assigned_to")
    public AssignedTo getAssignedTo() {
        return assignedTo;
    }

    @JsonProperty("assigned_to")
    public void setAssignedTo(AssignedTo assignedTo) {
        this.assignedTo = assignedTo;
    }

    @JsonProperty("u_assigned_to")
    public String getuAssignedTo() {
        return uAssignedTo;
    }

    @JsonProperty("u_assigned_to")
    public void setuAssignedTo(String uAssignedTo) {
        this.uAssignedTo = uAssignedTo;
    }

    @JsonProperty("u_breach_time")
    public String getuBreachTime() {
        return uBreachTime;
    }

    @JsonProperty("u_breach_time")
    public void setuBreachTime(String uBreachTime) {
        this.uBreachTime = uBreachTime;
    }

    @JsonProperty("sla_due")
    public String getSlaDue() {
        return slaDue;
    }

    @JsonProperty("sla_due")
    public void setSlaDue(String slaDue) {
        this.slaDue = slaDue;
    }

    @JsonProperty("u_bmcps_dedup_id")
    public String getuBmcpsDedupId() {
        return uBmcpsDedupId;
    }

    @JsonProperty("u_bmcps_dedup_id")
    public void setuBmcpsDedupId(String uBmcpsDedupId) {
        this.uBmcpsDedupId = uBmcpsDedupId;
    }

    @JsonProperty("upon_approval")
    public String getUponApproval() {
        return uponApproval;
    }

    @JsonProperty("upon_approval")
    public void setUponApproval(String uponApproval) {
        this.uponApproval = uponApproval;
    }

    @JsonProperty("u_response_sla")
    public String getuResponseSla() {
        return uResponseSla;
    }

    @JsonProperty("u_response_sla")
    public void setuResponseSla(String uResponseSla) {
        this.uResponseSla = uResponseSla;
    }

    @JsonProperty("u_restoration_manager")
    public String getuRestorationManager() {
        return uRestorationManager;
    }

    @JsonProperty("u_restoration_manager")
    public void setuRestorationManager(String uRestorationManager) {
        this.uRestorationManager = uRestorationManager;
    }

    @JsonProperty("u_data_migration")
    public String getuDataMigration() {
        return uDataMigration;
    }

    @JsonProperty("u_data_migration")
    public void setuDataMigration(String uDataMigration) {
        this.uDataMigration = uDataMigration;
    }

    @JsonProperty("u_first_incident_at")
    public String getuFirstIncidentAt() {
        return uFirstIncidentAt;
    }

    @JsonProperty("u_first_incident_at")
    public void setuFirstIncidentAt(String uFirstIncidentAt) {
        this.uFirstIncidentAt = uFirstIncidentAt;
    }

    @JsonProperty("promoted_on")
    public String getPromotedOn() {
        return promotedOn;
    }

    @JsonProperty("promoted_on")
    public void setPromotedOn(String promotedOn) {
        this.promotedOn = promotedOn;
    }

    @JsonProperty("child_incidents")
    public String getChildIncidents() {
        return childIncidents;
    }

    @JsonProperty("child_incidents")
    public void setChildIncidents(String childIncidents) {
        this.childIncidents = childIncidents;
    }

    @JsonProperty("task_effective_number")
    public String getTaskEffectiveNumber() {
        return taskEffectiveNumber;
    }

    @JsonProperty("task_effective_number")
    public void setTaskEffectiveNumber(String taskEffectiveNumber) {
        this.taskEffectiveNumber = taskEffectiveNumber;
    }

    @JsonProperty("resolved_by")
    public String getResolvedBy() {
        return resolvedBy;
    }

    @JsonProperty("resolved_by")
    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    @JsonProperty("sys_updated_by")
    public String getSysUpdatedBy() {
        return sysUpdatedBy;
    }

    @JsonProperty("sys_updated_by")
    public void setSysUpdatedBy(String sysUpdatedBy) {
        this.sysUpdatedBy = sysUpdatedBy;
    }

    @JsonProperty("opened_by")
    public OpenedBy getOpenedBy() {
        return openedBy;
    }

    @JsonProperty("opened_by")
    public void setOpenedBy(OpenedBy openedBy) {
        this.openedBy = openedBy;
    }

    @JsonProperty("sys_domain")
    public SysDomain getSysDomain() {
        return sysDomain;
    }

    @JsonProperty("sys_domain")
    public void setSysDomain(SysDomain sysDomain) {
        this.sysDomain = sysDomain;
    }

    @JsonProperty("proposed_on")
    public String getProposedOn() {
        return proposedOn;
    }

    @JsonProperty("proposed_on")
    public void setProposedOn(String proposedOn) {
        this.proposedOn = proposedOn;
    }

    @JsonProperty("u_ci_details")
    public String getuCiDetails() {
        return uCiDetails;
    }

    @JsonProperty("u_ci_details")
    public void setuCiDetails(String uCiDetails) {
        this.uCiDetails = uCiDetails;
    }

    @JsonProperty("u_mm")
    public String getuMm() {
        return uMm;
    }

    @JsonProperty("u_mm")
    public void setuMm(String uMm) {
        this.uMm = uMm;
    }

    @JsonProperty("u_software")
    public String getuSoftware() {
        return uSoftware;
    }

    @JsonProperty("u_software")
    public void setuSoftware(String uSoftware) {
        this.uSoftware = uSoftware;
    }

    @JsonProperty("business_service")
    public String getBusinessService() {
        return businessService;
    }

    @JsonProperty("business_service")
    public void setBusinessService(String businessService) {
        this.businessService = businessService;
    }

    @JsonProperty("u_retail")
    public String getuRetail() {
        return uRetail;
    }

    @JsonProperty("u_retail")
    public void setuRetail(String uRetail) {
        this.uRetail = uRetail;
    }

    @JsonProperty("u_symptom")
    public String getuSymptom() {
        return uSymptom;
    }

    @JsonProperty("u_symptom")
    public void setuSymptom(String uSymptom) {
        this.uSymptom = uSymptom;
    }

    @JsonProperty("expected_start")
    public String getExpectedStart() {
        return expectedStart;
    }

    @JsonProperty("expected_start")
    public void setExpectedStart(String expectedStart) {
        this.expectedStart = expectedStart;
    }

    @JsonProperty("opened_at")
    public String getOpenedAt() {
        return openedAt;
    }

    @JsonProperty("opened_at")
    public void setOpenedAt(String openedAt) {
        this.openedAt = openedAt;
    }

    @JsonProperty("u_impacted_business_area")
    public String getuImpactedBusinessArea() {
        return uImpactedBusinessArea;
    }

    @JsonProperty("u_impacted_business_area")
    public void setuImpactedBusinessArea(String uImpactedBusinessArea) {
        this.uImpactedBusinessArea = uImpactedBusinessArea;
    }

    @JsonProperty("u_accounting")
    public String getuAccounting() {
        return uAccounting;
    }

    @JsonProperty("u_accounting")
    public void setuAccounting(String uAccounting) {
        this.uAccounting = uAccounting;
    }

    @JsonProperty("vz_release_for_test_datetime")
    public String getVzReleaseForTestDatetime() {
        return vzReleaseForTestDatetime;
    }

    @JsonProperty("vz_release_for_test_datetime")
    public void setVzReleaseForTestDatetime(String vzReleaseForTestDatetime) {
        this.vzReleaseForTestDatetime = vzReleaseForTestDatetime;
    }

    @JsonProperty("u_awaiting_reason")
    public String getuAwaitingReason() {
        return uAwaitingReason;
    }

    @JsonProperty("u_awaiting_reason")
    public void setuAwaitingReason(String uAwaitingReason) {
        this.uAwaitingReason = uAwaitingReason;
    }

    @JsonProperty("u_caused_by_story")
    public String getuCausedByStory() {
        return uCausedByStory;
    }

    @JsonProperty("u_caused_by_story")
    public void setuCausedByStory(String uCausedByStory) {
        this.uCausedByStory = uCausedByStory;
    }

    @JsonProperty("caller_id")
    public CallerId getCallerId() {
        return callerId;
    }

    @JsonProperty("caller_id")
    public void setCallerId(CallerId callerId) {
        this.callerId = callerId;
    }

    @JsonProperty("reopened_time")
    public String getReopenedTime() {
        return reopenedTime;
    }

    @JsonProperty("reopened_time")
    public void setReopenedTime(String reopenedTime) {
        this.reopenedTime = reopenedTime;
    }

    @JsonProperty("resolved_at")
    public String getResolvedAt() {
        return resolvedAt;
    }

    @JsonProperty("resolved_at")
    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    @JsonProperty("u_problem")
    public String getuProblem() {
        return uProblem;
    }

    @JsonProperty("u_problem")
    public void setuProblem(String uProblem) {
        this.uProblem = uProblem;
    }

    @JsonProperty("u_symptoms")
    public String getuSymptoms() {
        return uSymptoms;
    }

    @JsonProperty("u_symptoms")
    public void setuSymptoms(String uSymptoms) {
        this.uSymptoms = uSymptoms;
    }

    @JsonProperty("u_vendor_warranty_expiration")
    public String getuVendorWarrantyExpiration() {
        return uVendorWarrantyExpiration;
    }

    @JsonProperty("u_vendor_warranty_expiration")
    public void setuVendorWarrantyExpiration(String uVendorWarrantyExpiration) {
        this.uVendorWarrantyExpiration = uVendorWarrantyExpiration;
    }

    @JsonProperty("x_avne2_bmc_to_sno_source_ref")
    public String getxAvne2BmcToSnoSourceRef() {
        return xAvne2BmcToSnoSourceRef;
    }

    @JsonProperty("x_avne2_bmc_to_sno_source_ref")
    public void setxAvne2BmcToSnoSourceRef(String xAvne2BmcToSnoSourceRef) {
        this.xAvne2BmcToSnoSourceRef = xAvne2BmcToSnoSourceRef;
    }

    @JsonProperty("cause")
    public String getCause() {
        return cause;
    }

    @JsonProperty("cause")
    public void setCause(String cause) {
        this.cause = cause;
    }

    @JsonProperty("u_vendor_ticket")
    public String getuVendorTicket() {
        return uVendorTicket;
    }

    @JsonProperty("u_vendor_ticket")
    public void setuVendorTicket(String uVendorTicket) {
        this.uVendorTicket = uVendorTicket;
    }

    @JsonProperty("calendar_duration")
    public String getCalendarDuration() {
        return calendarDuration;
    }

    @JsonProperty("calendar_duration")
    public void setCalendarDuration(String calendarDuration) {
        this.calendarDuration = calendarDuration;
    }

    @JsonProperty("u_acme")
    public String getuAcme() {
        return uAcme;
    }

    @JsonProperty("u_acme")
    public void setuAcme(String uAcme) {
        this.uAcme = uAcme;
    }

    @JsonProperty("u_turnover_category")
    public String getuTurnoverCategory() {
        return uTurnoverCategory;
    }

    @JsonProperty("u_turnover_category")
    public void setuTurnoverCategory(String uTurnoverCategory) {
        this.uTurnoverCategory = uTurnoverCategory;
    }

    @JsonProperty("close_notes")
    public String getCloseNotes() {
        return closeNotes;
    }

    @JsonProperty("close_notes")
    public void setCloseNotes(String closeNotes) {
        this.closeNotes = closeNotes;
    }

    @JsonProperty("u_mc_object")
    public String getuMcObject() {
        return uMcObject;
    }

    @JsonProperty("u_mc_object")
    public void setuMcObject(String uMcObject) {
        this.uMcObject = uMcObject;
    }

    @JsonProperty("contact_type")
    public String getContactType() {
        return contactType;
    }

    @JsonProperty("contact_type")
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    @JsonProperty("incident_state")
    public String getIncidentState() {
        return incidentState;
    }

    @JsonProperty("incident_state")
    public void setIncidentState(String incidentState) {
        this.incidentState = incidentState;
    }

    @JsonProperty("u_southern")
    public String getuSouthern() {
        return uSouthern;
    }

    @JsonProperty("u_southern")
    public void setuSouthern(String uSouthern) {
        this.uSouthern = uSouthern;
    }

    @JsonProperty("problem_id")
    public String getProblemId() {
        return problemId;
    }

    @JsonProperty("problem_id")
    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    @JsonProperty("u_seattle")
    public String getuSeattle() {
        return uSeattle;
    }

    @JsonProperty("u_seattle")
    public void setuSeattle(String uSeattle) {
        this.uSeattle = uSeattle;
    }

    @JsonProperty("u_problem_created_at")
    public String getuProblemCreatedAt() {
        return uProblemCreatedAt;
    }

    @JsonProperty("u_problem_created_at")
    public void setuProblemCreatedAt(String uProblemCreatedAt) {
        this.uProblemCreatedAt = uProblemCreatedAt;
    }

    @JsonProperty("activity_due")
    public String getActivityDue() {
        return activityDue;
    }

    @JsonProperty("activity_due")
    public void setActivityDue(String activityDue) {
        this.activityDue = activityDue;
    }

    @JsonProperty("u_problem_related_by")
    public String getuProblemRelatedBy() {
        return uProblemRelatedBy;
    }

    @JsonProperty("u_problem_related_by")
    public void setuProblemRelatedBy(String uProblemRelatedBy) {
        this.uProblemRelatedBy = uProblemRelatedBy;
    }

    @JsonProperty("comments")
    public String getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(String comments) {
        this.comments = comments;
    }

    @JsonProperty("u_shaws")
    public String getuShaws() {
        return uShaws;
    }

    @JsonProperty("u_shaws")
    public void setuShaws(String uShaws) {
        this.uShaws = uShaws;
    }

    @JsonProperty("u_contact_phone")
    public String getuContactPhone() {
        return uContactPhone;
    }

    @JsonProperty("u_contact_phone")
    public void setuContactPhone(String uContactPhone) {
        this.uContactPhone = uContactPhone;
    }

    @JsonProperty("u_mc_object_uri_link")
    public String getuMcObjectUriLink() {
        return uMcObjectUriLink;
    }

    @JsonProperty("u_mc_object_uri_link")
    public void setuMcObjectUriLink(String uMcObjectUriLink) {
        this.uMcObjectUriLink = uMcObjectUriLink;
    }

    @JsonProperty("due_date")
    public String getDueDate() {
        return dueDate;
    }

    @JsonProperty("due_date")
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @JsonProperty("sys_mod_count")
    public String getSysModCount() {
        return sysModCount;
    }

    @JsonProperty("sys_mod_count")
    public void setSysModCount(String sysModCount) {
        this.sysModCount = sysModCount;
    }

    @JsonProperty("u_ncpdp_rejects")
    public String getuNcpdpRejects() {
        return uNcpdpRejects;
    }

    @JsonProperty("u_ncpdp_rejects")
    public void setuNcpdpRejects(String uNcpdpRejects) {
        this.uNcpdpRejects = uNcpdpRejects;
    }

    @JsonProperty("u_verizon_customer_name")
    public String getuVerizonCustomerName() {
        return uVerizonCustomerName;
    }

    @JsonProperty("u_verizon_customer_name")
    public void setuVerizonCustomerName(String uVerizonCustomerName) {
        this.uVerizonCustomerName = uVerizonCustomerName;
    }

    @JsonProperty("u_other_specify")
    public String getuOtherSpecify() {
        return uOtherSpecify;
    }

    @JsonProperty("u_other_specify")
    public void setuOtherSpecify(String uOtherSpecify) {
        this.uOtherSpecify = uOtherSpecify;
    }

    @JsonProperty("u_mc_modhist")
    public String getuMcModhist() {
        return uMcModhist;
    }

    @JsonProperty("u_mc_modhist")
    public void setuMcModhist(String uMcModhist) {
        this.uMcModhist = uMcModhist;
    }

    @JsonProperty("x_avne2_bmc_to_sno_source_modified")
    public String getxAvne2BmcToSnoSourceModified() {
        return xAvne2BmcToSnoSourceModified;
    }

    @JsonProperty("x_avne2_bmc_to_sno_source_modified")
    public void setxAvne2BmcToSnoSourceModified(String xAvne2BmcToSnoSourceModified) {
        this.xAvne2BmcToSnoSourceModified = xAvne2BmcToSnoSourceModified;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("u_it_shared_services")
    public String getuItSharedServices() {
        return uItSharedServices;
    }

    @JsonProperty("u_it_shared_services")
    public void setuItSharedServices(String uItSharedServices) {
        this.uItSharedServices = uItSharedServices;
    }

    @JsonProperty("u_turnover_review")
    public String getuTurnoverReview() {
        return uTurnoverReview;
    }

    @JsonProperty("u_turnover_review")
    public void setuTurnoverReview(String uTurnoverReview) {
        this.uTurnoverReview = uTurnoverReview;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ResultTableInc.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parent");
        sb.append('=');
        sb.append(((this.parent == null)?"<null>":this.parent));
        sb.append(',');
        sb.append("uReassignmentReason");
        sb.append('=');
        sb.append(((this.uReassignmentReason == null)?"<null>":this.uReassignmentReason));
        sb.append(',');
        sb.append("causedBy");
        sb.append('=');
        sb.append(((this.causedBy == null)?"<null>":this.causedBy));
        sb.append(',');
        sb.append("watchList");
        sb.append('=');
        sb.append(((this.watchList == null)?"<null>":this.watchList));
        sb.append(',');
        sb.append("uBmcpsAppTier");
        sb.append('=');
        sb.append(((this.uBmcpsAppTier == null)?"<null>":this.uBmcpsAppTier));
        sb.append(',');
        sb.append("sysUpdatedOn");
        sb.append('=');
        sb.append(((this.sysUpdatedOn == null)?"<null>":this.sysUpdatedOn));
        sb.append(',');
        sb.append("uVerizonCustomerLocation");
        sb.append('=');
        sb.append(((this.uVerizonCustomerLocation == null)?"<null>":this.uVerizonCustomerLocation));
        sb.append(',');
        sb.append("skills");
        sb.append('=');
        sb.append(((this.skills == null)?"<null>":this.skills));
        sb.append(',');
        sb.append("uPoncaCity");
        sb.append('=');
        sb.append(((this.uPoncaCity == null)?"<null>":this.uPoncaCity));
        sb.append(',');
        sb.append("uCancellationReason");
        sb.append('=');
        sb.append(((this.uCancellationReason == null)?"<null>":this.uCancellationReason));
        sb.append(',');
        sb.append("uSundriesBoise");
        sb.append('=');
        sb.append(((this.uSundriesBoise == null)?"<null>":this.uSundriesBoise));
        sb.append(',');
        sb.append("lessonsLearned");
        sb.append('=');
        sb.append(((this.lessonsLearned == null)?"<null>":this.lessonsLearned));
        sb.append(',');
        sb.append("uKbRelateXml");
        sb.append('=');
        sb.append(((this.uKbRelateXml == null)?"<null>":this.uKbRelateXml));
        sb.append(',');
        sb.append("uMcParameter");
        sb.append('=');
        sb.append(((this.uMcParameter == null)?"<null>":this.uMcParameter));
        sb.append(',');
        sb.append("uParameter");
        sb.append('=');
        sb.append(((this.uParameter == null)?"<null>":this.uParameter));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null)?"<null>":this.state));
        sb.append(',');
        sb.append("uPortland");
        sb.append('=');
        sb.append(((this.uPortland == null)?"<null>":this.uPortland));
        sb.append(',');
        sb.append("uSouthwestLasVegas");
        sb.append('=');
        sb.append(((this.uSouthwestLasVegas == null)?"<null>":this.uSouthwestLasVegas));
        sb.append(',');
        sb.append("sysCreatedBy");
        sb.append('=');
        sb.append(((this.sysCreatedBy == null)?"<null>":this.sysCreatedBy));
        sb.append(',');
        sb.append("knowledge");
        sb.append('=');
        sb.append(((this.knowledge == null)?"<null>":this.knowledge));
        sb.append(',');
        sb.append("impact");
        sb.append('=');
        sb.append(((this.impact == null)?"<null>":this.impact));
        sb.append(',');
        sb.append("active");
        sb.append('=');
        sb.append(((this.active == null)?"<null>":this.active));
        sb.append(',');
        sb.append("uVendor");
        sb.append('=');
        sb.append(((this.uVendor == null)?"<null>":this.uVendor));
        sb.append(',');
        sb.append("groupList");
        sb.append('=');
        sb.append(((this.groupList == null)?"<null>":this.groupList));
        sb.append(',');
        sb.append("uPharmacy");
        sb.append('=');
        sb.append(((this.uPharmacy == null)?"<null>":this.uPharmacy));
        sb.append(',');
        sb.append("majorIncidentState");
        sb.append('=');
        sb.append(((this.majorIncidentState == null)?"<null>":this.majorIncidentState));
        sb.append(',');
        sb.append("uDenver");
        sb.append('=');
        sb.append(((this.uDenver == null)?"<null>":this.uDenver));
        sb.append(',');
        sb.append("correlationDisplay");
        sb.append('=');
        sb.append(((this.correlationDisplay == null)?"<null>":this.correlationDisplay));
        sb.append(',');
        sb.append("uCallerName");
        sb.append('=');
        sb.append(((this.uCallerName == null)?"<null>":this.uCallerName));
        sb.append(',');
        sb.append("vzReleaseForTest");
        sb.append('=');
        sb.append(((this.vzReleaseForTest == null)?"<null>":this.vzReleaseForTest));
        sb.append(',');
        sb.append("followUp");
        sb.append('=');
        sb.append(((this.followUp == null)?"<null>":this.followUp));
        sb.append(',');
        sb.append("parentIncident");
        sb.append('=');
        sb.append(((this.parentIncident == null)?"<null>":this.parentIncident));
        sb.append(',');
        sb.append("reopenedBy");
        sb.append('=');
        sb.append(((this.reopenedBy == null)?"<null>":this.reopenedBy));
        sb.append(',');
        sb.append("uReporting");
        sb.append('=');
        sb.append(((this.uReporting == null)?"<null>":this.uReporting));
        sb.append(',');
        sb.append("uTargetDate");
        sb.append('=');
        sb.append(((this.uTargetDate == null)?"<null>":this.uTargetDate));
        sb.append(',');
        sb.append("uRecycleRequired");
        sb.append('=');
        sb.append(((this.uRecycleRequired == null)?"<null>":this.uRecycleRequired));
        sb.append(',');
        sb.append("uSiteZone");
        sb.append('=');
        sb.append(((this.uSiteZone == null)?"<null>":this.uSiteZone));
        sb.append(',');
        sb.append("uImpactedBusinessDepartment");
        sb.append('=');
        sb.append(((this.uImpactedBusinessDepartment == null)?"<null>":this.uImpactedBusinessDepartment));
        sb.append(',');
        sb.append("escalation");
        sb.append('=');
        sb.append(((this.escalation == null)?"<null>":this.escalation));
        sb.append(',');
        sb.append("uSocal");
        sb.append('=');
        sb.append(((this.uSocal == null)?"<null>":this.uSocal));
        sb.append(',');
        sb.append("correlationId");
        sb.append('=');
        sb.append(((this.correlationId == null)?"<null>":this.correlationId));
        sb.append(',');
        sb.append("timeline");
        sb.append('=');
        sb.append(((this.timeline == null)?"<null>":this.timeline));
        sb.append(',');
        sb.append("uVerizonProductType");
        sb.append('=');
        sb.append(((this.uVerizonProductType == null)?"<null>":this.uVerizonProductType));
        sb.append(',');
        sb.append("uConversionActivity");
        sb.append('=');
        sb.append(((this.uConversionActivity == null)?"<null>":this.uConversionActivity));
        sb.append(',');
        sb.append("uRelatedHrCase");
        sb.append('=');
        sb.append(((this.uRelatedHrCase == null)?"<null>":this.uRelatedHrCase));
        sb.append(',');
        sb.append("uMergerActivity");
        sb.append('=');
        sb.append(((this.uMergerActivity == null)?"<null>":this.uMergerActivity));
        sb.append(',');
        sb.append("uChange");
        sb.append('=');
        sb.append(((this.uChange == null)?"<null>":this.uChange));
        sb.append(',');
        sb.append("uVendorHardwareAsset");
        sb.append('=');
        sb.append(((this.uVendorHardwareAsset == null)?"<null>":this.uVendorHardwareAsset));
        sb.append(',');
        sb.append("userInput");
        sb.append('=');
        sb.append(((this.userInput == null)?"<null>":this.userInput));
        sb.append(',');
        sb.append("sysCreatedOn");
        sb.append('=');
        sb.append(((this.sysCreatedOn == null)?"<null>":this.sysCreatedOn));
        sb.append(',');
        sb.append("routeReason");
        sb.append('=');
        sb.append(((this.routeReason == null)?"<null>":this.routeReason));
        sb.append(',');
        sb.append("uSupportLevel");
        sb.append('=');
        sb.append(((this.uSupportLevel == null)?"<null>":this.uSupportLevel));
        sb.append(',');
        sb.append("calendarStc");
        sb.append('=');
        sb.append(((this.calendarStc == null)?"<null>":this.calendarStc));
        sb.append(',');
        sb.append("closedAt");
        sb.append('=');
        sb.append(((this.closedAt == null)?"<null>":this.closedAt));
        sb.append(',');
        sb.append("uJewel");
        sb.append('=');
        sb.append(((this.uJewel == null)?"<null>":this.uJewel));
        sb.append(',');
        sb.append("uSoft");
        sb.append('=');
        sb.append(((this.uSoft == null)?"<null>":this.uSoft));
        sb.append(',');
        sb.append("uConsolidatedEventData");
        sb.append('=');
        sb.append(((this.uConsolidatedEventData == null)?"<null>":this.uConsolidatedEventData));
        sb.append(',');
        sb.append("uFirstNotifiedAt");
        sb.append('=');
        sb.append(((this.uFirstNotifiedAt == null)?"<null>":this.uFirstNotifiedAt));
        sb.append(',');
        sb.append("uLastOutageAt");
        sb.append('=');
        sb.append(((this.uLastOutageAt == null)?"<null>":this.uLastOutageAt));
        sb.append(',');
        sb.append("uMcLongMsg");
        sb.append('=');
        sb.append(((this.uMcLongMsg == null)?"<null>":this.uMcLongMsg));
        sb.append(',');
        sb.append("uAlertSource");
        sb.append('=');
        sb.append(((this.uAlertSource == null)?"<null>":this.uAlertSource));
        sb.append(',');
        sb.append("uVendorTaskStatus");
        sb.append('=');
        sb.append(((this.uVendorTaskStatus == null)?"<null>":this.uVendorTaskStatus));
        sb.append(',');
        sb.append("businessImpact");
        sb.append('=');
        sb.append(((this.businessImpact == null)?"<null>":this.businessImpact));
        sb.append(',');
        sb.append("uSourceRef");
        sb.append('=');
        sb.append(((this.uSourceRef == null)?"<null>":this.uSourceRef));
        sb.append(',');
        sb.append("uTestField");
        sb.append('=');
        sb.append(((this.uTestField == null)?"<null>":this.uTestField));
        sb.append(',');
        sb.append("rfc");
        sb.append('=');
        sb.append(((this.rfc == null)?"<null>":this.rfc));
        sb.append(',');
        sb.append("timeWorked");
        sb.append('=');
        sb.append(((this.timeWorked == null)?"<null>":this.timeWorked));
        sb.append(',');
        sb.append("uEventHandle");
        sb.append('=');
        sb.append(((this.uEventHandle == null)?"<null>":this.uEventHandle));
        sb.append(',');
        sb.append("uVerizonSeverity");
        sb.append('=');
        sb.append(((this.uVerizonSeverity == null)?"<null>":this.uVerizonSeverity));
        sb.append(',');
        sb.append("workEnd");
        sb.append('=');
        sb.append(((this.workEnd == null)?"<null>":this.workEnd));
        sb.append(',');
        sb.append("uRelatedChatQueue");
        sb.append('=');
        sb.append(((this.uRelatedChatQueue == null)?"<null>":this.uRelatedChatQueue));
        sb.append(',');
        sb.append("subcategory");
        sb.append('=');
        sb.append(((this.subcategory == null)?"<null>":this.subcategory));
        sb.append(',');
        sb.append("uCurrentDate");
        sb.append('=');
        sb.append(((this.uCurrentDate == null)?"<null>":this.uCurrentDate));
        sb.append(',');
        sb.append("workNotes");
        sb.append('=');
        sb.append(((this.workNotes == null)?"<null>":this.workNotes));
        sb.append(',');
        sb.append("closeCode");
        sb.append('=');
        sb.append(((this.closeCode == null)?"<null>":this.closeCode));
        sb.append(',');
        sb.append("assignmentGroup");
        sb.append('=');
        sb.append(((this.assignmentGroup == null)?"<null>":this.assignmentGroup));
        sb.append(',');
        sb.append("businessStc");
        sb.append('=');
        sb.append(((this.businessStc == null)?"<null>":this.businessStc));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("uPauseDateTime");
        sb.append('=');
        sb.append(((this.uPauseDateTime == null)?"<null>":this.uPauseDateTime));
        sb.append(',');
        sb.append("sysId");
        sb.append('=');
        sb.append(((this.sysId == null)?"<null>":this.sysId));
        sb.append(',');
        sb.append("uAdditionalTracksAffected");
        sb.append('=');
        sb.append(((this.uAdditionalTracksAffected == null)?"<null>":this.uAdditionalTracksAffected));
        sb.append(',');
        sb.append("uFirstOutageAt");
        sb.append('=');
        sb.append(((this.uFirstOutageAt == null)?"<null>":this.uFirstOutageAt));
        sb.append(',');
        sb.append("uMcUeid");
        sb.append('=');
        sb.append(((this.uMcUeid == null)?"<null>":this.uMcUeid));
        sb.append(',');
        sb.append("uBmcpsMessage");
        sb.append('=');
        sb.append(((this.uBmcpsMessage == null)?"<null>":this.uBmcpsMessage));
        sb.append(',');
        sb.append("urgency");
        sb.append('=');
        sb.append(((this.urgency == null)?"<null>":this.urgency));
        sb.append(',');
        sb.append("company");
        sb.append('=');
        sb.append(((this.company == null)?"<null>":this.company));
        sb.append(',');
        sb.append("severity");
        sb.append('=');
        sb.append(((this.severity == null)?"<null>":this.severity));
        sb.append(',');
        sb.append("overview");
        sb.append('=');
        sb.append(((this.overview == null)?"<null>":this.overview));
        sb.append(',');
        sb.append("uReportedCi");
        sb.append('=');
        sb.append(((this.uReportedCi == null)?"<null>":this.uReportedCi));
        sb.append(',');
        sb.append("uSourceUid");
        sb.append('=');
        sb.append(((this.uSourceUid == null)?"<null>":this.uSourceUid));
        sb.append(',');
        sb.append("approval");
        sb.append('=');
        sb.append(((this.approval == null)?"<null>":this.approval));
        sb.append(',');
        sb.append("uMcObjectUri");
        sb.append('=');
        sb.append(((this.uMcObjectUri == null)?"<null>":this.uMcObjectUri));
        sb.append(',');
        sb.append("reopenCount");
        sb.append('=');
        sb.append(((this.reopenCount == null)?"<null>":this.reopenCount));
        sb.append(',');
        sb.append("uDevQaEnvironment");
        sb.append('=');
        sb.append(((this.uDevQaEnvironment == null)?"<null>":this.uDevQaEnvironment));
        sb.append(',');
        sb.append("uHouston");
        sb.append('=');
        sb.append(((this.uHouston == null)?"<null>":this.uHouston));
        sb.append(',');
        sb.append("sysTags");
        sb.append('=');
        sb.append(((this.sysTags == null)?"<null>":this.sysTags));
        sb.append(',');
        sb.append("uKnowledgeArticle");
        sb.append('=');
        sb.append(((this.uKnowledgeArticle == null)?"<null>":this.uKnowledgeArticle));
        sb.append(',');
        sb.append("location");
        sb.append('=');
        sb.append(((this.location == null)?"<null>":this.location));
        sb.append(',');
        sb.append("promotedBy");
        sb.append('=');
        sb.append(((this.promotedBy == null)?"<null>":this.promotedBy));
        sb.append(',');
        sb.append("xAvne2BmcToSnoSourceUid");
        sb.append('=');
        sb.append(((this.xAvne2BmcToSnoSourceUid == null)?"<null>":this.xAvne2BmcToSnoSourceUid));
        sb.append(',');
        sb.append("uSupplyChain");
        sb.append('=');
        sb.append(((this.uSupplyChain == null)?"<null>":this.uSupplyChain));
        sb.append(',');
        sb.append("uHpi");
        sb.append('=');
        sb.append(((this.uHpi == null)?"<null>":this.uHpi));
        sb.append(',');
        sb.append("uponReject");
        sb.append('=');
        sb.append(((this.uponReject == null)?"<null>":this.uponReject));
        sb.append(',');
        sb.append("uBestCallback");
        sb.append('=');
        sb.append(((this.uBestCallback == null)?"<null>":this.uBestCallback));
        sb.append(',');
        sb.append("uMcObjectClass");
        sb.append('=');
        sb.append(((this.uMcObjectClass == null)?"<null>":this.uMcObjectClass));
        sb.append(',');
        sb.append("approvalHistory");
        sb.append('=');
        sb.append(((this.approvalHistory == null)?"<null>":this.approvalHistory));
        sb.append(',');
        sb.append("number");
        sb.append('=');
        sb.append(((this.number == null)?"<null>":this.number));
        sb.append(',');
        sb.append("proposedBy");
        sb.append('=');
        sb.append(((this.proposedBy == null)?"<null>":this.proposedBy));
        sb.append(',');
        sb.append("uInstance");
        sb.append('=');
        sb.append(((this.uInstance == null)?"<null>":this.uInstance));
        sb.append(',');
        sb.append("uAdapterHost");
        sb.append('=');
        sb.append(((this.uAdapterHost == null)?"<null>":this.uAdapterHost));
        sb.append(',');
        sb.append("order");
        sb.append('=');
        sb.append(((this.order == null)?"<null>":this.order));
        sb.append(',');
        sb.append("uHr");
        sb.append('=');
        sb.append(((this.uHr == null)?"<null>":this.uHr));
        sb.append(',');
        sb.append("uMissingCategory");
        sb.append('=');
        sb.append(((this.uMissingCategory == null)?"<null>":this.uMissingCategory));
        sb.append(',');
        sb.append("cmdbCi");
        sb.append('=');
        sb.append(((this.cmdbCi == null)?"<null>":this.cmdbCi));
        sb.append(',');
        sb.append("uTrackName");
        sb.append('=');
        sb.append(((this.uTrackName == null)?"<null>":this.uTrackName));
        sb.append(',');
        sb.append("uVendorTypeModel");
        sb.append('=');
        sb.append(((this.uVendorTypeModel == null)?"<null>":this.uVendorTypeModel));
        sb.append(',');
        sb.append("priority");
        sb.append('=');
        sb.append(((this.priority == null)?"<null>":this.priority));
        sb.append(',');
        sb.append("uAllDivisions");
        sb.append('=');
        sb.append(((this.uAllDivisions == null)?"<null>":this.uAllDivisions));
        sb.append(',');
        sb.append("uReasonForSlaException");
        sb.append('=');
        sb.append(((this.uReasonForSlaException == null)?"<null>":this.uReasonForSlaException));
        sb.append(',');
        sb.append("businessDuration");
        sb.append('=');
        sb.append(((this.businessDuration == null)?"<null>":this.businessDuration));
        sb.append(',');
        sb.append("approvalSet");
        sb.append('=');
        sb.append(((this.approvalSet == null)?"<null>":this.approvalSet));
        sb.append(',');
        sb.append("uVendorSent");
        sb.append('=');
        sb.append(((this.uVendorSent == null)?"<null>":this.uVendorSent));
        sb.append(',');
        sb.append("universalRequest");
        sb.append('=');
        sb.append(((this.universalRequest == null)?"<null>":this.universalRequest));
        sb.append(',');
        sb.append("shortDescription");
        sb.append('=');
        sb.append(((this.shortDescription == null)?"<null>":this.shortDescription));
        sb.append(',');
        sb.append("uCircuitId");
        sb.append('=');
        sb.append(((this.uCircuitId == null)?"<null>":this.uCircuitId));
        sb.append(',');
        sb.append("workStart");
        sb.append('=');
        sb.append(((this.workStart == null)?"<null>":this.workStart));
        sb.append(',');
        sb.append("uIntermountain");
        sb.append('=');
        sb.append(((this.uIntermountain == null)?"<null>":this.uIntermountain));
        sb.append(',');
        sb.append("uAlertRef");
        sb.append('=');
        sb.append(((this.uAlertRef == null)?"<null>":this.uAlertRef));
        sb.append(',');
        sb.append("additionalAssigneeList");
        sb.append('=');
        sb.append(((this.additionalAssigneeList == null)?"<null>":this.additionalAssigneeList));
        sb.append(',');
        sb.append("uVendorStatus");
        sb.append('=');
        sb.append(((this.uVendorStatus == null)?"<null>":this.uVendorStatus));
        sb.append(',');
        sb.append("notify");
        sb.append('=');
        sb.append(((this.notify == null)?"<null>":this.notify));
        sb.append(',');
        sb.append("sysClassName");
        sb.append('=');
        sb.append(((this.sysClassName == null)?"<null>":this.sysClassName));
        sb.append(',');
        sb.append("closedBy");
        sb.append('=');
        sb.append(((this.closedBy == null)?"<null>":this.closedBy));
        sb.append(',');
        sb.append("uRelatedCall");
        sb.append('=');
        sb.append(((this.uRelatedCall == null)?"<null>":this.uRelatedCall));
        sb.append(',');
        sb.append("uVendorSerialNumber");
        sb.append('=');
        sb.append(((this.uVendorSerialNumber == null)?"<null>":this.uVendorSerialNumber));
        sb.append(',');
        sb.append("reassignmentCount");
        sb.append('=');
        sb.append(((this.reassignmentCount == null)?"<null>":this.reassignmentCount));
        sb.append(',');
        sb.append("uBusinessThirdParty");
        sb.append('=');
        sb.append(((this.uBusinessThirdParty == null)?"<null>":this.uBusinessThirdParty));
        sb.append(',');
        sb.append("assignedTo");
        sb.append('=');
        sb.append(((this.assignedTo == null)?"<null>":this.assignedTo));
        sb.append(',');
        sb.append("uAssignedTo");
        sb.append('=');
        sb.append(((this.uAssignedTo == null)?"<null>":this.uAssignedTo));
        sb.append(',');
        sb.append("uBreachTime");
        sb.append('=');
        sb.append(((this.uBreachTime == null)?"<null>":this.uBreachTime));
        sb.append(',');
        sb.append("slaDue");
        sb.append('=');
        sb.append(((this.slaDue == null)?"<null>":this.slaDue));
        sb.append(',');
        sb.append("uBmcpsDedupId");
        sb.append('=');
        sb.append(((this.uBmcpsDedupId == null)?"<null>":this.uBmcpsDedupId));
        sb.append(',');
        sb.append("uponApproval");
        sb.append('=');
        sb.append(((this.uponApproval == null)?"<null>":this.uponApproval));
        sb.append(',');
        sb.append("uResponseSla");
        sb.append('=');
        sb.append(((this.uResponseSla == null)?"<null>":this.uResponseSla));
        sb.append(',');
        sb.append("uRestorationManager");
        sb.append('=');
        sb.append(((this.uRestorationManager == null)?"<null>":this.uRestorationManager));
        sb.append(',');
        sb.append("uDataMigration");
        sb.append('=');
        sb.append(((this.uDataMigration == null)?"<null>":this.uDataMigration));
        sb.append(',');
        sb.append("uFirstIncidentAt");
        sb.append('=');
        sb.append(((this.uFirstIncidentAt == null)?"<null>":this.uFirstIncidentAt));
        sb.append(',');
        sb.append("promotedOn");
        sb.append('=');
        sb.append(((this.promotedOn == null)?"<null>":this.promotedOn));
        sb.append(',');
        sb.append("childIncidents");
        sb.append('=');
        sb.append(((this.childIncidents == null)?"<null>":this.childIncidents));
        sb.append(',');
        sb.append("taskEffectiveNumber");
        sb.append('=');
        sb.append(((this.taskEffectiveNumber == null)?"<null>":this.taskEffectiveNumber));
        sb.append(',');
        sb.append("resolvedBy");
        sb.append('=');
        sb.append(((this.resolvedBy == null)?"<null>":this.resolvedBy));
        sb.append(',');
        sb.append("sysUpdatedBy");
        sb.append('=');
        sb.append(((this.sysUpdatedBy == null)?"<null>":this.sysUpdatedBy));
        sb.append(',');
        sb.append("openedBy");
        sb.append('=');
        sb.append(((this.openedBy == null)?"<null>":this.openedBy));
        sb.append(',');
        sb.append("sysDomain");
        sb.append('=');
        sb.append(((this.sysDomain == null)?"<null>":this.sysDomain));
        sb.append(',');
        sb.append("proposedOn");
        sb.append('=');
        sb.append(((this.proposedOn == null)?"<null>":this.proposedOn));
        sb.append(',');
        sb.append("uCiDetails");
        sb.append('=');
        sb.append(((this.uCiDetails == null)?"<null>":this.uCiDetails));
        sb.append(',');
        sb.append("uMm");
        sb.append('=');
        sb.append(((this.uMm == null)?"<null>":this.uMm));
        sb.append(',');
        sb.append("uSoftware");
        sb.append('=');
        sb.append(((this.uSoftware == null)?"<null>":this.uSoftware));
        sb.append(',');
        sb.append("businessService");
        sb.append('=');
        sb.append(((this.businessService == null)?"<null>":this.businessService));
        sb.append(',');
        sb.append("uRetail");
        sb.append('=');
        sb.append(((this.uRetail == null)?"<null>":this.uRetail));
        sb.append(',');
        sb.append("uSymptom");
        sb.append('=');
        sb.append(((this.uSymptom == null)?"<null>":this.uSymptom));
        sb.append(',');
        sb.append("expectedStart");
        sb.append('=');
        sb.append(((this.expectedStart == null)?"<null>":this.expectedStart));
        sb.append(',');
        sb.append("openedAt");
        sb.append('=');
        sb.append(((this.openedAt == null)?"<null>":this.openedAt));
        sb.append(',');
        sb.append("uImpactedBusinessArea");
        sb.append('=');
        sb.append(((this.uImpactedBusinessArea == null)?"<null>":this.uImpactedBusinessArea));
        sb.append(',');
        sb.append("uAccounting");
        sb.append('=');
        sb.append(((this.uAccounting == null)?"<null>":this.uAccounting));
        sb.append(',');
        sb.append("vzReleaseForTestDatetime");
        sb.append('=');
        sb.append(((this.vzReleaseForTestDatetime == null)?"<null>":this.vzReleaseForTestDatetime));
        sb.append(',');
        sb.append("uAwaitingReason");
        sb.append('=');
        sb.append(((this.uAwaitingReason == null)?"<null>":this.uAwaitingReason));
        sb.append(',');
        sb.append("uCausedByStory");
        sb.append('=');
        sb.append(((this.uCausedByStory == null)?"<null>":this.uCausedByStory));
        sb.append(',');
        sb.append("callerId");
        sb.append('=');
        sb.append(((this.callerId == null)?"<null>":this.callerId));
        sb.append(',');
        sb.append("reopenedTime");
        sb.append('=');
        sb.append(((this.reopenedTime == null)?"<null>":this.reopenedTime));
        sb.append(',');
        sb.append("resolvedAt");
        sb.append('=');
        sb.append(((this.resolvedAt == null)?"<null>":this.resolvedAt));
        sb.append(',');
        sb.append("uProblem");
        sb.append('=');
        sb.append(((this.uProblem == null)?"<null>":this.uProblem));
        sb.append(',');
        sb.append("uSymptoms");
        sb.append('=');
        sb.append(((this.uSymptoms == null)?"<null>":this.uSymptoms));
        sb.append(',');
        sb.append("uVendorWarrantyExpiration");
        sb.append('=');
        sb.append(((this.uVendorWarrantyExpiration == null)?"<null>":this.uVendorWarrantyExpiration));
        sb.append(',');
        sb.append("xAvne2BmcToSnoSourceRef");
        sb.append('=');
        sb.append(((this.xAvne2BmcToSnoSourceRef == null)?"<null>":this.xAvne2BmcToSnoSourceRef));
        sb.append(',');
        sb.append("cause");
        sb.append('=');
        sb.append(((this.cause == null)?"<null>":this.cause));
        sb.append(',');
        sb.append("uVendorTicket");
        sb.append('=');
        sb.append(((this.uVendorTicket == null)?"<null>":this.uVendorTicket));
        sb.append(',');
        sb.append("calendarDuration");
        sb.append('=');
        sb.append(((this.calendarDuration == null)?"<null>":this.calendarDuration));
        sb.append(',');
        sb.append("uAcme");
        sb.append('=');
        sb.append(((this.uAcme == null)?"<null>":this.uAcme));
        sb.append(',');
        sb.append("uTurnoverCategory");
        sb.append('=');
        sb.append(((this.uTurnoverCategory == null)?"<null>":this.uTurnoverCategory));
        sb.append(',');
        sb.append("closeNotes");
        sb.append('=');
        sb.append(((this.closeNotes == null)?"<null>":this.closeNotes));
        sb.append(',');
        sb.append("uMcObject");
        sb.append('=');
        sb.append(((this.uMcObject == null)?"<null>":this.uMcObject));
        sb.append(',');
        sb.append("contactType");
        sb.append('=');
        sb.append(((this.contactType == null)?"<null>":this.contactType));
        sb.append(',');
        sb.append("incidentState");
        sb.append('=');
        sb.append(((this.incidentState == null)?"<null>":this.incidentState));
        sb.append(',');
        sb.append("uSouthern");
        sb.append('=');
        sb.append(((this.uSouthern == null)?"<null>":this.uSouthern));
        sb.append(',');
        sb.append("problemId");
        sb.append('=');
        sb.append(((this.problemId == null)?"<null>":this.problemId));
        sb.append(',');
        sb.append("uSeattle");
        sb.append('=');
        sb.append(((this.uSeattle == null)?"<null>":this.uSeattle));
        sb.append(',');
        sb.append("uProblemCreatedAt");
        sb.append('=');
        sb.append(((this.uProblemCreatedAt == null)?"<null>":this.uProblemCreatedAt));
        sb.append(',');
        sb.append("activityDue");
        sb.append('=');
        sb.append(((this.activityDue == null)?"<null>":this.activityDue));
        sb.append(',');
        sb.append("uProblemRelatedBy");
        sb.append('=');
        sb.append(((this.uProblemRelatedBy == null)?"<null>":this.uProblemRelatedBy));
        sb.append(',');
        sb.append("comments");
        sb.append('=');
        sb.append(((this.comments == null)?"<null>":this.comments));
        sb.append(',');
        sb.append("uShaws");
        sb.append('=');
        sb.append(((this.uShaws == null)?"<null>":this.uShaws));
        sb.append(',');
        sb.append("uContactPhone");
        sb.append('=');
        sb.append(((this.uContactPhone == null)?"<null>":this.uContactPhone));
        sb.append(',');
        sb.append("uMcObjectUriLink");
        sb.append('=');
        sb.append(((this.uMcObjectUriLink == null)?"<null>":this.uMcObjectUriLink));
        sb.append(',');
        sb.append("dueDate");
        sb.append('=');
        sb.append(((this.dueDate == null)?"<null>":this.dueDate));
        sb.append(',');
        sb.append("sysModCount");
        sb.append('=');
        sb.append(((this.sysModCount == null)?"<null>":this.sysModCount));
        sb.append(',');
        sb.append("uNcpdpRejects");
        sb.append('=');
        sb.append(((this.uNcpdpRejects == null)?"<null>":this.uNcpdpRejects));
        sb.append(',');
        sb.append("uVerizonCustomerName");
        sb.append('=');
        sb.append(((this.uVerizonCustomerName == null)?"<null>":this.uVerizonCustomerName));
        sb.append(',');
        sb.append("uOtherSpecify");
        sb.append('=');
        sb.append(((this.uOtherSpecify == null)?"<null>":this.uOtherSpecify));
        sb.append(',');
        sb.append("uMcModhist");
        sb.append('=');
        sb.append(((this.uMcModhist == null)?"<null>":this.uMcModhist));
        sb.append(',');
        sb.append("xAvne2BmcToSnoSourceModified");
        sb.append('=');
        sb.append(((this.xAvne2BmcToSnoSourceModified == null)?"<null>":this.xAvne2BmcToSnoSourceModified));
        sb.append(',');
        sb.append("category");
        sb.append('=');
        sb.append(((this.category == null)?"<null>":this.category));
        sb.append(',');
        sb.append("uItSharedServices");
        sb.append('=');
        sb.append(((this.uItSharedServices == null)?"<null>":this.uItSharedServices));
        sb.append(',');
        sb.append("uTurnoverReview");
        sb.append('=');
        sb.append(((this.uTurnoverReview == null)?"<null>":this.uTurnoverReview));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.sysDomain == null)? 0 :this.sysDomain.hashCode()));
        result = ((result* 31)+((this.proposedOn == null)? 0 :this.proposedOn.hashCode()));
        result = ((result* 31)+((this.parent == null)? 0 :this.parent.hashCode()));
        result = ((result* 31)+((this.parentIncident == null)? 0 :this.parentIncident.hashCode()));
        result = ((result* 31)+((this.incidentState == null)? 0 :this.incidentState.hashCode()));
        result = ((result* 31)+((this.uAwaitingReason == null)? 0 :this.uAwaitingReason.hashCode()));
        result = ((result* 31)+((this.uConsolidatedEventData == null)? 0 :this.uConsolidatedEventData.hashCode()));
        result = ((result* 31)+((this.skills == null)? 0 :this.skills.hashCode()));
        result = ((result* 31)+((this.uSocal == null)? 0 :this.uSocal.hashCode()));
        result = ((result* 31)+((this.sysCreatedBy == null)? 0 :this.sysCreatedBy.hashCode()));
        result = ((result* 31)+((this.uVendorWarrantyExpiration == null)? 0 :this.uVendorWarrantyExpiration.hashCode()));
        result = ((result* 31)+((this.state == null)? 0 :this.state.hashCode()));
        result = ((result* 31)+((this.calendarStc == null)? 0 :this.calendarStc.hashCode()));
        result = ((result* 31)+((this.uTurnoverCategory == null)? 0 :this.uTurnoverCategory.hashCode()));
        result = ((result* 31)+((this.uInstance == null)? 0 :this.uInstance.hashCode()));
        result = ((result* 31)+((this.knowledge == null)? 0 :this.knowledge.hashCode()));
        result = ((result* 31)+((this.uBestCallback == null)? 0 :this.uBestCallback.hashCode()));
        result = ((result* 31)+((this.impact == null)? 0 :this.impact.hashCode()));
        result = ((result* 31)+((this.active == null)? 0 :this.active.hashCode()));
        result = ((result* 31)+((this.uAlertSource == null)? 0 :this.uAlertSource.hashCode()));
        result = ((result* 31)+((this.groupList == null)? 0 :this.groupList.hashCode()));
        result = ((result* 31)+((this.vzReleaseForTest == null)? 0 :this.vzReleaseForTest.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.workNotes == null)? 0 :this.workNotes.hashCode()));
        result = ((result* 31)+((this.xAvne2BmcToSnoSourceUid == null)? 0 :this.xAvne2BmcToSnoSourceUid.hashCode()));
        result = ((result* 31)+((this.uProblemCreatedAt == null)? 0 :this.uProblemCreatedAt.hashCode()));
        result = ((result* 31)+((this.uSourceRef == null)? 0 :this.uSourceRef.hashCode()));
        result = ((result* 31)+((this.sysId == null)? 0 :this.sysId.hashCode()));
        result = ((result* 31)+((this.uAlertRef == null)? 0 :this.uAlertRef.hashCode()));
        result = ((result* 31)+((this.uSiteZone == null)? 0 :this.uSiteZone.hashCode()));
        result = ((result* 31)+((this.expectedStart == null)? 0 :this.expectedStart.hashCode()));
        result = ((result* 31)+((this.uFirstOutageAt == null)? 0 :this.uFirstOutageAt.hashCode()));
        result = ((result* 31)+((this.uDenver == null)? 0 :this.uDenver.hashCode()));
        result = ((result* 31)+((this.uMergerActivity == null)? 0 :this.uMergerActivity.hashCode()));
        result = ((result* 31)+((this.correlationId == null)? 0 :this.correlationId.hashCode()));
        result = ((result* 31)+((this.sysModCount == null)? 0 :this.sysModCount.hashCode()));
        result = ((result* 31)+((this.uHr == null)? 0 :this.uHr.hashCode()));
        result = ((result* 31)+((this.uDevQaEnvironment == null)? 0 :this.uDevQaEnvironment.hashCode()));
        result = ((result* 31)+((this.businessService == null)? 0 :this.businessService.hashCode()));
        result = ((result* 31)+((this.universalRequest == null)? 0 :this.universalRequest.hashCode()));
        result = ((result* 31)+((this.uReassignmentReason == null)? 0 :this.uReassignmentReason.hashCode()));
        result = ((result* 31)+((this.xAvne2BmcToSnoSourceRef == null)? 0 :this.xAvne2BmcToSnoSourceRef.hashCode()));
        result = ((result* 31)+((this.reopenedBy == null)? 0 :this.reopenedBy.hashCode()));
        result = ((result* 31)+((this.escalation == null)? 0 :this.escalation.hashCode()));
        result = ((result* 31)+((this.uBmcpsDedupId == null)? 0 :this.uBmcpsDedupId.hashCode()));
        result = ((result* 31)+((this.timeline == null)? 0 :this.timeline.hashCode()));
        result = ((result* 31)+((this.uCurrentDate == null)? 0 :this.uCurrentDate.hashCode()));
        result = ((result* 31)+((this.uSymptoms == null)? 0 :this.uSymptoms.hashCode()));
        result = ((result* 31)+((this.uSupportLevel == null)? 0 :this.uSupportLevel.hashCode()));
        result = ((result* 31)+((this.uSourceUid == null)? 0 :this.uSourceUid.hashCode()));
        result = ((result* 31)+((this.vzReleaseForTestDatetime == null)? 0 :this.vzReleaseForTestDatetime.hashCode()));
        result = ((result* 31)+((this.dueDate == null)? 0 :this.dueDate.hashCode()));
        result = ((result* 31)+((this.uRelatedChatQueue == null)? 0 :this.uRelatedChatQueue.hashCode()));
        result = ((result* 31)+((this.sysUpdatedOn == null)? 0 :this.sysUpdatedOn.hashCode()));
        result = ((result* 31)+((this.uSupplyChain == null)? 0 :this.uSupplyChain.hashCode()));
        result = ((result* 31)+((this.xAvne2BmcToSnoSourceModified == null)? 0 :this.xAvne2BmcToSnoSourceModified.hashCode()));
        result = ((result* 31)+((this.uVendorSerialNumber == null)? 0 :this.uVendorSerialNumber.hashCode()));
        result = ((result* 31)+((this.uBmcpsMessage == null)? 0 :this.uBmcpsMessage.hashCode()));
        result = ((result* 31)+((this.routeReason == null)? 0 :this.routeReason.hashCode()));
        result = ((result* 31)+((this.uReportedCi == null)? 0 :this.uReportedCi.hashCode()));
        result = ((result* 31)+((this.uRelatedCall == null)? 0 :this.uRelatedCall.hashCode()));
        result = ((result* 31)+((this.uVendorTypeModel == null)? 0 :this.uVendorTypeModel.hashCode()));
        result = ((result* 31)+((this.uSoft == null)? 0 :this.uSoft.hashCode()));
        result = ((result* 31)+((this.approvalSet == null)? 0 :this.approvalSet.hashCode()));
        result = ((result* 31)+((this.reopenedTime == null)? 0 :this.reopenedTime.hashCode()));
        result = ((result* 31)+((this.rfc == null)? 0 :this.rfc.hashCode()));
        result = ((result* 31)+((this.uOtherSpecify == null)? 0 :this.uOtherSpecify.hashCode()));
        result = ((result* 31)+((this.assignmentGroup == null)? 0 :this.assignmentGroup.hashCode()));
        result = ((result* 31)+((this.uLastOutageAt == null)? 0 :this.uLastOutageAt.hashCode()));
        result = ((result* 31)+((this.subcategory == null)? 0 :this.subcategory.hashCode()));
        result = ((result* 31)+((this.uMcObjectClass == null)? 0 :this.uMcObjectClass.hashCode()));
        result = ((result* 31)+((this.uCausedByStory == null)? 0 :this.uCausedByStory.hashCode()));
        result = ((result* 31)+((this.uBreachTime == null)? 0 :this.uBreachTime.hashCode()));
        result = ((result* 31)+((this.uProblem == null)? 0 :this.uProblem.hashCode()));
        result = ((result* 31)+((this.resolvedBy == null)? 0 :this.resolvedBy.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.uSymptom == null)? 0 :this.uSymptom.hashCode()));
        result = ((result* 31)+((this.uReporting == null)? 0 :this.uReporting.hashCode()));
        result = ((result* 31)+((this.uResponseSla == null)? 0 :this.uResponseSla.hashCode()));
        result = ((result* 31)+((this.approvalHistory == null)? 0 :this.approvalHistory.hashCode()));
        result = ((result* 31)+((this.uVerizonCustomerName == null)? 0 :this.uVerizonCustomerName.hashCode()));
        result = ((result* 31)+((this.urgency == null)? 0 :this.urgency.hashCode()));
        result = ((result* 31)+((this.company == null)? 0 :this.company.hashCode()));
        result = ((result* 31)+((this.severity == null)? 0 :this.severity.hashCode()));
        result = ((result* 31)+((this.overview == null)? 0 :this.overview.hashCode()));
        result = ((result* 31)+((this.uCiDetails == null)? 0 :this.uCiDetails.hashCode()));
        result = ((result* 31)+((this.promotedOn == null)? 0 :this.promotedOn.hashCode()));
        result = ((result* 31)+((this.resolvedAt == null)? 0 :this.resolvedAt.hashCode()));
        result = ((result* 31)+((this.uImpactedBusinessDepartment == null)? 0 :this.uImpactedBusinessDepartment.hashCode()));
        result = ((result* 31)+((this.sysTags == null)? 0 :this.sysTags.hashCode()));
        result = ((result* 31)+((this.approval == null)? 0 :this.approval.hashCode()));
        result = ((result* 31)+((this.uSeattle == null)? 0 :this.uSeattle.hashCode()));
        result = ((result* 31)+((this.workEnd == null)? 0 :this.workEnd.hashCode()));
        result = ((result* 31)+((this.uRecycleRequired == null)? 0 :this.uRecycleRequired.hashCode()));
        result = ((result* 31)+((this.uMcObjectUriLink == null)? 0 :this.uMcObjectUriLink.hashCode()));
        result = ((result* 31)+((this.uMm == null)? 0 :this.uMm.hashCode()));
        result = ((result* 31)+((this.uSoftware == null)? 0 :this.uSoftware.hashCode()));
        result = ((result* 31)+((this.location == null)? 0 :this.location.hashCode()));
        result = ((result* 31)+((this.uKbRelateXml == null)? 0 :this.uKbRelateXml.hashCode()));
        result = ((result* 31)+((this.uVendorTaskStatus == null)? 0 :this.uVendorTaskStatus.hashCode()));
        result = ((result* 31)+((this.uVendorStatus == null)? 0 :this.uVendorStatus.hashCode()));
        result = ((result* 31)+((this.uTrackName == null)? 0 :this.uTrackName.hashCode()));
        result = ((result* 31)+((this.uCircuitId == null)? 0 :this.uCircuitId.hashCode()));
        result = ((result* 31)+((this.sysUpdatedBy == null)? 0 :this.sysUpdatedBy.hashCode()));
        result = ((result* 31)+((this.uVendorTicket == null)? 0 :this.uVendorTicket.hashCode()));
        result = ((result* 31)+((this.contactType == null)? 0 :this.contactType.hashCode()));
        result = ((result* 31)+((this.uMcObjectUri == null)? 0 :this.uMcObjectUri.hashCode()));
        result = ((result* 31)+((this.uMcUeid == null)? 0 :this.uMcUeid.hashCode()));
        result = ((result* 31)+((this.number == null)? 0 :this.number.hashCode()));
        result = ((result* 31)+((this.uAllDivisions == null)? 0 :this.uAllDivisions.hashCode()));
        result = ((result* 31)+((this.uRestorationManager == null)? 0 :this.uRestorationManager.hashCode()));
        result = ((result* 31)+((this.childIncidents == null)? 0 :this.childIncidents.hashCode()));
        result = ((result* 31)+((this.uPortland == null)? 0 :this.uPortland.hashCode()));
        result = ((result* 31)+((this.calendarDuration == null)? 0 :this.calendarDuration.hashCode()));
        result = ((result* 31)+((this.uVerizonCustomerLocation == null)? 0 :this.uVerizonCustomerLocation.hashCode()));
        result = ((result* 31)+((this.uVerizonProductType == null)? 0 :this.uVerizonProductType.hashCode()));
        result = ((result* 31)+((this.order == null)? 0 :this.order.hashCode()));
        result = ((result* 31)+((this.uJewel == null)? 0 :this.uJewel.hashCode()));
        result = ((result* 31)+((this.priority == null)? 0 :this.priority.hashCode()));
        result = ((result* 31)+((this.uMcModhist == null)? 0 :this.uMcModhist.hashCode()));
        result = ((result* 31)+((this.closeCode == null)? 0 :this.closeCode.hashCode()));
        result = ((result* 31)+((this.uRetail == null)? 0 :this.uRetail.hashCode()));
        result = ((result* 31)+((this.uProblemRelatedBy == null)? 0 :this.uProblemRelatedBy.hashCode()));
        result = ((result* 31)+((this.uParameter == null)? 0 :this.uParameter.hashCode()));
        result = ((result* 31)+((this.uRelatedHrCase == null)? 0 :this.uRelatedHrCase.hashCode()));
        result = ((result* 31)+((this.uIntermountain == null)? 0 :this.uIntermountain.hashCode()));
        result = ((result* 31)+((this.activityDue == null)? 0 :this.activityDue.hashCode()));
        result = ((result* 31)+((this.uItSharedServices == null)? 0 :this.uItSharedServices.hashCode()));
        result = ((result* 31)+((this.correlationDisplay == null)? 0 :this.correlationDisplay.hashCode()));
        result = ((result* 31)+((this.taskEffectiveNumber == null)? 0 :this.taskEffectiveNumber.hashCode()));
        result = ((result* 31)+((this.uAssignedTo == null)? 0 :this.uAssignedTo.hashCode()));
        result = ((result* 31)+((this.uContactPhone == null)? 0 :this.uContactPhone.hashCode()));
        result = ((result* 31)+((this.uMissingCategory == null)? 0 :this.uMissingCategory.hashCode()));
        result = ((result* 31)+((this.businessStc == null)? 0 :this.businessStc.hashCode()));
        result = ((result* 31)+((this.uponReject == null)? 0 :this.uponReject.hashCode()));
        result = ((result* 31)+((this.timeWorked == null)? 0 :this.timeWorked.hashCode()));
        result = ((result* 31)+((this.additionalAssigneeList == null)? 0 :this.additionalAssigneeList.hashCode()));
        result = ((result* 31)+((this.watchList == null)? 0 :this.watchList.hashCode()));
        result = ((result* 31)+((this.notify == null)? 0 :this.notify.hashCode()));
        result = ((result* 31)+((this.reassignmentCount == null)? 0 :this.reassignmentCount.hashCode()));
        result = ((result* 31)+((this.openedBy == null)? 0 :this.openedBy.hashCode()));
        result = ((result* 31)+((this.uNcpdpRejects == null)? 0 :this.uNcpdpRejects.hashCode()));
        result = ((result* 31)+((this.uDataMigration == null)? 0 :this.uDataMigration.hashCode()));
        result = ((result* 31)+((this.lessonsLearned == null)? 0 :this.lessonsLearned.hashCode()));
        result = ((result* 31)+((this.promotedBy == null)? 0 :this.promotedBy.hashCode()));
        result = ((result* 31)+((this.uSouthern == null)? 0 :this.uSouthern.hashCode()));
        result = ((result* 31)+((this.uPharmacy == null)? 0 :this.uPharmacy.hashCode()));
        result = ((result* 31)+((this.uTargetDate == null)? 0 :this.uTargetDate.hashCode()));
        result = ((result* 31)+((this.uMcObject == null)? 0 :this.uMcObject.hashCode()));
        result = ((result* 31)+((this.uSundriesBoise == null)? 0 :this.uSundriesBoise.hashCode()));
        result = ((result* 31)+((this.sysClassName == null)? 0 :this.sysClassName.hashCode()));
        result = ((result* 31)+((this.businessImpact == null)? 0 :this.businessImpact.hashCode()));
        result = ((result* 31)+((this.uImpactedBusinessArea == null)? 0 :this.uImpactedBusinessArea.hashCode()));
        result = ((result* 31)+((this.uFirstNotifiedAt == null)? 0 :this.uFirstNotifiedAt.hashCode()));
        result = ((result* 31)+((this.uShaws == null)? 0 :this.uShaws.hashCode()));
        result = ((result* 31)+((this.problemId == null)? 0 :this.problemId.hashCode()));
        result = ((result* 31)+((this.reopenCount == null)? 0 :this.reopenCount.hashCode()));
        result = ((result* 31)+((this.closeNotes == null)? 0 :this.closeNotes.hashCode()));
        result = ((result* 31)+((this.uEventHandle == null)? 0 :this.uEventHandle.hashCode()));
        result = ((result* 31)+((this.workStart == null)? 0 :this.workStart.hashCode()));
        result = ((result* 31)+((this.openedAt == null)? 0 :this.openedAt.hashCode()));
        result = ((result* 31)+((this.uVerizonSeverity == null)? 0 :this.uVerizonSeverity.hashCode()));
        result = ((result* 31)+((this.businessDuration == null)? 0 :this.businessDuration.hashCode()));
        result = ((result* 31)+((this.uCancellationReason == null)? 0 :this.uCancellationReason.hashCode()));
        result = ((result* 31)+((this.uAdditionalTracksAffected == null)? 0 :this.uAdditionalTracksAffected.hashCode()));
        result = ((result* 31)+((this.uReasonForSlaException == null)? 0 :this.uReasonForSlaException.hashCode()));
        result = ((result* 31)+((this.uCallerName == null)? 0 :this.uCallerName.hashCode()));
        result = ((result* 31)+((this.sysCreatedOn == null)? 0 :this.sysCreatedOn.hashCode()));
        result = ((result* 31)+((this.uHpi == null)? 0 :this.uHpi.hashCode()));
        result = ((result* 31)+((this.uTurnoverReview == null)? 0 :this.uTurnoverReview.hashCode()));
        result = ((result* 31)+((this.uAdapterHost == null)? 0 :this.uAdapterHost.hashCode()));
        result = ((result* 31)+((this.closedAt == null)? 0 :this.closedAt.hashCode()));
        result = ((result* 31)+((this.uAccounting == null)? 0 :this.uAccounting.hashCode()));
        result = ((result* 31)+((this.uVendor == null)? 0 :this.uVendor.hashCode()));
        result = ((result* 31)+((this.uHouston == null)? 0 :this.uHouston.hashCode()));
        result = ((result* 31)+((this.uKnowledgeArticle == null)? 0 :this.uKnowledgeArticle.hashCode()));
        result = ((result* 31)+((this.causedBy == null)? 0 :this.causedBy.hashCode()));
        result = ((result* 31)+((this.shortDescription == null)? 0 :this.shortDescription.hashCode()));
        result = ((result* 31)+((this.uMcParameter == null)? 0 :this.uMcParameter.hashCode()));
        result = ((result* 31)+((this.proposedBy == null)? 0 :this.proposedBy.hashCode()));
        result = ((result* 31)+((this.userInput == null)? 0 :this.userInput.hashCode()));
        result = ((result* 31)+((this.callerId == null)? 0 :this.callerId.hashCode()));
        result = ((result* 31)+((this.closedBy == null)? 0 :this.closedBy.hashCode()));
        result = ((result* 31)+((this.uAcme == null)? 0 :this.uAcme.hashCode()));
        result = ((result* 31)+((this.uConversionActivity == null)? 0 :this.uConversionActivity.hashCode()));
        result = ((result* 31)+((this.uMcLongMsg == null)? 0 :this.uMcLongMsg.hashCode()));
        result = ((result* 31)+((this.cause == null)? 0 :this.cause.hashCode()));
        result = ((result* 31)+((this.uBmcpsAppTier == null)? 0 :this.uBmcpsAppTier.hashCode()));
        result = ((result* 31)+((this.assignedTo == null)? 0 :this.assignedTo.hashCode()));
        result = ((result* 31)+((this.followUp == null)? 0 :this.followUp.hashCode()));
        result = ((result* 31)+((this.uTestField == null)? 0 :this.uTestField.hashCode()));
        result = ((result* 31)+((this.uVendorHardwareAsset == null)? 0 :this.uVendorHardwareAsset.hashCode()));
        result = ((result* 31)+((this.uBusinessThirdParty == null)? 0 :this.uBusinessThirdParty.hashCode()));
        result = ((result* 31)+((this.uponApproval == null)? 0 :this.uponApproval.hashCode()));
        result = ((result* 31)+((this.comments == null)? 0 :this.comments.hashCode()));
        result = ((result* 31)+((this.majorIncidentState == null)? 0 :this.majorIncidentState.hashCode()));
        result = ((result* 31)+((this.uPoncaCity == null)? 0 :this.uPoncaCity.hashCode()));
        result = ((result* 31)+((this.uPauseDateTime == null)? 0 :this.uPauseDateTime.hashCode()));
        result = ((result* 31)+((this.uFirstIncidentAt == null)? 0 :this.uFirstIncidentAt.hashCode()));
        result = ((result* 31)+((this.uVendorSent == null)? 0 :this.uVendorSent.hashCode()));
        result = ((result* 31)+((this.uChange == null)? 0 :this.uChange.hashCode()));
        result = ((result* 31)+((this.cmdbCi == null)? 0 :this.cmdbCi.hashCode()));
        result = ((result* 31)+((this.slaDue == null)? 0 :this.slaDue.hashCode()));
        result = ((result* 31)+((this.category == null)? 0 :this.category.hashCode()));
        result = ((result* 31)+((this.uSouthwestLasVegas == null)? 0 :this.uSouthwestLasVegas.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ResultTableInc) == false) {
            return false;
        }
        ResultTableInc rhs = ((ResultTableInc) other);
        return ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((this.sysDomain == rhs.sysDomain)||((this.sysDomain!= null)&&this.sysDomain.equals(rhs.sysDomain)))&&((this.proposedOn == rhs.proposedOn)||((this.proposedOn!= null)&&this.proposedOn.equals(rhs.proposedOn))))&&((this.parent == rhs.parent)||((this.parent!= null)&&this.parent.equals(rhs.parent))))&&((this.parentIncident == rhs.parentIncident)||((this.parentIncident!= null)&&this.parentIncident.equals(rhs.parentIncident))))&&((this.incidentState == rhs.incidentState)||((this.incidentState!= null)&&this.incidentState.equals(rhs.incidentState))))&&((this.uAwaitingReason == rhs.uAwaitingReason)||((this.uAwaitingReason!= null)&&this.uAwaitingReason.equals(rhs.uAwaitingReason))))&&((this.uConsolidatedEventData == rhs.uConsolidatedEventData)||((this.uConsolidatedEventData!= null)&&this.uConsolidatedEventData.equals(rhs.uConsolidatedEventData))))&&((this.skills == rhs.skills)||((this.skills!= null)&&this.skills.equals(rhs.skills))))&&((this.uSocal == rhs.uSocal)||((this.uSocal!= null)&&this.uSocal.equals(rhs.uSocal))))&&((this.sysCreatedBy == rhs.sysCreatedBy)||((this.sysCreatedBy!= null)&&this.sysCreatedBy.equals(rhs.sysCreatedBy))))&&((this.uVendorWarrantyExpiration == rhs.uVendorWarrantyExpiration)||((this.uVendorWarrantyExpiration!= null)&&this.uVendorWarrantyExpiration.equals(rhs.uVendorWarrantyExpiration))))&&((this.state == rhs.state)||((this.state!= null)&&this.state.equals(rhs.state))))&&((this.calendarStc == rhs.calendarStc)||((this.calendarStc!= null)&&this.calendarStc.equals(rhs.calendarStc))))&&((this.uTurnoverCategory == rhs.uTurnoverCategory)||((this.uTurnoverCategory!= null)&&this.uTurnoverCategory.equals(rhs.uTurnoverCategory))))&&((this.uInstance == rhs.uInstance)||((this.uInstance!= null)&&this.uInstance.equals(rhs.uInstance))))&&((this.knowledge == rhs.knowledge)||((this.knowledge!= null)&&this.knowledge.equals(rhs.knowledge))))&&((this.uBestCallback == rhs.uBestCallback)||((this.uBestCallback!= null)&&this.uBestCallback.equals(rhs.uBestCallback))))&&((this.impact == rhs.impact)||((this.impact!= null)&&this.impact.equals(rhs.impact))))&&((this.active == rhs.active)||((this.active!= null)&&this.active.equals(rhs.active))))&&((this.uAlertSource == rhs.uAlertSource)||((this.uAlertSource!= null)&&this.uAlertSource.equals(rhs.uAlertSource))))&&((this.groupList == rhs.groupList)||((this.groupList!= null)&&this.groupList.equals(rhs.groupList))))&&((this.vzReleaseForTest == rhs.vzReleaseForTest)||((this.vzReleaseForTest!= null)&&this.vzReleaseForTest.equals(rhs.vzReleaseForTest))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.workNotes == rhs.workNotes)||((this.workNotes!= null)&&this.workNotes.equals(rhs.workNotes))))&&((this.xAvne2BmcToSnoSourceUid == rhs.xAvne2BmcToSnoSourceUid)||((this.xAvne2BmcToSnoSourceUid!= null)&&this.xAvne2BmcToSnoSourceUid.equals(rhs.xAvne2BmcToSnoSourceUid))))&&((this.uProblemCreatedAt == rhs.uProblemCreatedAt)||((this.uProblemCreatedAt!= null)&&this.uProblemCreatedAt.equals(rhs.uProblemCreatedAt))))&&((this.uSourceRef == rhs.uSourceRef)||((this.uSourceRef!= null)&&this.uSourceRef.equals(rhs.uSourceRef))))&&((this.sysId == rhs.sysId)||((this.sysId!= null)&&this.sysId.equals(rhs.sysId))))&&((this.uAlertRef == rhs.uAlertRef)||((this.uAlertRef!= null)&&this.uAlertRef.equals(rhs.uAlertRef))))&&((this.uSiteZone == rhs.uSiteZone)||((this.uSiteZone!= null)&&this.uSiteZone.equals(rhs.uSiteZone))))&&((this.expectedStart == rhs.expectedStart)||((this.expectedStart!= null)&&this.expectedStart.equals(rhs.expectedStart))))&&((this.uFirstOutageAt == rhs.uFirstOutageAt)||((this.uFirstOutageAt!= null)&&this.uFirstOutageAt.equals(rhs.uFirstOutageAt))))&&((this.uDenver == rhs.uDenver)||((this.uDenver!= null)&&this.uDenver.equals(rhs.uDenver))))&&((this.uMergerActivity == rhs.uMergerActivity)||((this.uMergerActivity!= null)&&this.uMergerActivity.equals(rhs.uMergerActivity))))&&((this.correlationId == rhs.correlationId)||((this.correlationId!= null)&&this.correlationId.equals(rhs.correlationId))))&&((this.sysModCount == rhs.sysModCount)||((this.sysModCount!= null)&&this.sysModCount.equals(rhs.sysModCount))))&&((this.uHr == rhs.uHr)||((this.uHr!= null)&&this.uHr.equals(rhs.uHr))))&&((this.uDevQaEnvironment == rhs.uDevQaEnvironment)||((this.uDevQaEnvironment!= null)&&this.uDevQaEnvironment.equals(rhs.uDevQaEnvironment))))&&((this.businessService == rhs.businessService)||((this.businessService!= null)&&this.businessService.equals(rhs.businessService))))&&((this.universalRequest == rhs.universalRequest)||((this.universalRequest!= null)&&this.universalRequest.equals(rhs.universalRequest))))&&((this.uReassignmentReason == rhs.uReassignmentReason)||((this.uReassignmentReason!= null)&&this.uReassignmentReason.equals(rhs.uReassignmentReason))))&&((this.xAvne2BmcToSnoSourceRef == rhs.xAvne2BmcToSnoSourceRef)||((this.xAvne2BmcToSnoSourceRef!= null)&&this.xAvne2BmcToSnoSourceRef.equals(rhs.xAvne2BmcToSnoSourceRef))))&&((this.reopenedBy == rhs.reopenedBy)||((this.reopenedBy!= null)&&this.reopenedBy.equals(rhs.reopenedBy))))&&((this.escalation == rhs.escalation)||((this.escalation!= null)&&this.escalation.equals(rhs.escalation))))&&((this.uBmcpsDedupId == rhs.uBmcpsDedupId)||((this.uBmcpsDedupId!= null)&&this.uBmcpsDedupId.equals(rhs.uBmcpsDedupId))))&&((this.timeline == rhs.timeline)||((this.timeline!= null)&&this.timeline.equals(rhs.timeline))))&&((this.uCurrentDate == rhs.uCurrentDate)||((this.uCurrentDate!= null)&&this.uCurrentDate.equals(rhs.uCurrentDate))))&&((this.uSymptoms == rhs.uSymptoms)||((this.uSymptoms!= null)&&this.uSymptoms.equals(rhs.uSymptoms))))&&((this.uSupportLevel == rhs.uSupportLevel)||((this.uSupportLevel!= null)&&this.uSupportLevel.equals(rhs.uSupportLevel))))&&((this.uSourceUid == rhs.uSourceUid)||((this.uSourceUid!= null)&&this.uSourceUid.equals(rhs.uSourceUid))))&&((this.vzReleaseForTestDatetime == rhs.vzReleaseForTestDatetime)||((this.vzReleaseForTestDatetime!= null)&&this.vzReleaseForTestDatetime.equals(rhs.vzReleaseForTestDatetime))))&&((this.dueDate == rhs.dueDate)||((this.dueDate!= null)&&this.dueDate.equals(rhs.dueDate))))&&((this.uRelatedChatQueue == rhs.uRelatedChatQueue)||((this.uRelatedChatQueue!= null)&&this.uRelatedChatQueue.equals(rhs.uRelatedChatQueue))))&&((this.sysUpdatedOn == rhs.sysUpdatedOn)||((this.sysUpdatedOn!= null)&&this.sysUpdatedOn.equals(rhs.sysUpdatedOn))))&&((this.uSupplyChain == rhs.uSupplyChain)||((this.uSupplyChain!= null)&&this.uSupplyChain.equals(rhs.uSupplyChain))))&&((this.xAvne2BmcToSnoSourceModified == rhs.xAvne2BmcToSnoSourceModified)||((this.xAvne2BmcToSnoSourceModified!= null)&&this.xAvne2BmcToSnoSourceModified.equals(rhs.xAvne2BmcToSnoSourceModified))))&&((this.uVendorSerialNumber == rhs.uVendorSerialNumber)||((this.uVendorSerialNumber!= null)&&this.uVendorSerialNumber.equals(rhs.uVendorSerialNumber))))&&((this.uBmcpsMessage == rhs.uBmcpsMessage)||((this.uBmcpsMessage!= null)&&this.uBmcpsMessage.equals(rhs.uBmcpsMessage))))&&((this.routeReason == rhs.routeReason)||((this.routeReason!= null)&&this.routeReason.equals(rhs.routeReason))))&&((this.uReportedCi == rhs.uReportedCi)||((this.uReportedCi!= null)&&this.uReportedCi.equals(rhs.uReportedCi))))&&((this.uRelatedCall == rhs.uRelatedCall)||((this.uRelatedCall!= null)&&this.uRelatedCall.equals(rhs.uRelatedCall))))&&((this.uVendorTypeModel == rhs.uVendorTypeModel)||((this.uVendorTypeModel!= null)&&this.uVendorTypeModel.equals(rhs.uVendorTypeModel))))&&((this.uSoft == rhs.uSoft)||((this.uSoft!= null)&&this.uSoft.equals(rhs.uSoft))))&&((this.approvalSet == rhs.approvalSet)||((this.approvalSet!= null)&&this.approvalSet.equals(rhs.approvalSet))))&&((this.reopenedTime == rhs.reopenedTime)||((this.reopenedTime!= null)&&this.reopenedTime.equals(rhs.reopenedTime))))&&((this.rfc == rhs.rfc)||((this.rfc!= null)&&this.rfc.equals(rhs.rfc))))&&((this.uOtherSpecify == rhs.uOtherSpecify)||((this.uOtherSpecify!= null)&&this.uOtherSpecify.equals(rhs.uOtherSpecify))))&&((this.assignmentGroup == rhs.assignmentGroup)||((this.assignmentGroup!= null)&&this.assignmentGroup.equals(rhs.assignmentGroup))))&&((this.uLastOutageAt == rhs.uLastOutageAt)||((this.uLastOutageAt!= null)&&this.uLastOutageAt.equals(rhs.uLastOutageAt))))&&((this.subcategory == rhs.subcategory)||((this.subcategory!= null)&&this.subcategory.equals(rhs.subcategory))))&&((this.uMcObjectClass == rhs.uMcObjectClass)||((this.uMcObjectClass!= null)&&this.uMcObjectClass.equals(rhs.uMcObjectClass))))&&((this.uCausedByStory == rhs.uCausedByStory)||((this.uCausedByStory!= null)&&this.uCausedByStory.equals(rhs.uCausedByStory))))&&((this.uBreachTime == rhs.uBreachTime)||((this.uBreachTime!= null)&&this.uBreachTime.equals(rhs.uBreachTime))))&&((this.uProblem == rhs.uProblem)||((this.uProblem!= null)&&this.uProblem.equals(rhs.uProblem))))&&((this.resolvedBy == rhs.resolvedBy)||((this.resolvedBy!= null)&&this.resolvedBy.equals(rhs.resolvedBy))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.uSymptom == rhs.uSymptom)||((this.uSymptom!= null)&&this.uSymptom.equals(rhs.uSymptom))))&&((this.uReporting == rhs.uReporting)||((this.uReporting!= null)&&this.uReporting.equals(rhs.uReporting))))&&((this.uResponseSla == rhs.uResponseSla)||((this.uResponseSla!= null)&&this.uResponseSla.equals(rhs.uResponseSla))))&&((this.approvalHistory == rhs.approvalHistory)||((this.approvalHistory!= null)&&this.approvalHistory.equals(rhs.approvalHistory))))&&((this.uVerizonCustomerName == rhs.uVerizonCustomerName)||((this.uVerizonCustomerName!= null)&&this.uVerizonCustomerName.equals(rhs.uVerizonCustomerName))))&&((this.urgency == rhs.urgency)||((this.urgency!= null)&&this.urgency.equals(rhs.urgency))))&&((this.company == rhs.company)||((this.company!= null)&&this.company.equals(rhs.company))))&&((this.severity == rhs.severity)||((this.severity!= null)&&this.severity.equals(rhs.severity))))&&((this.overview == rhs.overview)||((this.overview!= null)&&this.overview.equals(rhs.overview))))&&((this.uCiDetails == rhs.uCiDetails)||((this.uCiDetails!= null)&&this.uCiDetails.equals(rhs.uCiDetails))))&&((this.promotedOn == rhs.promotedOn)||((this.promotedOn!= null)&&this.promotedOn.equals(rhs.promotedOn))))&&((this.resolvedAt == rhs.resolvedAt)||((this.resolvedAt!= null)&&this.resolvedAt.equals(rhs.resolvedAt))))&&((this.uImpactedBusinessDepartment == rhs.uImpactedBusinessDepartment)||((this.uImpactedBusinessDepartment!= null)&&this.uImpactedBusinessDepartment.equals(rhs.uImpactedBusinessDepartment))))&&((this.sysTags == rhs.sysTags)||((this.sysTags!= null)&&this.sysTags.equals(rhs.sysTags))))&&((this.approval == rhs.approval)||((this.approval!= null)&&this.approval.equals(rhs.approval))))&&((this.uSeattle == rhs.uSeattle)||((this.uSeattle!= null)&&this.uSeattle.equals(rhs.uSeattle))))&&((this.workEnd == rhs.workEnd)||((this.workEnd!= null)&&this.workEnd.equals(rhs.workEnd))))&&((this.uRecycleRequired == rhs.uRecycleRequired)||((this.uRecycleRequired!= null)&&this.uRecycleRequired.equals(rhs.uRecycleRequired))))&&((this.uMcObjectUriLink == rhs.uMcObjectUriLink)||((this.uMcObjectUriLink!= null)&&this.uMcObjectUriLink.equals(rhs.uMcObjectUriLink))))&&((this.uMm == rhs.uMm)||((this.uMm!= null)&&this.uMm.equals(rhs.uMm))))&&((this.uSoftware == rhs.uSoftware)||((this.uSoftware!= null)&&this.uSoftware.equals(rhs.uSoftware))))&&((this.location == rhs.location)||((this.location!= null)&&this.location.equals(rhs.location))))&&((this.uKbRelateXml == rhs.uKbRelateXml)||((this.uKbRelateXml!= null)&&this.uKbRelateXml.equals(rhs.uKbRelateXml))))&&((this.uVendorTaskStatus == rhs.uVendorTaskStatus)||((this.uVendorTaskStatus!= null)&&this.uVendorTaskStatus.equals(rhs.uVendorTaskStatus))))&&((this.uVendorStatus == rhs.uVendorStatus)||((this.uVendorStatus!= null)&&this.uVendorStatus.equals(rhs.uVendorStatus))))&&((this.uTrackName == rhs.uTrackName)||((this.uTrackName!= null)&&this.uTrackName.equals(rhs.uTrackName))))&&((this.uCircuitId == rhs.uCircuitId)||((this.uCircuitId!= null)&&this.uCircuitId.equals(rhs.uCircuitId))))&&((this.sysUpdatedBy == rhs.sysUpdatedBy)||((this.sysUpdatedBy!= null)&&this.sysUpdatedBy.equals(rhs.sysUpdatedBy))))&&((this.uVendorTicket == rhs.uVendorTicket)||((this.uVendorTicket!= null)&&this.uVendorTicket.equals(rhs.uVendorTicket))))&&((this.contactType == rhs.contactType)||((this.contactType!= null)&&this.contactType.equals(rhs.contactType))))&&((this.uMcObjectUri == rhs.uMcObjectUri)||((this.uMcObjectUri!= null)&&this.uMcObjectUri.equals(rhs.uMcObjectUri))))&&((this.uMcUeid == rhs.uMcUeid)||((this.uMcUeid!= null)&&this.uMcUeid.equals(rhs.uMcUeid))))&&((this.number == rhs.number)||((this.number!= null)&&this.number.equals(rhs.number))))&&((this.uAllDivisions == rhs.uAllDivisions)||((this.uAllDivisions!= null)&&this.uAllDivisions.equals(rhs.uAllDivisions))))&&((this.uRestorationManager == rhs.uRestorationManager)||((this.uRestorationManager!= null)&&this.uRestorationManager.equals(rhs.uRestorationManager))))&&((this.childIncidents == rhs.childIncidents)||((this.childIncidents!= null)&&this.childIncidents.equals(rhs.childIncidents))))&&((this.uPortland == rhs.uPortland)||((this.uPortland!= null)&&this.uPortland.equals(rhs.uPortland))))&&((this.calendarDuration == rhs.calendarDuration)||((this.calendarDuration!= null)&&this.calendarDuration.equals(rhs.calendarDuration))))&&((this.uVerizonCustomerLocation == rhs.uVerizonCustomerLocation)||((this.uVerizonCustomerLocation!= null)&&this.uVerizonCustomerLocation.equals(rhs.uVerizonCustomerLocation))))&&((this.uVerizonProductType == rhs.uVerizonProductType)||((this.uVerizonProductType!= null)&&this.uVerizonProductType.equals(rhs.uVerizonProductType))))&&((this.order == rhs.order)||((this.order!= null)&&this.order.equals(rhs.order))))&&((this.uJewel == rhs.uJewel)||((this.uJewel!= null)&&this.uJewel.equals(rhs.uJewel))))&&((this.priority == rhs.priority)||((this.priority!= null)&&this.priority.equals(rhs.priority))))&&((this.uMcModhist == rhs.uMcModhist)||((this.uMcModhist!= null)&&this.uMcModhist.equals(rhs.uMcModhist))))&&((this.closeCode == rhs.closeCode)||((this.closeCode!= null)&&this.closeCode.equals(rhs.closeCode))))&&((this.uRetail == rhs.uRetail)||((this.uRetail!= null)&&this.uRetail.equals(rhs.uRetail))))&&((this.uProblemRelatedBy == rhs.uProblemRelatedBy)||((this.uProblemRelatedBy!= null)&&this.uProblemRelatedBy.equals(rhs.uProblemRelatedBy))))&&((this.uParameter == rhs.uParameter)||((this.uParameter!= null)&&this.uParameter.equals(rhs.uParameter))))&&((this.uRelatedHrCase == rhs.uRelatedHrCase)||((this.uRelatedHrCase!= null)&&this.uRelatedHrCase.equals(rhs.uRelatedHrCase))))&&((this.uIntermountain == rhs.uIntermountain)||((this.uIntermountain!= null)&&this.uIntermountain.equals(rhs.uIntermountain))))&&((this.activityDue == rhs.activityDue)||((this.activityDue!= null)&&this.activityDue.equals(rhs.activityDue))))&&((this.uItSharedServices == rhs.uItSharedServices)||((this.uItSharedServices!= null)&&this.uItSharedServices.equals(rhs.uItSharedServices))))&&((this.correlationDisplay == rhs.correlationDisplay)||((this.correlationDisplay!= null)&&this.correlationDisplay.equals(rhs.correlationDisplay))))&&((this.taskEffectiveNumber == rhs.taskEffectiveNumber)||((this.taskEffectiveNumber!= null)&&this.taskEffectiveNumber.equals(rhs.taskEffectiveNumber))))&&((this.uAssignedTo == rhs.uAssignedTo)||((this.uAssignedTo!= null)&&this.uAssignedTo.equals(rhs.uAssignedTo))))&&((this.uContactPhone == rhs.uContactPhone)||((this.uContactPhone!= null)&&this.uContactPhone.equals(rhs.uContactPhone))))&&((this.uMissingCategory == rhs.uMissingCategory)||((this.uMissingCategory!= null)&&this.uMissingCategory.equals(rhs.uMissingCategory))))&&((this.businessStc == rhs.businessStc)||((this.businessStc!= null)&&this.businessStc.equals(rhs.businessStc))))&&((this.uponReject == rhs.uponReject)||((this.uponReject!= null)&&this.uponReject.equals(rhs.uponReject))))&&((this.timeWorked == rhs.timeWorked)||((this.timeWorked!= null)&&this.timeWorked.equals(rhs.timeWorked))))&&((this.additionalAssigneeList == rhs.additionalAssigneeList)||((this.additionalAssigneeList!= null)&&this.additionalAssigneeList.equals(rhs.additionalAssigneeList))))&&((this.watchList == rhs.watchList)||((this.watchList!= null)&&this.watchList.equals(rhs.watchList))))&&((this.notify == rhs.notify)||((this.notify!= null)&&this.notify.equals(rhs.notify))))&&((this.reassignmentCount == rhs.reassignmentCount)||((this.reassignmentCount!= null)&&this.reassignmentCount.equals(rhs.reassignmentCount))))&&((this.openedBy == rhs.openedBy)||((this.openedBy!= null)&&this.openedBy.equals(rhs.openedBy))))&&((this.uNcpdpRejects == rhs.uNcpdpRejects)||((this.uNcpdpRejects!= null)&&this.uNcpdpRejects.equals(rhs.uNcpdpRejects))))&&((this.uDataMigration == rhs.uDataMigration)||((this.uDataMigration!= null)&&this.uDataMigration.equals(rhs.uDataMigration))))&&((this.lessonsLearned == rhs.lessonsLearned)||((this.lessonsLearned!= null)&&this.lessonsLearned.equals(rhs.lessonsLearned))))&&((this.promotedBy == rhs.promotedBy)||((this.promotedBy!= null)&&this.promotedBy.equals(rhs.promotedBy))))&&((this.uSouthern == rhs.uSouthern)||((this.uSouthern!= null)&&this.uSouthern.equals(rhs.uSouthern))))&&((this.uPharmacy == rhs.uPharmacy)||((this.uPharmacy!= null)&&this.uPharmacy.equals(rhs.uPharmacy))))&&((this.uTargetDate == rhs.uTargetDate)||((this.uTargetDate!= null)&&this.uTargetDate.equals(rhs.uTargetDate))))&&((this.uMcObject == rhs.uMcObject)||((this.uMcObject!= null)&&this.uMcObject.equals(rhs.uMcObject))))&&((this.uSundriesBoise == rhs.uSundriesBoise)||((this.uSundriesBoise!= null)&&this.uSundriesBoise.equals(rhs.uSundriesBoise))))&&((this.sysClassName == rhs.sysClassName)||((this.sysClassName!= null)&&this.sysClassName.equals(rhs.sysClassName))))&&((this.businessImpact == rhs.businessImpact)||((this.businessImpact!= null)&&this.businessImpact.equals(rhs.businessImpact))))&&((this.uImpactedBusinessArea == rhs.uImpactedBusinessArea)||((this.uImpactedBusinessArea!= null)&&this.uImpactedBusinessArea.equals(rhs.uImpactedBusinessArea))))&&((this.uFirstNotifiedAt == rhs.uFirstNotifiedAt)||((this.uFirstNotifiedAt!= null)&&this.uFirstNotifiedAt.equals(rhs.uFirstNotifiedAt))))&&((this.uShaws == rhs.uShaws)||((this.uShaws!= null)&&this.uShaws.equals(rhs.uShaws))))&&((this.problemId == rhs.problemId)||((this.problemId!= null)&&this.problemId.equals(rhs.problemId))))&&((this.reopenCount == rhs.reopenCount)||((this.reopenCount!= null)&&this.reopenCount.equals(rhs.reopenCount))))&&((this.closeNotes == rhs.closeNotes)||((this.closeNotes!= null)&&this.closeNotes.equals(rhs.closeNotes))))&&((this.uEventHandle == rhs.uEventHandle)||((this.uEventHandle!= null)&&this.uEventHandle.equals(rhs.uEventHandle))))&&((this.workStart == rhs.workStart)||((this.workStart!= null)&&this.workStart.equals(rhs.workStart))))&&((this.openedAt == rhs.openedAt)||((this.openedAt!= null)&&this.openedAt.equals(rhs.openedAt))))&&((this.uVerizonSeverity == rhs.uVerizonSeverity)||((this.uVerizonSeverity!= null)&&this.uVerizonSeverity.equals(rhs.uVerizonSeverity))))&&((this.businessDuration == rhs.businessDuration)||((this.businessDuration!= null)&&this.businessDuration.equals(rhs.businessDuration))))&&((this.uCancellationReason == rhs.uCancellationReason)||((this.uCancellationReason!= null)&&this.uCancellationReason.equals(rhs.uCancellationReason))))&&((this.uAdditionalTracksAffected == rhs.uAdditionalTracksAffected)||((this.uAdditionalTracksAffected!= null)&&this.uAdditionalTracksAffected.equals(rhs.uAdditionalTracksAffected))))&&((this.uReasonForSlaException == rhs.uReasonForSlaException)||((this.uReasonForSlaException!= null)&&this.uReasonForSlaException.equals(rhs.uReasonForSlaException))))&&((this.uCallerName == rhs.uCallerName)||((this.uCallerName!= null)&&this.uCallerName.equals(rhs.uCallerName))))&&((this.sysCreatedOn == rhs.sysCreatedOn)||((this.sysCreatedOn!= null)&&this.sysCreatedOn.equals(rhs.sysCreatedOn))))&&((this.uHpi == rhs.uHpi)||((this.uHpi!= null)&&this.uHpi.equals(rhs.uHpi))))&&((this.uTurnoverReview == rhs.uTurnoverReview)||((this.uTurnoverReview!= null)&&this.uTurnoverReview.equals(rhs.uTurnoverReview))))&&((this.uAdapterHost == rhs.uAdapterHost)||((this.uAdapterHost!= null)&&this.uAdapterHost.equals(rhs.uAdapterHost))))&&((this.closedAt == rhs.closedAt)||((this.closedAt!= null)&&this.closedAt.equals(rhs.closedAt))))&&((this.uAccounting == rhs.uAccounting)||((this.uAccounting!= null)&&this.uAccounting.equals(rhs.uAccounting))))&&((this.uVendor == rhs.uVendor)||((this.uVendor!= null)&&this.uVendor.equals(rhs.uVendor))))&&((this.uHouston == rhs.uHouston)||((this.uHouston!= null)&&this.uHouston.equals(rhs.uHouston))))&&((this.uKnowledgeArticle == rhs.uKnowledgeArticle)||((this.uKnowledgeArticle!= null)&&this.uKnowledgeArticle.equals(rhs.uKnowledgeArticle))))&&((this.causedBy == rhs.causedBy)||((this.causedBy!= null)&&this.causedBy.equals(rhs.causedBy))))&&((this.shortDescription == rhs.shortDescription)||((this.shortDescription!= null)&&this.shortDescription.equals(rhs.shortDescription))))&&((this.uMcParameter == rhs.uMcParameter)||((this.uMcParameter!= null)&&this.uMcParameter.equals(rhs.uMcParameter))))&&((this.proposedBy == rhs.proposedBy)||((this.proposedBy!= null)&&this.proposedBy.equals(rhs.proposedBy))))&&((this.userInput == rhs.userInput)||((this.userInput!= null)&&this.userInput.equals(rhs.userInput))))&&((this.callerId == rhs.callerId)||((this.callerId!= null)&&this.callerId.equals(rhs.callerId))))&&((this.closedBy == rhs.closedBy)||((this.closedBy!= null)&&this.closedBy.equals(rhs.closedBy))))&&((this.uAcme == rhs.uAcme)||((this.uAcme!= null)&&this.uAcme.equals(rhs.uAcme))))&&((this.uConversionActivity == rhs.uConversionActivity)||((this.uConversionActivity!= null)&&this.uConversionActivity.equals(rhs.uConversionActivity))))&&((this.uMcLongMsg == rhs.uMcLongMsg)||((this.uMcLongMsg!= null)&&this.uMcLongMsg.equals(rhs.uMcLongMsg))))&&((this.cause == rhs.cause)||((this.cause!= null)&&this.cause.equals(rhs.cause))))&&((this.uBmcpsAppTier == rhs.uBmcpsAppTier)||((this.uBmcpsAppTier!= null)&&this.uBmcpsAppTier.equals(rhs.uBmcpsAppTier))))&&((this.assignedTo == rhs.assignedTo)||((this.assignedTo!= null)&&this.assignedTo.equals(rhs.assignedTo))))&&((this.followUp == rhs.followUp)||((this.followUp!= null)&&this.followUp.equals(rhs.followUp))))&&((this.uTestField == rhs.uTestField)||((this.uTestField!= null)&&this.uTestField.equals(rhs.uTestField))))&&((this.uVendorHardwareAsset == rhs.uVendorHardwareAsset)||((this.uVendorHardwareAsset!= null)&&this.uVendorHardwareAsset.equals(rhs.uVendorHardwareAsset))))&&((this.uBusinessThirdParty == rhs.uBusinessThirdParty)||((this.uBusinessThirdParty!= null)&&this.uBusinessThirdParty.equals(rhs.uBusinessThirdParty))))&&((this.uponApproval == rhs.uponApproval)||((this.uponApproval!= null)&&this.uponApproval.equals(rhs.uponApproval))))&&((this.comments == rhs.comments)||((this.comments!= null)&&this.comments.equals(rhs.comments))))&&((this.majorIncidentState == rhs.majorIncidentState)||((this.majorIncidentState!= null)&&this.majorIncidentState.equals(rhs.majorIncidentState))))&&((this.uPoncaCity == rhs.uPoncaCity)||((this.uPoncaCity!= null)&&this.uPoncaCity.equals(rhs.uPoncaCity))))&&((this.uPauseDateTime == rhs.uPauseDateTime)||((this.uPauseDateTime!= null)&&this.uPauseDateTime.equals(rhs.uPauseDateTime))))&&((this.uFirstIncidentAt == rhs.uFirstIncidentAt)||((this.uFirstIncidentAt!= null)&&this.uFirstIncidentAt.equals(rhs.uFirstIncidentAt))))&&((this.uVendorSent == rhs.uVendorSent)||((this.uVendorSent!= null)&&this.uVendorSent.equals(rhs.uVendorSent))))&&((this.uChange == rhs.uChange)||((this.uChange!= null)&&this.uChange.equals(rhs.uChange))))&&((this.cmdbCi == rhs.cmdbCi)||((this.cmdbCi!= null)&&this.cmdbCi.equals(rhs.cmdbCi))))&&((this.slaDue == rhs.slaDue)||((this.slaDue!= null)&&this.slaDue.equals(rhs.slaDue))))&&((this.category == rhs.category)||((this.category!= null)&&this.category.equals(rhs.category))))&&((this.uSouthwestLasVegas == rhs.uSouthwestLasVegas)||((this.uSouthwestLasVegas!= null)&&this.uSouthwestLasVegas.equals(rhs.uSouthwestLasVegas))));
    }

}
