package com.albertsons.argus.meraki.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author kbuen03
 * @since 04/07/22
 * @version 1.0
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MerakiWebserviceException extends RuntimeException{
    public MerakiWebserviceException(String errorMessage) {
		super(errorMessage);
	}
}
