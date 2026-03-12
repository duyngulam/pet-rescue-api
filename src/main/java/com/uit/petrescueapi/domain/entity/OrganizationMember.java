package com.uit.petrescueapi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMember {
    private UUID organizationId;
    private UUID userId;
    private String role;      // OWNER |STAFF | VET
    private String status;    // ACTIVE | INACTIVE
    private LocalDateTime joinedAt;
}
