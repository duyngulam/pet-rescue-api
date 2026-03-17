package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for pet ownership queries.
 *
 * <p>Maps from JPQL {@code SELECT ... FROM pet_ownerships po} query.
 * Column aliases must match getter names (camelCase).</p>
 */
public interface PetOwnershipProjection {

    UUID getPetId();
    String getOwnerType();
    UUID getOwnerId();
    LocalDateTime getFromTime();
    LocalDateTime getToTime();
}
