package com.uit.petrescueapi.infrastructure.persistence.projection;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Spring Data interface projection for rescue case detail queries.
 *
 * <p>Returns all rescue case fields plus reporter, organization, and pet data
 * via LEFT JOIN. Contains names (wardName, provinceName) for response.</p>
 */
public interface RescueCaseDetailProjection {

    // ── Summary fields ───────────────────────────
    UUID getCaseId();
    String getSpecies();
    String getStatus();
    LocalDateTime getReportedAt();
    String getLocationText();
    String getReporterUsername();

    // ── Detail fields ────────────────────────────
    UUID getReportedBy();
    UUID getOrganizationId();
    String getOrganizationName();
    UUID getPetId();
    String getPetName();
    String getColor();
    String getSize();
    String getCondition();
    String getDescription();
    Double getLocationLat();
    Double getLocationLng();
    String getWardName();
    String getProvinceName();
    LocalDateTime getResolvedAt();
}
