package com.uit.petrescueapi.application.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for tag.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tag for categorizing posts")
public class TagResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID tagId;

    @Schema(example = "rescue-story")
    private String code;

    @Schema(example = "Rescue Story")
    private String name;

    @Schema(example = "Posts about animal rescue stories")
    private String description;

    private LocalDateTime createdAt;
}
