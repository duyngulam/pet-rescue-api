package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.rescue.RescueCaseResponseDto;
import com.uit.petrescueapi.application.dto.rescue.RescueCaseSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.RescueCaseQueryDataPort;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.infrastructure.persistence.projection.RescueCaseDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.RescueCaseSummaryProjection;
import com.uit.petrescueapi.infrastructure.persistence.repository.RescueCaseQueryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Query-side adapter (CQRS read path) for RescueCase.
 *
 * <p>Executes optimized JOIN queries via {@link RescueCaseQueryJpaRepository},
 * maps infrastructure projections to application DTOs.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RescueCaseQueryAdapter implements RescueCaseQueryDataPort {

    private final RescueCaseQueryJpaRepository queryRepo;

    // ── List (summary) queries ──────────────────

    @Override
    public Page<RescueCaseSummaryResponseDto> findAllSummaries(Pageable pageable) {
        return queryRepo.findAllSummaries(pageable).map(this::toSummaryDto);
    }

    @Override
    public Page<RescueCaseSummaryResponseDto> findNearbySummaries(double lat, double lng,
                                                                   double distanceMeters,
                                                                   Pageable pageable) {
        // Port passes (lat, lng) but JPA repo expects (lng, lat) — swap parameters
        return queryRepo.findNearby(lng, lat, distanceMeters, pageable).map(this::toSummaryDto);
    }

    @Override
    public Page<RescueCaseSummaryResponseDto> findWithinBoundingBoxSummaries(double minLat, double minLng,
                                                                             double maxLat, double maxLng,
                                                                             Pageable pageable) {
        // Port passes (minLat, minLng, maxLat, maxLng) but JPA repo expects (minLng, minLat, maxLng, maxLat)
        return queryRepo.findWithinBoundingBox(minLng, minLat, maxLng, maxLat, pageable).map(this::toSummaryDto);
    }

    // ── Detail (single rescue case) query ───────

    @Override
    public RescueCaseResponseDto findById(UUID caseId) {
        RescueCaseDetailProjection proj = queryRepo.findDetailById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("RescueCase", "caseId", caseId));
        return toResponseDto(proj);
    }

    // ── Projection → DTO mappers ────────────────

    private RescueCaseSummaryResponseDto toSummaryDto(RescueCaseSummaryProjection p) {
        return RescueCaseSummaryResponseDto.builder()
                .caseId(p.getCaseId())
                .status(p.getStatus())
                .reporterUsername(p.getReporterUsername())
                .reportedAt(p.getReportedAt())
                .build();
    }

    private RescueCaseResponseDto toResponseDto(RescueCaseDetailProjection p) {
        return RescueCaseResponseDto.builder()
                .caseId(p.getCaseId())
                .petId(p.getPetId())
                .petName(p.getPetName())
                .reportedBy(p.getReportedBy())
                .reporterUsername(p.getReporterUsername())
                .organizationId(p.getOrganizationId())
                .organizationName(p.getOrganizationName())
                .species(p.getSpecies())
                .color(p.getColor())
                .size(p.getSize())
                .condition(p.getCondition())
                .description(p.getDescription())
                .status(p.getStatus())
                .latitude(p.getLocationLat())
                .longitude(p.getLocationLng())
                .locationText(p.getLocationText())
                .wardName(p.getWardName())
                .provinceName(p.getProvinceName())
                .reportedAt(p.getReportedAt())
                .resolvedAt(p.getResolvedAt())
                .build();
    }
}
