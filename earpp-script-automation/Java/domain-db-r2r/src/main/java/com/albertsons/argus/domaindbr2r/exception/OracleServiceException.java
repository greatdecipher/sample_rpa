package com.albertsons.argus.domaindbr2r.exception;

public class OracleServiceException extends Exception{
    public OracleServiceException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
