package com.uit.petrescueapi.application.dto.rescue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rescue case")
public class RescueCaseResponseDto {
    private UUID caseId;
    private UUID petId;
    private String petName;
    private UUID reportedBy;
    private String reporterUsername;
    private UUID organizationId;
    private String organizationName;
    private String species;
    private String color;
    private String size;
    private String condition;
    private String description;
    @Schema(example = "IN_PROGRESS", allowableValues = {"REPORTED", "IN_PROGRESS", "RESCUED", "CLOSED"})
    private String status;
    private Double latitude;
    private Double longitude;
    private String locationText;
    private String provinceCode;
    private String wardCode;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
}
