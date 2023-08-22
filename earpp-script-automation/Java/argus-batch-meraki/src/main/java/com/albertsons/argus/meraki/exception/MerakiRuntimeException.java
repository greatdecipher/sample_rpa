package com.albertsons.argus.meraki.exception;

import org.springframework.boot.ExitCodeGenerator;

/**
 * 
 * @author kbuen03
 * @since 1/27/2022
 * 
 */
public class MerakiRuntimeException extends RuntimeException implements ExitCodeGenerator{
    public MerakiRuntimeException(String message) {
        super(message);
    }

    @Override
    public int getExitCode() {
        return 1;
    }
}
