package com.uit.petrescueapi.application.dto.adoption;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for adoption application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Adoption application")
public class AdoptionResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID applicationId;

    private UUID petId;

    @Schema(description = "Pet name (denormalized for list views)", example = "Buddy")
    private String petName;

    private UUID applicantId;

    @Schema(example = "johndoe")
    private String applicantUsername;

    private UUID organizationId;

    @Schema(example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED", "CANCELLED"})
    private String status;

    @Schema(example = "I have experience with dogs and a large backyard.")
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime decidedAt;
    private UUID decidedBy;
}
