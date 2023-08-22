package com.albertsons.argus.webservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author jborj20
 * @since 11/10/22
 * @version 1.0
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "URL not found. Please check your url and try again.")
public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
