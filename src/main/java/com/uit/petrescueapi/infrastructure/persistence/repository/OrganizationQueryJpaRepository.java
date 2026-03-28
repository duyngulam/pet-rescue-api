package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.domain.valueobject.OrganizationStatus;
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
                   o.status         AS status,
                   o.streetAddress  AS street_address,
                   o.wardName       AS ward_name,
                   o.provinceName   AS province_name,
                   o.phone          AS phone,
                   o.email          AS email
            FROM OrganizationJpaEntity o
            WHERE o.status = 'ACTIVE'
            """)
    Page<OrganizationSummaryProjection> findAllSummary(Pageable pageable);

    @Query("""
            SELECT o.organizationId AS organizationId,
                   o.name           AS name,
                   o.type           AS type,
                   o.status         AS status,
                   o.streetAddress  AS street_address,
                   o.wardName       AS ward_name,
                   o.provinceName   AS province_name,
                   o.phone          AS phone,
                   o.email          AS email
            FROM OrganizationJpaEntity o
            WHERE o.status = :status
            """)
    Page<OrganizationSummaryProjection> findByStatus(@Param("status") OrganizationStatus status, Pageable pageable);

    @Query(value = """
            SELECT o.organization_id     AS organizationId,
                   o.name                AS name,
                   o.description         AS description,
                   o.type                AS type,
                   o.street_address      AS street_address,
                   o.ward_name           AS ward_name,
                   o.province_name       AS province_name,
                   o.phone               AS phone,
                   o.email               AS email,
                   o.official_link       AS official_link,
                   ST_Y(o.location)      AS latitude,
                   ST_X(o.location)      AS longitude,
                   o.status              AS status,
                   o.requested_by_user_id AS requested_by_user_id,
                   o.created_by          AS createdBy,
                   o.created_at          AS createdAt,
                   o.updated_at          AS updatedAt
            FROM organizations o
            WHERE o.organization_id = :id
            """, nativeQuery = true)
    Optional<OrganizationDetailProjection> findDetailById(@Param("id") UUID id);
}