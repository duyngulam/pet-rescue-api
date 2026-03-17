package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.PostJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link PostJpaEntity}.
 * Used by the <b>command-side</b> adapter for post management.
 *
 * <p>For read/query-side operations, see {@link PostQueryJpaRepository}.</p>
 */
@Repository
public interface PostJpaRepository extends JpaRepository<PostJpaEntity, UUID> {

    Optional<PostJpaEntity> findByPostIdAndDeletedFalse(UUID postId);
}
