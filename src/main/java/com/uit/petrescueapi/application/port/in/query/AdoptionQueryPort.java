package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.adoption.AdoptionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for Adoption operations.
 * Handles adoption application lookups and listing.
 */
public interface AdoptionQueryPort {

    AdoptionResponseDto findById(UUID applicationId);

    Page<AdoptionResponseDto> findAll(Pageable pageable);
}
