package com.albertsons.argus.eps.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.eps.ws.bo.ResponseGetIncidentDetailBO;

@Service
public class GetIncidentDetailsJsonServiceImpl implements AutomationService<ResponseGetIncidentDetailBO>{
    @Override
    public Class<ResponseGetIncidentDetailBO> getTClass() {
        return ResponseGetIncidentDetailBO.class;
    }
}
