package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for post detail queries.
 *
 * <p>Returns all post fields plus author and rescue case data
 * via LEFT JOIN.</p>
 */
public interface PostDetailProjection {

    // ── Summary fields ───────────────────────────
    UUID getPostId();
    String getAuthorUsername();
    String getContent();
    LocalDateTime getCreatedAt();

    // ── Detail fields ────────────────────────────
    UUID getAuthorId();
    UUID getRescueCaseId();
    LocalDateTime getUpdatedAt();
}
