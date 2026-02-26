package com.uit.petrescueapi.domain.entity;

import com.uit.petrescueapi.domain.valueobject.PetStatus;
import com.uit.petrescueapi.domain.valueobject.Gender;
import com.uit.petrescueapi.domain.valueobject.HealthStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Pet — the aggregate root of the Pet Rescue domain.
 *
 * This is a pure domain entity: no JPA annotations, no framework coupling.
 * Persistence is handled by the infrastructure layer via mappers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    private UUID id;
    private String name;
    private String species;
    private String breed;
    private Integer age;          // months
    private Gender gender;
    private String color;
    private BigDecimal weight;    // kg
    private String description;
    private PetStatus status;
    private HealthStatus healthStatus;
    private boolean vaccinated;
    private boolean neutered;
    private LocalDate rescueDate;
    private String rescueLocation;

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    private UUID shelterId;

    // ── audit fields ────────────────────────────
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
}
