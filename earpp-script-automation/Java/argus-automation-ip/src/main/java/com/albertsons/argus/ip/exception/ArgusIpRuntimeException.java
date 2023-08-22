package com.albertsons.argus.ip.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusIpRuntimeException extends RuntimeException implements ExitCodeGenerator{
    public ArgusIpRuntimeException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return 1;
    }
}
