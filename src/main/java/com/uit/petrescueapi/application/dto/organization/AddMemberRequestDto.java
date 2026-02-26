package com.uit.petrescueapi.application.dto.organization;

import lombok.*;

import java.util.UUID;

/**
 * Request DTO for adding a member to an organization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberRequestDto {

    private UUID userId;
    private String role;  // OWNER | MANAGER | MEMBER | VOLUNTEER
}
