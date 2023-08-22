package com.albertsons.argus.toggle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "There is something wrong with the request at this time. Please check the request message.")
public class BadRequestException extends RuntimeException{
    public BadRequestException(String errorMessage) {
		super(errorMessage);
	}  
}