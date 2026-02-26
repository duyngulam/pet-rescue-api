package com.uit.petrescueapi.application.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * Lightweight response DTO for tag list views.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tag summary for list views")
public class TagSummaryResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID tagId;

    @Schema(example = "rescue-story")
    private String code;

    @Schema(example = "Rescue Story")
    private String name;
}
