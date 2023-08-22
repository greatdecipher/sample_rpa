package com.albertsons.argus.toggle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid Token.")
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String errorMessage) {
		super(errorMessage);
	}
}