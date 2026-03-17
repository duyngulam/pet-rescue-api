package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.application.dto.rescue.RescueCaseResponseDto;
import com.uit.petrescueapi.application.dto.rescue.RescueCaseSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Output port for RescueCase read operations (CQRS query side).
 *
 * <p>The infrastructure layer implements this to execute optimized
 * JOIN queries and map projections directly to application DTOs.</p>
 */
public interface RescueCaseQueryDataPort {

    RescueCaseResponseDto findById(UUID caseId);

    Page<RescueCaseSummaryResponseDto> findAllSummaries(Pageable pageable);

    Page<RescueCaseSummaryResponseDto> findNearbySummaries(double lat, double lng, double distanceMeters, Pageable pageable);

    Page<RescueCaseSummaryResponseDto> findWithinBoundingBoxSummaries(double minLat, double minLng, double maxLat, double maxLng, Pageable pageable);
}
