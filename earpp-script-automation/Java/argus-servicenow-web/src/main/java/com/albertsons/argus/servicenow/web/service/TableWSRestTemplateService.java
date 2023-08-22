package com.albertsons.argus.servicenow.web.service;

import java.util.Map;

import com.albertsons.argus.domain.bo.generated.TableGenericBO;
import com.albertsons.argus.domain.bo.generated.TableIncBO;
import com.albertsons.argus.domain.bo.generated.TableSysUserBO;
import com.albertsons.argus.domain.bo.generated.TableSysUserGroupBO;

/**
 * @author kbuen03
 * @since 05/20/21
 * @version 1.0
 * 
 */
public interface TableWSRestTemplateService {
    public TableIncBO getMethodResponseBodyByIncident(Map<String, String> allParams);
    public TableGenericBO getMethodResponseBodyByGenericTableAPI(String uri);
    public TableSysUserBO getMethodResponseBodyBySysUser(String userId);
    public TableSysUserGroupBO getMethodResponseBodyBySysUserGroup(String userGrpId);

}
