package com.uit.petrescueapi.application.dto.role;

import lombok.*;

/**
 * Request DTO for creating a role.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequestDto {

    private String code;
    private String name;
    private String description;
}
