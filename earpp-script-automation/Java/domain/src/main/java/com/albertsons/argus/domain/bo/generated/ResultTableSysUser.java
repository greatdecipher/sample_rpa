
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
    "calendar_integration",
    "last_position_update",
    "u_it",
    "sys_updated_on",
    "building",
    "u_concur",
    "u_irg_unit2_swy",
    "u_licensed_project_number",
    "u_job_code",
    "u_work_info",
    "sso_source",
    "u_custom4_swy_custom",
    "state",
    "vip",
    "sys_created_by",
    "zip",
    "time_format",
    "u_management_level",
    "last_login",
    "active",
    "u_bi_manager",
    "u_peoplesoft_division",
    "u_cliqbook_travel",
    "u_personal_phone_number",
    "cost_center",
    "phone",
    "u_employee_dept_name",
    "employee_number",
    "u_contractor",
    "u_created_integration",
    "u_labor_agreement",
    "u_facility_name",
    "gender",
    "city",
    "u_rx_phone_number",
    "user_name",
    "latitude",
    "sys_class_name",
    "u_hr_division_administrator",
    "u_employee_type",
    "u_it_vp",
    "email",
    "manager",
    "locked_out",
    "last_name",
    "photo",
    "avatar",
    "u_set_preferred_name",
    "u_paygroup",
    "on_schedule",
    "date_format",
    "u_peoplesoft_job_description",
    "u_exp_user",
    "country",
    "last_login_time",
    "u_division",
    "u_personal_email_address",
    "u_employee_email",
    "u_test_data",
    "u_peoplesoft_paygroup_formatted",
    "u_org_unit3_swy",
    "u_peoplesoft_district",
    "source",
    "web_service_access_only",
    "u_concur_approver",
    "u_employee_job_code",
    "notification",
    "u_level_3",
    "u_level_2",
    "u_trx_type",
    "enable_multifactor_authn",
    "sys_updated_by",
    "u_approver_reviewer",
    "u_level_1",
    "u_org_unit1_swy",
    "u_emp_id_expense_report",
    "sys_created_on",
    "agent_status",
    "sys_domain",
    "u_location_code",
    "longitude",
    "u_avis_vendor_user",
    "u_facility_code",
    "u_employee_dept_code",
    "u_peoplesoft_location",
    "geolocation_tracked",
    "average_daily_fte",
    "u_director",
    "name",
    "u_ctry_code",
    "u_updated_integration",
    "u_expense_user",
    "u_login_id",
    "u_department",
    "u_legal_name",
    "failed_attempts",
    "u_licensed_group_name",
    "title",
    "sys_id",
    "internal_integration_user",
    "mobile_phone",
    "street",
    "u_licensed_project_name",
    "company",
    "department",
    "u_type",
    "first_name",
    "introduction",
    "preferred_language",
    "u_highlight_user_notes",
    "u_integration_reporting_account",
    "u_facility_address",
    "u_it_gvp_evp",
    "u_license_state",
    "sys_mod_count",
    "middle_name",
    "sys_tags",
    "time_zone",
    "u_peoplesoft_div",
    "schedule",
    "u_vp",
    "u_remote",
    "u_supports_paygroups",
    "u_job_name",
    "u_preserve_user",
    "location",
    "u_custom_concur_expense"
})
@Generated("jsonschema2pojo")
public class ResultTableSysUser {

    @JsonProperty("calendar_integration")
    private String calendarIntegration;
    @JsonProperty("last_position_update")
    private String lastPositionUpdate;
    @JsonProperty("u_it")
    private String uIt;
    @JsonProperty("sys_updated_on")
    private String sysUpdatedOn;
    @JsonProperty("building")
    private String building;
    @JsonProperty("u_concur")
    private String uConcur;
    @JsonProperty("u_irg_unit2_swy")
    private String uIrgUnit2Swy;
    @JsonProperty("u_licensed_project_number")
    private String uLicensedProjectNumber;
    @JsonProperty("u_job_code")
    private String uJobCode;
    @JsonProperty("u_work_info")
    private String uWorkInfo;
    @JsonProperty("sso_source")
    private String ssoSource;
    @JsonProperty("u_custom4_swy_custom")
    private String uCustom4SwyCustom;
    @JsonProperty("state")
    private String state;
    @JsonProperty("vip")
    private String vip;
    @JsonProperty("sys_created_by")
    private String sysCreatedBy;
    @JsonProperty("zip")
    private String zip;
    @JsonProperty("time_format")
    private String timeFormat;
    @JsonProperty("u_management_level")
    private String uManagementLevel;
    @JsonProperty("last_login")
    private String lastLogin;
    @JsonProperty("active")
    private String active;
    @JsonProperty("u_bi_manager")
    private String uBiManager;
    @JsonProperty("u_peoplesoft_division")
    private String uPeoplesoftDivision;
    @JsonProperty("u_cliqbook_travel")
    private String uCliqbookTravel;
    @JsonProperty("u_personal_phone_number")
    private String uPersonalPhoneNumber;
    @JsonProperty("cost_center")
    private CostCenter costCenter;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("u_employee_dept_name")
    private String uEmployeeDeptName;
    @JsonProperty("employee_number")
    private String employeeNumber;
    @JsonProperty("u_contractor")
    private String uContractor;
    @JsonProperty("u_created_integration")
    private String uCreatedIntegration;
    @JsonProperty("u_labor_agreement")
    private String uLaborAgreement;
    @JsonProperty("u_facility_name")
    private String uFacilityName;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("city")
    private String city;
    @JsonProperty("u_rx_phone_number")
    private String uRxPhoneNumber;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("sys_class_name")
    private String sysClassName;
    @JsonProperty("u_hr_division_administrator")
    private String uHrDivisionAdministrator;
    @JsonProperty("u_employee_type")
    private String uEmployeeType;
    @JsonProperty("u_it_vp")
    private String uItVp;
    @JsonProperty("email")
    private String email;
    @JsonProperty("manager")
    private Manager manager;
    @JsonProperty("locked_out")
    private String lockedOut;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("u_set_preferred_name")
    private String uSetPreferredName;
    @JsonProperty("u_paygroup")
    private String uPaygroup;
    @JsonProperty("on_schedule")
    private String onSchedule;
    @JsonProperty("date_format")
    private String dateFormat;
    @JsonProperty("u_peoplesoft_job_description")
    private String uPeoplesoftJobDescription;
    @JsonProperty("u_exp_user")
    private String uExpUser;
    @JsonProperty("country")
    private String country;
    @JsonProperty("last_login_time")
    private String lastLoginTime;
    @JsonProperty("u_division")
    private String uDivision;
    @JsonProperty("u_personal_email_address")
    private String uPersonalEmailAddress;
    @JsonProperty("u_employee_email")
    private String uEmployeeEmail;
    @JsonProperty("u_test_data")
    private String uTestData;
    @JsonProperty("u_peoplesoft_paygroup_formatted")
    private String uPeoplesoftPaygroupFormatted;
    @JsonProperty("u_org_unit3_swy")
    private String uOrgUnit3Swy;
    @JsonProperty("u_peoplesoft_district")
    private String uPeoplesoftDistrict;
    @JsonProperty("source")
    private String source;
    @JsonProperty("web_service_access_only")
    private String webServiceAccessOnly;
    @JsonProperty("u_concur_approver")
    private String uConcurApprover;
    @JsonProperty("u_employee_job_code")
    private String uEmployeeJobCode;
    @JsonProperty("notification")
    private String notification;
    @JsonProperty("u_level_3")
    private String uLevel3;
    @JsonProperty("u_level_2")
    private String uLevel2;
    @JsonProperty("u_trx_type")
    private String uTrxType;
    @JsonProperty("enable_multifactor_authn")
    private String enableMultifactorAuthn;
    @JsonProperty("sys_updated_by")
    private String sysUpdatedBy;
    @JsonProperty("u_approver_reviewer")
    private String uApproverReviewer;
    @JsonProperty("u_level_1")
    private String uLevel1;
    @JsonProperty("u_org_unit1_swy")
    private String uOrgUnit1Swy;
    @JsonProperty("u_emp_id_expense_report")
    private String uEmpIdExpenseReport;
    @JsonProperty("sys_created_on")
    private String sysCreatedOn;
    @JsonProperty("agent_status")
    private String agentStatus;
    @JsonProperty("sys_domain")
    private SysDomain sysDomain;
    @JsonProperty("u_location_code")
    private String uLocationCode;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("u_avis_vendor_user")
    private String uAvisVendorUser;
    @JsonProperty("u_facility_code")
    private String uFacilityCode;
    @JsonProperty("u_employee_dept_code")
    private String uEmployeeDeptCode;
    @JsonProperty("u_peoplesoft_location")
    private String uPeoplesoftLocation;
    @JsonProperty("geolocation_tracked")
    private String geolocationTracked;
    @JsonProperty("average_daily_fte")
    private String averageDailyFte;
    @JsonProperty("u_director")
    private UDirector uDirector;
    @JsonProperty("name")
    private String name;
    @JsonProperty("u_ctry_code")
    private String uCtryCode;
    @JsonProperty("u_updated_integration")
    private String uUpdatedIntegration;
    @JsonProperty("u_expense_user")
    private String uExpenseUser;
    @JsonProperty("u_login_id")
    private String uLoginId;
    @JsonProperty("u_department")
    private String uDepartment;
    @JsonProperty("u_legal_name")
    private String uLegalName;
    @JsonProperty("failed_attempts")
    private String failedAttempts;
    @JsonProperty("u_licensed_group_name")
    private String uLicensedGroupName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("sys_id")
    private String sysId;
    @JsonProperty("internal_integration_user")
    private String internalIntegrationUser;
    @JsonProperty("mobile_phone")
    private String mobilePhone;
    @JsonProperty("street")
    private String street;
    @JsonProperty("u_licensed_project_name")
    private String uLicensedProjectName;
    @JsonProperty("company")
    private Company company;
    @JsonProperty("department")
    private String department;
    @JsonProperty("u_type")
    private String uType;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("introduction")
    private String introduction;
    @JsonProperty("preferred_language")
    private String preferredLanguage;
    @JsonProperty("u_highlight_user_notes")
    private String uHighlightUserNotes;
    @JsonProperty("u_integration_reporting_account")
    private String uIntegrationReportingAccount;
    @JsonProperty("u_facility_address")
    private String uFacilityAddress;
    @JsonProperty("u_it_gvp_evp")
    private UItGvpEvp uItGvpEvp;
    @JsonProperty("u_license_state")
    private String uLicenseState;
    @JsonProperty("sys_mod_count")
    private String sysModCount;
    @JsonProperty("middle_name")
    private String middleName;
    @JsonProperty("sys_tags")
    private String sysTags;
    @JsonProperty("time_zone")
    private String timeZone;
    @JsonProperty("u_peoplesoft_div")
    private String uPeoplesoftDiv;
    @JsonProperty("schedule")
    private String schedule;
    @JsonProperty("u_vp")
    private UVp uVp;
    @JsonProperty("u_remote")
    private String uRemote;
    @JsonProperty("u_supports_paygroups")
    private String uSupportsPaygroups;
    @JsonProperty("u_job_name")
    private String uJobName;
    @JsonProperty("u_preserve_user")
    private String uPreserveUser;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("u_custom_concur_expense")
    private String uCustomConcurExpense;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("calendar_integration")
    public String getCalendarIntegration() {
        return calendarIntegration;
    }

    @JsonProperty("calendar_integration")
    public void setCalendarIntegration(String calendarIntegration) {
        this.calendarIntegration = calendarIntegration;
    }

