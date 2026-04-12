package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationSummaryResponseDto;
import com.uit.petrescueapi.domain.valueobject.OrganizationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for the Organization CQRS read-side adapter.
 * Implemented by the infrastructure query adapter.
 */
public interface OrganizationQueryDataPort {
    Page<OrganizationSummaryResponseDto> findAllSummary(OrganizationStatus status, Pageable pageable);
    Optional<OrganizationResponseDto> findById(UUID id);
    Page<OrganizationMemberResponseDto> findMembers(UUID organizationId, Pageable pageable);
}
