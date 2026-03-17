package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.adoption.AdoptionResponseDto;
import com.uit.petrescueapi.application.dto.adoption.AdoptionSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.AdoptionQueryDataPort;
import com.uit.petrescueapi.application.port.query.AdoptionQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Query (read) use-case for Adoption operations.
 *
 * <p>Thin orchestrator: delegates directly to {@link AdoptionQueryDataPort}
 * (implemented by the infrastructure query adapter). No domain service
 * involvement -- queries bypass the domain layer entirely (CQRS).</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionQueryUseCase implements AdoptionQueryPort {

    private final AdoptionQueryDataPort queryDataPort;

    @Override
    public AdoptionResponseDto findById(UUID applicationId) {
        log.debug("Query: find adoption application by id {}", applicationId);
        return queryDataPort.findById(applicationId);
    }

    @Override
    public Page<AdoptionSummaryResponseDto> findAll(String status, Pageable pageable) {
        log.debug("Query: find all adoption applications (status={}, paginated)", status);
        return queryDataPort.findAllSummaries(status, pageable);
    }
}
