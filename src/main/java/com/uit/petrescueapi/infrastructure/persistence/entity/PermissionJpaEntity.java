package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * JPA entity mapped to the {@code permissions} table.
 * Simple reference data — does not extend BaseJpaEntity.
 */
@Entity
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(name = "uq_permission_resource_action", columnNames = {"resource", "action"})
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "resource", length = 50)
    private String resource;

    @Column(name = "action", length = 50)
    private String action;

    @Column(name = "code", unique = true, length = 120)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
