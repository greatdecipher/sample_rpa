package com.albertsons.argus.domaindb.exception;

public class DB2ServiceException extends Exception{
    public DB2ServiceException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
