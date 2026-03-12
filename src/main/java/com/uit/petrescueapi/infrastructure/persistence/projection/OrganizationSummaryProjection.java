package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.util.UUID;

public interface OrganizationSummaryProjection {
     UUID getOrganizationId();
     String getName();
     String getType(); // SHELTER | VET_CENTER
     String getStreet_address();
     String getProvince_code();
     String getProvince();
     String getWard_code();
     String getWard();
     String getPhone();
     String getEmail();
}
