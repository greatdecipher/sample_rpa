package com.albertsons.argus.toggle.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentListBO;

@Service
public class GetIncidentListServiceImpl implements AutomationService<ResponseGetIncidentListBO>{

    @Override
    public Class<ResponseGetIncidentListBO> getTClass() {
        return ResponseGetIncidentListBO.class;
    }

}
