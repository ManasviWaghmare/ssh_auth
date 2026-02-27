package com.org.sshauth.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Model representing an SSH Request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ssh_request")
public class SSHRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requestId;
    private String host;
    private Integer port;
    private String username;
    private String command;
    private String keyPairId;
    private LocalDateTime createdAt;
    private LocalDateTime executedAt;
    private String status;
    private String result;
    private String errorMessage;
    private Long executionTimeMs;
}
