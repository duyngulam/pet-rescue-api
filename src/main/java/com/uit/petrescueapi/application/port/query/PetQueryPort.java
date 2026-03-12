package com.uit.petrescueapi.application.port.query;

import com.uit.petrescueapi.application.dto.pet.PetResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for Pet operations.
 *
 * <p>Returns DTOs directly — consistent with all other query ports.
 * Organization data is always included via optimized JOIN queries.</p>
 */
public interface PetQueryPort {

    PetResponseDto findById(UUID id);

    Page<PetSummaryResponseDto> findAll(Pageable pageable);

    Page<PetSummaryResponseDto> findAvailable(Pageable pageable);

    Page<PetSummaryResponseDto> findByOrganizationId(UUID organizationId, Pageable pageable);
}
