package com.uit.petrescueapi.domain.entity;

import com.uit.petrescueapi.domain.valueobject.RescueCaseStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * RescueCase — represents a reported animal rescue case.
 * Extends BaseEntity for audit fields.
 * Pure domain entity: no JPA annotations.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RescueCase extends BaseEntity {

    private UUID caseId;
    private UUID reportedBy;
    private UUID organizationId;
    private UUID petId;
    private String species;
    private String color;
    private String size;
    private String condition;
    private String description;
    private Double latitude;
    private Double longitude;
    private String locationText;
    private String provinceCode;
    private String wardCode;
    private RescueCaseStatus status;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
}
