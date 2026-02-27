package com.org.sshauth.api.controller;

import com.org.sshauth.api.dto.CommandRequest;
import com.org.sshauth.api.dto.SSHConnectRequest;
import com.org.sshauth.api.dto.SSHResponse;
import com.org.sshauth.core.ssh_core_library.CommandExecutor;
import com.org.sshauth.core.ssh_core_library.SSHConnectionManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * REST Controller for SSH operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ssh")
@RequiredArgsConstructor
public class SSHRestController {

    private final SSHConnectionManager sshConnectionManager;
    private final CommandExecutor commandExecutor;

    /**
     * Test SSH connection to a remote host.
     */
    @PostMapping("/connect")
    public ResponseEntity<SSHResponse> testConnection(@Valid @RequestBody SSHConnectRequest request) {
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        try {
            log.info("Testing SSH connection to host: {} with requestId: {}", request.getHost(), requestId);

            boolean isConnected = sshConnectionManager.testConnection(
                    request.getHost(),
                    request.getPort(),
                    request.getUsername(),
                    request.getKeyPairId()
            );

            long executionTime = System.currentTimeMillis() - startTime;

            SSHResponse response = SSHResponse.builder()
                    .success(isConnected)
                    .message(isConnected ? "Connection successful" : "Connection failed")
                    .requestId(requestId)
                    .timestamp(LocalDateTime.now())
                    .executionTimeMs(executionTime)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error testing SSH connection for requestId: {}", requestId, e);
            long executionTime = System.currentTimeMillis() - startTime;

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Connection test failed")
                    .requestId(requestId)
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .executionTimeMs(executionTime)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Execute a command on a remote SSH server.
     */
    @PostMapping("/execute")
    public ResponseEntity<SSHResponse> executeCommand(@Valid @RequestBody CommandRequest request) {
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        try {
            log.info("Executing command on host: {} with requestId: {}", request.getHost(), requestId);

            String commandOutput = commandExecutor.executeCommand(
                    request.getHost(),
                    request.getPort() != null ? request.getPort() : 22,
                    request.getUsername(),
                    request.getCommand(),
                    request.getKeyPairId(),
                    request.getExecutionTimeout()
            );

            long executionTime = System.currentTimeMillis() - startTime;

            SSHResponse response = SSHResponse.builder()
                    .success(true)
                    .message("Command executed successfully")
                    .requestId(requestId)
                    .data(commandOutput)
                    .timestamp(LocalDateTime.now())
                    .executionTimeMs(executionTime)
                    .exitCode(0)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error executing command for requestId: {}", requestId, e);
            long executionTime = System.currentTimeMillis() - startTime;

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Command execution failed")
                    .requestId(requestId)
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .executionTimeMs(executionTime)
                    .exitCode(1)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get SSH connection status.
     */
    @GetMapping("/status/{host}")
    public ResponseEntity<SSHResponse> getConnectionStatus(@PathVariable String host) {
        try {
            log.info("Fetching connection status for host: {}", host);

            SSHResponse response = SSHResponse.builder()
                    .success(true)
                    .message("Status retrieved successfully")
                    .data("Connected")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error fetching connection status for host: {}", host, e);

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Failed to fetch status")
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
