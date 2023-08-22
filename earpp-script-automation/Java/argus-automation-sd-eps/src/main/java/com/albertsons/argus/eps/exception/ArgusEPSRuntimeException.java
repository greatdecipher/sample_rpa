package com.albertsons.argus.eps.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusEPSRuntimeException extends RuntimeException implements ExitCodeGenerator{
    public ArgusEPSRuntimeException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return 1;
    }
}
