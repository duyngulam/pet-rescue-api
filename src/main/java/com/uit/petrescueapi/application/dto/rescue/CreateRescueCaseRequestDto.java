package com.uit.petrescueapi.application.dto.rescue;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRescueCaseRequestDto {
    private UUID petId;
    private UUID organizationId;
    private String species;
    private String color;
    private String size;
    private String condition;
    private String description;
    private Double latitude;
    private Double longitude;
    private String locationText;
    private String provinceCode;
    private String wardCode;
}
