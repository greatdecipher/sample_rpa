package com.albertsons.argus.patchingctask.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeByIDBO;

@Service
public class GetChangeByIdJsonServiceImpl implements AutomationService<ResponseGetChangeByIDBO>{
    @Override
    public Class<ResponseGetChangeByIDBO> getTClass() {
        return ResponseGetChangeByIDBO.class;
    }
}
