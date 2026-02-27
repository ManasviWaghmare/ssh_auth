package com.org.sshauth.core.exception;

/**
 * Exception thrown when SSH authentication fails.
 */
public class SSHAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SSHAuthenticationException(String message) {
        super(message);
    }

    public SSHAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHAuthenticationException(Throwable cause) {
        super(cause);
    }
}
