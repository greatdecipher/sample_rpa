package com.albertsons.argus.wic.service.impl;

import com.albertsons.argus.wic.service.JsonService;
import com.albertsons.argus.wic.ws.bo.ResponseGetTaskListBO;

import org.springframework.stereotype.Service;

@Service
public class GetTaskListJsonServiceImpl implements JsonService<ResponseGetTaskListBO>{
    @Override
    public Class<ResponseGetTaskListBO> getTClass() {
        return ResponseGetTaskListBO.class;
    }
}
