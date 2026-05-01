package com.uit.petrescueapi.application.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * Lightweight response DTO for organization list views.
 * Contains names only (no codes).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Organization summary for list views")
public class OrganizationSummaryResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID organizationId;

    @Schema(example = "O-1001")
    private String organizationCode;

    @Schema(example = "Happy Paws Shelter")
    private String name;

    @Schema(example = "SHELTER", allowableValues = {"SHELTER", "VET_CENTER"})
    private String type;

    @Schema(example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "PENDING"})
    private String status;

    @Schema(example = "Ngõ 621 giao lộ 907")
    private String streetAddress;

    @Schema(example = "Yên Láng")
    private String wardName;

    @Schema(example = "Hà Nội")
    private String provinceName;

    @Schema(example = "0842417411")
    private String phone;

    @Schema(example = "duyngu@gmail.com")
    private String email;

    @Schema(example = "https://cdn.example.com/organizations/org-cover.jpg")
    private String imageUrl;
}
