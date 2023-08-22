package com.albertsons.argusautomationlexmarkorder.services.impl;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argusautomationlexmarkorder.dto.JsonItem;

public class JsonItemAutomationServiceImpl implements AutomationService<JsonItem>{

    @Override
    public Class<JsonItem> getTClass() {
        return JsonItem.class;
    }
    
}
