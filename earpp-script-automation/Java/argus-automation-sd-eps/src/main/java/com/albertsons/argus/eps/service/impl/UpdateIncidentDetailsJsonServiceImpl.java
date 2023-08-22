package com.albertsons.argus.eps.service.impl;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.eps.ws.bo.ResponseUpdateIncidentDetailsBO;

@Service
public class UpdateIncidentDetailsJsonServiceImpl implements AutomationService<ResponseUpdateIncidentDetailsBO> {
    @Override
    public Class<ResponseUpdateIncidentDetailsBO> getTClass() {
        return ResponseUpdateIncidentDetailsBO.class;
    }
}
