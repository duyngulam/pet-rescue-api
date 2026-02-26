package com.uit.petrescueapi.application.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Lightweight response DTO for role list views.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Role summary for list views")
public class RoleSummaryResponseDto {

    @Schema(example = "1")
    private Integer roleId;

    @Schema(example = "ADMIN")
    private String code;

    @Schema(example = "Administrator")
    private String name;
}
