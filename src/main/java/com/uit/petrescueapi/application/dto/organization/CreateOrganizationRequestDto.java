package com.uit.petrescueapi.application.dto.organization;

import lombok.*;

/**
 * Request DTO for creating an organization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequestDto {

    private String name;
    private String type;  // SHELTER | VET_CENTER
    private String address;
    private String phone;
    private Double latitude;
    private Double longitude;
}
