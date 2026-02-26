package com.uit.petrescueapi.application.dto.pet;

import lombok.*;

/**
 * Request DTO for creating a pet medical record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicalRecordRequestDto {

    private String description;
    private String vaccine;
    private String diagnosis;
    private String recordDate;
}
