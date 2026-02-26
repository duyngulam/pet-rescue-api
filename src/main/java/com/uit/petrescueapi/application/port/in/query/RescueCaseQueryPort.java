package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.rescue.RescueCaseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for Rescue Case operations.
 * Handles rescue case lookups and listing.
 */
public interface RescueCaseQueryPort {

    RescueCaseResponseDto findById(UUID caseId);

    Page<RescueCaseResponseDto> findAll(Pageable pageable);
}
