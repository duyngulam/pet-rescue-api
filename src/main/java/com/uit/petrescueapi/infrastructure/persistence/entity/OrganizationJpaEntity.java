package com.uit.petrescueapi.infrastructure.persistence.entity;

import com.uit.petrescueapi.domain.valueobject.OrganizationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

/**
 * JPA entity mapped to the {@code organizations} table.
 */
@Entity
@Table(name = "organizations")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "organization_id", updatable = false, nullable = false)
    private UUID organizationId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", length = 50)
    private String type; // SHELTER | VET_CENTER

    @Column(name = "street_address", columnDefinition = "TEXT")
    private String streetAddress;

    @Column(name = "ward_code")
    private String wardCode;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "official_link", columnDefinition = "TEXT")
    private String officialLink;

    @Column(name = "location", columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private OrganizationStatus status; // PENDING | ACTIVE | INACTIVE

    @Column(name = "requested_by_user_id")
    private UUID requestedByUserId; // User who requested org creation (for OWNER assignment on approval)
}
