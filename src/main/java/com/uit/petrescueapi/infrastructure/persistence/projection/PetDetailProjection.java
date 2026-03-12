package com.uit.petrescueapi.infrastructure.persistence.projection;

import com.uit.petrescueapi.domain.valueobject.Gender;
import com.uit.petrescueapi.domain.valueobject.HealthStatus;
import com.uit.petrescueapi.domain.valueobject.PetStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for the pet detail query.
 *
 * <p>Returns all pet fields plus organization summary via LEFT JOIN.
 * {@code imageUrls} is fetched separately (ElementCollection) and
 * merged in the adapter.</p>
 */
public interface PetDetailProjection {

    // ── Pet fields ──────────────────────────────
    UUID getId();
    String getName();
    String getSpecies();
    String getBreed();
    Integer getAge();
    Gender getGender();
    String getColor();
    BigDecimal getWeight();
    String getDescription();
    PetStatus getStatus();
    HealthStatus getHealthStatus();
    boolean getVaccinated();
    boolean getNeutered();
    LocalDate getRescueDate();
    String getRescueLocation();
    UUID getShelterId();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    // ── Organization fields (nullable via LEFT JOIN) ─
    UUID getOrganizationId();
    String getOrganizationName();
    String getOrganizationType();
    String getOrganizationStatus();
}
