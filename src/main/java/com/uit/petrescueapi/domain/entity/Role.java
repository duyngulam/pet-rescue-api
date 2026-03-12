package com.uit.petrescueapi.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Role — value-like entity for RBAC.
 * Pure domain: no JPA annotations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer id;
    private String code;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
