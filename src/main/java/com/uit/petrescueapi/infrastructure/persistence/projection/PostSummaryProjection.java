package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for post summary list queries.
 *
 * <p>Maps from JPQL {@code SELECT ... FROM posts p LEFT JOIN users u}
 * query. Column aliases must match getter names (camelCase).</p>
 */
public interface PostSummaryProjection {

    UUID getPostId();
    UUID getAuthorId();
    String getAuthorUsername();
    String getContent();
    Integer getLikeCount();
    Integer getCommentCount();
    LocalDateTime getCreatedAt();
}
