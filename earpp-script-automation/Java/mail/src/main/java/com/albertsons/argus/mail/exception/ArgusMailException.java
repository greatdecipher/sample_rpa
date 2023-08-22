package com.albertsons.argus.mail.exception;
/**
 * @author kbuen03
 * @since 07/14/2021
 * @version 1.0
 * 
 */
public class ArgusMailException extends Exception{
    public ArgusMailException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
