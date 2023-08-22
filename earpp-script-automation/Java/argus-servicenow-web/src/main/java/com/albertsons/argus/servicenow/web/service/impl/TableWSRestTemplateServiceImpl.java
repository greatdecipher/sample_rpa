package com.albertsons.argus.servicenow.web.service.impl;

import java.time.Duration;
import java.util.Map;

import com.albertsons.argus.domain.bo.generated.TableGenericBO;
import com.albertsons.argus.domain.bo.generated.TableIncBO;
import com.albertsons.argus.domain.bo.generated.TableSysUserBO;
import com.albertsons.argus.domain.bo.generated.TableSysUserGroupBO;
import com.albertsons.argus.servicenow.web.exception.RestTemplateResponseErrorHandler;
import com.albertsons.argus.servicenow.web.service.TableWSRestTemplateService;
import com.albertsons.argus.servicenow.web.util.ArgusWebUriEnum;
import com.albertsons.argus.servicenow.web.util.ArgusWebUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author kbuen03
 * @since 05/25/2021
 * @version 1.0
 * 
 */
@Service
public class TableWSRestTemplateServiceImpl implements TableWSRestTemplateService {

  private RestTemplate restTemplate;

  private Environment environment;

  @Autowired
  public TableWSRestTemplateServiceImpl(RestTemplateBuilder restTemplateBuilder, Environment env) {
    this.environment = env;
    this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(500))
        .setReadTimeout(Duration.ofSeconds(500))
        .basicAuthentication(env.getProperty("encrypted.web.ws.property.username"),
            env.getProperty("encrypted.web.ws.property.password"))
        .errorHandler(new RestTemplateResponseErrorHandler()).build();
  }

  @Override
  public TableIncBO getMethodResponseBodyByIncident(Map<String, String> allParams) {
    ArgusWebUtil argusWebUtil = new ArgusWebUtil();

    return restTemplate.getForObject(environment.getProperty("argus.web.ws.uri.host")
        + ArgusWebUriEnum.SERVICENOW.getUri() + ArgusWebUriEnum.TABLE.getUri() + ArgusWebUriEnum.INCIDENT.getUri()
        + "?sysparm_query=" + argusWebUtil.encodeUri(allParams.get("sysparm_query")) + "&sysparm_limit="
        + allParams.get("sysparm_limit") + "&sysparm_display_value="
        + argusWebUtil.encodeUri(allParams.get("sysparm_display_value")), TableIncBO.class);
  }

  /**
   * @implNote - This is used by generic object that is dynamic value for
   *           TableUerBO (JsonAnySetter), either used by system user, system user
   *           group, cost center or vice versa as long as under TableAPI
   * @param uri
   * 
   */
  @Override
  public TableGenericBO getMethodResponseBodyByGenericTableAPI(String uri) {
    return restTemplate.getForObject(environment.getProperty("argus.web.ws.uri.host")
        + ArgusWebUriEnum.SERVICENOW.getUri() + ArgusWebUriEnum.TABLE.getUri() + uri, TableGenericBO.class);
  }

  @Override
  public TableSysUserBO getMethodResponseBodyBySysUser(String userId) {
    return restTemplate.getForObject(
        environment.getProperty("argus.web.ws.uri.host") + ArgusWebUriEnum.SERVICENOW.getUri()
            + ArgusWebUriEnum.TABLE.getUri() + ArgusWebUriEnum.SYSTEMUSER.getUri() + "/" + userId,
        TableSysUserBO.class);
  }

  @Override
  public TableSysUserGroupBO getMethodResponseBodyBySysUserGroup(String userGrpId) {
    return restTemplate.getForObject(
        environment.getProperty("argus.web.ws.uri.host") + ArgusWebUriEnum.SERVICENOW.getUri()
            + ArgusWebUriEnum.TABLE.getUri() + ArgusWebUriEnum.SYSTEMUSERGRP.getUri() + "/" + userGrpId,
        TableSysUserGroupBO.class);
  }

}
