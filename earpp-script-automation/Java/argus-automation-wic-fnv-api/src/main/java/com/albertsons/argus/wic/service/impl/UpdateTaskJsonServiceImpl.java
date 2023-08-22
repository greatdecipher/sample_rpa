package com.albertsons.argus.wic.service.impl;

import com.albertsons.argus.wic.service.JsonService;
import com.albertsons.argus.wic.ws.bo.ResponseUpdateTaskBO;

import org.springframework.stereotype.Service;

/**
 * 
 * @author kbuen03
 * @since 5/2/22
 * @version 1.0
 */
@Service
public class UpdateTaskJsonServiceImpl implements JsonService<ResponseUpdateTaskBO> {

    @Override
    public Class<ResponseUpdateTaskBO> getTClass() {
        return ResponseUpdateTaskBO.class;
    }
    
}
