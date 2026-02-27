package com.org.sshauth.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Model representing an SSH Key Pair.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSHKeyPair {

    private Long id;
    private String keyName;
    private String publicKey;
    private String privateKey;
    private String keyFingerprint;
    private String keyAlgorithm;
    private Integer keySize;
    private String owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String description;
}
