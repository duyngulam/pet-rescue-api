package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.application.port.out.AuthQueryDataPort;
import com.uit.petrescueapi.domain.entity.User;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Query-side adapter for Auth/User read operations.
 * Maps domain entities → DTOs for the CQRS read path.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryAdapter implements AuthQueryDataPort {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto findUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .emailVerified(user.isEmailVerified())
                .roles(user.getRoles().stream().map(r -> r.getCode()).toList())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
