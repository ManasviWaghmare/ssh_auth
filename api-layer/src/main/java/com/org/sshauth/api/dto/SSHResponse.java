package com.org.sshauth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for SSH Response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SSHResponse {

    private Boolean success;
    private String message;
    private String requestId;
    private String data;
    private String errorDetails;
    private LocalDateTime timestamp;
    private Long executionTimeMs;
    private Integer exitCode;
}
