package com.albertsons.argus.domaindb.exception;

public class OracleServiceException extends Exception{
    public OracleServiceException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
