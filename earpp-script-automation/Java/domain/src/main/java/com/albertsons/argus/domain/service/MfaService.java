package com.albertsons.argus.domain.service;

import com.microsoft.playwright.Page;

public interface MfaService {

    public String getMfaCode(String pythonScript, String secretKey);

    public Page loginAlbertsons(Page page, String loginUrl);

    public Page loginGenericAlbertsons(Page page, String loginUrl, String userName, String password);
}
