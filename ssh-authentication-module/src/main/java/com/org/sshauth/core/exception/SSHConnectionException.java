package com.org.sshauth.core.exception;

/**
 * Exception thrown when SSH connection fails.
 */
public class SSHConnectionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SSHConnectionException(String message) {
        super(message);
    }

    public SSHConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHConnectionException(Throwable cause) {
        super(cause);
    }
}
