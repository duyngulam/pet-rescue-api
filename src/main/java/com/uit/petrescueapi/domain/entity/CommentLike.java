package com.uit.petrescueapi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CommentLike — represents one user's like on one comment.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {

    private UUID commentId;
    private UUID userId;
    private LocalDateTime createdAt;
}