    @JsonProperty("last_position_update")
    public String getLastPositionUpdate() {
        return lastPositionUpdate;
    }

    @JsonProperty("last_position_update")
    public void setLastPositionUpdate(String lastPositionUpdate) {
        this.lastPositionUpdate = lastPositionUpdate;
    }

    @JsonProperty("u_it")
    public String getuIt() {
        return uIt;
    }

    @JsonProperty("u_it")
    public void setuIt(String uIt) {
        this.uIt = uIt;
    }

    @JsonProperty("sys_updated_on")
    public String getSysUpdatedOn() {
        return sysUpdatedOn;
    }

    @JsonProperty("sys_updated_on")
    public void setSysUpdatedOn(String sysUpdatedOn) {
        this.sysUpdatedOn = sysUpdatedOn;
    }

    @JsonProperty("building")
    public String getBuilding() {
        return building;
    }

    @JsonProperty("building")
    public void setBuilding(String building) {
        this.building = building;
    }

    @JsonProperty("u_concur")
    public String getuConcur() {
        return uConcur;
    }

    @JsonProperty("u_concur")
    public void setuConcur(String uConcur) {
        this.uConcur = uConcur;
    }

    @JsonProperty("u_irg_unit2_swy")
    public String getuIrgUnit2Swy() {
        return uIrgUnit2Swy;
    }

    @JsonProperty("u_irg_unit2_swy")
    public void setuIrgUnit2Swy(String uIrgUnit2Swy) {
        this.uIrgUnit2Swy = uIrgUnit2Swy;
    }

    @JsonProperty("u_licensed_project_number")
    public String getuLicensedProjectNumber() {
        return uLicensedProjectNumber;
    }

    @JsonProperty("u_licensed_project_number")
    public void setuLicensedProjectNumber(String uLicensedProjectNumber) {
        this.uLicensedProjectNumber = uLicensedProjectNumber;
    }

    @JsonProperty("u_job_code")
    public String getuJobCode() {
        return uJobCode;
    }

    @JsonProperty("u_job_code")
    public void setuJobCode(String uJobCode) {
        this.uJobCode = uJobCode;
    }

    @JsonProperty("u_work_info")
    public String getuWorkInfo() {
        return uWorkInfo;
    }

    @JsonProperty("u_work_info")
    public void setuWorkInfo(String uWorkInfo) {
        this.uWorkInfo = uWorkInfo;
    }

    @JsonProperty("sso_source")
    public String getSsoSource() {
        return ssoSource;
    }

    @JsonProperty("sso_source")
    public void setSsoSource(String ssoSource) {
        this.ssoSource = ssoSource;
    }

    @JsonProperty("u_custom4_swy_custom")
    public String getuCustom4SwyCustom() {
        return uCustom4SwyCustom;
    }

