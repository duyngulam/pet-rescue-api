package com.uit.petrescueapi.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Tag — represents a tag/label for categorizing posts.
 * Extends BaseEntity for audit fields.
 * Pure domain entity: no JPA annotations.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseEntity {

    private UUID tagId;
    private String code;
    private String name;
    private String description;
}
