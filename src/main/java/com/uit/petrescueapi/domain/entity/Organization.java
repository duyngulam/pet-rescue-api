package com.uit.petrescueapi.domain.entity;

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
	private String name;
	private String type; // SHELTER | VET_CENTER
	private String street_address;
	private String ward_code;
	private String ward_name;
	private String province_code;
	private String province_name;
	private String phone;
	private String email;
	private String officialLink;
	private Double latitude;
	private Double longitude;
	private String status; // ACTIVE | INACTIVE | PENDING
}
