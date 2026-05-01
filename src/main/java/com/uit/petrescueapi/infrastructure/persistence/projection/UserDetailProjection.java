package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for user detail queries.
 *
 * <p>Returns all user fields needed for a detail view, including organization data
 * (if the user has MEMBER role and is linked to an organization via organization_members).</p>
 */
public interface UserDetailProjection {

    // ── Summary fields ───────────────────────────
    UUID getUserId();
    String getUserCode();
    String getUsername();
    String getEmail();
    String getStatus();

    // ── Detail fields ────────────────────────────
    String getAvatarUrl();
    String getFullName();
    String getPhone();
    String getGender();
    String getStreetAddress();
    String getWardName();
    String getProvinceName();
    boolean getEmailVerified();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    // ── Organization fields (via LEFT JOIN organization_members + organizations) ──
    UUID getOrganizationId();
    String getOrganizationName();
    String getOrganizationRole();
}
