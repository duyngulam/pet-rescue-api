package com.uit.petrescueapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Enables JPA auditing so {@code @CreatedDate}, {@code @LastModifiedDate}, etc.
 * are populated automatically on every entity save.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<UUID> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                // No authenticated user -> leave audit user fields null
                return Optional.empty();
            }
            try {
                // Extract user ID from Authentication (JWT subject stores user UUID)
                return Optional.of(UUID.fromString(auth.getName()));
            } catch (IllegalArgumentException e) {
                // If the authentication name is not a valid UUID, skip setting auditor
                return Optional.empty();
            }
        };
    }
}
