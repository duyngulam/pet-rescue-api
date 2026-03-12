package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository contract for refresh tokens.
 */
public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    void revokeAllByUserId(UUID userId);
}
