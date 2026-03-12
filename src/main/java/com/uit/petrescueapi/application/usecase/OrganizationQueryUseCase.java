package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.OrganizationQueryDataPort;
import com.uit.petrescueapi.application.port.query.OrganizationQueryPort;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Query (read) use-case for Organization operations.
 *
 * <p>Thin orchestrator — delegates directly to {@link OrganizationQueryDataPort}
 * (infrastructure query adapter). Queries bypass the domain layer entirely (CQRS).</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationQueryUseCase implements OrganizationQueryPort {

    private final OrganizationQueryDataPort queryDataPort;

    @Override
    public OrganizationResponseDto findById(UUID organizationId) {
        return queryDataPort.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", organizationId));
    }

    @Override
    public Page<OrganizationSummaryResponseDto> findAll(Pageable pageable) {
        return queryDataPort.findAllSummary(pageable);
    }

    @Override
    public Page<OrganizationMemberResponseDto> findMembers(UUID organizationId, Pageable pageable) {
        return queryDataPort.findMembers(organizationId, pageable);
    }
}
