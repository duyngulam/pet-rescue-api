package com.uit.petrescueapi.application.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Request DTO for creating or updating an organization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequestDto {

    @Schema(example = "Happy Paws Shelter")
    private String name;

    @Schema(example = "SHELTER", allowableValues = {"SHELTER", "VET_CENTER"})
    private String type;

    @Schema(example = "123 Nguyen Trai")
    private String street_address;

    @Schema(example = "00001")
    private String ward_code;

    @Schema(example = "Phuong 1")
    private String ward_name;

    @Schema(example = "79")
    private String province_code;

    @Schema(example = "Ho Chi Minh")
    private String province_name;

    @Schema(example = "+84-28-1234-5678")
    private String phone;

    @Schema(example = "contact@shelter.vn")
    private String email;

    @Schema(example = "10.762622")
    private Double latitude;

    @Schema(example = "106.660172")
    private Double longitude;
}
