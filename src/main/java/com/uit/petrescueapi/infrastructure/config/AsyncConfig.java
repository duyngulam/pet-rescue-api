package com.uit.petrescueapi.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Enables Spring's {@code @Async} support so that methods annotated with
 * {@link org.springframework.scheduling.annotation.Async} (e.g. email sending)
 * run in a separate thread pool.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
