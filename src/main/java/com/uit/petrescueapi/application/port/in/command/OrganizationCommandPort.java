package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.organization.AddMemberRequestDto;
import com.uit.petrescueapi.application.dto.organization.CreateOrganizationRequestDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;

import java.util.UUID;

/**
 * Command (write) port for Organization operations.
 * Handles organization CRUD and member management.
 */
public interface OrganizationCommandPort {

    OrganizationResponseDto create(CreateOrganizationRequestDto cmd);

    OrganizationResponseDto update(UUID id, CreateOrganizationRequestDto cmd);

    void deactivate(UUID id);

    OrganizationMemberResponseDto addMember(UUID organizationId, AddMemberRequestDto cmd);

    void removeMember(UUID organizationId, UUID userId);
}
