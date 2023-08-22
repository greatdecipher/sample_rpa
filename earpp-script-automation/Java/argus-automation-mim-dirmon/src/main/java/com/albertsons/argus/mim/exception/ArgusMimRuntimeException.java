package com.albertsons.argus.mim.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusMimRuntimeException extends RuntimeException implements ExitCodeGenerator {
	public ArgusMimRuntimeException(String message) {
		super(message);
	}
	
	@Override
    public int getExitCode() {
        return 1;
    }
}
