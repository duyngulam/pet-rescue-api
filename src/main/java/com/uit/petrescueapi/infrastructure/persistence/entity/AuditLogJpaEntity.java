package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code audit_logs} table.
 * Does not extend BaseJpaEntity — audit logs ARE the audit records.
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_entity", columnList = "entity_type, entity_id"),
        @Index(name = "idx_audit_actor", columnList = "actor_id"),
        @Index(name = "idx_audit_created", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogJpaEntity {

    @Id
    @Column(name = "log_id", updatable = false, nullable = false)
    private UUID logId;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    @Column(name = "entity_id", nullable = false, length = 100)
    private String entityId;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "actor_id")
    private UUID actorId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "old_value", columnDefinition = "jsonb")
    private String oldValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_value", columnDefinition = "jsonb")
    private String newValue;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;
}
