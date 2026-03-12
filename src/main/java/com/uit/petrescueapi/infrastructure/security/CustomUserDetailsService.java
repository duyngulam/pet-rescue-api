package com.uit.petrescueapi.infrastructure.security;

import com.uit.petrescueapi.infrastructure.persistence.entity.UserJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Loads a {@link UserDetails} from the database for Spring Security.
 * The {@code username} field in the security context stores the user's UUID.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdOrEmail) throws UsernameNotFoundException {
        UserJpaEntity entity = userJpaRepository.findByEmail(userIdOrEmail)
                .or(() -> {
                    try {
                        return userJpaRepository.findById(java.util.UUID.fromString(userIdOrEmail));
                    } catch (IllegalArgumentException e) {
                        return java.util.Optional.empty();
                    }
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userIdOrEmail));

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
