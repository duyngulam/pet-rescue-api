package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * JPA entity mapped to the {@code tags} table.
 */
@Entity
@Table(name = "tags")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TagJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "tag_id", updatable = false, nullable = false)
    private UUID tagId;

    @Column(name = "code", unique = true, length = 100)
    private String code;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
