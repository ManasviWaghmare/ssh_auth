package com.org.sshauth.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Command Execution Request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandRequest {

    @NotBlank(message = "Host cannot be blank")
    private String host;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Command cannot be blank")
    private String command;

    @NotBlank(message = "Key Pair ID cannot be blank")
    private String keyPairId;

    private Integer port;
    private Long executionTimeout;
}
