package com.uit.petrescueapi.domain.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
@MappedSuperclass
public abstract class BaseEntity {

    protected LocalDateTime createdAt;

    protected String createdBy;

    protected LocalDateTime updatedAt;

    protected String updatedBy;

    protected boolean deleted;

    protected LocalDateTime deletedAt;

    protected String deletedBy;
}
