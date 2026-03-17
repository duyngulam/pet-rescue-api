package com.uit.petrescueapi.application.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * Lightweight response DTO for organization list views.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Organization summary for list views")
public class OrganizationSummaryResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID organizationId;

    @Schema(example = "Happy Paws Shelter")
    private String name;

    @Schema(example = "SHELTER", allowableValues = {"SHELTER", "VET_CENTER"})
    private String type;

    @Schema(example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "PENDING"})
    private String status;

    @Schema(example = "Ngõ 621 giao lộ 907")
    private String streetAddress;

    @Schema(example = "1234")
    private String wardCode;

    @Schema(example = "Yên láng")
    private String ward;

    @Schema(example = "0123")
    private String provinceCode;

    @Schema(example = "Hà Nội")
    private String province;

    @Schema(example = "0842417411")
    private String phone;

    @Schema(example = "duyngu@gmail.com")
    private String email;
}
