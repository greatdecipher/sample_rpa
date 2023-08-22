
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
    "u_expiration_date",
    "description",
    "source",
    "sys_updated_on",
    "type",
    "points",
    "queue_managers",
    "sys_id",
    "sys_updated_by",
    "u_tower",
    "u_work_info",
    "default_assignee",
    "sys_created_on",
    "u_senior_vp",
    "queue_managed",
    "u_km_view",
    "u_senior_director",
    "vendors",
    "email",
    "sys_created_by",
    "u_senior_manager",
    "u_support_level",
    "manager",
    "sys_mod_count",
    "active",
    "average_daily_fte",
    "sys_tags",
    "u_vp",
    "cost_center",
    "hourly_rate",
    "u_director",
    "name",
    "exclude_manager",
    "include_members"
})
@Generated("jsonschema2pojo")
public class ResultTableSysUserGroup {

    @JsonProperty("parent")
    private Parent parent;
    @JsonProperty("u_expiration_date")
    private String uExpirationDate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("source")
    private String source;
    @JsonProperty("sys_updated_on")
    private String sysUpdatedOn;
    @JsonProperty("type")
    private String type;
    @JsonProperty("points")
    private String points;
    @JsonProperty("queue_managers")
    private String queueManagers;
    @JsonProperty("sys_id")
    private String sysId;
    @JsonProperty("sys_updated_by")
    private String sysUpdatedBy;
    @JsonProperty("u_tower")
    private String uTower;
    @JsonProperty("u_work_info")
    private String uWorkInfo;
    @JsonProperty("default_assignee")
    private String defaultAssignee;
    @JsonProperty("sys_created_on")
    private String sysCreatedOn;
    @JsonProperty("u_senior_vp")
    private USeniorVp uSeniorVp;
    @JsonProperty("queue_managed")
    private String queueManaged;
    @JsonProperty("u_km_view")
    private String uKmView;
    @JsonProperty("u_senior_director")
    private String uSeniorDirector;
    @JsonProperty("vendors")
    private String vendors;
    @JsonProperty("email")
    private String email;
    @JsonProperty("sys_created_by")
    private String sysCreatedBy;
    @JsonProperty("u_senior_manager")
    private String uSeniorManager;
    @JsonProperty("u_support_level")
    private String uSupportLevel;
    @JsonProperty("manager")
    private Manager manager;
    @JsonProperty("sys_mod_count")
    private String sysModCount;
    @JsonProperty("active")
    private String active;
    @JsonProperty("average_daily_fte")
    private String averageDailyFte;
    @JsonProperty("sys_tags")
    private String sysTags;
    @JsonProperty("u_vp")
    private UVp uVp;
    @JsonProperty("cost_center")
    private String costCenter;
    @JsonProperty("hourly_rate")
    private String hourlyRate;
    @JsonProperty("u_director")
    private UDirector uDirector;
    @JsonProperty("name")
    private String name;
    @JsonProperty("exclude_manager")
    private String excludeManager;
    @JsonProperty("include_members")
    private String includeMembers;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("parent")
    public Parent getParent() {
        return parent;
    }

    @JsonProperty("parent")
    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @JsonProperty("u_expiration_date")
    public String getuExpirationDate() {
        return uExpirationDate;
    }

