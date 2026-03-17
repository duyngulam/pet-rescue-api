package com.uit.petrescueapi.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PetMedicalRecord — represents a medical record for a pet.
 * Extends BaseEntity for audit fields.
 * Pure domain entity: no JPA annotations.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PetMedicalRecord extends BaseEntity {

    private UUID recordId;
    private UUID petId;
    private String description;
    private String vaccine;
    private String diagnosis;
    private LocalDateTime recordDate;
}
