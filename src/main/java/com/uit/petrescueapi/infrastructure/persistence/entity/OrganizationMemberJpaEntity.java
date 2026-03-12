package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code organization_members} table.
 * Composite primary key: (organization_id, user_id).
 */
@Entity
@Table(name = "organization_members")
@IdClass(OrganizationMemberId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMemberJpaEntity {

    @Id
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Read-only association used by JOIN queries only.
     * Persistence is handled by the {@code userId} field above.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserJpaEntity user;

    @Column(name = "role", length = 30)
    private String role;  // OWNER | MANAGER | MEMBER | VOLUNTEER

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "status", length = 50)
    private String status;  // ACTIVE | INACTIVE
}
