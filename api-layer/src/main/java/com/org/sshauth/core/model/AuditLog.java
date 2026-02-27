package com.org.sshauth.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Model representing an Audit Log entry.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String action;
    private String resource;
    private String resourceId;
    private String status;
    private String details;
    private String ipAddress;
    private LocalDateTime timestamp;
    private Long durationMs;
    private String changesSummary;
}
