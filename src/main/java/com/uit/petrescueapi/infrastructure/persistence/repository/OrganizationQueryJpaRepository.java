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

    @Query("""
            SELECT o.organizationId AS organizationId,
                   o.name           AS name,
                   o.type           AS type,
                   o.streetAddress  AS street_address,
                   o.wardCode       AS ward_code,
                   o.wardName       AS ward_name,
                   o.provinceCode   AS province_code,
                   o.provinceName   AS province_name,
                   o.phone          AS phone,
                   o.email          AS email,
                   o.latitude       AS latitude,
                   o.longitude      AS longitude,
                   o.status         AS status,
                   o.createdBy      AS createdBy,
                   o.createdAt      AS createdAt
            FROM OrganizationJpaEntity o
            WHERE o.organizationId = :id
            """)
    Optional<OrganizationDetailProjection> findDetailById(@Param("id") UUID id);
}