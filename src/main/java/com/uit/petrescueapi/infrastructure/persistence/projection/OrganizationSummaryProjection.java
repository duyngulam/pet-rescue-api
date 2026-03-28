package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.util.UUID;

public interface OrganizationSummaryProjection {
     UUID getOrganizationId();
     String getName();
     String getType(); // SHELTER | VET_CENTER
     String getStatus();
     String getStreet_address();
     String getWard_name();
     String getProvince_name();
     String getPhone();
     String getEmail();
}
