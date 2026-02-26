package com.uit.petrescueapi.application.dto.rescue;

import lombok.*;

import java.util.UUID;

/**
 * Request DTO for reporting a new rescue case.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRescueCaseRequestDto {

    private UUID petId;
    private UUID organizationId;
    private String description;
}
