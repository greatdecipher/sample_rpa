package com.albertsons.argus.patchingctask.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.domain.service.AutomationService;

import com.albertsons.argus.patchingctask.ws.bo.ResponseCreateChangeTaskBO;


@Service
public class CreateCtaskJsonServiceImpl implements AutomationService<ResponseCreateChangeTaskBO>{
    @Override
    public Class<ResponseCreateChangeTaskBO> getTClass() {
        return ResponseCreateChangeTaskBO.class;
    }
}
