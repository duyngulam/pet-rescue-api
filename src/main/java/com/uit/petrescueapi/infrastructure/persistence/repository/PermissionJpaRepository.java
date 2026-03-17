package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.PermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link PermissionJpaEntity}.
 * Used by the <b>command-side</b> adapter for permission management.
 */
@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, Integer> {

    Optional<PermissionJpaEntity> findByCode(String code);
}
