package com.uit.petrescueapi.application.dto.adoption;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Lightweight response DTO for adoption application list views.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Adoption application summary for list views")
public class AdoptionSummaryResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID applicationId;

    @Schema(example = "Buddy")
    private String petName;

    @Schema(example = "johndoe")
    private String applicantUsername;

    @Schema(example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED", "CANCELLED"})
    private String status;

    private LocalDateTime createdAt;
}
