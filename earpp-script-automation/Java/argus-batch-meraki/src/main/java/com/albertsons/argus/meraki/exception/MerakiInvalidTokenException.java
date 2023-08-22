package com.albertsons.argus.meraki.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author kbuen03
 * @since 04/07/22
 * @version 1.0
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MerakiInvalidTokenException extends RuntimeException {
    
    public MerakiInvalidTokenException(String errorMessage) {
		super(errorMessage);
	}
}
