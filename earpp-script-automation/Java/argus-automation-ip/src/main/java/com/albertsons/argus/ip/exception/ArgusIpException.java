package com.albertsons.argus.ip.exception;

public class ArgusIpException extends Exception {
    public ArgusIpException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
