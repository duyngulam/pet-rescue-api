package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code user_reputations} table.
 * Tracks user reputation score and level — does not extend BaseJpaEntity.
 */
@Entity
@Table(name = "user_reputations")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserReputationJpaEntity {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "score", nullable = false)
    @Builder.Default
    private Integer score = 0;

    @Column(name = "level", length = 50)
    private String level;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Read-only association used by JOIN queries only.
     * Persistence is handled by the {@code userId} field above.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserJpaEntity user;

    @PrePersist
    void prePersist() {
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
