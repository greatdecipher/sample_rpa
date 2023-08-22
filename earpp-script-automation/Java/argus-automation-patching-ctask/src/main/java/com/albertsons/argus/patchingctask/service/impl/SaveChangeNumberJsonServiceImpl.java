package com.albertsons.argus.patchingctask.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;
import com.albertsons.argus.patchingctask.ws.bo.ResponseSaveChangeBO;

@Service
public class SaveChangeNumberJsonServiceImpl implements AutomationService<ResponseSaveChangeBO>{
    @Override
    public Class<ResponseSaveChangeBO> getTClass() {
        return ResponseSaveChangeBO.class;
    }
}
