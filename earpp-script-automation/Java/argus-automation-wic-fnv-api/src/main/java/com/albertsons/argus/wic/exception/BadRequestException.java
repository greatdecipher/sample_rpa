package com.albertsons.argus.wic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author kbuen03
 * @since 4/27/22
 * @version 1.0
 */

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "here is something wrong the request at this time. Please checked request message.")
public class BadRequestException extends RuntimeException{
    public BadRequestException(String errorMessage) {
		super(errorMessage);
	}
    
}

