package com.uit.petrescueapi.application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequestDto {
    private UUID parentCommentId;

    @NotBlank(message = "Comment content is required")
    private String content;
}
