package com.uit.petrescueapi.infrastructure.persistence.projection;

import com.uit.petrescueapi.domain.valueobject.Gender;
import com.uit.petrescueapi.domain.valueobject.HealthStatus;
import com.uit.petrescueapi.domain.valueobject.PetStatus;

import java.util.UUID;

/**
 * Spring Data interface projection for the pet summary list query.
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
    String getImagePublicId();

    // ── Organization fields (for nested OrganizationMinimalDto) ─
    UUID getOrganizationId();
    String getOrganizationName();

    // ── Location fields (from organization) ─
    String getProvinceName();
    Integer getProvinceCode();
    String getWardName();
    Integer getWardCode();
}
