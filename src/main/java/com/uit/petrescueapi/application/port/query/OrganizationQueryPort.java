package com.uit.petrescueapi.application.port.query;

import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for Organization operations.
 * list → OrganizationSummaryResponseDto (lightweight, CQRS JOIN projection)
 * detail → OrganizationResponseDto
 * members → OrganizationMemberResponseDto (JOIN with users table)
 */
public interface OrganizationQueryPort {

    OrganizationResponseDto findById(UUID organizationId);

    Page<OrganizationSummaryResponseDto> findAll(Pageable pageable);

    Page<OrganizationMemberResponseDto> findMembers(UUID organizationId, Pageable pageable);
}
