package com.albertsons.argus.wic.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author kbuen03
 * @since 4/27/22
 * @version 1.0
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid Token.")
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String errorMessage) {
		super(errorMessage);
	}
}