package com.albertsons.argus.ip.service.impl;

import org.springframework.stereotype.Service;
import com.albertsons.argus.ip.ws.bo.ResponseGetInvoiceListBO;
import com.albertsons.argus.domain.service.AutomationService;

@Service
public class GetInvoiceListJsonServiceImpl implements AutomationService<ResponseGetInvoiceListBO>{
    @Override
    public Class<ResponseGetInvoiceListBO> getTClass() {
        return ResponseGetInvoiceListBO.class;
    }
}
