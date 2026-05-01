package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interface projection for organization member list.
 * JOINs organization_members with users — avoids N+1.
 */
public interface OrganizationMemberProjection {
    UUID getOrganizationId();
    String getOrganizationName();
    UUID getUserId();
    String getUsername();
    String getRole();
    String getStatus();
    LocalDateTime getJoinedAt();
}
