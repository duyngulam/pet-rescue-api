package com.uit.petrescueapi.application.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Admin-only request for assigning an organization role to an existing user.
 *
 * <p>Target user must have system role USER — admins cannot be assigned org roles.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Assign organization role to existing user")
public class AssignOrgRoleRequestDto {

    @NotNull
    @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "ID of the existing user to assign")
    private UUID userId;

    @NotBlank
    @Schema(example = "STAFF", allowableValues = {"OWNER", "STAFF", "VET"},
            description = "Organization role to assign")
    private String organizationRole;
}
