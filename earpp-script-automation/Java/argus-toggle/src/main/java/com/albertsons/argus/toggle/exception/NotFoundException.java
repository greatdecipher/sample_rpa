package com.albertsons.argus.toggle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "URL not found. Please check your url and try again.")
public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage) {
		super(errorMessage);
	}
}