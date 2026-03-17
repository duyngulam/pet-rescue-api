package com.uit.petrescueapi.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Permission — represents a granular permission for RBAC.
 * Pure domain entity: no JPA annotations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    private Integer id;
    private String resource;
    private String action;
    private String code;
    private String description;
    private LocalDateTime createdAt;
}
