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
    String getType();
    String getStreet_address();
    String getWard_code();
    String getWard_name();
    String getProvince_code();
    String getProvince_name();
    String getPhone();
    String getEmail();
    Double getLatitude();
    Double getLongitude();
    String getStatus();
    UUID getCreatedBy();
    LocalDateTime getCreatedAt();
}
