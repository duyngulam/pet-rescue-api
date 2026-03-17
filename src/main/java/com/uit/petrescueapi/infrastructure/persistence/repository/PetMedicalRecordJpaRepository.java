package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.PetMedicalRecordJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PetMedicalRecordJpaEntity}.
 * Used by the <b>command-side</b> adapter for pet medical record management.
 */
@Repository
public interface PetMedicalRecordJpaRepository extends JpaRepository<PetMedicalRecordJpaEntity, UUID> {

    Page<PetMedicalRecordJpaEntity> findByPetIdAndDeletedFalse(UUID petId, Pageable pageable);
}
