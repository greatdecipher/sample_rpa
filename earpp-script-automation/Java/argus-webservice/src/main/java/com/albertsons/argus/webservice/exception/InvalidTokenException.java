package com.albertsons.argus.webservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author jborj20
 * @since 11/10/22
 * @version 1.0
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid Token.")
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String errorMessage) {
		super(errorMessage);
	}
}
