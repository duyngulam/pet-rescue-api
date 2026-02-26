package com.uit.petrescueapi.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for user reputation / score.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User reputation / score")
public class UserReputationResponseDto {

    private UUID userId;

    @Schema(example = "150")
    private Integer score;

    @Schema(example = "GOLD", allowableValues = {"BRONZE", "SILVER", "GOLD", "PLATINUM"})
    private String level;

    private LocalDateTime updatedAt;
}
