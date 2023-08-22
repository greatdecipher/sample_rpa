package com.albertsons.argus.sedgwick.exception;

public class ArgusSedgwickException extends Exception {
    public ArgusSedgwickException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
