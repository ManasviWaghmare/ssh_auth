package com.org.sshauth.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for SSH Connection Request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSHConnectRequest {

    @NotBlank(message = "Host cannot be blank")
    private String host;

    @NotNull(message = "Port cannot be null")
    @Positive(message = "Port must be a positive integer")
    private Integer port;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Key Pair ID cannot be blank")
    private String keyPairId;

    private Integer connectionTimeout;
    private Integer sessionTimeout;
    private Boolean strictHostKeyChecking;
}
