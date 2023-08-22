package com.albertsons.argus.servicenow.web.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.albertsons.argus.domain.bo.generated.AssignedTo;
import com.albertsons.argus.domain.bo.generated.AssignmentGroup;
import com.albertsons.argus.domain.bo.generated.Company;
import com.albertsons.argus.domain.bo.generated.CostCenter;
import com.albertsons.argus.domain.bo.generated.IncidentBO;
import com.albertsons.argus.domain.bo.generated.Manager;
import com.albertsons.argus.domain.bo.generated.OpenedBy;
import com.albertsons.argus.domain.bo.generated.Parent;
import com.albertsons.argus.domain.bo.generated.ResultTableSysUser;
import com.albertsons.argus.domain.bo.generated.ResultTableSysUserGroup;
import com.albertsons.argus.domain.bo.generated.TableGenericBO;
import com.albertsons.argus.domain.bo.generated.TableIncBO;
import com.albertsons.argus.domain.bo.generated.TableSysUserBO;
import com.albertsons.argus.domain.bo.generated.TableSysUserGroupBO;
import com.albertsons.argus.servicenow.web.ArgusWebAppConfig;
import com.albertsons.argus.servicenow.web.service.TableWSRestTemplateService;
import com.albertsons.argus.servicenow.web.service.impl.TableWSRestTemplateServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;

/**
 * @author kbuen03
 * @since 06/3/2021
 * @version 1.0
 */
@ContextConfiguration(classes = { ArgusWebAppConfig.class, TableWSRestTemplateServiceImpl.class })
@RestClientTest(TableWSRestTemplateServiceImpl.class)
public class TableWSRestTemplateServiceTest {
    private static final Logger LOG = LogManager.getLogger(TableWSRestTemplateServiceTest.class);
    @Autowired
    private TableWSRestTemplateService tableWSRestTemplateService;

    @Autowired
    private Environment env;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getMethodResponseBodyByIncidentTest() {
        LOG.log(Level.DEBUG, "------------------Start load for getMethodResponseBodyByIncidentTest() -------------");
        String host = env.getProperty("argus.web.ws.uri.host");
        List<IncidentBO> result = new ArrayList<>();

        TableIncBO tableIncBO = new TableIncBO();
        IncidentBO incidentBO = new IncidentBO();

        AssignedTo assignedTo = new AssignedTo();
        assignedTo.setValue("ppno10");
        assignedTo.setLink("www.test.com");

        incidentBO.setAssignedTo(assignedTo);

        AssignmentGroup assignmentGroup = new AssignmentGroup();
        assignmentGroup.setValue("AutomationTest");
        assignmentGroup.setLink("wwww.testgroup.com");

        incidentBO.setAssignmentGroup(assignmentGroup);
        incidentBO.setComments("test Comments");
        incidentBO.setuCallerName("test uCallerName");
        incidentBO.setNumber("INC000Test");

        OpenedBy openedBy = new OpenedBy();
        openedBy.setValue("elma00");
        openedBy.setLink("www.testlink.com");
        incidentBO.setOpenedBy(openedBy);

        incidentBO.setShortDescription("shortDescription test");
        incidentBO.setState("state test");
        incidentBO.setuCallerName("uCallerName test");
        incidentBO.setuTestField("uTestField test");

        result.add(incidentBO);

        tableIncBO.setResult(result);

        String detailsString = "";
        try {
            detailsString = objectMapper.writeValueAsString(tableIncBO);
            this.server
                    .expect(requestTo(host
                            + "/api/now/table/incident?sysparm_query=test&sysparm_limit=1&sysparm_display_value=test2"))
                    .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }
        LOG.log(Level.DEBUG, "------------------END load for getMethodResponseBodyByIncidentTest() -------------");
        Map<String, String> allparams = new HashMap<>();
        allparams.put("sysparm_query", "test");
        allparams.put("sysparm_limit", "1");
        allparams.put("sysparm_display_value", "test2");

        TableIncBO bo = tableWSRestTemplateService.getMethodResponseBodyByIncident(allparams);

        assertEquals("ppno10", bo.getResult().get(0).getAssignedTo().getValue());

    }

