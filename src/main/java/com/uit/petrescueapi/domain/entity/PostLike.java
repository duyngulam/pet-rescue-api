package com.uit.petrescueapi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PostLike — represents one user's like on one post.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {

    private UUID postId;
    private UUID userId;
    private LocalDateTime createdAt;
}
