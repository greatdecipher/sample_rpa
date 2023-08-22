package com.albertsons.argus.servicenow.web.controller;

import java.util.Arrays;
import java.util.Map;

import com.albertsons.argus.domain.bo.generated.TableGenericBO;
import com.albertsons.argus.domain.bo.generated.TableIncBO;
import com.albertsons.argus.servicenow.web.service.RestTemplateService;
import com.albertsons.argus.servicenow.web.service.TableWSRestTemplateService;
import com.albertsons.argus.servicenow.web.util.ArgusWebUriEnum;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kbuen03
 * @since 05/28/21
 * @version 1.0
 * 
 */
@RestController
@RequestMapping("/argus/web")
public class TableWSController {
    private static final Logger LOG = LogManager.getLogger(TableWSController.class);

    @Autowired
    private RestTemplateService restTemplateService2;

    @Autowired
    private TableWSRestTemplateService restTemplateService;

    /**
     * 
     * @param id - /api/now/table/sys_user_group/{id}
     * @return
     */
    @GetMapping("/table/sys_user_group/{id}")
    public ResponseEntity<TableGenericBO> getTablesSystemUserGroupObj(@PathVariable(value = "id") String id) {
        LOG.log(Level.DEBUG, "Begin method . . .");

        LOG.log(Level.DEBUG, "End method . . .");
        return ResponseEntity.ok()
                .body(restTemplateService.getMethodResponseBodyByGenericTableAPI(ArgusWebUriEnum.SERVICENOW.getUri()
                        + ArgusWebUriEnum.TABLE.getUri() + ArgusWebUriEnum.SYSTEMUSERGRP.getUri() + "/" + id));
    }

    /**
     * 
     * @param allParams
     * @return
     */
    @PostMapping("/table/getIncident")
    public ResponseEntity<TableIncBO> getTableIncident(@RequestParam Map<String, String> allParams) {
        

        return ResponseEntity.ok()
                .body(restTemplateService.getMethodResponseBodyByIncident(allParams));

        // return
        // ResponseEntity.ok().body(restTemplateService.getMethodResponseBodyByIncident(
        // "/api/now/table/incident?sysparm_query=assigned_to%3D14c9581bdbc8f740154c24684b961983%5Estate&sysparm_limit=50"));
    }

    /**
     * 
     * @param tableName
     * @param sysId
     * @param tableBodyStr
     * @return
     */
    @PutMapping("/table/incident/{tablename}/{sysId}")
    public ResponseEntity<String> putTables(@PathVariable(value = "tablename") String tableName,
            @PathVariable(value = "sysId") String sysId, @RequestBody String tableBodyStr) {
        LOG.log(Level.DEBUG, "Begin method . . .");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        LOG.log(Level.DEBUG, "End method . . .");

        return ResponseEntity.ok(restTemplateService2.getRestResponseBodyString(ArgusWebUriEnum.SERVICENOW.getUri()+"/"
        +tableName+"/"+sysId, headers,
                tableBodyStr, HttpMethod.PUT));
    }
}
