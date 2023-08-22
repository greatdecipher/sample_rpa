package com.albertsons.argus.toggle.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.toggle.bo.ResponseUpdateIncidentBO;

@Service
public class UpdateIncidentStatusServiceImpl implements AutomationService<ResponseUpdateIncidentBO>{

    @Override
    public Class<ResponseUpdateIncidentBO> getTClass() {
        return ResponseUpdateIncidentBO.class;
    }

}
