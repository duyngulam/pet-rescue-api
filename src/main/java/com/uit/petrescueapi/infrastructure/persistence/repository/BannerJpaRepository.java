package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.BannerJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BannerJpaRepository extends JpaRepository<BannerJpaEntity, UUID> {

    Page<BannerJpaEntity> findByDeletedFalse(Pageable pageable);

    Page<BannerJpaEntity> findByTargetPageAndDeletedFalse(String targetPage, Pageable pageable);

    /**
     * Find active banners for a specific target page that are within their display date range.
     * Ordered by display_order ascending.
     */
    @Query("""
        SELECT b FROM BannerJpaEntity b
        WHERE b.targetPage = :targetPage
          AND b.active = true
          AND b.deleted = false
          AND (b.startDate IS NULL OR b.startDate <= :now)
          AND (b.endDate IS NULL OR b.endDate >= :now)
        ORDER BY b.displayOrder ASC
        """)
    List<BannerJpaEntity> findActiveByTargetPage(
            @Param("targetPage") String targetPage,
            @Param("now") LocalDateTime now
    );
}
