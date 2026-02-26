package com.uit.petrescueapi.application.dto.post;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for updating a post.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDto {

    private String content;
    private List<UUID> mediaIds;
    private List<String> tagCodes;
}
