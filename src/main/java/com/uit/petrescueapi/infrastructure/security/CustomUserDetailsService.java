package com.uit.petrescueapi.infrastructure.security;

import com.uit.petrescueapi.infrastructure.persistence.entity.UserJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Loads a {@link UserDetails} from the database for Spring Security.
 * The {@code username} field in the security context stores the user's UUID.
 *
 * <p><strong>@Transactional is critical</strong> — ensures Hibernate session
 * remains open while loading user and eagerly fetching roles to avoid
 * LazyInitializationException in JWT authentication filter.</p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional(readOnly = true)  // ← CRITICAL: Keeps Hibernate session open
    public UserDetails loadUserByUsername(String userIdOrEmail) throws UsernameNotFoundException {
        // Try to load user with roles eagerly fetched to avoid LazyInitializationException
        java.util.Optional<UserJpaEntity> opt = userJpaRepository.findByEmailWithRoles(userIdOrEmail);
        if (opt.isEmpty()) {
            try {
                java.util.UUID id = java.util.UUID.fromString(userIdOrEmail);
                opt = userJpaRepository.findByIdWithRoles(id);
            } catch (IllegalArgumentException ignored) {
                // not a UUID, continue
            }
        }

        // Fallback to the original lookups if necessary
        if (opt.isEmpty()) {
            opt = userJpaRepository.findByEmail(userIdOrEmail)
                    .or(() -> {
                        try {
                            return userJpaRepository.findById(java.util.UUID.fromString(userIdOrEmail));
                        } catch (IllegalArgumentException e) {
                            return java.util.Optional.empty();
                        }
                    });
        }

        UserJpaEntity entity = opt.orElseThrow(() -> new UsernameNotFoundException("User not found: " + userIdOrEmail));

        var authorities = entity.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getCode()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                entity.getUserId().toString(),
                entity.getPasswordHash() != null ? entity.getPasswordHash() : "",
                entity.isEmailVerified(),      // enabled only if verified
                true,                          // accountNonExpired
                true,                          // credentialsNonExpired
                !"BANNED".equals(entity.getStatus().name()), // accountNonLocked
                authorities
        );
    }
}
