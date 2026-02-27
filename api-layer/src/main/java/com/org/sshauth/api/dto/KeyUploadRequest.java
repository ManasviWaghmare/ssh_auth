package com.org.sshauth.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for SSH Key Upload Request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyUploadRequest {

    @NotBlank(message = "Key name cannot be blank")
    private String keyName;

    @NotBlank(message = "Public key cannot be blank")
    private String publicKey;

    @NotBlank(message = "Private key cannot be blank")
    private String privateKey;

    private String keyAlgorithm;
    private Integer keySize;
    private String description;
    private Boolean setAsDefault;
}
