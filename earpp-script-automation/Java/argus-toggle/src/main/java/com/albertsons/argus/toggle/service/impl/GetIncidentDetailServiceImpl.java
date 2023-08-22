package com.albertsons.argus.toggle.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.toggle.bo.ResponseGetIncidentDetailBO;

@Service
public class GetIncidentDetailServiceImpl implements AutomationService<ResponseGetIncidentDetailBO>{

    @Override
    public Class<ResponseGetIncidentDetailBO> getTClass() {
        return ResponseGetIncidentDetailBO.class;
    }

}
