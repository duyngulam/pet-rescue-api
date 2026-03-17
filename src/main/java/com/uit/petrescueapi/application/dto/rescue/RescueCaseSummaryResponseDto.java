package com.uit.petrescueapi.application.dto.rescue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rescue case summary")
public class RescueCaseSummaryResponseDto {
    private UUID caseId;
    private String species;
    @Schema(example = "IN_PROGRESS", allowableValues = {"REPORTED", "IN_PROGRESS", "RESCUED", "CLOSED"})
    private String status;
    private String reporterUsername;
    private String locationText;
    private LocalDateTime reportedAt;
}
