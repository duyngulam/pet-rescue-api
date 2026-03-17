package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.UserReputationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for {@link UserReputationJpaEntity}.
 * Used by the <b>command-side</b> adapter for user reputation management.
 *
 * <p>{@code findById(UUID)} from {@link JpaRepository} is sufficient
 * since the primary key is the userId.</p>
 */
@Repository
public interface UserReputationJpaRepository extends JpaRepository<UserReputationJpaEntity, UUID> {
}
