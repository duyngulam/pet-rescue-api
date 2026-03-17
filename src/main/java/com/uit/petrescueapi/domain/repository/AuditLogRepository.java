package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Domain repository contract for the AuditLog entity.
 */
public interface AuditLogRepository {

    AuditLog save(AuditLog auditLog);

    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId, Pageable pageable);
}
