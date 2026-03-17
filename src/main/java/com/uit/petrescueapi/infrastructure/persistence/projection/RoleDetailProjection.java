package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;

/**
 * Spring Data interface projection for role detail queries.
 *
 * <p>Returns all role fields needed for a detail view.</p>
 */
public interface RoleDetailProjection {

    // ── Summary fields ───────────────────────────
    Integer getRoleId();
    String getCode();
    String getName();

    // ── Detail fields ────────────────────────────
    String getDescription();
    LocalDateTime getCreatedAt();
}
