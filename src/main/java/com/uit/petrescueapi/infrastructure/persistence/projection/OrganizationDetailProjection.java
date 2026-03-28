package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interface projection for organization detail view.
 * Used by {@code OrganizationQueryJpaRepository.findDetailById()}.
 */
public interface OrganizationDetailProjection {
    UUID getOrganizationId();
    String getName();
    String getDescription();
    String getType();
    String getStreet_address();
    String getWard_name();
    String getProvince_name();
    String getPhone();
    String getEmail();
    String getOfficial_link();
    Double getLatitude();
    Double getLongitude();
    String getStatus();
    UUID getRequested_by_user_id();
    UUID getCreatedBy();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
