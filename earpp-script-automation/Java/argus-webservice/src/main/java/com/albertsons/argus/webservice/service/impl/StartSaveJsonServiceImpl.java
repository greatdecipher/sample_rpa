package com.albertsons.argus.webservice.service.impl;

import org.springframework.stereotype.Service;

import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.JsonService;

/**
 * 
 * @author kbuen03
 * @since 5/27/22
 * @version 1.0
 */
@Service
public class StartSaveJsonServiceImpl implements JsonService<ResponseStartSaveBO>{

    @Override
    public Class<ResponseStartSaveBO> getTClass() {
        return ResponseStartSaveBO.class;
    }
    
}
