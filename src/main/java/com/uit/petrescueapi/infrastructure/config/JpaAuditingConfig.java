package com.uit.petrescueapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Enables JPA auditing so {@code @CreatedDate}, {@code @LastModifiedDate}, etc.
 * are populated automatically on every entity save.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        // In a real app this would extract the authenticated user.
        return () -> Optional.of("system");
    }
}
