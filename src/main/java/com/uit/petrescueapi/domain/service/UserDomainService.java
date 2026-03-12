package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.Role;
import com.uit.petrescueapi.domain.entity.User;
import com.uit.petrescueapi.domain.exception.ResourceAlreadyExistsException;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.repository.RoleRepository;
import com.uit.petrescueapi.domain.repository.UserRepository;
import com.uit.petrescueapi.domain.valueobject.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Domain service for user-profile business rules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserDomainService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Create a user with specified role (for admin/dev operations).
     * Password must be pre-hashed by the caller.
     */
    public User createUser(String username, String email, String hashedPassword, String roleCode) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("User", "email", email);
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("User", "username", username);
        }

        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "code", roleCode));

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .passwordHash(hashedPassword)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)  // Auto-verified for admin-created accounts
                .roles(Set.of(role))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public User updateProfile(UUID id, String username, String avatarUrl) {
        User user = findById(id);
        if (username != null) user.setUsername(username);
        if (avatarUrl != null) user.setAvatarUrl(avatarUrl);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
