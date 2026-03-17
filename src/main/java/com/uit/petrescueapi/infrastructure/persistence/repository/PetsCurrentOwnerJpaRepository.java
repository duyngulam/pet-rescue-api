package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.PetsCurrentOwnerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PetsCurrentOwnerJpaEntity}.
 * Used by the <b>command-side</b> adapter for tracking current pet ownership.
 */
@Repository
public interface PetsCurrentOwnerJpaRepository extends JpaRepository<PetsCurrentOwnerJpaEntity, UUID> {
}
