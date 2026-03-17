package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.PetMediaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PetMediaJpaEntity}.
 * Used by the <b>command-side</b> adapter for pet media management.
 */
@Repository
public interface PetMediaJpaRepository extends JpaRepository<PetMediaJpaEntity, UUID> {

    List<PetMediaJpaEntity> findAllByPetId(UUID petId);

    @Modifying
    @Query("DELETE FROM PetMediaJpaEntity pm WHERE pm.petId = :petId")
    void deleteAllByPetId(@Param("petId") UUID petId);
}
