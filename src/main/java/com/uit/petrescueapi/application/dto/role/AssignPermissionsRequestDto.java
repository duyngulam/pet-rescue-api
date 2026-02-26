package com.uit.petrescueapi.application.dto.role;

import lombok.*;

import java.util.List;

/**
 * Request DTO for assigning permissions to a role.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignPermissionsRequestDto {

    private List<Integer> permissionIds;
}
