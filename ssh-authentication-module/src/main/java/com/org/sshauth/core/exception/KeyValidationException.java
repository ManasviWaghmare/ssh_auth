package com.org.sshauth.core.exception;

/**
 * Exception thrown when SSH key validation fails.
 */
public class KeyValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public KeyValidationException(String message) {
        super(message);
    }

    public KeyValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyValidationException(Throwable cause) {
        super(cause);
    }
}
