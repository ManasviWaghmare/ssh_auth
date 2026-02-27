package com.org.sshauth.api.controller;

import com.org.sshauth.api.dto.KeyUploadRequest;
import com.org.sshauth.api.dto.SSHResponse;
import com.org.sshauth.core.ssh_core_library.KeyAuthenticationService;
import com.org.sshauth.core.model.SSHKeyPair;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for SSH Key Management operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/keys")
@RequiredArgsConstructor
public class KeyManagementController {

    private final KeyAuthenticationService keyAuthenticationService;

    /**
     * Upload a new SSH key pair.
     */
    @PostMapping("/upload")
    public ResponseEntity<SSHResponse> uploadKey(@Valid @RequestBody KeyUploadRequest request) {
        String requestId = UUID.randomUUID().toString();

        try {
            log.info("Uploading SSH key: {} with requestId: {}", request.getKeyName(), requestId);

            SSHKeyPair keyPair = SSHKeyPair.builder()
                    .keyName(request.getKeyName())
                    .publicKey(request.getPublicKey())
                    .privateKey(request.getPrivateKey())
                    .keyAlgorithm(request.getKeyAlgorithm())
                    .keySize(request.getKeySize())
                    .description(request.getDescription())
                    .createdAt(LocalDateTime.now())
                    .isActive(true)
                    .build();

            SSHKeyPair uploadedKey = keyAuthenticationService.uploadKey(keyPair);

            SSHResponse response = SSHResponse.builder()
                    .success(true)
                    .message("Key uploaded successfully")
                    .requestId(requestId)
                    .data(uploadedKey.getId().toString())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error uploading key for requestId: {}", requestId, e);

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Key upload failed")
                    .requestId(requestId)
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Retrieve all SSH keys.
     */
    @GetMapping("/all")
    public ResponseEntity<SSHResponse> getAllKeys() {
        try {
            log.info("Retrieving all SSH keys");

            List<SSHKeyPair> keys = keyAuthenticationService.getAllKeys();

            SSHResponse response = SSHResponse.builder()
                    .success(true)
                    .message("Keys retrieved successfully")
                    .data(keys.size() + " keys found")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error retrieving SSH keys", e);

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Failed to retrieve keys")
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get a specific SSH key by ID.
     */
    @GetMapping("/{keyId}")
    public ResponseEntity<SSHResponse> getKeyById(@PathVariable String keyId) {
        try {
            log.info("Retrieving SSH key with ID: {}", keyId);

            SSHKeyPair key = keyAuthenticationService.getKeyById(Long.parseLong(keyId));

            SSHResponse response = SSHResponse.builder()
                    .success(true)
                    .message("Key retrieved successfully")
                    .data(key.getKeyName())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error retrieving key with ID: {}", keyId, e);

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Key not found")
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Delete a SSH key.
     */
    @DeleteMapping("/{keyId}")
    public ResponseEntity<SSHResponse> deleteKey(@PathVariable String keyId) {
        try {
            log.info("Deleting SSH key with ID: {}", keyId);

            keyAuthenticationService.deleteKey(Long.parseLong(keyId));

            SSHResponse response = SSHResponse.builder()
                    .success(true)
                    .message("Key deleted successfully")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error deleting key with ID: {}", keyId, e);

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Failed to delete key")
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Validate a SSH key.
     */
    @PostMapping("/{keyId}/validate")
    public ResponseEntity<SSHResponse> validateKey(@PathVariable String keyId) {
        try {
            log.info("Validating SSH key with ID: {}", keyId);

            boolean isValid = keyAuthenticationService.validateKey(Long.parseLong(keyId));

            SSHResponse response = SSHResponse.builder()
                    .success(isValid)
                    .message(isValid ? "Key is valid" : "Key validation failed")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error validating key with ID: {}", keyId, e);

            SSHResponse errorResponse = SSHResponse.builder()
                    .success(false)
                    .message("Key validation failed")
                    .errorDetails(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
