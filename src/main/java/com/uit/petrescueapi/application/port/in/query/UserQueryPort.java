package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.user.UserReputationResponseDto;
import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for User operations.
 * Handles profile lookups, user listing and reputation queries.
 */
public interface UserQueryPort {

    UserResponseDto findById(UUID userId);

    UserResponseDto getCurrentUser();

    Page<UserResponseDto> findAll(Pageable pageable);

    UserReputationResponseDto getReputation(UUID userId);
}
