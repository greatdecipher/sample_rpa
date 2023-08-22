package com.albertsons.argus.q2c.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusQ2cRuntimeException extends RuntimeException implements ExitCodeGenerator{
    public ArgusQ2cRuntimeException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return 1;
    }
}
