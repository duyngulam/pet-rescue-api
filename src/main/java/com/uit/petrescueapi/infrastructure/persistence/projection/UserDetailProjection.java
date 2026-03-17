package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for user detail queries.
 *
 * <p>Returns all user fields needed for a detail view.</p>
 */
public interface UserDetailProjection {

    // ── Summary fields ───────────────────────────
    UUID getUserId();
    String getUsername();
    String getEmail();
    String getStatus();

    // ── Detail fields ────────────────────────────
    String getAvatarUrl();
    boolean getEmailVerified();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
