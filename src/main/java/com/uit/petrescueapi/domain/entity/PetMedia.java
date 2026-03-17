package com.uit.petrescueapi.domain.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PetMedia — represents a media attachment (image/video) associated with a pet.
 * Pure domain entity: no JPA annotations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetMedia {

    private UUID mediaId;
    private UUID petId;
    private String url;
    private String type;
    private LocalDateTime createdAt;
}
