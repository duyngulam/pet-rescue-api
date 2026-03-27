package com.uit.petrescueapi.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * MediaFile — represents an uploaded media file (e.g. Cloudinary).
 * Extends BaseEntity for audit fields.
 * Pure domain entity: no JPA annotations.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFile extends BaseEntity {

    private UUID mediaId;
    private UUID uploaderId;
    private String publicId;
    private String resourceType;
    private String format;
    private Integer width;
    private Integer height;
    private Integer bytes;
    private String folder;
    private String status; // TEMP or PERMANENT
}
