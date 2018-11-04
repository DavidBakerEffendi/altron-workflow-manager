package com.altron.workflowmanager.service.exceptions;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 3932689612363615742L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
