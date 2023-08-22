package com.albertsons.argus.servicenow.web.util;

import java.nio.charset.StandardCharsets;

import org.springframework.web.util.UriUtils;

/**
 * 
 */
public class ArgusWebUtil {
    // private static final Logger LOG = LogManager.getLogger(ArgusWebUtil.class);
    
    public String decodeUri(String value) {       
        return UriUtils.decode(value, StandardCharsets.UTF_8);
    }

    public String encodeUri(String value){
        return UriUtils.encode(value, StandardCharsets.UTF_8);
    }

    public String encodePath(String value){
        return UriUtils.encodePath(value, StandardCharsets.UTF_8);
    }
}
