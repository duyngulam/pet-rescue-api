package com.uit.petrescueapi.application.dto.pet;

import com.uit.petrescueapi.domain.valueobject.Gender;
import com.uit.petrescueapi.domain.valueobject.HealthStatus;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Pet data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponseDto {

    private UUID id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String ageDisplay;
    private Gender gender;
    private String color;
    private BigDecimal weight;
    private String description;
    private PetStatus status;
    private HealthStatus healthStatus;
    private boolean vaccinated;
    private boolean neutered;
    private LocalDate rescueDate;
    private String rescueLocation;
    private List<String> imageUrls;
    private UUID shelterId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
