package com.org.sshauth.core.utilities_security;

import com.org.sshauth.api.dto.SSHResponse;
import com.org.sshauth.core.exception.KeyValidationException;
import com.org.sshauth.core.exception.SSHAuthenticationException;
import com.org.sshauth.core.exception.SSHConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler utility for the application.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerUtil {

    /**
     * Handle SSH Connection Exception.
     */
    @ExceptionHandler(SSHConnectionException.class)
    public ResponseEntity<SSHResponse> handleSSHConnectionException(SSHConnectionException e) {
        log.error("SSH Connection Exception occurred: {}", e.getMessage());

        SSHResponse response = SSHResponse.builder()
                .success(false)
                .message("SSH Connection Failed")
                .errorDetails(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    /**
     * Handle SSH Authentication Exception.
     */
    @ExceptionHandler(SSHAuthenticationException.class)
    public ResponseEntity<SSHResponse> handleSSHAuthenticationException(SSHAuthenticationException e) {
        log.error("SSH Authentication Exception occurred: {}", e.getMessage());

        SSHResponse response = SSHResponse.builder()
                .success(false)
                .message("SSH Authentication Failed")
                .errorDetails(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handle Key Validation Exception.
     */
    @ExceptionHandler(KeyValidationException.class)
    public ResponseEntity<SSHResponse> handleKeyValidationException(KeyValidationException e) {
        log.error("Key Validation Exception occurred: {}", e.getMessage());

        SSHResponse response = SSHResponse.builder()
                .success(false)
                .message("Key Validation Failed")
                .errorDetails(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle General Exception.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SSHResponse> handleGeneralException(Exception e) {
        log.error("An unexpected exception occurred: {}", e.getMessage(), e);

        SSHResponse response = SSHResponse.builder()
                .success(false)
                .message("An unexpected error occurred")
                .errorDetails(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Log exception details.
     */
    public static void logExceptionDetails(Exception e, String context) {
        log.error("Exception in {}: {} - {}", context, e.getClass().getSimpleName(), e.getMessage());
        e.printStackTrace();
    }
}
