package com.uit.petrescueapi.application.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for permission information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Permission information")
public class PermissionResponseDto {

    @Schema(example = "1")
    private Integer permissionId;

    @Schema(example = "PET")
    private String resource;

    @Schema(example = "CREATE")
    private String action;

    @Schema(example = "PET:CREATE")
    private String code;

    @Schema(example = "Create a new pet")
    private String description;

    private LocalDateTime createdAt;
}
