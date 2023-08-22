package com.albertsons.argus.wic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found URL. Please checked your url and try again.")
public class NotFoundException extends RuntimeException{
    public NotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
