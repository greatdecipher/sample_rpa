package com.albertsons.argus.tma.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusTmaRuntimeException extends RuntimeException implements ExitCodeGenerator{
    public ArgusTmaRuntimeException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return 1;
    }
    
}
