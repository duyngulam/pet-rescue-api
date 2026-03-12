package com.uit.petrescueapi.infrastructure.persistence.projection;

import com.uit.petrescueapi.domain.valueobject.Gender;
import com.uit.petrescueapi.domain.valueobject.HealthStatus;
import com.uit.petrescueapi.domain.valueobject.PetStatus;

import java.util.UUID;

/**
 * Spring Data interface projection for the pet summary list query.
 *
 * <p>Maps directly from the JPQL {@code SELECT ... FROM pets p LEFT JOIN organizations o}
 * query. Column aliases must match getter names (camelCase).</p>
 *
 * <p>Add new fields here + in the JPQL SELECT to expose more data.</p>
 */
public interface PetSummaryProjection {

    // ── Pet fields ──────────────────────────────
    UUID getId();
    String getName();
    String getSpecies();
    String getBreed();
    Integer getAge();
    boolean getVaccinated();
    Gender getGender();
    PetStatus getStatus();
    HealthStatus getHealthStatus();

    // ── Organization fields (nullable via LEFT JOIN) ─
    UUID getOrganizationId();
    String getOrganizationName();
    String getOrganizationType();
    String getOrganizationStatus();
}
