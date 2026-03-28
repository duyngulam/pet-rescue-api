package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.PetJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.projection.PetDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.PetSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Dedicated read-only JPA repository for Pet queries (CQRS query side).
 */
@Repository
public interface PetQueryJpaRepository extends JpaRepository<PetJpaEntity, UUID> {

    // ── Summary (list views) with filters ────────────────────

    @Query(value = """
        SELECT p.pet_id AS id,
               p.name AS name,
               p.species AS species,
               p.breed AS breed,
               p.age AS age,
               p.is_vaccinated AS vaccinated,
               p.gender AS gender,
               p.status AS status,
               p.health_status AS healthStatus,
               (SELECT mf.public_id FROM pet_media pm JOIN media_files mf ON pm.media_file_id = mf.media_id
                WHERE pm.pet_id = p.pet_id ORDER BY pm.created_at LIMIT 1) AS imagePublicId,
               o.organization_id AS organizationId,
               o.name AS organizationName,
               o.province_name AS provinceName,
               CAST(o.province_code AS INTEGER) AS provinceCode,
               o.ward_name AS wardName,
               CAST(o.ward_code AS INTEGER) AS wardCode
        FROM pets p
        LEFT JOIN organizations o ON p.shelter_id = o.organization_id
        WHERE p.is_deleted = false
          AND (:species IS NULL OR p.species = :species)
          AND (:breed IS NULL OR p.breed = :breed)
          AND (:gender IS NULL OR p.gender = :gender)
    """, nativeQuery = true)
    Page<PetSummaryProjection> findAllWithFilters(
            @Param("species") String species,
            @Param("breed") String breed,
            @Param("gender") String gender,
            Pageable pageable);

    @Query(value = """
        SELECT p.pet_id AS id,
               p.name AS name,
               p.species AS species,
               p.breed AS breed,
               p.age AS age,
               p.is_vaccinated AS vaccinated,
               p.gender AS gender,
               p.status AS status,
               p.health_status AS healthStatus,
               (SELECT mf.public_id FROM pet_media pm JOIN media_files mf ON pm.media_file_id = mf.media_id
                WHERE pm.pet_id = p.pet_id ORDER BY pm.created_at LIMIT 1) AS imagePublicId,
               o.organization_id AS organizationId,
               o.name AS organizationName,
               o.province_name AS provinceName,
               CAST(o.province_code AS INTEGER) AS provinceCode,
               o.ward_name AS wardName,
               CAST(o.ward_code AS INTEGER) AS wardCode
        FROM pets p
        LEFT JOIN organizations o ON p.shelter_id = o.organization_id
        WHERE p.is_deleted = false AND p.status = 'AVAILABLE'
          AND (:species IS NULL OR p.species = :species)
          AND (:breed IS NULL OR p.breed = :breed)
          AND (:gender IS NULL OR p.gender = :gender)
    """, nativeQuery = true)
    Page<PetSummaryProjection> findAvailableWithFilters(
            @Param("species") String species,
            @Param("breed") String breed,
            @Param("gender") String gender,
            Pageable pageable);

    // ── Detail (single pet) ─────────────────────

    @Query(value = """
        SELECT p.pet_id AS id,
               p.name AS name,
               p.species AS species,
               p.breed AS breed,
               p.age AS age,
               p.gender AS gender,
               p.color AS color,
               p.weight AS weight,
               p.description AS description,
               p.status AS status,
               p.health_status AS healthStatus,
               p.is_vaccinated AS vaccinated,
               p.is_neutered AS neutered,
               p.rescue_date AS rescueDate,
               p.rescue_location AS rescueLocation,
               p.shelter_id AS shelterId,
               p.created_at AS createdAt,
               p.updated_at AS updatedAt,
               o.organization_id AS organizationId,
               o.name AS organizationName,
               o.province_name AS provinceName,
               CAST(o.province_code AS INTEGER) AS provinceCode,
               o.ward_name AS wardName,
               CAST(o.ward_code AS INTEGER) AS wardCode
        FROM pets p
        LEFT JOIN organizations o ON p.shelter_id = o.organization_id
        WHERE p.is_deleted = false AND p.pet_id = :id
    """, nativeQuery = true)
    Optional<PetDetailProjection> findDetailById(@Param("id") UUID id);

    // ── By Organization ────

    @Query(value = """
        SELECT p.pet_id AS id,
               p.name AS name,
               p.species AS species,
               p.breed AS breed,
               p.age AS age,
               p.is_vaccinated AS vaccinated,
               p.gender AS gender,
               p.status AS status,
               p.health_status AS healthStatus,
               (SELECT mf.public_id FROM pet_media pm JOIN media_files mf ON pm.media_file_id = mf.media_id
                WHERE pm.pet_id = p.pet_id ORDER BY pm.created_at LIMIT 1) AS imagePublicId,
               o.organization_id AS organizationId,
               o.name AS organizationName,
               o.province_name AS provinceName,
               CAST(o.province_code AS INTEGER) AS provinceCode,
               o.ward_name AS wardName,
               CAST(o.ward_code AS INTEGER) AS wardCode
        FROM pet_ownerships po
        JOIN pets p ON po.pet_id = p.pet_id
        LEFT JOIN organizations o ON p.shelter_id = o.organization_id
        WHERE po.owner_id = :organizationId
          AND po.owner_type = 'ORGANIZATION'
          AND po.to_time IS NULL
          AND p.is_deleted = false
    """, nativeQuery = true)
    Page<PetSummaryProjection> findSummariesByOrganizationId(
            @Param("organizationId") UUID organizationId,
            Pageable pageable);

    // ── Image public_ids ──

    @Query(value = """
            SELECT mf.public_id
            FROM pet_media pm
            JOIN media_files mf ON pm.media_file_id = mf.media_id
            JOIN pets p ON pm.pet_id = p.pet_id
            WHERE p.pet_id = :id AND p.is_deleted = false
            ORDER BY pm.created_at DESC
            """, nativeQuery = true)
    List<String> findImagePublicIdsById(@Param("id") UUID id);
}