    @Test
    public void getMethodResponseBodyBySysUserTest() {
        LOG.log(Level.DEBUG, "------------------Start load for getMethodResponseBodyBySysUserTest() -------------");
        String host = env.getProperty("argus.web.ws.uri.host");
        // load for getMethodResponseBodyBySysUserTest
        TableSysUserBO sysUserBO = new TableSysUserBO();
        ResultTableSysUser resultTableSysUser = new ResultTableSysUser();
        resultTableSysUser.setActive("active");
        resultTableSysUser.setAgentStatus("agentStatus");
        resultTableSysUser.setAvatar("avatar");
        resultTableSysUser.setAverageDailyFte("averageDailyFte");
        resultTableSysUser.setBuilding("building");
        resultTableSysUser.setCalendarIntegration("calendarIntegration");
        resultTableSysUser.setCity("city");

        Company company = new Company();
        company.setValue("valueCompany");
        company.setLink("www.company.com");
        resultTableSysUser.setCompany(company);

        CostCenter costCenter = new CostCenter();
        costCenter.setValue("valueCC");
        costCenter.setLink("www.cc.com");
        resultTableSysUser.setCostCenter(costCenter);

        resultTableSysUser.setCountry("country");
        resultTableSysUser.setDateFormat("dateFormat");
        resultTableSysUser.setDepartment("department");
        resultTableSysUser.setEmail("email@safeway.com");
        resultTableSysUser.setEmployeeNumber("098765");
        sysUserBO.setResult(resultTableSysUser);

        String detailSysUserBOStr = "";
        try {
            detailSysUserBOStr = objectMapper.writeValueAsString(sysUserBO);
            this.server.expect(requestTo(host + "/api/now/table/sys_user/12345"))
                    .andRespond(withSuccess(detailSysUserBOStr, MediaType.APPLICATION_JSON));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        LOG.log(Level.DEBUG, "------------------End load for getMethodResponseBodyBySysUserTest() -------------");

        TableSysUserBO sysUserBO1 = tableWSRestTemplateService.getMethodResponseBodyBySysUser("12345");

        assertEquals("098765", sysUserBO1.getResult().getEmployeeNumber());
    }

    @Test
    public void getMethodResponseBodyBySysUserGroupTest() {
        LOG.log(Level.DEBUG,
                "------------------Start load for getMethodResponseBodyBySysUserGroupTest() -------------");
        String host = env.getProperty("argus.web.ws.uri.host");
        TableSysUserGroupBO sysUserGroupBO = new TableSysUserGroupBO();
        ResultTableSysUserGroup resultTableSysUserGroup = new ResultTableSysUserGroup();

        resultTableSysUserGroup.setActive("active");
        resultTableSysUserGroup.setAverageDailyFte("averageDailyFte");
        resultTableSysUserGroup.setCostCenter("costCenter");
        resultTableSysUserGroup.setDefaultAssignee("defaultAssignee");
        resultTableSysUserGroup.setDescription("description");
        resultTableSysUserGroup.setEmail("email@safeway.com");
        resultTableSysUserGroup.setExcludeManager("excludeManager");
        resultTableSysUserGroup.setHourlyRate("1");

        Manager manager = new Manager();
        manager.setValue("value");
        manager.setLink("link.com");
        resultTableSysUserGroup.setManager(manager);
        resultTableSysUserGroup.setName("name");

        Parent parent = new Parent();
        parent.setValue("value");
        parent.setLink("link@yahoo.com");
        resultTableSysUserGroup.setParent(parent);
        resultTableSysUserGroup.setPoints("points");
        resultTableSysUserGroup.setQueueManaged("queueManaged");
        resultTableSysUserGroup.setQueueManagers("queueManagers");
        resultTableSysUserGroup.setSource("source");
        resultTableSysUserGroup.setSysCreatedBy("sysCreatedBy");
        resultTableSysUserGroup.setSysCreatedOn("sysCreatedOn");
        resultTableSysUserGroup.setSysId("sysId");
        resultTableSysUserGroup.setSysModCount("sysModCount");
        resultTableSysUserGroup.setSysTags("sysTags");
        resultTableSysUserGroup.setSysUpdatedBy("sysUpdatedBy");
        resultTableSysUserGroup.setSysUpdatedOn("sysUpdatedOn");
        resultTableSysUserGroup.setSysId("1010238");

        sysUserGroupBO.setResult(resultTableSysUserGroup);
        String detailSysUserGrpBOStr = "";
        try {
            detailSysUserGrpBOStr = objectMapper.writeValueAsString(sysUserGroupBO);
            this.server.expect(requestTo(host + "/api/now/table/sys_user_group/5675Group"))
                    .andRespond(withSuccess(detailSysUserGrpBOStr, MediaType.APPLICATION_JSON));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        LOG.log(Level.DEBUG, "------------------End load for getMethodResponseBodyBySysUserGroupTest() -------------");

        TableSysUserGroupBO sysUserGroupBO2 = tableWSRestTemplateService
                .getMethodResponseBodyBySysUserGroup("5675Group");

        assertEquals("1010238", sysUserGroupBO2.getResult().getSysId());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getMethodResponseBodyByGenericTableAPITest() {
        LOG.log(Level.DEBUG,
                "------------------Start load for getMethodResponseBodyByGenericTableAPITest() -------------");
        String host = env.getProperty("argus.web.ws.uri.host");
        TableGenericBO tableGenericBO = new TableGenericBO();
        tableGenericBO.setDetail("id", "12123243");
        tableGenericBO.setDetail("email", "safeway@yahoo.com");
        AssignedTo assignedTo = new AssignedTo();
        assignedTo.setValue("Larry Bird");
        assignedTo.setLink("www.larrybird.com");

        tableGenericBO.setDetail("assignedTo", assignedTo);

        String detailGenericBOStr = "";
        try {
            detailGenericBOStr = objectMapper.writeValueAsString(tableGenericBO);
            this.server.expect(requestTo(host + "/api/now/table/costCenter/testCost"))
                    .andRespond(withSuccess(detailGenericBOStr, MediaType.APPLICATION_JSON));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        LOG.log(Level.DEBUG,
                "------------------End load for getMethodResponseBodyByGenericTableAPITest() -------------");

        TableGenericBO sysUserGroupBO2 = tableWSRestTemplateService
                .getMethodResponseBodyByGenericTableAPI("/costCenter/testCost");

        assertEquals("12123243", sysUserGroupBO2.getDetails().get("id"));
        LinkedHashMap<String, String> assignedTo2 =   (LinkedHashMap<String, String>) sysUserGroupBO2.getDetails().get("assignedTo");

        assertEquals("Larry Bird", assignedTo2.get("value"));
    }
}
