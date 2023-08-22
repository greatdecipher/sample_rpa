package com.albertsons.argus.patchingctask.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;

import com.albertsons.argus.patchingctask.ws.bo.ResponseGetChangeListBO;


@Service
public class GetChangeListJsonServiceImpl implements AutomationService<ResponseGetChangeListBO>{
    @Override
    public Class<ResponseGetChangeListBO> getTClass() {
        return ResponseGetChangeListBO.class;
    }
}
