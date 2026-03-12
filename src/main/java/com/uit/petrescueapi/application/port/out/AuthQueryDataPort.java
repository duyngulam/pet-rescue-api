package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.application.dto.user.UserResponseDto;

import java.util.UUID;

/**
 * Output port for Auth/User CQRS read-side adapter.
 */
public interface AuthQueryDataPort {

    UserResponseDto findUserById(UUID userId);
}
