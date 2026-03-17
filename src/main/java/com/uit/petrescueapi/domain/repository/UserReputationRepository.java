package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.UserReputation;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository contract for the UserReputation entity.
 */
public interface UserReputationRepository {

    UserReputation save(UserReputation reputation);

    Optional<UserReputation> findByUserId(UUID userId);
}
