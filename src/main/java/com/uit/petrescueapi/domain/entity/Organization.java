package com.uit.petrescueapi.domain.entity;

import com.uit.petrescueapi.domain.valueobject.OrganizationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {

	private java.util.UUID organizationId;
	private String organizationCode;
	private String name;
	private String description;
	private String type; // SHELTER | VET_CENTER
	private String streetAddress;
	private String wardCode;
	private String wardName;
	private String provinceCode;
	private String provinceName;
	private String phone;
	private String email;
	private String imageUrl;
	private String officialLink;
	private Double latitude;
	private Double longitude;
	private OrganizationStatus status; // PENDING | ACTIVE | INACTIVE
	private java.util.UUID requestedByUserId; // User who requested org creation (for OWNER assignment on approval)
}
