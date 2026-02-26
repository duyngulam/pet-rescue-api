package com.uit.petrescueapi.application.dto.post;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a new post.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDto {

    private String content;
    private UUID rescueCaseId;
    private List<UUID> mediaIds;
    private List<String> tagCodes;
}
