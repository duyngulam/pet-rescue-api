package com.uit.petrescueapi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Comment — represents a comment on a post.
 * Supports 2-level hierarchy (parent comment + child reply).
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    private UUID commentId;
    private UUID postId;
    private UUID parentCommentId;
    private UUID authorId;
    private String content;
    private Integer likeCount;
    private Integer replyCount;
}
