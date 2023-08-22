package com.albertsons.argus.ip.service.impl;
import com.albertsons.argus.ip.service.JsonService;
import com.albertsons.argus.ip.ws.bo.ResponseUpdateInvoiceDetailsBO;

import org.springframework.stereotype.Service;

@Service
public class UpdateInvoiceDetailsJsonServiceImpl implements JsonService<ResponseUpdateInvoiceDetailsBO> {
    @Override
    public Class<ResponseUpdateInvoiceDetailsBO> getTClass() {
        return ResponseUpdateInvoiceDetailsBO.class;
    }
}
