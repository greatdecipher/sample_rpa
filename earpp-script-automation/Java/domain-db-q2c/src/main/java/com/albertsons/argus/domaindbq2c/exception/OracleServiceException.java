package com.albertsons.argus.domaindbq2c.exception;

public class OracleServiceException extends Exception{
    public OracleServiceException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
