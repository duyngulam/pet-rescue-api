package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.application.dto.pet.PetResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Output port for Pet read operations (CQRS query side).
 */
public interface PetQueryDataPort {

    PetResponseDto findById(UUID id);

    Page<PetSummaryResponseDto> findAllSummaries(Pageable pageable);

    Page<PetSummaryResponseDto> findAllWithFilters(String species, String breed, String gender, Pageable pageable);

    Page<PetSummaryResponseDto> findAvailableSummaries(Pageable pageable);

    Page<PetSummaryResponseDto> findAvailableWithFilters(String species, String breed, String gender, Pageable pageable);

    Page<PetSummaryResponseDto> findSummariesByOrganizationId(UUID organizationId, Pageable pageable);
}
