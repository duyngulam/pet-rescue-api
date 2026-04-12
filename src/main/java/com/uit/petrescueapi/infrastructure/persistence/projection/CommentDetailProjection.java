package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Projection for single comment detail query.
 */
public interface CommentDetailProjection {

    UUID getCommentId();
    UUID getPostId();
    UUID getParentCommentId();
    UUID getAuthorId();
    String getAuthorUsername();
    String getContent();
    Integer getLikeCount();
    Integer getReplyCount();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
