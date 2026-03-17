package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.AuditLog;
import com.uit.petrescueapi.domain.repository.AuditLogRepository;
import com.uit.petrescueapi.domain.valueobject.AuditAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain service encapsulating AuditLog business rules.
 *
 * Provides a simple entry point for recording audit trail entries
 * whenever domain entities are created, updated, or deleted.
 *
 * {@code @Transactional} lives here only — not on adapters or controllers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditLogDomainService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Record an audit log entry.
     *
     * @param entityType the type of entity (e.g. "Pet", "RescueCase")
     * @param entityId   the ID of the entity
     * @param action     the action performed (CREATE, UPDATE, DELETE)
     * @param actorId    the UUID of the user who performed the action
     * @param oldVal     the old value (JSON string, nullable for CREATE)
     * @param newVal     the new value (JSON string, nullable for DELETE)
     * @param ip         the IP address of the requester
     * @return the saved AuditLog entry
     */
    public AuditLog log(String entityType, String entityId, AuditAction action,
                        UUID actorId, String oldVal, String newVal, String ip) {
        log.debug("Audit: {} {} {} by {}", action, entityType, entityId, actorId);
        AuditLog auditLog = AuditLog.builder()
                .logId(UUID.randomUUID())
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .actorId(actorId)
                .oldValue(oldVal)
                .newValue(newVal)
                .ipAddress(ip)
                .createdAt(LocalDateTime.now())
                .build();
        return auditLogRepository.save(auditLog);
    }
}
