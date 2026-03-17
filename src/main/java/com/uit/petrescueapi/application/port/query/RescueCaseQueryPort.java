package com.uit.petrescueapi.application.port.query;

import com.uit.petrescueapi.application.dto.rescue.RescueCaseResponseDto;
import com.uit.petrescueapi.application.dto.rescue.RescueCaseSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RescueCaseQueryPort {
    RescueCaseResponseDto findById(UUID caseId);
    Page<RescueCaseSummaryResponseDto> findAll(Pageable pageable);
    Page<RescueCaseSummaryResponseDto> findNearby(double lat, double lng, double distanceMeters, Pageable pageable);
    Page<RescueCaseSummaryResponseDto> findWithinBoundingBox(double minLat, double minLng, double maxLat, double maxLng, Pageable pageable);
}
