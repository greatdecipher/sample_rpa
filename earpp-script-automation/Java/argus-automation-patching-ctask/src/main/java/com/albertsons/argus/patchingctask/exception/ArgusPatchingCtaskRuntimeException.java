package com.albertsons.argus.patchingctask.exception;

import org.springframework.boot.ExitCodeGenerator;

public class ArgusPatchingCtaskRuntimeException extends RuntimeException implements ExitCodeGenerator {
	public ArgusPatchingCtaskRuntimeException(String message) {
		super(message);
	}
	
	@Override
    public int getExitCode() {
        return 1;
    }
}