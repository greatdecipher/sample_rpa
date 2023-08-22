package com.albertsons.argusautomationlexmarkorder.services.impl;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argusautomationlexmarkorder.dto.JsonResult;

public class JsonResultAutomationServiceImpl implements AutomationService<JsonResult> {

    @Override
    public Class<JsonResult> getTClass() {
        return JsonResult.class;
    }
    
}
