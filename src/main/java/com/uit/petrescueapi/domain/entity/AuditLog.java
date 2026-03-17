package com.uit.petrescueapi.domain.entity;

import com.uit.petrescueapi.domain.valueobject.AuditAction;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AuditLog — represents an audit trail entry for entity changes.
 * Pure domain entity: no JPA annotations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    private UUID logId;
    private String entityType;
    private String entityId;
    private AuditAction action;
    private UUID actorId;
    private String oldValue;
    private String newValue;
    private String ipAddress;
    private LocalDateTime createdAt;
}
