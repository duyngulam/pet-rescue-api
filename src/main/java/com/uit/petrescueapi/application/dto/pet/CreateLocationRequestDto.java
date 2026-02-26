package com.uit.petrescueapi.application.dto.pet;

import lombok.*;

/**
 * Request DTO for recording a pet GPS location.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLocationRequestDto {

    private Double latitude;
    private Double longitude;
    private String source;
}
