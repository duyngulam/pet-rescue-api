package com.uit.petrescueapi.application.port.query;

import com.uit.petrescueapi.application.dto.user.UserResponseDto;

import java.util.UUID;

/**
 * Query (read) port for Auth/User operations.
 * Returns DTOs directly (CQRS read path).
 */
public interface AuthQueryPort {

    /**
     * Get user profile by ID.
     * Used for /auth/me endpoint.
     */
    UserResponseDto findUserById(UUID userId);
}
