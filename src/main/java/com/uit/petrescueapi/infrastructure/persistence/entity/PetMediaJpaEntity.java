package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code pet_media} table.
 * Lightweight media reference — does not extend BaseJpaEntity.
 */
@Entity
@Table(name = "pet_media")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PetMediaJpaEntity {

    @Id
    @Column(name = "media_id", updatable = false, nullable = false)
    private UUID mediaId;

    @Column(name = "pet_id", nullable = false)
    private UUID petId;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Read-only association used by JOIN queries only.
     * Persistence is handled by the {@code petId} field above.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", insertable = false, updatable = false)
    private PetJpaEntity pet;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
