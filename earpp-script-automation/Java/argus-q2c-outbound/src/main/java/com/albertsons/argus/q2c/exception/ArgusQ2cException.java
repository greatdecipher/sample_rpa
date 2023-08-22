package com.albertsons.argus.q2c.exception;

public class ArgusQ2cException extends Exception {
    public ArgusQ2cException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
