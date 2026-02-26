package com.uit.petrescueapi.application.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for pet GPS location.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pet GPS location")
public class PetLocationResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID locationId;

    private UUID petId;

    @Schema(example = "10.762622")
    private Double latitude;

    @Schema(example = "106.660172")
    private Double longitude;

    @Schema(example = "GPS_TRACKER", allowableValues = {"GPS_TRACKER", "MANUAL", "SHELTER"})
    private String source;

    private LocalDateTime recordedAt;
}
