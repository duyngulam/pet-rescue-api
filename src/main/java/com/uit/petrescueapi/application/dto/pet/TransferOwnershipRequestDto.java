package com.uit.petrescueapi.application.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for manually transferring pet ownership.
 * Used by admins and org owners to change pet ownership.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOwnershipRequestDto {

    @NotBlank(message = "Owner type is required")
    @Pattern(regexp = "^(USER|ORGANIZATION)$", message = "Owner type must be USER or ORGANIZATION")
    private String newOwnerType;

    @NotNull(message = "New owner ID is required")
    private UUID newOwnerId;
}
