package com.albertsons.argus.webservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author jborj20
 * @since 11/10/22
 * @version 1.0
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "There is something wrong with the request at this time. Please check your request message.")
public class BadRequestException extends RuntimeException{
    public BadRequestException(String errorMessage) {
		super(errorMessage);
	}
    
}
