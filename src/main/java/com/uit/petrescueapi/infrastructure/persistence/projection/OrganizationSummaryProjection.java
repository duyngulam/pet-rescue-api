package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.util.UUID;

public interface OrganizationSummaryProjection {
     UUID getOrganizationId();
     String getOrganizationCode();
     String getName();
     String getType(); // SHELTER | VET_CENTER
     String getStatus();
     String getStreetAddress();
     String getWardName();
     String getProvinceName();
     String getPhone();
     String getEmail();
     String getImageUrl();
}
