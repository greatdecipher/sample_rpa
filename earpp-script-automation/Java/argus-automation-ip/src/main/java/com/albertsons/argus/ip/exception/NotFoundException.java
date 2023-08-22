package com.albertsons.argus.ip.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found URL. Please checked your url and try again.")
public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage) {
		super(errorMessage);
	}
}