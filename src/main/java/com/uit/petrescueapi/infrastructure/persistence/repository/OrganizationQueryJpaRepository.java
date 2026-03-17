package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.OrganizationJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationQueryJpaRepository extends JpaRepository<OrganizationJpaEntity, UUID> {

    @Query("""
            SELECT o.organizationId AS organizationId,
                   o.name           AS name,
                   o.type           AS type,
                   o.streetAddress  AS street_address,
                   o.wardCode       AS ward_code,
                   o.wardName       AS ward,
                   o.provinceCode   AS province_code,
                   o.provinceName   AS province,
                   o.phone          AS phone,
                   o.email          AS email
            FROM OrganizationJpaEntity o
            WHERE o.status = 'ACTIVE'
            """)
    Page<OrganizationSummaryProjection> findAllSummary(Pageable pageable);

    @Query(value = """
            SELECT o.organization_id AS organizationId,
                   o.name            AS name,
                   o.type            AS type,
                   o.street_address  AS street_address,
                   o.ward_code       AS ward_code,
                   o.ward_name       AS ward_name,
                   o.province_code   AS province_code,
                   o.province_name   AS province_name,
                   o.phone           AS phone,
                   o.email           AS email,
                   ST_Y(o.location)  AS latitude,
                   ST_X(o.location)  AS longitude,
                   o.status          AS status,
                   o.created_by      AS createdBy,
                   o.created_at      AS createdAt
            FROM organizations o
            WHERE o.organization_id = :id
            """, nativeQuery = true)
    Optional<OrganizationDetailProjection> findDetailById(@Param("id") UUID id);
}