    @JsonProperty("u_custom4_swy_custom")
    public void setuCustom4SwyCustom(String uCustom4SwyCustom) {
        this.uCustom4SwyCustom = uCustom4SwyCustom;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("vip")
    public String getVip() {
        return vip;
    }

    @JsonProperty("vip")
    public void setVip(String vip) {
        this.vip = vip;
    }

    @JsonProperty("sys_created_by")
    public String getSysCreatedBy() {
        return sysCreatedBy;
    }

    @JsonProperty("sys_created_by")
    public void setSysCreatedBy(String sysCreatedBy) {
        this.sysCreatedBy = sysCreatedBy;
    }

    @JsonProperty("zip")
    public String getZip() {
        return zip;
    }

    @JsonProperty("zip")
    public void setZip(String zip) {
        this.zip = zip;
    }

    @JsonProperty("time_format")
    public String getTimeFormat() {
        return timeFormat;
    }

    @JsonProperty("time_format")
    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    @JsonProperty("u_management_level")
    public String getuManagementLevel() {
        return uManagementLevel;
    }

    @JsonProperty("u_management_level")
    public void setuManagementLevel(String uManagementLevel) {
        this.uManagementLevel = uManagementLevel;
    }

    @JsonProperty("last_login")
    public String getLastLogin() {
        return lastLogin;
    }

    @JsonProperty("last_login")
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    @JsonProperty("active")
    public String getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(String active) {
        this.active = active;
    }

    @JsonProperty("u_bi_manager")
    public String getuBiManager() {
        return uBiManager;
    }

    @JsonProperty("u_bi_manager")
    public void setuBiManager(String uBiManager) {
        this.uBiManager = uBiManager;
    }

    @JsonProperty("u_peoplesoft_division")
    public String getuPeoplesoftDivision() {
        return uPeoplesoftDivision;
    }

    @JsonProperty("u_peoplesoft_division")
    public void setuPeoplesoftDivision(String uPeoplesoftDivision) {
        this.uPeoplesoftDivision = uPeoplesoftDivision;
    }

    @JsonProperty("u_cliqbook_travel")
    public String getuCliqbookTravel() {
        return uCliqbookTravel;
    }

    @JsonProperty("u_cliqbook_travel")
    public void setuCliqbookTravel(String uCliqbookTravel) {
        this.uCliqbookTravel = uCliqbookTravel;
    }

    @JsonProperty("u_personal_phone_number")
    public String getuPersonalPhoneNumber() {
        return uPersonalPhoneNumber;
    }

    @JsonProperty("u_personal_phone_number")
    public void setuPersonalPhoneNumber(String uPersonalPhoneNumber) {
        this.uPersonalPhoneNumber = uPersonalPhoneNumber;
    }

    @JsonProperty("cost_center")
    public CostCenter getCostCenter() {
        return costCenter;
    }

    @JsonProperty("cost_center")
    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("u_employee_dept_name")
    public String getuEmployeeDeptName() {
        return uEmployeeDeptName;
    }

    @JsonProperty("u_employee_dept_name")
    public void setuEmployeeDeptName(String uEmployeeDeptName) {
        this.uEmployeeDeptName = uEmployeeDeptName;
    }

    @JsonProperty("employee_number")
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    @JsonProperty("employee_number")
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    @JsonProperty("u_contractor")
    public String getuContractor() {
        return uContractor;
    }

    @JsonProperty("u_contractor")
    public void setuContractor(String uContractor) {
        this.uContractor = uContractor;
    }

    @JsonProperty("u_created_integration")
    public String getuCreatedIntegration() {
        return uCreatedIntegration;
    }

    @JsonProperty("u_created_integration")
    public void setuCreatedIntegration(String uCreatedIntegration) {
        this.uCreatedIntegration = uCreatedIntegration;
    }

    @JsonProperty("u_labor_agreement")
    public String getuLaborAgreement() {
        return uLaborAgreement;
    }

    @JsonProperty("u_labor_agreement")
    public void setuLaborAgreement(String uLaborAgreement) {
        this.uLaborAgreement = uLaborAgreement;
    }

    @JsonProperty("u_facility_name")
    public String getuFacilityName() {
        return uFacilityName;
    }

    @JsonProperty("u_facility_name")
    public void setuFacilityName(String uFacilityName) {
        this.uFacilityName = uFacilityName;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("u_rx_phone_number")
    public String getuRxPhoneNumber() {
        return uRxPhoneNumber;
    }

    @JsonProperty("u_rx_phone_number")
    public void setuRxPhoneNumber(String uRxPhoneNumber) {
        this.uRxPhoneNumber = uRxPhoneNumber;
    }

    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("sys_class_name")
    public String getSysClassName() {
        return sysClassName;
    }

    @JsonProperty("sys_class_name")
    public void setSysClassName(String sysClassName) {
        this.sysClassName = sysClassName;
    }

    @JsonProperty("u_hr_division_administrator")
    public String getuHrDivisionAdministrator() {
        return uHrDivisionAdministrator;
    }

    @JsonProperty("u_hr_division_administrator")
    public void setuHrDivisionAdministrator(String uHrDivisionAdministrator) {
        this.uHrDivisionAdministrator = uHrDivisionAdministrator;
    }

    @JsonProperty("u_employee_type")
    public String getuEmployeeType() {
        return uEmployeeType;
    }

    @JsonProperty("u_employee_type")
    public void setuEmployeeType(String uEmployeeType) {
        this.uEmployeeType = uEmployeeType;
    }

    @JsonProperty("u_it_vp")
    public String getuItVp() {
        return uItVp;
    }

    @JsonProperty("u_it_vp")
    public void setuItVp(String uItVp) {
        this.uItVp = uItVp;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("manager")
    public Manager getManager() {
        return manager;
    }

    @JsonProperty("manager")
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @JsonProperty("locked_out")
    public String getLockedOut() {
        return lockedOut;
    }

    @JsonProperty("locked_out")
    public void setLockedOut(String lockedOut) {
        this.lockedOut = lockedOut;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("photo")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("u_set_preferred_name")
    public String getuSetPreferredName() {
        return uSetPreferredName;
    }

    @JsonProperty("u_set_preferred_name")
    public void setuSetPreferredName(String uSetPreferredName) {
        this.uSetPreferredName = uSetPreferredName;
    }

    @JsonProperty("u_paygroup")
    public String getuPaygroup() {
        return uPaygroup;
    }

    @JsonProperty("u_paygroup")
    public void setuPaygroup(String uPaygroup) {
        this.uPaygroup = uPaygroup;
    }

    @JsonProperty("on_schedule")
    public String getOnSchedule() {
        return onSchedule;
    }

    @JsonProperty("on_schedule")
    public void setOnSchedule(String onSchedule) {
        this.onSchedule = onSchedule;
    }

    @JsonProperty("date_format")
    public String getDateFormat() {
        return dateFormat;
    }

    @JsonProperty("date_format")
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @JsonProperty("u_peoplesoft_job_description")
    public String getuPeoplesoftJobDescription() {
        return uPeoplesoftJobDescription;
    }

    @JsonProperty("u_peoplesoft_job_description")
    public void setuPeoplesoftJobDescription(String uPeoplesoftJobDescription) {
        this.uPeoplesoftJobDescription = uPeoplesoftJobDescription;
    }

    @JsonProperty("u_exp_user")
    public String getuExpUser() {
        return uExpUser;
    }

    @JsonProperty("u_exp_user")
    public void setuExpUser(String uExpUser) {
        this.uExpUser = uExpUser;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("last_login_time")
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    @JsonProperty("last_login_time")
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @JsonProperty("u_division")
    public String getuDivision() {
        return uDivision;
    }

    @JsonProperty("u_division")
    public void setuDivision(String uDivision) {
        this.uDivision = uDivision;
    }

    @JsonProperty("u_personal_email_address")
    public String getuPersonalEmailAddress() {
        return uPersonalEmailAddress;
    }

    @JsonProperty("u_personal_email_address")
    public void setuPersonalEmailAddress(String uPersonalEmailAddress) {
        this.uPersonalEmailAddress = uPersonalEmailAddress;
    }

    @JsonProperty("u_employee_email")
    public String getuEmployeeEmail() {
        return uEmployeeEmail;
    }

    @JsonProperty("u_employee_email")
    public void setuEmployeeEmail(String uEmployeeEmail) {
        this.uEmployeeEmail = uEmployeeEmail;
    }

    @JsonProperty("u_test_data")
    public String getuTestData() {
        return uTestData;
    }

    @JsonProperty("u_test_data")
    public void setuTestData(String uTestData) {
        this.uTestData = uTestData;
    }

    @JsonProperty("u_peoplesoft_paygroup_formatted")
    public String getuPeoplesoftPaygroupFormatted() {
        return uPeoplesoftPaygroupFormatted;
    }

    @JsonProperty("u_peoplesoft_paygroup_formatted")
    public void setuPeoplesoftPaygroupFormatted(String uPeoplesoftPaygroupFormatted) {
        this.uPeoplesoftPaygroupFormatted = uPeoplesoftPaygroupFormatted;
    }

    @JsonProperty("u_org_unit3_swy")
    public String getuOrgUnit3Swy() {
        return uOrgUnit3Swy;
    }

    @JsonProperty("u_org_unit3_swy")
    public void setuOrgUnit3Swy(String uOrgUnit3Swy) {
        this.uOrgUnit3Swy = uOrgUnit3Swy;
    }

    @JsonProperty("u_peoplesoft_district")
    public String getuPeoplesoftDistrict() {
        return uPeoplesoftDistrict;
    }

    @JsonProperty("u_peoplesoft_district")
    public void setuPeoplesoftDistrict(String uPeoplesoftDistrict) {
        this.uPeoplesoftDistrict = uPeoplesoftDistrict;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("web_service_access_only")
    public String getWebServiceAccessOnly() {
        return webServiceAccessOnly;
    }

    @JsonProperty("web_service_access_only")
    public void setWebServiceAccessOnly(String webServiceAccessOnly) {
        this.webServiceAccessOnly = webServiceAccessOnly;
    }

    @JsonProperty("u_concur_approver")
    public String getuConcurApprover() {
        return uConcurApprover;
    }

    @JsonProperty("u_concur_approver")
    public void setuConcurApprover(String uConcurApprover) {
        this.uConcurApprover = uConcurApprover;
    }

    @JsonProperty("u_employee_job_code")
    public String getuEmployeeJobCode() {
        return uEmployeeJobCode;
    }

    @JsonProperty("u_employee_job_code")
    public void setuEmployeeJobCode(String uEmployeeJobCode) {
        this.uEmployeeJobCode = uEmployeeJobCode;
    }

    @JsonProperty("notification")
    public String getNotification() {
        return notification;
    }

    @JsonProperty("notification")
    public void setNotification(String notification) {
        this.notification = notification;
    }

    @JsonProperty("u_level_3")
    public String getuLevel3() {
        return uLevel3;
    }

    @JsonProperty("u_level_3")
    public void setuLevel3(String uLevel3) {
        this.uLevel3 = uLevel3;
    }

    @JsonProperty("u_level_2")
    public String getuLevel2() {
        return uLevel2;
    }

    @JsonProperty("u_level_2")
    public void setuLevel2(String uLevel2) {
        this.uLevel2 = uLevel2;
    }

    @JsonProperty("u_trx_type")
    public String getuTrxType() {
        return uTrxType;
    }

    @JsonProperty("u_trx_type")
    public void setuTrxType(String uTrxType) {
        this.uTrxType = uTrxType;
    }

    @JsonProperty("enable_multifactor_authn")
    public String getEnableMultifactorAuthn() {
        return enableMultifactorAuthn;
    }

    @JsonProperty("enable_multifactor_authn")
    public void setEnableMultifactorAuthn(String enableMultifactorAuthn) {
        this.enableMultifactorAuthn = enableMultifactorAuthn;
    }

    @JsonProperty("sys_updated_by")
    public String getSysUpdatedBy() {
        return sysUpdatedBy;
    }

    @JsonProperty("sys_updated_by")
    public void setSysUpdatedBy(String sysUpdatedBy) {
        this.sysUpdatedBy = sysUpdatedBy;
    }

    @JsonProperty("u_approver_reviewer")
    public String getuApproverReviewer() {
        return uApproverReviewer;
    }

    @JsonProperty("u_approver_reviewer")
    public void setuApproverReviewer(String uApproverReviewer) {
        this.uApproverReviewer = uApproverReviewer;
    }

    @JsonProperty("u_level_1")
    public String getuLevel1() {
        return uLevel1;
    }

    @JsonProperty("u_level_1")
    public void setuLevel1(String uLevel1) {
        this.uLevel1 = uLevel1;
    }

    @JsonProperty("u_org_unit1_swy")
    public String getuOrgUnit1Swy() {
        return uOrgUnit1Swy;
    }

    @JsonProperty("u_org_unit1_swy")
    public void setuOrgUnit1Swy(String uOrgUnit1Swy) {
        this.uOrgUnit1Swy = uOrgUnit1Swy;
    }

    @JsonProperty("u_emp_id_expense_report")
    public String getuEmpIdExpenseReport() {
        return uEmpIdExpenseReport;
    }

    @JsonProperty("u_emp_id_expense_report")
    public void setuEmpIdExpenseReport(String uEmpIdExpenseReport) {
        this.uEmpIdExpenseReport = uEmpIdExpenseReport;
    }

    @JsonProperty("sys_created_on")
    public String getSysCreatedOn() {
        return sysCreatedOn;
    }

    @JsonProperty("sys_created_on")
    public void setSysCreatedOn(String sysCreatedOn) {
        this.sysCreatedOn = sysCreatedOn;
    }

    @JsonProperty("agent_status")
    public String getAgentStatus() {
        return agentStatus;
    }

    @JsonProperty("agent_status")
    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    @JsonProperty("sys_domain")
    public SysDomain getSysDomain() {
        return sysDomain;
    }

    @JsonProperty("sys_domain")
    public void setSysDomain(SysDomain sysDomain) {
        this.sysDomain = sysDomain;
    }

    @JsonProperty("u_location_code")
    public String getuLocationCode() {
        return uLocationCode;
    }

    @JsonProperty("u_location_code")
    public void setuLocationCode(String uLocationCode) {
        this.uLocationCode = uLocationCode;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("u_avis_vendor_user")
    public String getuAvisVendorUser() {
        return uAvisVendorUser;
    }

    @JsonProperty("u_avis_vendor_user")
    public void setuAvisVendorUser(String uAvisVendorUser) {
        this.uAvisVendorUser = uAvisVendorUser;
    }

    @JsonProperty("u_facility_code")
    public String getuFacilityCode() {
        return uFacilityCode;
    }

    @JsonProperty("u_facility_code")
    public void setuFacilityCode(String uFacilityCode) {
        this.uFacilityCode = uFacilityCode;
    }

    @JsonProperty("u_employee_dept_code")
    public String getuEmployeeDeptCode() {
        return uEmployeeDeptCode;
    }

    @JsonProperty("u_employee_dept_code")
    public void setuEmployeeDeptCode(String uEmployeeDeptCode) {
        this.uEmployeeDeptCode = uEmployeeDeptCode;
    }

    @JsonProperty("u_peoplesoft_location")
    public String getuPeoplesoftLocation() {
        return uPeoplesoftLocation;
    }

    @JsonProperty("u_peoplesoft_location")
    public void setuPeoplesoftLocation(String uPeoplesoftLocation) {
        this.uPeoplesoftLocation = uPeoplesoftLocation;
    }

    @JsonProperty("geolocation_tracked")
    public String getGeolocationTracked() {
        return geolocationTracked;
    }

    @JsonProperty("geolocation_tracked")
    public void setGeolocationTracked(String geolocationTracked) {
        this.geolocationTracked = geolocationTracked;
    }

    @JsonProperty("average_daily_fte")
    public String getAverageDailyFte() {
        return averageDailyFte;
    }

    @JsonProperty("average_daily_fte")
    public void setAverageDailyFte(String averageDailyFte) {
        this.averageDailyFte = averageDailyFte;
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

    @JsonProperty("u_ctry_code")
    public String getuCtryCode() {
        return uCtryCode;
    }

    @JsonProperty("u_ctry_code")
    public void setuCtryCode(String uCtryCode) {
        this.uCtryCode = uCtryCode;
    }

    @JsonProperty("u_updated_integration")
    public String getuUpdatedIntegration() {
        return uUpdatedIntegration;
    }

    @JsonProperty("u_updated_integration")
    public void setuUpdatedIntegration(String uUpdatedIntegration) {
        this.uUpdatedIntegration = uUpdatedIntegration;
    }

    @JsonProperty("u_expense_user")
    public String getuExpenseUser() {
        return uExpenseUser;
    }

    @JsonProperty("u_expense_user")
    public void setuExpenseUser(String uExpenseUser) {
        this.uExpenseUser = uExpenseUser;
    }

    @JsonProperty("u_login_id")
    public String getuLoginId() {
        return uLoginId;
    }

    @JsonProperty("u_login_id")
    public void setuLoginId(String uLoginId) {
        this.uLoginId = uLoginId;
    }

    @JsonProperty("u_department")
    public String getuDepartment() {
        return uDepartment;
    }

    @JsonProperty("u_department")
    public void setuDepartment(String uDepartment) {
        this.uDepartment = uDepartment;
    }

    @JsonProperty("u_legal_name")
    public String getuLegalName() {
        return uLegalName;
    }

    @JsonProperty("u_legal_name")
    public void setuLegalName(String uLegalName) {
        this.uLegalName = uLegalName;
    }

    @JsonProperty("failed_attempts")
    public String getFailedAttempts() {
        return failedAttempts;
    }

    @JsonProperty("failed_attempts")
    public void setFailedAttempts(String failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    @JsonProperty("u_licensed_group_name")
    public String getuLicensedGroupName() {
        return uLicensedGroupName;
    }

    @JsonProperty("u_licensed_group_name")
    public void setuLicensedGroupName(String uLicensedGroupName) {
        this.uLicensedGroupName = uLicensedGroupName;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("sys_id")
    public String getSysId() {
        return sysId;
    }

    @JsonProperty("sys_id")
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    @JsonProperty("internal_integration_user")
    public String getInternalIntegrationUser() {
        return internalIntegrationUser;
    }

    @JsonProperty("internal_integration_user")
    public void setInternalIntegrationUser(String internalIntegrationUser) {
        this.internalIntegrationUser = internalIntegrationUser;
    }

    @JsonProperty("mobile_phone")
    public String getMobilePhone() {
        return mobilePhone;
    }

    @JsonProperty("mobile_phone")
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @JsonProperty("street")
    public String getStreet() {
        return street;
    }

    @JsonProperty("street")
    public void setStreet(String street) {
        this.street = street;
    }

    @JsonProperty("u_licensed_project_name")
    public String getuLicensedProjectName() {
        return uLicensedProjectName;
    }

    @JsonProperty("u_licensed_project_name")
    public void setuLicensedProjectName(String uLicensedProjectName) {
        this.uLicensedProjectName = uLicensedProjectName;
    }

    @JsonProperty("company")
    public Company getCompany() {
        return company;
    }

    @JsonProperty("company")
    public void setCompany(Company company) {
        this.company = company;
    }

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("department")
    public void setDepartment(String department) {
        this.department = department;
    }

    @JsonProperty("u_type")
    public String getuType() {
        return uType;
    }

    @JsonProperty("u_type")
    public void setuType(String uType) {
        this.uType = uType;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("introduction")
    public String getIntroduction() {
        return introduction;
    }

    @JsonProperty("introduction")
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @JsonProperty("preferred_language")
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    @JsonProperty("preferred_language")
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    @JsonProperty("u_highlight_user_notes")
    public String getuHighlightUserNotes() {
        return uHighlightUserNotes;
    }

    @JsonProperty("u_highlight_user_notes")
    public void setuHighlightUserNotes(String uHighlightUserNotes) {
        this.uHighlightUserNotes = uHighlightUserNotes;
    }

    @JsonProperty("u_integration_reporting_account")
    public String getuIntegrationReportingAccount() {
        return uIntegrationReportingAccount;
    }

    @JsonProperty("u_integration_reporting_account")
    public void setuIntegrationReportingAccount(String uIntegrationReportingAccount) {
        this.uIntegrationReportingAccount = uIntegrationReportingAccount;
    }

    @JsonProperty("u_facility_address")
    public String getuFacilityAddress() {
        return uFacilityAddress;
    }

    @JsonProperty("u_facility_address")
    public void setuFacilityAddress(String uFacilityAddress) {
        this.uFacilityAddress = uFacilityAddress;
    }

    @JsonProperty("u_it_gvp_evp")
    public UItGvpEvp getuItGvpEvp() {
        return uItGvpEvp;
    }

    @JsonProperty("u_it_gvp_evp")
    public void setuItGvpEvp(UItGvpEvp uItGvpEvp) {
        this.uItGvpEvp = uItGvpEvp;
    }

    @JsonProperty("u_license_state")
    public String getuLicenseState() {
        return uLicenseState;
    }

    @JsonProperty("u_license_state")
    public void setuLicenseState(String uLicenseState) {
        this.uLicenseState = uLicenseState;
    }

    @JsonProperty("sys_mod_count")
    public String getSysModCount() {
        return sysModCount;
    }

    @JsonProperty("sys_mod_count")
    public void setSysModCount(String sysModCount) {
        this.sysModCount = sysModCount;
    }

    @JsonProperty("middle_name")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middle_name")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("sys_tags")
    public String getSysTags() {
        return sysTags;
    }

    @JsonProperty("sys_tags")
    public void setSysTags(String sysTags) {
        this.sysTags = sysTags;
    }

    @JsonProperty("time_zone")
    public String getTimeZone() {
        return timeZone;
    }

    @JsonProperty("time_zone")
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @JsonProperty("u_peoplesoft_div")
    public String getuPeoplesoftDiv() {
        return uPeoplesoftDiv;
    }

    @JsonProperty("u_peoplesoft_div")
    public void setuPeoplesoftDiv(String uPeoplesoftDiv) {
        this.uPeoplesoftDiv = uPeoplesoftDiv;
    }

    @JsonProperty("schedule")
    public String getSchedule() {
        return schedule;
    }

    @JsonProperty("schedule")
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @JsonProperty("u_vp")
    public UVp getuVp() {
        return uVp;
    }

    @JsonProperty("u_vp")
    public void setuVp(UVp uVp) {
        this.uVp = uVp;
    }

    @JsonProperty("u_remote")
    public String getuRemote() {
        return uRemote;
    }

    @JsonProperty("u_remote")
    public void setuRemote(String uRemote) {
        this.uRemote = uRemote;
    }

    @JsonProperty("u_supports_paygroups")
    public String getuSupportsPaygroups() {
        return uSupportsPaygroups;
    }

    @JsonProperty("u_supports_paygroups")
    public void setuSupportsPaygroups(String uSupportsPaygroups) {
        this.uSupportsPaygroups = uSupportsPaygroups;
    }

    @JsonProperty("u_job_name")
    public String getuJobName() {
        return uJobName;
    }

    @JsonProperty("u_job_name")
    public void setuJobName(String uJobName) {
        this.uJobName = uJobName;
    }

    @JsonProperty("u_preserve_user")
    public String getuPreserveUser() {
        return uPreserveUser;
    }

    @JsonProperty("u_preserve_user")
    public void setuPreserveUser(String uPreserveUser) {
        this.uPreserveUser = uPreserveUser;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("u_custom_concur_expense")
    public String getuCustomConcurExpense() {
        return uCustomConcurExpense;
    }

    @JsonProperty("u_custom_concur_expense")
    public void setuCustomConcurExpense(String uCustomConcurExpense) {
        this.uCustomConcurExpense = uCustomConcurExpense;
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
        sb.append(ResultTableSysUser.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("calendarIntegration");
        sb.append('=');
        sb.append(((this.calendarIntegration == null)?"<null>":this.calendarIntegration));
        sb.append(',');
        sb.append("lastPositionUpdate");
        sb.append('=');
        sb.append(((this.lastPositionUpdate == null)?"<null>":this.lastPositionUpdate));
        sb.append(',');
        sb.append("uIt");
        sb.append('=');
        sb.append(((this.uIt == null)?"<null>":this.uIt));
        sb.append(',');
        sb.append("sysUpdatedOn");
        sb.append('=');
        sb.append(((this.sysUpdatedOn == null)?"<null>":this.sysUpdatedOn));
        sb.append(',');
        sb.append("building");
        sb.append('=');
        sb.append(((this.building == null)?"<null>":this.building));
        sb.append(',');
        sb.append("uConcur");
        sb.append('=');
        sb.append(((this.uConcur == null)?"<null>":this.uConcur));
        sb.append(',');
        sb.append("uIrgUnit2Swy");
        sb.append('=');
        sb.append(((this.uIrgUnit2Swy == null)?"<null>":this.uIrgUnit2Swy));
        sb.append(',');
        sb.append("uLicensedProjectNumber");
        sb.append('=');
        sb.append(((this.uLicensedProjectNumber == null)?"<null>":this.uLicensedProjectNumber));
        sb.append(',');
        sb.append("uJobCode");
        sb.append('=');
        sb.append(((this.uJobCode == null)?"<null>":this.uJobCode));
        sb.append(',');
        sb.append("uWorkInfo");
        sb.append('=');
        sb.append(((this.uWorkInfo == null)?"<null>":this.uWorkInfo));
        sb.append(',');
        sb.append("ssoSource");
        sb.append('=');
        sb.append(((this.ssoSource == null)?"<null>":this.ssoSource));
        sb.append(',');
        sb.append("uCustom4SwyCustom");
        sb.append('=');
        sb.append(((this.uCustom4SwyCustom == null)?"<null>":this.uCustom4SwyCustom));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null)?"<null>":this.state));
        sb.append(',');
        sb.append("vip");
        sb.append('=');
        sb.append(((this.vip == null)?"<null>":this.vip));
        sb.append(',');
        sb.append("sysCreatedBy");
        sb.append('=');
        sb.append(((this.sysCreatedBy == null)?"<null>":this.sysCreatedBy));
        sb.append(',');
        sb.append("zip");
        sb.append('=');
        sb.append(((this.zip == null)?"<null>":this.zip));
        sb.append(',');
        sb.append("timeFormat");
        sb.append('=');
        sb.append(((this.timeFormat == null)?"<null>":this.timeFormat));
        sb.append(',');
        sb.append("uManagementLevel");
        sb.append('=');
        sb.append(((this.uManagementLevel == null)?"<null>":this.uManagementLevel));
        sb.append(',');
        sb.append("lastLogin");
        sb.append('=');
        sb.append(((this.lastLogin == null)?"<null>":this.lastLogin));
        sb.append(',');
        sb.append("active");
        sb.append('=');
        sb.append(((this.active == null)?"<null>":this.active));
        sb.append(',');
        sb.append("uBiManager");
        sb.append('=');
        sb.append(((this.uBiManager == null)?"<null>":this.uBiManager));
        sb.append(',');
        sb.append("uPeoplesoftDivision");
        sb.append('=');
        sb.append(((this.uPeoplesoftDivision == null)?"<null>":this.uPeoplesoftDivision));
        sb.append(',');
        sb.append("uCliqbookTravel");
        sb.append('=');
        sb.append(((this.uCliqbookTravel == null)?"<null>":this.uCliqbookTravel));
        sb.append(',');
        sb.append("uPersonalPhoneNumber");
        sb.append('=');
        sb.append(((this.uPersonalPhoneNumber == null)?"<null>":this.uPersonalPhoneNumber));
        sb.append(',');
        sb.append("costCenter");
        sb.append('=');
        sb.append(((this.costCenter == null)?"<null>":this.costCenter));
        sb.append(',');
        sb.append("phone");
        sb.append('=');
        sb.append(((this.phone == null)?"<null>":this.phone));
        sb.append(',');
        sb.append("uEmployeeDeptName");
        sb.append('=');
        sb.append(((this.uEmployeeDeptName == null)?"<null>":this.uEmployeeDeptName));
        sb.append(',');
        sb.append("employeeNumber");
        sb.append('=');
        sb.append(((this.employeeNumber == null)?"<null>":this.employeeNumber));
        sb.append(',');
        sb.append("uContractor");
        sb.append('=');
        sb.append(((this.uContractor == null)?"<null>":this.uContractor));
        sb.append(',');
        sb.append("uCreatedIntegration");
        sb.append('=');
        sb.append(((this.uCreatedIntegration == null)?"<null>":this.uCreatedIntegration));
        sb.append(',');
        sb.append("uLaborAgreement");
        sb.append('=');
        sb.append(((this.uLaborAgreement == null)?"<null>":this.uLaborAgreement));
        sb.append(',');
        sb.append("uFacilityName");
        sb.append('=');
        sb.append(((this.uFacilityName == null)?"<null>":this.uFacilityName));
        sb.append(',');
        sb.append("gender");
        sb.append('=');
        sb.append(((this.gender == null)?"<null>":this.gender));
        sb.append(',');
        sb.append("city");
        sb.append('=');
        sb.append(((this.city == null)?"<null>":this.city));
        sb.append(',');
        sb.append("uRxPhoneNumber");
        sb.append('=');
        sb.append(((this.uRxPhoneNumber == null)?"<null>":this.uRxPhoneNumber));
        sb.append(',');
        sb.append("userName");
        sb.append('=');
        sb.append(((this.userName == null)?"<null>":this.userName));
        sb.append(',');
        sb.append("latitude");
        sb.append('=');
        sb.append(((this.latitude == null)?"<null>":this.latitude));
        sb.append(',');
        sb.append("sysClassName");
        sb.append('=');
        sb.append(((this.sysClassName == null)?"<null>":this.sysClassName));
        sb.append(',');
        sb.append("uHrDivisionAdministrator");
        sb.append('=');
        sb.append(((this.uHrDivisionAdministrator == null)?"<null>":this.uHrDivisionAdministrator));
        sb.append(',');
        sb.append("uEmployeeType");
        sb.append('=');
        sb.append(((this.uEmployeeType == null)?"<null>":this.uEmployeeType));
        sb.append(',');
        sb.append("uItVp");
        sb.append('=');
        sb.append(((this.uItVp == null)?"<null>":this.uItVp));
        sb.append(',');
        sb.append("email");
        sb.append('=');
        sb.append(((this.email == null)?"<null>":this.email));
        sb.append(',');
        sb.append("manager");
        sb.append('=');
        sb.append(((this.manager == null)?"<null>":this.manager));
        sb.append(',');
        sb.append("lockedOut");
        sb.append('=');
        sb.append(((this.lockedOut == null)?"<null>":this.lockedOut));
        sb.append(',');
        sb.append("lastName");
        sb.append('=');
        sb.append(((this.lastName == null)?"<null>":this.lastName));
        sb.append(',');
        sb.append("photo");
        sb.append('=');
        sb.append(((this.photo == null)?"<null>":this.photo));
        sb.append(',');
        sb.append("avatar");
        sb.append('=');
        sb.append(((this.avatar == null)?"<null>":this.avatar));
        sb.append(',');
        sb.append("uSetPreferredName");
        sb.append('=');
        sb.append(((this.uSetPreferredName == null)?"<null>":this.uSetPreferredName));
        sb.append(',');
        sb.append("uPaygroup");
        sb.append('=');
        sb.append(((this.uPaygroup == null)?"<null>":this.uPaygroup));
        sb.append(',');
        sb.append("onSchedule");
        sb.append('=');
        sb.append(((this.onSchedule == null)?"<null>":this.onSchedule));
        sb.append(',');
        sb.append("dateFormat");
        sb.append('=');
        sb.append(((this.dateFormat == null)?"<null>":this.dateFormat));
        sb.append(',');
        sb.append("uPeoplesoftJobDescription");
        sb.append('=');
        sb.append(((this.uPeoplesoftJobDescription == null)?"<null>":this.uPeoplesoftJobDescription));
        sb.append(',');
        sb.append("uExpUser");
        sb.append('=');
        sb.append(((this.uExpUser == null)?"<null>":this.uExpUser));
        sb.append(',');
        sb.append("country");
        sb.append('=');
        sb.append(((this.country == null)?"<null>":this.country));
        sb.append(',');
        sb.append("lastLoginTime");
        sb.append('=');
        sb.append(((this.lastLoginTime == null)?"<null>":this.lastLoginTime));
        sb.append(',');
        sb.append("uDivision");
        sb.append('=');
        sb.append(((this.uDivision == null)?"<null>":this.uDivision));
        sb.append(',');
        sb.append("uPersonalEmailAddress");
        sb.append('=');
        sb.append(((this.uPersonalEmailAddress == null)?"<null>":this.uPersonalEmailAddress));
        sb.append(',');
        sb.append("uEmployeeEmail");
        sb.append('=');
        sb.append(((this.uEmployeeEmail == null)?"<null>":this.uEmployeeEmail));
        sb.append(',');
        sb.append("uTestData");
        sb.append('=');
        sb.append(((this.uTestData == null)?"<null>":this.uTestData));
        sb.append(',');
        sb.append("uPeoplesoftPaygroupFormatted");
        sb.append('=');
        sb.append(((this.uPeoplesoftPaygroupFormatted == null)?"<null>":this.uPeoplesoftPaygroupFormatted));
        sb.append(',');
        sb.append("uOrgUnit3Swy");
        sb.append('=');
        sb.append(((this.uOrgUnit3Swy == null)?"<null>":this.uOrgUnit3Swy));
        sb.append(',');
        sb.append("uPeoplesoftDistrict");
        sb.append('=');
        sb.append(((this.uPeoplesoftDistrict == null)?"<null>":this.uPeoplesoftDistrict));
        sb.append(',');
        sb.append("source");
        sb.append('=');
        sb.append(((this.source == null)?"<null>":this.source));
        sb.append(',');
        sb.append("webServiceAccessOnly");
        sb.append('=');
        sb.append(((this.webServiceAccessOnly == null)?"<null>":this.webServiceAccessOnly));
        sb.append(',');
        sb.append("uConcurApprover");
        sb.append('=');
        sb.append(((this.uConcurApprover == null)?"<null>":this.uConcurApprover));
        sb.append(',');
        sb.append("uEmployeeJobCode");
        sb.append('=');
        sb.append(((this.uEmployeeJobCode == null)?"<null>":this.uEmployeeJobCode));
        sb.append(',');
        sb.append("notification");
        sb.append('=');
        sb.append(((this.notification == null)?"<null>":this.notification));
        sb.append(',');
        sb.append("uLevel3");
        sb.append('=');
        sb.append(((this.uLevel3 == null)?"<null>":this.uLevel3));
        sb.append(',');
        sb.append("uLevel2");
        sb.append('=');
        sb.append(((this.uLevel2 == null)?"<null>":this.uLevel2));
        sb.append(',');
        sb.append("uTrxType");
        sb.append('=');
        sb.append(((this.uTrxType == null)?"<null>":this.uTrxType));
        sb.append(',');
        sb.append("enableMultifactorAuthn");
        sb.append('=');
        sb.append(((this.enableMultifactorAuthn == null)?"<null>":this.enableMultifactorAuthn));
        sb.append(',');
        sb.append("sysUpdatedBy");
        sb.append('=');
        sb.append(((this.sysUpdatedBy == null)?"<null>":this.sysUpdatedBy));
        sb.append(',');
        sb.append("uApproverReviewer");
        sb.append('=');
        sb.append(((this.uApproverReviewer == null)?"<null>":this.uApproverReviewer));
        sb.append(',');
        sb.append("uLevel1");
        sb.append('=');
        sb.append(((this.uLevel1 == null)?"<null>":this.uLevel1));
        sb.append(',');
        sb.append("uOrgUnit1Swy");
        sb.append('=');
        sb.append(((this.uOrgUnit1Swy == null)?"<null>":this.uOrgUnit1Swy));
        sb.append(',');
        sb.append("uEmpIdExpenseReport");
        sb.append('=');
        sb.append(((this.uEmpIdExpenseReport == null)?"<null>":this.uEmpIdExpenseReport));
        sb.append(',');
        sb.append("sysCreatedOn");
        sb.append('=');
        sb.append(((this.sysCreatedOn == null)?"<null>":this.sysCreatedOn));
        sb.append(',');
        sb.append("agentStatus");
        sb.append('=');
        sb.append(((this.agentStatus == null)?"<null>":this.agentStatus));
        sb.append(',');
        sb.append("sysDomain");
        sb.append('=');
        sb.append(((this.sysDomain == null)?"<null>":this.sysDomain));
        sb.append(',');
        sb.append("uLocationCode");
        sb.append('=');
        sb.append(((this.uLocationCode == null)?"<null>":this.uLocationCode));
        sb.append(',');
        sb.append("longitude");
        sb.append('=');
        sb.append(((this.longitude == null)?"<null>":this.longitude));
        sb.append(',');
        sb.append("uAvisVendorUser");
        sb.append('=');
        sb.append(((this.uAvisVendorUser == null)?"<null>":this.uAvisVendorUser));
        sb.append(',');
        sb.append("uFacilityCode");
        sb.append('=');
        sb.append(((this.uFacilityCode == null)?"<null>":this.uFacilityCode));
        sb.append(',');
        sb.append("uEmployeeDeptCode");
        sb.append('=');
        sb.append(((this.uEmployeeDeptCode == null)?"<null>":this.uEmployeeDeptCode));
        sb.append(',');
        sb.append("uPeoplesoftLocation");
        sb.append('=');
        sb.append(((this.uPeoplesoftLocation == null)?"<null>":this.uPeoplesoftLocation));
        sb.append(',');
        sb.append("geolocationTracked");
        sb.append('=');
        sb.append(((this.geolocationTracked == null)?"<null>":this.geolocationTracked));
        sb.append(',');
        sb.append("averageDailyFte");
        sb.append('=');
        sb.append(((this.averageDailyFte == null)?"<null>":this.averageDailyFte));
        sb.append(',');
        sb.append("uDirector");
        sb.append('=');
        sb.append(((this.uDirector == null)?"<null>":this.uDirector));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("uCtryCode");
        sb.append('=');
        sb.append(((this.uCtryCode == null)?"<null>":this.uCtryCode));
        sb.append(',');
        sb.append("uUpdatedIntegration");
        sb.append('=');
        sb.append(((this.uUpdatedIntegration == null)?"<null>":this.uUpdatedIntegration));
        sb.append(',');
        sb.append("uExpenseUser");
        sb.append('=');
        sb.append(((this.uExpenseUser == null)?"<null>":this.uExpenseUser));
        sb.append(',');
        sb.append("uLoginId");
        sb.append('=');
        sb.append(((this.uLoginId == null)?"<null>":this.uLoginId));
        sb.append(',');
        sb.append("uDepartment");
        sb.append('=');
        sb.append(((this.uDepartment == null)?"<null>":this.uDepartment));
        sb.append(',');
        sb.append("uLegalName");
        sb.append('=');
        sb.append(((this.uLegalName == null)?"<null>":this.uLegalName));
        sb.append(',');
        sb.append("failedAttempts");
        sb.append('=');
        sb.append(((this.failedAttempts == null)?"<null>":this.failedAttempts));
        sb.append(',');
        sb.append("uLicensedGroupName");
        sb.append('=');
        sb.append(((this.uLicensedGroupName == null)?"<null>":this.uLicensedGroupName));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("sysId");
        sb.append('=');
        sb.append(((this.sysId == null)?"<null>":this.sysId));
        sb.append(',');
        sb.append("internalIntegrationUser");
        sb.append('=');
        sb.append(((this.internalIntegrationUser == null)?"<null>":this.internalIntegrationUser));
        sb.append(',');
        sb.append("mobilePhone");
        sb.append('=');
        sb.append(((this.mobilePhone == null)?"<null>":this.mobilePhone));
        sb.append(',');
        sb.append("street");
        sb.append('=');
        sb.append(((this.street == null)?"<null>":this.street));
        sb.append(',');
        sb.append("uLicensedProjectName");
        sb.append('=');
        sb.append(((this.uLicensedProjectName == null)?"<null>":this.uLicensedProjectName));
        sb.append(',');
        sb.append("company");
        sb.append('=');
        sb.append(((this.company == null)?"<null>":this.company));
        sb.append(',');
        sb.append("department");
        sb.append('=');
        sb.append(((this.department == null)?"<null>":this.department));
        sb.append(',');
        sb.append("uType");
        sb.append('=');
        sb.append(((this.uType == null)?"<null>":this.uType));
        sb.append(',');
        sb.append("firstName");
        sb.append('=');
        sb.append(((this.firstName == null)?"<null>":this.firstName));
        sb.append(',');
        sb.append("introduction");
        sb.append('=');
        sb.append(((this.introduction == null)?"<null>":this.introduction));
        sb.append(',');
        sb.append("preferredLanguage");
        sb.append('=');
        sb.append(((this.preferredLanguage == null)?"<null>":this.preferredLanguage));
        sb.append(',');
        sb.append("uHighlightUserNotes");
        sb.append('=');
        sb.append(((this.uHighlightUserNotes == null)?"<null>":this.uHighlightUserNotes));
        sb.append(',');
        sb.append("uIntegrationReportingAccount");
        sb.append('=');
        sb.append(((this.uIntegrationReportingAccount == null)?"<null>":this.uIntegrationReportingAccount));
        sb.append(',');
        sb.append("uFacilityAddress");
        sb.append('=');
        sb.append(((this.uFacilityAddress == null)?"<null>":this.uFacilityAddress));
        sb.append(',');
        sb.append("uItGvpEvp");
        sb.append('=');
        sb.append(((this.uItGvpEvp == null)?"<null>":this.uItGvpEvp));
        sb.append(',');
        sb.append("uLicenseState");
        sb.append('=');
        sb.append(((this.uLicenseState == null)?"<null>":this.uLicenseState));
        sb.append(',');
        sb.append("sysModCount");
        sb.append('=');
        sb.append(((this.sysModCount == null)?"<null>":this.sysModCount));
        sb.append(',');
        sb.append("middleName");
        sb.append('=');
        sb.append(((this.middleName == null)?"<null>":this.middleName));
        sb.append(',');
        sb.append("sysTags");
        sb.append('=');
        sb.append(((this.sysTags == null)?"<null>":this.sysTags));
        sb.append(',');
        sb.append("timeZone");
        sb.append('=');
        sb.append(((this.timeZone == null)?"<null>":this.timeZone));
        sb.append(',');
        sb.append("uPeoplesoftDiv");
        sb.append('=');
        sb.append(((this.uPeoplesoftDiv == null)?"<null>":this.uPeoplesoftDiv));
        sb.append(',');
        sb.append("schedule");
        sb.append('=');
        sb.append(((this.schedule == null)?"<null>":this.schedule));
        sb.append(',');
        sb.append("uVp");
        sb.append('=');
        sb.append(((this.uVp == null)?"<null>":this.uVp));
        sb.append(',');
        sb.append("uRemote");
        sb.append('=');
        sb.append(((this.uRemote == null)?"<null>":this.uRemote));
        sb.append(',');
        sb.append("uSupportsPaygroups");
        sb.append('=');
        sb.append(((this.uSupportsPaygroups == null)?"<null>":this.uSupportsPaygroups));
        sb.append(',');
        sb.append("uJobName");
        sb.append('=');
        sb.append(((this.uJobName == null)?"<null>":this.uJobName));
        sb.append(',');
        sb.append("uPreserveUser");
        sb.append('=');
        sb.append(((this.uPreserveUser == null)?"<null>":this.uPreserveUser));
        sb.append(',');
        sb.append("location");
        sb.append('=');
        sb.append(((this.location == null)?"<null>":this.location));
        sb.append(',');
        sb.append("uCustomConcurExpense");
        sb.append('=');
        sb.append(((this.uCustomConcurExpense == null)?"<null>":this.uCustomConcurExpense));
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
        result = ((result* 31)+((this.sysUpdatedBy == null)? 0 :this.sysUpdatedBy.hashCode()));
        result = ((result* 31)+((this.uPeoplesoftLocation == null)? 0 :this.uPeoplesoftLocation.hashCode()));
        result = ((result* 31)+((this.dateFormat == null)? 0 :this.dateFormat.hashCode()));
        result = ((result* 31)+((this.uPersonalPhoneNumber == null)? 0 :this.uPersonalPhoneNumber.hashCode()));
        result = ((result* 31)+((this.building == null)? 0 :this.building.hashCode()));
        result = ((result* 31)+((this.uVp == null)? 0 :this.uVp.hashCode()));
        result = ((result* 31)+((this.sysCreatedBy == null)? 0 :this.sysCreatedBy.hashCode()));
        result = ((result* 31)+((this.onSchedule == null)? 0 :this.onSchedule.hashCode()));
        result = ((result* 31)+((this.uLegalName == null)? 0 :this.uLegalName.hashCode()));
        result = ((result* 31)+((this.state == null)? 0 :this.state.hashCode()));
        result = ((result* 31)+((this.uLicensedProjectNumber == null)? 0 :this.uLicensedProjectNumber.hashCode()));
        result = ((result* 31)+((this.vip == null)? 0 :this.vip.hashCode()));
        result = ((result* 31)+((this.uTrxType == null)? 0 :this.uTrxType.hashCode()));
        result = ((result* 31)+((this.zip == null)? 0 :this.zip.hashCode()));
        result = ((result* 31)+((this.uFacilityName == null)? 0 :this.uFacilityName.hashCode()));
        result = ((result* 31)+((this.uIntegrationReportingAccount == null)? 0 :this.uIntegrationReportingAccount.hashCode()));
        result = ((result* 31)+((this.costCenter == null)? 0 :this.costCenter.hashCode()));
        result = ((result* 31)+((this.active == null)? 0 :this.active.hashCode()));
        result = ((result* 31)+((this.timeZone == null)? 0 :this.timeZone.hashCode()));
        result = ((result* 31)+((this.uPreserveUser == null)? 0 :this.uPreserveUser.hashCode()));
        result = ((result* 31)+((this.uPeoplesoftDiv == null)? 0 :this.uPeoplesoftDiv.hashCode()));
        result = ((result* 31)+((this.agentStatus == null)? 0 :this.agentStatus.hashCode()));
        result = ((result* 31)+((this.uOrgUnit1Swy == null)? 0 :this.uOrgUnit1Swy.hashCode()));
        result = ((result* 31)+((this.uEmployeeJobCode == null)? 0 :this.uEmployeeJobCode.hashCode()));
        result = ((result* 31)+((this.mobilePhone == null)? 0 :this.mobilePhone.hashCode()));
        result = ((result* 31)+((this.phone == null)? 0 :this.phone.hashCode()));
        result = ((result* 31)+((this.geolocationTracked == null)? 0 :this.geolocationTracked.hashCode()));
        result = ((result* 31)+((this.uEmployeeDeptCode == null)? 0 :this.uEmployeeDeptCode.hashCode()));
        result = ((result* 31)+((this.lockedOut == null)? 0 :this.lockedOut.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.uPeoplesoftPaygroupFormatted == null)? 0 :this.uPeoplesoftPaygroupFormatted.hashCode()));
        result = ((result* 31)+((this.internalIntegrationUser == null)? 0 :this.internalIntegrationUser.hashCode()));
        result = ((result* 31)+((this.lastName == null)? 0 :this.lastName.hashCode()));
        result = ((result* 31)+((this.uDepartment == null)? 0 :this.uDepartment.hashCode()));
        result = ((result* 31)+((this.gender == null)? 0 :this.gender.hashCode()));
        result = ((result* 31)+((this.city == null)? 0 :this.city.hashCode()));
        result = ((result* 31)+((this.sysId == null)? 0 :this.sysId.hashCode()));
        result = ((result* 31)+((this.uContractor == null)? 0 :this.uContractor.hashCode()));
        result = ((result* 31)+((this.latitude == null)? 0 :this.latitude.hashCode()));
        result = ((result* 31)+((this.uApproverReviewer == null)? 0 :this.uApproverReviewer.hashCode()));
        result = ((result* 31)+((this.uManagementLevel == null)? 0 :this.uManagementLevel.hashCode()));
        result = ((result* 31)+((this.uCustom4SwyCustom == null)? 0 :this.uCustom4SwyCustom.hashCode()));
        result = ((result* 31)+((this.uConcurApprover == null)? 0 :this.uConcurApprover.hashCode()));
        result = ((result* 31)+((this.uLicensedProjectName == null)? 0 :this.uLicensedProjectName.hashCode()));
        result = ((result* 31)+((this.uAvisVendorUser == null)? 0 :this.uAvisVendorUser.hashCode()));
        result = ((result* 31)+((this.uTestData == null)? 0 :this.uTestData.hashCode()));
        result = ((result* 31)+((this.uPaygroup == null)? 0 :this.uPaygroup.hashCode()));
        result = ((result* 31)+((this.uRxPhoneNumber == null)? 0 :this.uRxPhoneNumber.hashCode()));
        result = ((result* 31)+((this.uExpUser == null)? 0 :this.uExpUser.hashCode()));
        result = ((result* 31)+((this.sysModCount == null)? 0 :this.sysModCount.hashCode()));
        result = ((result* 31)+((this.email == null)? 0 :this.email.hashCode()));
        result = ((result* 31)+((this.manager == null)? 0 :this.manager.hashCode()));
        result = ((result* 31)+((this.uExpenseUser == null)? 0 :this.uExpenseUser.hashCode()));
        result = ((result* 31)+((this.sysClassName == null)? 0 :this.sysClassName.hashCode()));
        result = ((result* 31)+((this.photo == null)? 0 :this.photo.hashCode()));
        result = ((result* 31)+((this.lastPositionUpdate == null)? 0 :this.lastPositionUpdate.hashCode()));
        result = ((result* 31)+((this.avatar == null)? 0 :this.avatar.hashCode()));
        result = ((result* 31)+((this.uCreatedIntegration == null)? 0 :this.uCreatedIntegration.hashCode()));
        result = ((result* 31)+((this.uOrgUnit3Swy == null)? 0 :this.uOrgUnit3Swy.hashCode()));
        result = ((result* 31)+((this.uJobCode == null)? 0 :this.uJobCode.hashCode()));
        result = ((result* 31)+((this.lastLoginTime == null)? 0 :this.lastLoginTime.hashCode()));
        result = ((result* 31)+((this.uPeoplesoftDistrict == null)? 0 :this.uPeoplesoftDistrict.hashCode()));
        result = ((result* 31)+((this.averageDailyFte == null)? 0 :this.averageDailyFte.hashCode()));
        result = ((result* 31)+((this.uLocationCode == null)? 0 :this.uLocationCode.hashCode()));
        result = ((result* 31)+((this.uIt == null)? 0 :this.uIt.hashCode()));
        result = ((result* 31)+((this.lastLogin == null)? 0 :this.lastLogin.hashCode()));
        result = ((result* 31)+((this.country == null)? 0 :this.country.hashCode()));
        result = ((result* 31)+((this.uEmpIdExpenseReport == null)? 0 :this.uEmpIdExpenseReport.hashCode()));
        result = ((result* 31)+((this.sysUpdatedOn == null)? 0 :this.sysUpdatedOn.hashCode()));
        result = ((result* 31)+((this.uFacilityAddress == null)? 0 :this.uFacilityAddress.hashCode()));
        result = ((result* 31)+((this.source == null)? 0 :this.source.hashCode()));
        result = ((result* 31)+((this.uConcur == null)? 0 :this.uConcur.hashCode()));
        result = ((result* 31)+((this.employeeNumber == null)? 0 :this.employeeNumber.hashCode()));
        result = ((result* 31)+((this.notification == null)? 0 :this.notification.hashCode()));
        result = ((result* 31)+((this.uCliqbookTravel == null)? 0 :this.uCliqbookTravel.hashCode()));
        result = ((result* 31)+((this.webServiceAccessOnly == null)? 0 :this.webServiceAccessOnly.hashCode()));
        result = ((result* 31)+((this.uPeoplesoftDivision == null)? 0 :this.uPeoplesoftDivision.hashCode()));
        result = ((result* 31)+((this.sysCreatedOn == null)? 0 :this.sysCreatedOn.hashCode()));
        result = ((result* 31)+((this.uDivision == null)? 0 :this.uDivision.hashCode()));
        result = ((result* 31)+((this.longitude == null)? 0 :this.longitude.hashCode()));
        result = ((result* 31)+((this.ssoSource == null)? 0 :this.ssoSource.hashCode()));
        result = ((result* 31)+((this.enableMultifactorAuthn == null)? 0 :this.enableMultifactorAuthn.hashCode()));
        result = ((result* 31)+((this.uLoginId == null)? 0 :this.uLoginId.hashCode()));
        result = ((result* 31)+((this.uSetPreferredName == null)? 0 :this.uSetPreferredName.hashCode()));
        result = ((result* 31)+((this.uLevel3 == null)? 0 :this.uLevel3 .hashCode()));
        result = ((result* 31)+((this.uLevel2 == null)? 0 :this.uLevel2 .hashCode()));
        result = ((result* 31)+((this.uHighlightUserNotes == null)? 0 :this.uHighlightUserNotes.hashCode()));
        result = ((result* 31)+((this.uIrgUnit2Swy == null)? 0 :this.uIrgUnit2Swy.hashCode()));
        result = ((result* 31)+((this.uLevel1 == null)? 0 :this.uLevel1 .hashCode()));
        result = ((result* 31)+((this.uLaborAgreement == null)? 0 :this.uLaborAgreement.hashCode()));
        result = ((result* 31)+((this.firstName == null)? 0 :this.firstName.hashCode()));
        result = ((result* 31)+((this.uEmployeeType == null)? 0 :this.uEmployeeType.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.uEmployeeEmail == null)? 0 :this.uEmployeeEmail.hashCode()));
        result = ((result* 31)+((this.uCustomConcurExpense == null)? 0 :this.uCustomConcurExpense.hashCode()));
        result = ((result* 31)+((this.uCtryCode == null)? 0 :this.uCtryCode.hashCode()));
        result = ((result* 31)+((this.uEmployeeDeptName == null)? 0 :this.uEmployeeDeptName.hashCode()));
        result = ((result* 31)+((this.preferredLanguage == null)? 0 :this.preferredLanguage.hashCode()));
        result = ((result* 31)+((this.uUpdatedIntegration == null)? 0 :this.uUpdatedIntegration.hashCode()));
        result = ((result* 31)+((this.uLicenseState == null)? 0 :this.uLicenseState.hashCode()));
        result = ((result* 31)+((this.uItVp == null)? 0 :this.uItVp.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.calendarIntegration == null)? 0 :this.calendarIntegration.hashCode()));
        result = ((result* 31)+((this.street == null)? 0 :this.street.hashCode()));
        result = ((result* 31)+((this.uType == null)? 0 :this.uType.hashCode()));
        result = ((result* 31)+((this.uJobName == null)? 0 :this.uJobName.hashCode()));
        result = ((result* 31)+((this.company == null)? 0 :this.company.hashCode()));
        result = ((result* 31)+((this.uWorkInfo == null)? 0 :this.uWorkInfo.hashCode()));
        result = ((result* 31)+((this.department == null)? 0 :this.department.hashCode()));
        result = ((result* 31)+((this.uSupportsPaygroups == null)? 0 :this.uSupportsPaygroups.hashCode()));
        result = ((result* 31)+((this.introduction == null)? 0 :this.introduction.hashCode()));
        result = ((result* 31)+((this.uBiManager == null)? 0 :this.uBiManager.hashCode()));
        result = ((result* 31)+((this.uPeoplesoftJobDescription == null)? 0 :this.uPeoplesoftJobDescription.hashCode()));
        result = ((result* 31)+((this.sysTags == null)? 0 :this.sysTags.hashCode()));
        result = ((result* 31)+((this.failedAttempts == null)? 0 :this.failedAttempts.hashCode()));
        result = ((result* 31)+((this.uHrDivisionAdministrator == null)? 0 :this.uHrDivisionAdministrator.hashCode()));
        result = ((result* 31)+((this.uDirector == null)? 0 :this.uDirector.hashCode()));
        result = ((result* 31)+((this.uLicensedGroupName == null)? 0 :this.uLicensedGroupName.hashCode()));
        result = ((result* 31)+((this.uPersonalEmailAddress == null)? 0 :this.uPersonalEmailAddress.hashCode()));
        result = ((result* 31)+((this.userName == null)? 0 :this.userName.hashCode()));
        result = ((result* 31)+((this.schedule == null)? 0 :this.schedule.hashCode()));
        result = ((result* 31)+((this.uRemote == null)? 0 :this.uRemote.hashCode()));
        result = ((result* 31)+((this.uItGvpEvp == null)? 0 :this.uItGvpEvp.hashCode()));
        result = ((result* 31)+((this.timeFormat == null)? 0 :this.timeFormat.hashCode()));
        result = ((result* 31)+((this.uFacilityCode == null)? 0 :this.uFacilityCode.hashCode()));
        result = ((result* 31)+((this.middleName == null)? 0 :this.middleName.hashCode()));
        result = ((result* 31)+((this.location == null)? 0 :this.location.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ResultTableSysUser) == false) {
            return false;
        }
        ResultTableSysUser rhs = ((ResultTableSysUser) other);
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((this.sysDomain == rhs.sysDomain)||((this.sysDomain!= null)&&this.sysDomain.equals(rhs.sysDomain)))&&((this.sysUpdatedBy == rhs.sysUpdatedBy)||((this.sysUpdatedBy!= null)&&this.sysUpdatedBy.equals(rhs.sysUpdatedBy))))&&((this.uPeoplesoftLocation == rhs.uPeoplesoftLocation)||((this.uPeoplesoftLocation!= null)&&this.uPeoplesoftLocation.equals(rhs.uPeoplesoftLocation))))&&((this.dateFormat == rhs.dateFormat)||((this.dateFormat!= null)&&this.dateFormat.equals(rhs.dateFormat))))&&((this.uPersonalPhoneNumber == rhs.uPersonalPhoneNumber)||((this.uPersonalPhoneNumber!= null)&&this.uPersonalPhoneNumber.equals(rhs.uPersonalPhoneNumber))))&&((this.building == rhs.building)||((this.building!= null)&&this.building.equals(rhs.building))))&&((this.uVp == rhs.uVp)||((this.uVp!= null)&&this.uVp.equals(rhs.uVp))))&&((this.sysCreatedBy == rhs.sysCreatedBy)||((this.sysCreatedBy!= null)&&this.sysCreatedBy.equals(rhs.sysCreatedBy))))&&((this.onSchedule == rhs.onSchedule)||((this.onSchedule!= null)&&this.onSchedule.equals(rhs.onSchedule))))&&((this.uLegalName == rhs.uLegalName)||((this.uLegalName!= null)&&this.uLegalName.equals(rhs.uLegalName))))&&((this.state == rhs.state)||((this.state!= null)&&this.state.equals(rhs.state))))&&((this.uLicensedProjectNumber == rhs.uLicensedProjectNumber)||((this.uLicensedProjectNumber!= null)&&this.uLicensedProjectNumber.equals(rhs.uLicensedProjectNumber))))&&((this.vip == rhs.vip)||((this.vip!= null)&&this.vip.equals(rhs.vip))))&&((this.uTrxType == rhs.uTrxType)||((this.uTrxType!= null)&&this.uTrxType.equals(rhs.uTrxType))))&&((this.zip == rhs.zip)||((this.zip!= null)&&this.zip.equals(rhs.zip))))&&((this.uFacilityName == rhs.uFacilityName)||((this.uFacilityName!= null)&&this.uFacilityName.equals(rhs.uFacilityName))))&&((this.uIntegrationReportingAccount == rhs.uIntegrationReportingAccount)||((this.uIntegrationReportingAccount!= null)&&this.uIntegrationReportingAccount.equals(rhs.uIntegrationReportingAccount))))&&((this.costCenter == rhs.costCenter)||((this.costCenter!= null)&&this.costCenter.equals(rhs.costCenter))))&&((this.active == rhs.active)||((this.active!= null)&&this.active.equals(rhs.active))))&&((this.timeZone == rhs.timeZone)||((this.timeZone!= null)&&this.timeZone.equals(rhs.timeZone))))&&((this.uPreserveUser == rhs.uPreserveUser)||((this.uPreserveUser!= null)&&this.uPreserveUser.equals(rhs.uPreserveUser))))&&((this.uPeoplesoftDiv == rhs.uPeoplesoftDiv)||((this.uPeoplesoftDiv!= null)&&this.uPeoplesoftDiv.equals(rhs.uPeoplesoftDiv))))&&((this.agentStatus == rhs.agentStatus)||((this.agentStatus!= null)&&this.agentStatus.equals(rhs.agentStatus))))&&((this.uOrgUnit1Swy == rhs.uOrgUnit1Swy)||((this.uOrgUnit1Swy!= null)&&this.uOrgUnit1Swy.equals(rhs.uOrgUnit1Swy))))&&((this.uEmployeeJobCode == rhs.uEmployeeJobCode)||((this.uEmployeeJobCode!= null)&&this.uEmployeeJobCode.equals(rhs.uEmployeeJobCode))))&&((this.mobilePhone == rhs.mobilePhone)||((this.mobilePhone!= null)&&this.mobilePhone.equals(rhs.mobilePhone))))&&((this.phone == rhs.phone)||((this.phone!= null)&&this.phone.equals(rhs.phone))))&&((this.geolocationTracked == rhs.geolocationTracked)||((this.geolocationTracked!= null)&&this.geolocationTracked.equals(rhs.geolocationTracked))))&&((this.uEmployeeDeptCode == rhs.uEmployeeDeptCode)||((this.uEmployeeDeptCode!= null)&&this.uEmployeeDeptCode.equals(rhs.uEmployeeDeptCode))))&&((this.lockedOut == rhs.lockedOut)||((this.lockedOut!= null)&&this.lockedOut.equals(rhs.lockedOut))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.uPeoplesoftPaygroupFormatted == rhs.uPeoplesoftPaygroupFormatted)||((this.uPeoplesoftPaygroupFormatted!= null)&&this.uPeoplesoftPaygroupFormatted.equals(rhs.uPeoplesoftPaygroupFormatted))))&&((this.internalIntegrationUser == rhs.internalIntegrationUser)||((this.internalIntegrationUser!= null)&&this.internalIntegrationUser.equals(rhs.internalIntegrationUser))))&&((this.lastName == rhs.lastName)||((this.lastName!= null)&&this.lastName.equals(rhs.lastName))))&&((this.uDepartment == rhs.uDepartment)||((this.uDepartment!= null)&&this.uDepartment.equals(rhs.uDepartment))))&&((this.gender == rhs.gender)||((this.gender!= null)&&this.gender.equals(rhs.gender))))&&((this.city == rhs.city)||((this.city!= null)&&this.city.equals(rhs.city))))&&((this.sysId == rhs.sysId)||((this.sysId!= null)&&this.sysId.equals(rhs.sysId))))&&((this.uContractor == rhs.uContractor)||((this.uContractor!= null)&&this.uContractor.equals(rhs.uContractor))))&&((this.latitude == rhs.latitude)||((this.latitude!= null)&&this.latitude.equals(rhs.latitude))))&&((this.uApproverReviewer == rhs.uApproverReviewer)||((this.uApproverReviewer!= null)&&this.uApproverReviewer.equals(rhs.uApproverReviewer))))&&((this.uManagementLevel == rhs.uManagementLevel)||((this.uManagementLevel!= null)&&this.uManagementLevel.equals(rhs.uManagementLevel))))&&((this.uCustom4SwyCustom == rhs.uCustom4SwyCustom)||((this.uCustom4SwyCustom!= null)&&this.uCustom4SwyCustom.equals(rhs.uCustom4SwyCustom))))&&((this.uConcurApprover == rhs.uConcurApprover)||((this.uConcurApprover!= null)&&this.uConcurApprover.equals(rhs.uConcurApprover))))&&((this.uLicensedProjectName == rhs.uLicensedProjectName)||((this.uLicensedProjectName!= null)&&this.uLicensedProjectName.equals(rhs.uLicensedProjectName))))&&((this.uAvisVendorUser == rhs.uAvisVendorUser)||((this.uAvisVendorUser!= null)&&this.uAvisVendorUser.equals(rhs.uAvisVendorUser))))&&((this.uTestData == rhs.uTestData)||((this.uTestData!= null)&&this.uTestData.equals(rhs.uTestData))))&&((this.uPaygroup == rhs.uPaygroup)||((this.uPaygroup!= null)&&this.uPaygroup.equals(rhs.uPaygroup))))&&((this.uRxPhoneNumber == rhs.uRxPhoneNumber)||((this.uRxPhoneNumber!= null)&&this.uRxPhoneNumber.equals(rhs.uRxPhoneNumber))))&&((this.uExpUser == rhs.uExpUser)||((this.uExpUser!= null)&&this.uExpUser.equals(rhs.uExpUser))))&&((this.sysModCount == rhs.sysModCount)||((this.sysModCount!= null)&&this.sysModCount.equals(rhs.sysModCount))))&&((this.email == rhs.email)||((this.email!= null)&&this.email.equals(rhs.email))))&&((this.manager == rhs.manager)||((this.manager!= null)&&this.manager.equals(rhs.manager))))&&((this.uExpenseUser == rhs.uExpenseUser)||((this.uExpenseUser!= null)&&this.uExpenseUser.equals(rhs.uExpenseUser))))&&((this.sysClassName == rhs.sysClassName)||((this.sysClassName!= null)&&this.sysClassName.equals(rhs.sysClassName))))&&((this.photo == rhs.photo)||((this.photo!= null)&&this.photo.equals(rhs.photo))))&&((this.lastPositionUpdate == rhs.lastPositionUpdate)||((this.lastPositionUpdate!= null)&&this.lastPositionUpdate.equals(rhs.lastPositionUpdate))))&&((this.avatar == rhs.avatar)||((this.avatar!= null)&&this.avatar.equals(rhs.avatar))))&&((this.uCreatedIntegration == rhs.uCreatedIntegration)||((this.uCreatedIntegration!= null)&&this.uCreatedIntegration.equals(rhs.uCreatedIntegration))))&&((this.uOrgUnit3Swy == rhs.uOrgUnit3Swy)||((this.uOrgUnit3Swy!= null)&&this.uOrgUnit3Swy.equals(rhs.uOrgUnit3Swy))))&&((this.uJobCode == rhs.uJobCode)||((this.uJobCode!= null)&&this.uJobCode.equals(rhs.uJobCode))))&&((this.lastLoginTime == rhs.lastLoginTime)||((this.lastLoginTime!= null)&&this.lastLoginTime.equals(rhs.lastLoginTime))))&&((this.uPeoplesoftDistrict == rhs.uPeoplesoftDistrict)||((this.uPeoplesoftDistrict!= null)&&this.uPeoplesoftDistrict.equals(rhs.uPeoplesoftDistrict))))&&((this.averageDailyFte == rhs.averageDailyFte)||((this.averageDailyFte!= null)&&this.averageDailyFte.equals(rhs.averageDailyFte))))&&((this.uLocationCode == rhs.uLocationCode)||((this.uLocationCode!= null)&&this.uLocationCode.equals(rhs.uLocationCode))))&&((this.uIt == rhs.uIt)||((this.uIt!= null)&&this.uIt.equals(rhs.uIt))))&&((this.lastLogin == rhs.lastLogin)||((this.lastLogin!= null)&&this.lastLogin.equals(rhs.lastLogin))))&&((this.country == rhs.country)||((this.country!= null)&&this.country.equals(rhs.country))))&&((this.uEmpIdExpenseReport == rhs.uEmpIdExpenseReport)||((this.uEmpIdExpenseReport!= null)&&this.uEmpIdExpenseReport.equals(rhs.uEmpIdExpenseReport))))&&((this.sysUpdatedOn == rhs.sysUpdatedOn)||((this.sysUpdatedOn!= null)&&this.sysUpdatedOn.equals(rhs.sysUpdatedOn))))&&((this.uFacilityAddress == rhs.uFacilityAddress)||((this.uFacilityAddress!= null)&&this.uFacilityAddress.equals(rhs.uFacilityAddress))))&&((this.source == rhs.source)||((this.source!= null)&&this.source.equals(rhs.source))))&&((this.uConcur == rhs.uConcur)||((this.uConcur!= null)&&this.uConcur.equals(rhs.uConcur))))&&((this.employeeNumber == rhs.employeeNumber)||((this.employeeNumber!= null)&&this.employeeNumber.equals(rhs.employeeNumber))))&&((this.notification == rhs.notification)||((this.notification!= null)&&this.notification.equals(rhs.notification))))&&((this.uCliqbookTravel == rhs.uCliqbookTravel)||((this.uCliqbookTravel!= null)&&this.uCliqbookTravel.equals(rhs.uCliqbookTravel))))&&((this.webServiceAccessOnly == rhs.webServiceAccessOnly)||((this.webServiceAccessOnly!= null)&&this.webServiceAccessOnly.equals(rhs.webServiceAccessOnly))))&&((this.uPeoplesoftDivision == rhs.uPeoplesoftDivision)||((this.uPeoplesoftDivision!= null)&&this.uPeoplesoftDivision.equals(rhs.uPeoplesoftDivision))))&&((this.sysCreatedOn == rhs.sysCreatedOn)||((this.sysCreatedOn!= null)&&this.sysCreatedOn.equals(rhs.sysCreatedOn))))&&((this.uDivision == rhs.uDivision)||((this.uDivision!= null)&&this.uDivision.equals(rhs.uDivision))))&&((this.longitude == rhs.longitude)||((this.longitude!= null)&&this.longitude.equals(rhs.longitude))))&&((this.ssoSource == rhs.ssoSource)||((this.ssoSource!= null)&&this.ssoSource.equals(rhs.ssoSource))))&&((this.enableMultifactorAuthn == rhs.enableMultifactorAuthn)||((this.enableMultifactorAuthn!= null)&&this.enableMultifactorAuthn.equals(rhs.enableMultifactorAuthn))))&&((this.uLoginId == rhs.uLoginId)||((this.uLoginId!= null)&&this.uLoginId.equals(rhs.uLoginId))))&&((this.uSetPreferredName == rhs.uSetPreferredName)||((this.uSetPreferredName!= null)&&this.uSetPreferredName.equals(rhs.uSetPreferredName))))&&((this.uLevel3 == rhs.uLevel3)||((this.uLevel3 != null)&&this.uLevel3 .equals(rhs.uLevel3))))&&((this.uLevel2 == rhs.uLevel2)||((this.uLevel2 != null)&&this.uLevel2 .equals(rhs.uLevel2))))&&((this.uHighlightUserNotes == rhs.uHighlightUserNotes)||((this.uHighlightUserNotes!= null)&&this.uHighlightUserNotes.equals(rhs.uHighlightUserNotes))))&&((this.uIrgUnit2Swy == rhs.uIrgUnit2Swy)||((this.uIrgUnit2Swy!= null)&&this.uIrgUnit2Swy.equals(rhs.uIrgUnit2Swy))))&&((this.uLevel1 == rhs.uLevel1)||((this.uLevel1 != null)&&this.uLevel1 .equals(rhs.uLevel1))))&&((this.uLaborAgreement == rhs.uLaborAgreement)||((this.uLaborAgreement!= null)&&this.uLaborAgreement.equals(rhs.uLaborAgreement))))&&((this.firstName == rhs.firstName)||((this.firstName!= null)&&this.firstName.equals(rhs.firstName))))&&((this.uEmployeeType == rhs.uEmployeeType)||((this.uEmployeeType!= null)&&this.uEmployeeType.equals(rhs.uEmployeeType))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.uEmployeeEmail == rhs.uEmployeeEmail)||((this.uEmployeeEmail!= null)&&this.uEmployeeEmail.equals(rhs.uEmployeeEmail))))&&((this.uCustomConcurExpense == rhs.uCustomConcurExpense)||((this.uCustomConcurExpense!= null)&&this.uCustomConcurExpense.equals(rhs.uCustomConcurExpense))))&&((this.uCtryCode == rhs.uCtryCode)||((this.uCtryCode!= null)&&this.uCtryCode.equals(rhs.uCtryCode))))&&((this.uEmployeeDeptName == rhs.uEmployeeDeptName)||((this.uEmployeeDeptName!= null)&&this.uEmployeeDeptName.equals(rhs.uEmployeeDeptName))))&&((this.preferredLanguage == rhs.preferredLanguage)||((this.preferredLanguage!= null)&&this.preferredLanguage.equals(rhs.preferredLanguage))))&&((this.uUpdatedIntegration == rhs.uUpdatedIntegration)||((this.uUpdatedIntegration!= null)&&this.uUpdatedIntegration.equals(rhs.uUpdatedIntegration))))&&((this.uLicenseState == rhs.uLicenseState)||((this.uLicenseState!= null)&&this.uLicenseState.equals(rhs.uLicenseState))))&&((this.uItVp == rhs.uItVp)||((this.uItVp!= null)&&this.uItVp.equals(rhs.uItVp))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.calendarIntegration == rhs.calendarIntegration)||((this.calendarIntegration!= null)&&this.calendarIntegration.equals(rhs.calendarIntegration))))&&((this.street == rhs.street)||((this.street!= null)&&this.street.equals(rhs.street))))&&((this.uType == rhs.uType)||((this.uType!= null)&&this.uType.equals(rhs.uType))))&&((this.uJobName == rhs.uJobName)||((this.uJobName!= null)&&this.uJobName.equals(rhs.uJobName))))&&((this.company == rhs.company)||((this.company!= null)&&this.company.equals(rhs.company))))&&((this.uWorkInfo == rhs.uWorkInfo)||((this.uWorkInfo!= null)&&this.uWorkInfo.equals(rhs.uWorkInfo))))&&((this.department == rhs.department)||((this.department!= null)&&this.department.equals(rhs.department))))&&((this.uSupportsPaygroups == rhs.uSupportsPaygroups)||((this.uSupportsPaygroups!= null)&&this.uSupportsPaygroups.equals(rhs.uSupportsPaygroups))))&&((this.introduction == rhs.introduction)||((this.introduction!= null)&&this.introduction.equals(rhs.introduction))))&&((this.uBiManager == rhs.uBiManager)||((this.uBiManager!= null)&&this.uBiManager.equals(rhs.uBiManager))))&&((this.uPeoplesoftJobDescription == rhs.uPeoplesoftJobDescription)||((this.uPeoplesoftJobDescription!= null)&&this.uPeoplesoftJobDescription.equals(rhs.uPeoplesoftJobDescription))))&&((this.sysTags == rhs.sysTags)||((this.sysTags!= null)&&this.sysTags.equals(rhs.sysTags))))&&((this.failedAttempts == rhs.failedAttempts)||((this.failedAttempts!= null)&&this.failedAttempts.equals(rhs.failedAttempts))))&&((this.uHrDivisionAdministrator == rhs.uHrDivisionAdministrator)||((this.uHrDivisionAdministrator!= null)&&this.uHrDivisionAdministrator.equals(rhs.uHrDivisionAdministrator))))&&((this.uDirector == rhs.uDirector)||((this.uDirector!= null)&&this.uDirector.equals(rhs.uDirector))))&&((this.uLicensedGroupName == rhs.uLicensedGroupName)||((this.uLicensedGroupName!= null)&&this.uLicensedGroupName.equals(rhs.uLicensedGroupName))))&&((this.uPersonalEmailAddress == rhs.uPersonalEmailAddress)||((this.uPersonalEmailAddress!= null)&&this.uPersonalEmailAddress.equals(rhs.uPersonalEmailAddress))))&&((this.userName == rhs.userName)||((this.userName!= null)&&this.userName.equals(rhs.userName))))&&((this.schedule == rhs.schedule)||((this.schedule!= null)&&this.schedule.equals(rhs.schedule))))&&((this.uRemote == rhs.uRemote)||((this.uRemote!= null)&&this.uRemote.equals(rhs.uRemote))))&&((this.uItGvpEvp == rhs.uItGvpEvp)||((this.uItGvpEvp!= null)&&this.uItGvpEvp.equals(rhs.uItGvpEvp))))&&((this.timeFormat == rhs.timeFormat)||((this.timeFormat!= null)&&this.timeFormat.equals(rhs.timeFormat))))&&((this.uFacilityCode == rhs.uFacilityCode)||((this.uFacilityCode!= null)&&this.uFacilityCode.equals(rhs.uFacilityCode))))&&((this.middleName == rhs.middleName)||((this.middleName!= null)&&this.middleName.equals(rhs.middleName))))&&((this.location == rhs.location)||((this.location!= null)&&this.location.equals(rhs.location))));
    }

}
