package com.albertsons.argus.domain.service.impl;

import com.albertsons.argus.domain.bo.generated.MerakiBO;
import com.albertsons.argus.domain.service.AutomationService;

import org.springframework.stereotype.Service;

@Service
public class MerakiAutomationServiceImpl implements AutomationService<MerakiBO>{

    @Override
    public Class<MerakiBO> getTClass() {
        return MerakiBO.class;
    }
    
}
