package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for Organization operations.
 * Handles organization lookups, listing and member queries.
 */
public interface OrganizationQueryPort {

    OrganizationResponseDto findById(UUID organizationId);

    Page<OrganizationResponseDto> findAll(Pageable pageable);

    Page<OrganizationMemberResponseDto> findMembers(UUID organizationId, Pageable pageable);
}
