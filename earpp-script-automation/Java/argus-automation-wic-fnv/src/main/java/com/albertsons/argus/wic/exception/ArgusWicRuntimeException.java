package com.albertsons.argus.wic.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusWicRuntimeException extends RuntimeException implements ExitCodeGenerator {
	public ArgusWicRuntimeException(String message) {
		super(message);
	}
	
	@Override
    public int getExitCode() {
        return 1;
    }
}
