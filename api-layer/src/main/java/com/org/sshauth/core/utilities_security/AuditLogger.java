package com.org.sshauth.core.utilities_security;

import com.org.sshauth.core.model.AuditLog;
import com.org.sshauth.core.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Utility class for audit logging operations.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogger {

    private final AuditLogRepository auditLogRepository;

    /**
     * Log an audit event.
     */
    public void logAuditEvent(String userId, String action, String resource, String resourceId, String status) {
        try {
            log.info("Logging audit event - Action: {}, Resource: {}, Status: {}", action, resource, status);

            AuditLog auditLog = AuditLog.builder()
                    .userId(userId)
                    .action(action)
                    .resource(resource)
                    .resourceId(resourceId)
                    .status(status)
                    .timestamp(LocalDateTime.now())
                    .build();

            auditLogRepository.save(auditLog);

        } catch (Exception e) {
            log.error("Error logging audit event", e);
        }
    }

    /**
     * Log an audit event with details.
     */
    public void logAuditEventWithDetails(String userId, String action, String resource,
            String resourceId, String status, String details) {
        try {
            log.info("Logging audit event with details - Action: {}, Resource: {}", action, resource);

            AuditLog auditLog = AuditLog.builder()
                    .userId(userId)
                    .action(action)
                    .resource(resource)
                    .resourceId(resourceId)
                    .status(status)
                    .details(details)
                    .timestamp(LocalDateTime.now())
                    .build();

            auditLogRepository.save(auditLog);

        } catch (Exception e) {
            log.error("Error logging audit event with details", e);
        }
    }

    /**
     * Retrieve all audit logs.
     */
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    /**
     * Retrieve audit logs for a specific user.
     */
    public List<AuditLog> getAuditLogsByUser(String userId) {
        return auditLogRepository.findByUserId(userId);
    }

    /**
     * Retrieve audit logs for a specific action.
     */
    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    /**
     * Clear all audit logs.
     */
    public void clearAuditLogs() {
        log.warn("Clearing all audit logs");
        auditLogRepository.deleteAll();
    }
}
