package com.uit.petrescueapi.application.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Current pet owner summary")
public class PetOwnerSummaryDto {

    @Schema(example = "USER", allowableValues = {"USER", "ORGANIZATION"})
    private String ownerType;

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID ownerId;

    @Schema(example = "John Doe / Happy Paws Shelter")
    private String name;

    @Schema(example = "https://cdn.example.com/avatars/u1.jpg")
    private String avatarUrl;

    @Schema(example = "+84901234567")
    private String phone;

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000",
            description = "Primary caretaker user id when ownerType is ORGANIZATION")
    private UUID caretakerUserId;

    @Schema(example = "Caretaker User",
            description = "Primary caretaker display name when ownerType is ORGANIZATION")
    private String caretakerName;

    @Schema(example = "https://cdn.example.com/avatars/caretaker.jpg",
            description = "Primary caretaker avatar URL when ownerType is ORGANIZATION")
    private String caretakerAvatarUrl;

    @Schema(example = "+84909999999",
            description = "Primary caretaker phone when ownerType is ORGANIZATION")
    private String caretakerPhone;
}

