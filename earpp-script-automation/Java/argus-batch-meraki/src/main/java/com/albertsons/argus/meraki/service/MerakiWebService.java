package com.albertsons.argus.meraki.service;

import com.albertsons.argus.meraki.exception.MerakiException;

/**
 * @author kbuen03
 * @since 01/17/21
 */
public interface MerakiWebService {
    public String sendARPRequest(String request) throws MerakiException;
}
