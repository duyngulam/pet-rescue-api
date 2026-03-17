package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.PetMedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository contract for the PetMedicalRecord entity.
 */
public interface PetMedicalRecordRepository {

    PetMedicalRecord save(PetMedicalRecord record);

    Optional<PetMedicalRecord> findById(UUID recordId);

    Page<PetMedicalRecord> findByPetId(UUID petId, Pageable pageable);

    void delete(UUID recordId);
}
