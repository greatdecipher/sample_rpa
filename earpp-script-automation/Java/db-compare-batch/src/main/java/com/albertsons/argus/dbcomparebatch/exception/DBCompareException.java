package com.albertsons.argus.dbcomparebatch.exception;

public class DBCompareException extends Exception{
    public DBCompareException(String errorMessage, Throwable e){
        super(errorMessage, e);
    }
}
