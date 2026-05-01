package com.uit.petrescueapi.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Banner — represents a banner for the landing page.
 * Pure domain entity: no JPA annotations.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Banner extends BaseEntity {

    private UUID bannerId;
    private String title;
    private String subtitle;
    private String buttonText;
    private UUID mediaId;          // Reference to media_files
    private String linkUrl;
    private String linkType;       // INTERNAL, EXTERNAL, NONE
    private Integer displayOrder;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;
    private String targetPage;     // HOME, ADOPTION, RESCUE, etc.

    /**
     * Check if the banner is currently valid (within date range).
     */
    public boolean isWithinDateRange() {
        LocalDateTime now = LocalDateTime.now();
        boolean afterStart = startDate == null || !now.isBefore(startDate);
        boolean beforeEnd = endDate == null || !now.isAfter(endDate);
        return afterStart && beforeEnd;
    }

    /**
     * Check if the banner should be displayed (active + within date range + not deleted).
     */
    public boolean shouldDisplay() {
        return Boolean.TRUE.equals(active) && !isDeleted() && isWithinDateRange();
    }
}
