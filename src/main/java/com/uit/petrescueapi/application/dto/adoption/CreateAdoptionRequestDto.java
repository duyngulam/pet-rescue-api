package com.uit.petrescueapi.application.dto.adoption;

import lombok.*;

import java.util.UUID;

/**
 * Request DTO for submitting an adoption application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdoptionRequestDto {

    private UUID petId;
    private UUID organizationId;
    private String note;
}
