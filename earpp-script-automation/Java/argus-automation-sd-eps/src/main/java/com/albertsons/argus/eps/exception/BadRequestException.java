package com.albertsons.argus.eps.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "here is something wrong the request at this time. Please checked request message.")
public class BadRequestException extends RuntimeException{
    public BadRequestException(String errorMessage) {
		super(errorMessage);
	}
    
}