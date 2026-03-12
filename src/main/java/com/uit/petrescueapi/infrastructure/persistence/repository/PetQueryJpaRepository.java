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
 *
 * <p>All queries here use LEFT JOIN to fetch related data in a single SQL statement.
 * Results are returned as <b>interface projections</b> — Spring Data maps columns
 * to getter methods automatically.</p>
 *
 * <p><b>To add a new query endpoint:</b>
 * <ol>
 *   <li>Create a new projection interface in {@code infrastructure/persistence/projection/}</li>
 *   <li>Add a {@code @Query} method here returning that projection</li>
 *   <li>Add a mapping method in {@code PetQueryAdapter}</li>
 * </ol></p>
 */
@Repository
public interface PetQueryJpaRepository extends JpaRepository<PetJpaEntity, UUID> {

    // ── Summary (list views) ────────────────────

    @Query("""
        SELECT p.id          AS id,
               p.name        AS name,
               p.species     AS species,
               p.breed       AS breed,
               p.age         AS age,
               p.vaccinated  AS vaccinated,
               p.gender      AS gender,
               p.status      AS status,
               p.healthStatus AS healthStatus,
               o.organizationId AS organizationId,
               o.name        AS organizationName,
               o.type        AS organizationType,
               o.status      AS organizationStatus
        FROM PetJpaEntity p
        LEFT JOIN OrganizationJpaEntity o ON p.shelterId = o.organizationId
        WHERE p.deleted = false
    """)
    Page<PetSummaryProjection> findAllSummaries(Pageable pageable);

    @Query("""
        SELECT p.id          AS id,
               p.name        AS name,
               p.species     AS species,
               p.breed       AS breed,
               p.age         AS age,
               p.vaccinated  AS vaccinated,
               p.gender      AS gender,
               p.status      AS status,
               p.healthStatus AS healthStatus,
               o.organizationId AS organizationId,
               o.name        AS organizationName,
               o.type        AS organizationType,
               o.status      AS organizationStatus
        FROM PetJpaEntity p
        LEFT JOIN OrganizationJpaEntity o ON p.shelterId = o.organizationId
        WHERE p.deleted = false AND p.status = 'AVAILABLE'
    """)
    Page<PetSummaryProjection> findAvailableSummaries(Pageable pageable);

    // ── Detail (single pet) ─────────────────────

    @Query("""
        SELECT p.id           AS id,
               p.name         AS name,
               p.species      AS species,
               p.breed        AS breed,
               p.age          AS age,
               p.gender       AS gender,
               p.color        AS color,
               p.weight       AS weight,
               p.description  AS description,
               p.status       AS status,
               p.healthStatus AS healthStatus,
               p.vaccinated   AS vaccinated,
               p.neutered     AS neutered,
               p.rescueDate   AS rescueDate,
               p.rescueLocation AS rescueLocation,
               p.shelterId    AS shelterId,
               p.createdAt    AS createdAt,
               p.updatedAt    AS updatedAt,
               o.organizationId AS organizationId,
               o.name         AS organizationName,
               o.type         AS organizationType,
               o.status       AS organizationStatus
        FROM PetJpaEntity p
        LEFT JOIN OrganizationJpaEntity o ON p.shelterId = o.organizationId
        WHERE p.deleted = false AND p.id = :id
    """)
    Optional<PetDetailProjection> findDetailById(@Param("id") UUID id);

    // ── By Organization (via pet_ownerships join table) ────

    @Query("""
        SELECT p.id          AS id,
               p.name        AS name,
               p.species     AS species,
               p.breed       AS breed,
               p.age         AS age,
               p.vaccinated  AS vaccinated,
               p.gender      AS gender,
               p.status      AS status,
               p.healthStatus AS healthStatus,
               o.organizationId AS organizationId,
               o.name        AS organizationName,
               o.type        AS organizationType,
               o.status      AS organizationStatus
        FROM PetOwnershipJpaEntity po
        JOIN PetJpaEntity p ON po.petId = p.id
        LEFT JOIN OrganizationJpaEntity o ON p.shelterId = o.organizationId
        WHERE po.ownerId = :organizationId
          AND po.ownerType = 'ORGANIZATION'
          AND po.toTime IS NULL
          AND p.deleted = false
    """)
    Page<PetSummaryProjection> findSummariesByOrganizationId(
            @Param("organizationId") UUID organizationId,
            Pageable pageable);

    // ── Image URLs (ElementCollection, fetched separately) ──

    @Query("SELECT i FROM PetJpaEntity p JOIN p.imageUrls i WHERE p.id = :id AND p.deleted = false")
    List<String> findImageUrlsById(@Param("id") UUID id);
}
