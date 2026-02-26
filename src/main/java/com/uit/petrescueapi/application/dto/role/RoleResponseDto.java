package com.uit.petrescueapi.application.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for role information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role information")
public class RoleResponseDto {

    @Schema(example = "1")
    private Integer roleId;

    @Schema(example = "ADMIN")
    private String code;

    @Schema(example = "Administrator")
    private String name;

    @Schema(example = "Full system access")
    private String description;

    private LocalDateTime createdAt;
}
