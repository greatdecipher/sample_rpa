package com.albertsons.argus.q2c.exception;

public class Q2CEmployeePayrollException extends Exception{
    public Q2CEmployeePayrollException(String errorMessage) {
        super(errorMessage);
    }
}