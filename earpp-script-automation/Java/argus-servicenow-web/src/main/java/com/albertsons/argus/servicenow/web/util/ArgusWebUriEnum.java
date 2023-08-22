package com.albertsons.argus.servicenow.web.util;
/**
 * @author kbuen03 
 * @since 06/03/21
 * @version 1.0
 * 
 */
public enum ArgusWebUriEnum {
    SERVICENOW("/api/now"),
    TABLE("/table"),
    INCIDENT("/incident"),
    SYSTEMUSER("/sys_user"),
    SYSTEMUSERGRP("/sys_user_group")
    ;

    private String uri;

    public String getUri() {
        return uri;
    }

    private ArgusWebUriEnum(String uri) {
		this.uri = uri;
	}

    
}
