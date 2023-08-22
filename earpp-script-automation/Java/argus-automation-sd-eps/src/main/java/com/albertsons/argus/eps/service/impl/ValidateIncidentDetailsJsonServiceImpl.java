package com.albertsons.argus.eps.service.impl;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.eps.ws.bo.ResponseValidateIncidentBO;

@Service
public class ValidateIncidentDetailsJsonServiceImpl implements AutomationService<ResponseValidateIncidentBO> {
    @Override
    public Class<ResponseValidateIncidentBO> getTClass() {
        return ResponseValidateIncidentBO.class;
    }
}
