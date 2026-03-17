package com.uit.petrescueapi.infrastructure.persistence.projection;

/**
 * Spring Data interface projection for role summary list queries.
 *
 * <p>Maps from JPQL {@code SELECT ... FROM roles r} query.
 * Column aliases must match getter names (camelCase).</p>
 */
public interface RoleSummaryProjection {

    Integer getRoleId();
    String getCode();
    String getName();
}