    @JsonProperty("u_expiration_date")
    public void setuExpirationDate(String uExpirationDate) {
        this.uExpirationDate = uExpirationDate;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("sys_updated_on")
    public String getSysUpdatedOn() {
        return sysUpdatedOn;
    }

    @JsonProperty("sys_updated_on")
    public void setSysUpdatedOn(String sysUpdatedOn) {
        this.sysUpdatedOn = sysUpdatedOn;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("points")
    public String getPoints() {
        return points;
    }

    @JsonProperty("points")
    public void setPoints(String points) {
        this.points = points;
    }

    @JsonProperty("queue_managers")
    public String getQueueManagers() {
        return queueManagers;
    }

    @JsonProperty("queue_managers")
    public void setQueueManagers(String queueManagers) {
        this.queueManagers = queueManagers;
    }

    @JsonProperty("sys_id")
    public String getSysId() {
        return sysId;
    }

    @JsonProperty("sys_id")
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    @JsonProperty("sys_updated_by")
    public String getSysUpdatedBy() {
        return sysUpdatedBy;
    }

    @JsonProperty("sys_updated_by")
    public void setSysUpdatedBy(String sysUpdatedBy) {
        this.sysUpdatedBy = sysUpdatedBy;
    }

    @JsonProperty("u_tower")
    public String getuTower() {
        return uTower;
    }

    @JsonProperty("u_tower")
    public void setuTower(String uTower) {
        this.uTower = uTower;
    }

    @JsonProperty("u_work_info")
    public String getuWorkInfo() {
        return uWorkInfo;
    }

    @JsonProperty("u_work_info")
    public void setuWorkInfo(String uWorkInfo) {
        this.uWorkInfo = uWorkInfo;
    }

    @JsonProperty("default_assignee")
    public String getDefaultAssignee() {
        return defaultAssignee;
    }

    @JsonProperty("default_assignee")
    public void setDefaultAssignee(String defaultAssignee) {
        this.defaultAssignee = defaultAssignee;
    }

    @JsonProperty("sys_created_on")
    public String getSysCreatedOn() {
        return sysCreatedOn;
    }

    @JsonProperty("sys_created_on")
    public void setSysCreatedOn(String sysCreatedOn) {
        this.sysCreatedOn = sysCreatedOn;
    }

    @JsonProperty("u_senior_vp")
    public USeniorVp getuSeniorVp() {
        return uSeniorVp;
    }

    @JsonProperty("u_senior_vp")
    public void setuSeniorVp(USeniorVp uSeniorVp) {
        this.uSeniorVp = uSeniorVp;
    }

    @JsonProperty("queue_managed")
    public String getQueueManaged() {
        return queueManaged;
    }

    @JsonProperty("queue_managed")
    public void setQueueManaged(String queueManaged) {
        this.queueManaged = queueManaged;
    }

    @JsonProperty("u_km_view")
    public String getuKmView() {
        return uKmView;
    }

    @JsonProperty("u_km_view")
    public void setuKmView(String uKmView) {
        this.uKmView = uKmView;
    }

    @JsonProperty("u_senior_director")
    public String getuSeniorDirector() {
        return uSeniorDirector;
    }

    @JsonProperty("u_senior_director")
    public void setuSeniorDirector(String uSeniorDirector) {
        this.uSeniorDirector = uSeniorDirector;
    }

    @JsonProperty("vendors")
    public String getVendors() {
        return vendors;
    }

    @JsonProperty("vendors")
    public void setVendors(String vendors) {
        this.vendors = vendors;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("sys_created_by")
    public String getSysCreatedBy() {
        return sysCreatedBy;
    }

    @JsonProperty("sys_created_by")
    public void setSysCreatedBy(String sysCreatedBy) {
        this.sysCreatedBy = sysCreatedBy;
    }

    @JsonProperty("u_senior_manager")
    public String getuSeniorManager() {
        return uSeniorManager;
    }

    @JsonProperty("u_senior_manager")
    public void setuSeniorManager(String uSeniorManager) {
        this.uSeniorManager = uSeniorManager;
    }

    @JsonProperty("u_support_level")
    public String getuSupportLevel() {
        return uSupportLevel;
    }

    @JsonProperty("u_support_level")
    public void setuSupportLevel(String uSupportLevel) {
        this.uSupportLevel = uSupportLevel;
    }

    @JsonProperty("manager")
    public Manager getManager() {
        return manager;
    }

    @JsonProperty("manager")
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @JsonProperty("sys_mod_count")
    public String getSysModCount() {
        return sysModCount;
    }

    @JsonProperty("sys_mod_count")
    public void setSysModCount(String sysModCount) {
        this.sysModCount = sysModCount;
    }

    @JsonProperty("active")
    public String getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(String active) {
        this.active = active;
    }

    @JsonProperty("average_daily_fte")
    public String getAverageDailyFte() {
        return averageDailyFte;
    }

    @JsonProperty("average_daily_fte")
    public void setAverageDailyFte(String averageDailyFte) {
        this.averageDailyFte = averageDailyFte;
    }

    @JsonProperty("sys_tags")
    public String getSysTags() {
        return sysTags;
    }

    @JsonProperty("sys_tags")
    public void setSysTags(String sysTags) {
        this.sysTags = sysTags;
    }

    @JsonProperty("u_vp")
    public UVp getuVp() {
        return uVp;
    }

    @JsonProperty("u_vp")
    public void setuVp(UVp uVp) {
        this.uVp = uVp;
    }

    @JsonProperty("cost_center")
    public String getCostCenter() {
        return costCenter;
    }

    @JsonProperty("cost_center")
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    @JsonProperty("hourly_rate")
    public String getHourlyRate() {
        return hourlyRate;
    }

    @JsonProperty("hourly_rate")
    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @JsonProperty("u_director")
    public UDirector getuDirector() {
        return uDirector;
    }

    @JsonProperty("u_director")
    public void setuDirector(UDirector uDirector) {
        this.uDirector = uDirector;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("exclude_manager")
    public String getExcludeManager() {
        return excludeManager;
    }

    @JsonProperty("exclude_manager")
    public void setExcludeManager(String excludeManager) {
        this.excludeManager = excludeManager;
    }

    @JsonProperty("include_members")
    public String getIncludeMembers() {
        return includeMembers;
    }

    @JsonProperty("include_members")
    public void setIncludeMembers(String includeMembers) {
        this.includeMembers = includeMembers;
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
        sb.append(ResultTableSysUserGroup.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("parent");
        sb.append('=');
        sb.append(((this.parent == null)?"<null>":this.parent));
        sb.append(',');
        sb.append("uExpirationDate");
        sb.append('=');
        sb.append(((this.uExpirationDate == null)?"<null>":this.uExpirationDate));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("source");
        sb.append('=');
        sb.append(((this.source == null)?"<null>":this.source));
        sb.append(',');
        sb.append("sysUpdatedOn");
        sb.append('=');
        sb.append(((this.sysUpdatedOn == null)?"<null>":this.sysUpdatedOn));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("points");
        sb.append('=');
        sb.append(((this.points == null)?"<null>":this.points));
        sb.append(',');
        sb.append("queueManagers");
        sb.append('=');
        sb.append(((this.queueManagers == null)?"<null>":this.queueManagers));
        sb.append(',');
        sb.append("sysId");
        sb.append('=');
        sb.append(((this.sysId == null)?"<null>":this.sysId));
        sb.append(',');
        sb.append("sysUpdatedBy");
        sb.append('=');
        sb.append(((this.sysUpdatedBy == null)?"<null>":this.sysUpdatedBy));
        sb.append(',');
        sb.append("uTower");
        sb.append('=');
        sb.append(((this.uTower == null)?"<null>":this.uTower));
        sb.append(',');
        sb.append("uWorkInfo");
        sb.append('=');
        sb.append(((this.uWorkInfo == null)?"<null>":this.uWorkInfo));
        sb.append(',');
        sb.append("defaultAssignee");
        sb.append('=');
        sb.append(((this.defaultAssignee == null)?"<null>":this.defaultAssignee));
        sb.append(',');
        sb.append("sysCreatedOn");
        sb.append('=');
        sb.append(((this.sysCreatedOn == null)?"<null>":this.sysCreatedOn));
        sb.append(',');
        sb.append("uSeniorVp");
        sb.append('=');
        sb.append(((this.uSeniorVp == null)?"<null>":this.uSeniorVp));
        sb.append(',');
        sb.append("queueManaged");
        sb.append('=');
        sb.append(((this.queueManaged == null)?"<null>":this.queueManaged));
        sb.append(',');
        sb.append("uKmView");
        sb.append('=');
        sb.append(((this.uKmView == null)?"<null>":this.uKmView));
        sb.append(',');
        sb.append("uSeniorDirector");
        sb.append('=');
        sb.append(((this.uSeniorDirector == null)?"<null>":this.uSeniorDirector));
        sb.append(',');
        sb.append("vendors");
        sb.append('=');
        sb.append(((this.vendors == null)?"<null>":this.vendors));
        sb.append(',');
        sb.append("email");
        sb.append('=');
        sb.append(((this.email == null)?"<null>":this.email));
        sb.append(',');
        sb.append("sysCreatedBy");
        sb.append('=');
        sb.append(((this.sysCreatedBy == null)?"<null>":this.sysCreatedBy));
        sb.append(',');
        sb.append("uSeniorManager");
        sb.append('=');
        sb.append(((this.uSeniorManager == null)?"<null>":this.uSeniorManager));
        sb.append(',');
        sb.append("uSupportLevel");
        sb.append('=');
        sb.append(((this.uSupportLevel == null)?"<null>":this.uSupportLevel));
        sb.append(',');
        sb.append("manager");
        sb.append('=');
        sb.append(((this.manager == null)?"<null>":this.manager));
        sb.append(',');
        sb.append("sysModCount");
        sb.append('=');
        sb.append(((this.sysModCount == null)?"<null>":this.sysModCount));
        sb.append(',');
        sb.append("active");
        sb.append('=');
        sb.append(((this.active == null)?"<null>":this.active));
        sb.append(',');
        sb.append("averageDailyFte");
        sb.append('=');
        sb.append(((this.averageDailyFte == null)?"<null>":this.averageDailyFte));
        sb.append(',');
        sb.append("sysTags");
        sb.append('=');
        sb.append(((this.sysTags == null)?"<null>":this.sysTags));
        sb.append(',');
        sb.append("uVp");
        sb.append('=');
        sb.append(((this.uVp == null)?"<null>":this.uVp));
        sb.append(',');
        sb.append("costCenter");
        sb.append('=');
        sb.append(((this.costCenter == null)?"<null>":this.costCenter));
        sb.append(',');
        sb.append("hourlyRate");
        sb.append('=');
        sb.append(((this.hourlyRate == null)?"<null>":this.hourlyRate));
        sb.append(',');
        sb.append("uDirector");
        sb.append('=');
        sb.append(((this.uDirector == null)?"<null>":this.uDirector));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("excludeManager");
        sb.append('=');
        sb.append(((this.excludeManager == null)?"<null>":this.excludeManager));
        sb.append(',');
        sb.append("includeMembers");
        sb.append('=');
        sb.append(((this.includeMembers == null)?"<null>":this.includeMembers));
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
        result = ((result* 31)+((this.parent == null)? 0 :this.parent.hashCode()));
        result = ((result* 31)+((this.sysUpdatedBy == null)? 0 :this.sysUpdatedBy.hashCode()));
        result = ((result* 31)+((this.sysId == null)? 0 :this.sysId.hashCode()));
        result = ((result* 31)+((this.queueManaged == null)? 0 :this.queueManaged.hashCode()));
        result = ((result* 31)+((this.uSupportLevel == null)? 0 :this.uSupportLevel.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.sysUpdatedOn == null)? 0 :this.sysUpdatedOn.hashCode()));
        result = ((result* 31)+((this.source == null)? 0 :this.source.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.excludeManager == null)? 0 :this.excludeManager.hashCode()));
        result = ((result* 31)+((this.includeMembers == null)? 0 :this.includeMembers.hashCode()));
        result = ((result* 31)+((this.uVp == null)? 0 :this.uVp.hashCode()));
        result = ((result* 31)+((this.points == null)? 0 :this.points.hashCode()));
        result = ((result* 31)+((this.uSeniorVp == null)? 0 :this.uSeniorVp.hashCode()));
        result = ((result* 31)+((this.sysCreatedBy == null)? 0 :this.sysCreatedBy.hashCode()));
        result = ((result* 31)+((this.queueManagers == null)? 0 :this.queueManagers.hashCode()));
        result = ((result* 31)+((this.sysCreatedOn == null)? 0 :this.sysCreatedOn.hashCode()));
        result = ((result* 31)+((this.uWorkInfo == null)? 0 :this.uWorkInfo.hashCode()));
        result = ((result* 31)+((this.vendors == null)? 0 :this.vendors.hashCode()));
        result = ((result* 31)+((this.sysModCount == null)? 0 :this.sysModCount.hashCode()));
        result = ((result* 31)+((this.email == null)? 0 :this.email.hashCode()));
        result = ((result* 31)+((this.uKmView == null)? 0 :this.uKmView.hashCode()));
        result = ((result* 31)+((this.uExpirationDate == null)? 0 :this.uExpirationDate.hashCode()));
        result = ((result* 31)+((this.manager == null)? 0 :this.manager.hashCode()));
        result = ((result* 31)+((this.sysTags == null)? 0 :this.sysTags.hashCode()));
        result = ((result* 31)+((this.costCenter == null)? 0 :this.costCenter.hashCode()));
        result = ((result* 31)+((this.active == null)? 0 :this.active.hashCode()));
        result = ((result* 31)+((this.uDirector == null)? 0 :this.uDirector.hashCode()));
        result = ((result* 31)+((this.uSeniorDirector == null)? 0 :this.uSeniorDirector.hashCode()));
        result = ((result* 31)+((this.uTower == null)? 0 :this.uTower.hashCode()));
        result = ((result* 31)+((this.uSeniorManager == null)? 0 :this.uSeniorManager.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.averageDailyFte == null)? 0 :this.averageDailyFte.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.defaultAssignee == null)? 0 :this.defaultAssignee.hashCode()));
        result = ((result* 31)+((this.hourlyRate == null)? 0 :this.hourlyRate.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ResultTableSysUserGroup) == false) {
            return false;
        }
        ResultTableSysUserGroup rhs = ((ResultTableSysUserGroup) other);
        return (((((((((((((((((((((((((((((((((((((this.parent == rhs.parent)||((this.parent!= null)&&this.parent.equals(rhs.parent)))&&((this.sysUpdatedBy == rhs.sysUpdatedBy)||((this.sysUpdatedBy!= null)&&this.sysUpdatedBy.equals(rhs.sysUpdatedBy))))&&((this.sysId == rhs.sysId)||((this.sysId!= null)&&this.sysId.equals(rhs.sysId))))&&((this.queueManaged == rhs.queueManaged)||((this.queueManaged!= null)&&this.queueManaged.equals(rhs.queueManaged))))&&((this.uSupportLevel == rhs.uSupportLevel)||((this.uSupportLevel!= null)&&this.uSupportLevel.equals(rhs.uSupportLevel))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.sysUpdatedOn == rhs.sysUpdatedOn)||((this.sysUpdatedOn!= null)&&this.sysUpdatedOn.equals(rhs.sysUpdatedOn))))&&((this.source == rhs.source)||((this.source!= null)&&this.source.equals(rhs.source))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.excludeManager == rhs.excludeManager)||((this.excludeManager!= null)&&this.excludeManager.equals(rhs.excludeManager))))&&((this.includeMembers == rhs.includeMembers)||((this.includeMembers!= null)&&this.includeMembers.equals(rhs.includeMembers))))&&((this.uVp == rhs.uVp)||((this.uVp!= null)&&this.uVp.equals(rhs.uVp))))&&((this.points == rhs.points)||((this.points!= null)&&this.points.equals(rhs.points))))&&((this.uSeniorVp == rhs.uSeniorVp)||((this.uSeniorVp!= null)&&this.uSeniorVp.equals(rhs.uSeniorVp))))&&((this.sysCreatedBy == rhs.sysCreatedBy)||((this.sysCreatedBy!= null)&&this.sysCreatedBy.equals(rhs.sysCreatedBy))))&&((this.queueManagers == rhs.queueManagers)||((this.queueManagers!= null)&&this.queueManagers.equals(rhs.queueManagers))))&&((this.sysCreatedOn == rhs.sysCreatedOn)||((this.sysCreatedOn!= null)&&this.sysCreatedOn.equals(rhs.sysCreatedOn))))&&((this.uWorkInfo == rhs.uWorkInfo)||((this.uWorkInfo!= null)&&this.uWorkInfo.equals(rhs.uWorkInfo))))&&((this.vendors == rhs.vendors)||((this.vendors!= null)&&this.vendors.equals(rhs.vendors))))&&((this.sysModCount == rhs.sysModCount)||((this.sysModCount!= null)&&this.sysModCount.equals(rhs.sysModCount))))&&((this.email == rhs.email)||((this.email!= null)&&this.email.equals(rhs.email))))&&((this.uKmView == rhs.uKmView)||((this.uKmView!= null)&&this.uKmView.equals(rhs.uKmView))))&&((this.uExpirationDate == rhs.uExpirationDate)||((this.uExpirationDate!= null)&&this.uExpirationDate.equals(rhs.uExpirationDate))))&&((this.manager == rhs.manager)||((this.manager!= null)&&this.manager.equals(rhs.manager))))&&((this.sysTags == rhs.sysTags)||((this.sysTags!= null)&&this.sysTags.equals(rhs.sysTags))))&&((this.costCenter == rhs.costCenter)||((this.costCenter!= null)&&this.costCenter.equals(rhs.costCenter))))&&((this.active == rhs.active)||((this.active!= null)&&this.active.equals(rhs.active))))&&((this.uDirector == rhs.uDirector)||((this.uDirector!= null)&&this.uDirector.equals(rhs.uDirector))))&&((this.uSeniorDirector == rhs.uSeniorDirector)||((this.uSeniorDirector!= null)&&this.uSeniorDirector.equals(rhs.uSeniorDirector))))&&((this.uTower == rhs.uTower)||((this.uTower!= null)&&this.uTower.equals(rhs.uTower))))&&((this.uSeniorManager == rhs.uSeniorManager)||((this.uSeniorManager!= null)&&this.uSeniorManager.equals(rhs.uSeniorManager))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.averageDailyFte == rhs.averageDailyFte)||((this.averageDailyFte!= null)&&this.averageDailyFte.equals(rhs.averageDailyFte))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.defaultAssignee == rhs.defaultAssignee)||((this.defaultAssignee!= null)&&this.defaultAssignee.equals(rhs.defaultAssignee))))&&((this.hourlyRate == rhs.hourlyRate)||((this.hourlyRate!= null)&&this.hourlyRate.equals(rhs.hourlyRate))));
    }

}
