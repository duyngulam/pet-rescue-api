package com.uit.petrescueapi.application.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for pet ownership history entry.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pet ownership history entry")
public class PetOwnershipResponseDto {

    private UUID petId;

    @Schema(example = "USER", allowableValues = {"USER", "ORGANIZATION"})
    private String ownerType;

    private UUID ownerId;

    @Schema(example = "johndoe / Happy Paws Shelter")
    private String ownerName;

    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}
