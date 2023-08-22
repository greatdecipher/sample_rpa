package com.albertsons.argus.eps.exception;

public class ArgusEPSException extends Exception {
    public ArgusEPSException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
