package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.rescue.RescueCaseResponseDto;
import com.uit.petrescueapi.application.dto.rescue.RescueCaseSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.RescueCaseQueryDataPort;
import com.uit.petrescueapi.application.port.query.RescueCaseQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Query (read) use-case for RescueCase operations.
 *
 * <p>Thin orchestrator: delegates directly to {@link RescueCaseQueryDataPort}
 * (implemented by the infrastructure query adapter). No domain service
 * involvement -- queries bypass the domain layer entirely (CQRS).</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RescueCaseQueryUseCase implements RescueCaseQueryPort {

    private final RescueCaseQueryDataPort queryDataPort;

    @Override
    public RescueCaseResponseDto findById(UUID caseId) {
        log.debug("Query: find rescue case by id {}", caseId);
        return queryDataPort.findById(caseId);
    }

    @Override
    public Page<RescueCaseSummaryResponseDto> findAll(Pageable pageable) {
        log.debug("Query: find all rescue cases (paginated)");
        return queryDataPort.findAllSummaries(pageable);
    }

    @Override
    public Page<RescueCaseSummaryResponseDto> findNearby(double lat, double lng, double distanceMeters, Pageable pageable) {
        log.debug("Query: find rescue cases nearby ({}, {}) within {} meters", lat, lng, distanceMeters);
        return queryDataPort.findNearbySummaries(lat, lng, distanceMeters, pageable);
    }

    @Override
    public Page<RescueCaseSummaryResponseDto> findWithinBoundingBox(double minLat, double minLng, double maxLat, double maxLng, Pageable pageable) {
        log.debug("Query: find rescue cases within bounding box ({},{}) to ({},{})", minLat, minLng, maxLat, maxLng);
        return queryDataPort.findWithinBoundingBoxSummaries(minLat, minLng, maxLat, maxLng, pageable);
    }
}
