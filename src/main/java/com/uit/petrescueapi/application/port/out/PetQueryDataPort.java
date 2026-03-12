package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.application.dto.pet.PetResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Output port for Pet read operations (CQRS query side).
 *
 * <p>The infrastructure layer implements this to execute optimized
 * JOIN queries and map projections directly to application DTOs.</p>
 *
 * <p><b>Adding a new query endpoint</b> only requires:
 * <ol>
 *   <li>A new method here</li>
 *   <li>A new projection interface in infrastructure</li>
 *   <li>A new query + mapping in the adapter</li>
 * </ol></p>
 */
public interface PetQueryDataPort {

    PetResponseDto findById(UUID id);

    Page<PetSummaryResponseDto> findAllSummaries(Pageable pageable);

    Page<PetSummaryResponseDto> findAvailableSummaries(Pageable pageable);

    Page<PetSummaryResponseDto> findSummariesByOrganizationId(UUID organizationId, Pageable pageable);
}
