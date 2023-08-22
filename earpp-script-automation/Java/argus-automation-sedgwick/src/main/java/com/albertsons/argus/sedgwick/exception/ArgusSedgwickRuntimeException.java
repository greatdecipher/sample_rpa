package com.albertsons.argus.sedgwick.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusSedgwickRuntimeException extends RuntimeException implements ExitCodeGenerator{
    public ArgusSedgwickRuntimeException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return 1;
    }
}
