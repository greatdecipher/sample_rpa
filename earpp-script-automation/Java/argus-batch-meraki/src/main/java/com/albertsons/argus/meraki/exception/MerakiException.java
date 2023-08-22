package com.albertsons.argus.meraki.exception;
/**
 * 
 * @author kbuen03
 * @since 1/27/21
 * @version 1.0
 * 
 */
public class MerakiException extends Exception{
    public MerakiException(String errorMessage) {
        super(errorMessage);
    }
}
