package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.RescueCaseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link RescueCaseJpaEntity}.
 * Used by the <b>command-side</b> adapter for rescue case management.
 *
 * <p>For read/query-side operations, see {@link RescueCaseQueryJpaRepository}.</p>
 */
@Repository
public interface RescueCaseJpaRepository extends JpaRepository<RescueCaseJpaEntity, UUID> {

    Optional<RescueCaseJpaEntity> findByCaseIdAndDeletedFalse(UUID caseId);
}
