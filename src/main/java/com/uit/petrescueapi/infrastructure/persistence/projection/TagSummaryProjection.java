package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.util.UUID;

/**
 * Spring Data interface projection for tag summary list queries.
 *
 * <p>Maps from JPQL {@code SELECT ... FROM tags t} query.
 * Column aliases must match getter names (camelCase).</p>
 */
public interface TagSummaryProjection {

    UUID getTagId();
    String getCode();
    String getName();
}
