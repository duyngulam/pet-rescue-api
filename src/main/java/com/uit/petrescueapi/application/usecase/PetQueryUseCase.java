package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.pet.PetResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.PetQueryDataPort;
import com.uit.petrescueapi.application.port.query.PetQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Query (read) use-case for Pet operations.
 *
 * <p>Thin orchestrator: delegates directly to {@link PetQueryDataPort}
 * (implemented by the infrastructure query adapter). No domain service
 * involvement — queries bypass the domain layer entirely (CQRS).</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PetQueryUseCase implements PetQueryPort {

    private final PetQueryDataPort queryDataPort;

    @Override
    public PetResponseDto findById(UUID id) {
        log.debug("Query: find pet by id {}", id);
        return queryDataPort.findById(id);
    }

    @Override
    public Page<PetSummaryResponseDto> findAll(Pageable pageable) {
        log.debug("Query: find all pets (paginated)");

        return queryDataPort.findAllSummaries(pageable);
    }

    @Override
    public Page<PetSummaryResponseDto> findAvailable(Pageable pageable) {
        log.debug("Query: find available pets (paginated)");
        return queryDataPort.findAvailableSummaries(pageable);
    }

    @Override
    public Page<PetSummaryResponseDto> findByOrganizationId(UUID organizationId, Pageable pageable) {
        log.debug("Query: find pets by organization id {} (paginated)", organizationId);
        return queryDataPort.findSummariesByOrganizationId(organizationId, pageable);
    }
}
