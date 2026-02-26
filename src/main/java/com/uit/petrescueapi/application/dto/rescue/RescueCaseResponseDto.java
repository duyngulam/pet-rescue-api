package com.uit.petrescueapi.application.dto.rescue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for rescue case.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rescue case")
public class RescueCaseResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID caseId;

    private UUID petId;

    @Schema(example = "Buddy")
    private String petName;

    private UUID reportedBy;

    @Schema(example = "johndoe")
    private String reporterUsername;

    private UUID organizationId;

    @Schema(example = "Happy Paws Shelter")
    private String organizationName;

    @Schema(example = "IN_PROGRESS", allowableValues = {"REPORTED", "IN_PROGRESS", "RESCUED", "CLOSED"})
    private String status;

    private LocalDateTime reportedAt;
}
