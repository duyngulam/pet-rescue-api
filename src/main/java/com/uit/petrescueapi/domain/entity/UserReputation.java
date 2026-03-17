package com.uit.petrescueapi.domain.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UserReputation — tracks a user's reputation score and level.
 * Pure domain entity: no JPA annotations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReputation {

    private UUID userId;
    private Integer score;
    private String level;
    private LocalDateTime updatedAt;
}
