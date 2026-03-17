package com.uit.petrescueapi.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * BaseEntity — common audit fields for domain entities.
 *
 * This class lives in the domain layer and contains only plain Java types
 * (no framework annotations). Fields are protected to allow convenient
 * access from subclasses while keeping them encapsulated.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    protected LocalDateTime createdAt;

    protected UUID createdBy;

    protected LocalDateTime updatedAt;

    protected UUID updatedBy;

    protected boolean deleted;

    protected LocalDateTime deletedAt;

    protected UUID deletedBy;
}
