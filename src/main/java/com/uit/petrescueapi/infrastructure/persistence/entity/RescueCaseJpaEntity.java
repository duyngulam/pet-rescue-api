package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code rescue_cases} table.
 */
@Entity
@Table(name = "rescue_cases", indexes = {
        @Index(name = "idx_rescue_status", columnList = "status"),
        @Index(name = "idx_rescue_species", columnList = "species"),
        @Index(name = "idx_rescue_reported_at", columnList = "reported_at")
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RescueCaseJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "case_id", updatable = false, nullable = false)
    private UUID caseId;

    @Column(name = "reported_by")
    private UUID reportedBy;

    @Column(name = "organization_id")
    private UUID organizationId;

    @Column(name = "pet_id")
    private UUID petId;

    @Column(name = "species", length = 100)
    private String species;

    @Column(name = "color", length = 100)
    private String color;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "condition", length = 50)
    private String condition;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "location", columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Column(name = "location_text", columnDefinition = "TEXT")
    private String locationText;

    @Column(name = "province_code", length = 50)
    private String provinceCode;

    @Column(name = "ward_code", length = 50)
    private String wardCode;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "reported_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime reportedAt;

    @Column(name = "resolved_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", insertable = false, updatable = false)
    private UserJpaEntity reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    private OrganizationJpaEntity organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", insertable = false, updatable = false)
    private PetJpaEntity pet;
}
