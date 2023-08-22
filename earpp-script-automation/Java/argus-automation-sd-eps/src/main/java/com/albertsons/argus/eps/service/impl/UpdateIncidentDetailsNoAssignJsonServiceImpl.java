package com.albertsons.argus.eps.service.impl;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.eps.ws.bo.ResponseUpdateIncidentDetailsBO;
import com.albertsons.argus.eps.ws.bo.ResponseUpdateIncidentDetailsNoAssignBO;

@Service
public class UpdateIncidentDetailsNoAssignJsonServiceImpl implements AutomationService<ResponseUpdateIncidentDetailsNoAssignBO> {
    @Override
    public Class<ResponseUpdateIncidentDetailsNoAssignBO> getTClass() {
        return ResponseUpdateIncidentDetailsNoAssignBO.class;
    }
}
