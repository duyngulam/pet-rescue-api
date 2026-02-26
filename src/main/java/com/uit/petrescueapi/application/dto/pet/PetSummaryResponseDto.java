package com.uit.petrescueapi.application.dto.pet;

import com.uit.petrescueapi.domain.valueobject.Gender;
import com.uit.petrescueapi.domain.valueobject.HealthStatus;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * Lightweight response DTO for pet list / search results.
 * Contains only the essential fields needed for card/grid views.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pet summary for list views")
public class PetSummaryResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(example = "Buddy")
    private String name;

    @Schema(example = "Dog")
    private String species;

    @Schema(example = "Golden Retriever")
    private String breed;

    @Schema(example = "24",description = "month")
    private Integer age;

    @Schema(example = "true")
    private boolean vaccinated;

    private Gender gender;
    private PetStatus status;
    private HealthStatus healthStatus;

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID organizationId;

    @Schema(example = "TP Ho Chi Minh")
    private String province;

    @Schema(example = "1")
    private  int provinceCode;

    @Schema(example = "Di An")
    private String ward;
    @Schema(example = "2")
    private  int wardCode;

    @Schema(description = "Thumbnail image URL (first image)", example = "https://storage.example.com/pets/buddy-01.jpg")
    private String imageUrl;
}
