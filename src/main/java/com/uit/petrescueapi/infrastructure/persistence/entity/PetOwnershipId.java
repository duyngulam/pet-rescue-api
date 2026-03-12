package com.uit.petrescueapi.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Composite primary key for {@link PetOwnershipJpaEntity}.
 * Matches the {@code PRIMARY KEY (pet_id, from_time)} in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetOwnershipId implements Serializable {

    private UUID petId;
    private LocalDateTime fromTime;
}
