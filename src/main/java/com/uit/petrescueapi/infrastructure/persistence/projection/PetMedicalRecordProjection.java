package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for pet medical record queries.
 *
 * <p>Maps from JPQL {@code SELECT ... FROM pet_medical_records mr} query.
 * Column aliases must match getter names (camelCase).</p>
 */
public interface PetMedicalRecordProjection {

    UUID getRecordId();
    UUID getPetId();
    String getDescription();
    String getVaccine();
    String getDiagnosis();
    LocalDateTime getRecordDate();
    UUID getCreatedBy();
}
