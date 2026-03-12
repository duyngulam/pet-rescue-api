package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.organization.AddMemberRequestDto;
import com.uit.petrescueapi.application.dto.organization.CreateOrganizationRequestDto;
import com.uit.petrescueapi.application.port.command.OrganizationCommandPort;
import com.uit.petrescueapi.domain.entity.Organization;
import com.uit.petrescueapi.domain.entity.OrganizationMember;
import com.uit.petrescueapi.domain.service.OrganizationDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Command (write) use-case for Organization operations.
 * Translates request DTOs into domain calls and delegates business rules
 * to {@link OrganizationDomainService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationCommandUseCase implements OrganizationCommandPort {

    private final OrganizationDomainService domainService;

    @Override
    public Organization create(CreateOrganizationRequestDto cmd) {
        log.debug("Command: create organization '{}'", cmd.getName());
        Organization org = Organization.builder()
                .name(cmd.getName())
                .type(cmd.getType())
                .street_address(cmd.getStreet_address())
                .ward_code(cmd.getWard_code())
                .ward_name(cmd.getWard_name())
                .province_code(cmd.getProvince_code())
                .province_name(cmd.getProvince_name())
                .phone(cmd.getPhone())
                .email(cmd.getEmail())
                .latitude(cmd.getLatitude())
                .longitude(cmd.getLongitude())
                .build();
        return domainService.create(org);
    }

    @Override
    public Organization update(UUID id, CreateOrganizationRequestDto cmd) {
        log.debug("Command: update organization {}", id);
        Organization patch = Organization.builder()
                .name(cmd.getName())
                .type(cmd.getType())
                .street_address(cmd.getStreet_address())
                .phone(cmd.getPhone())
                .email(cmd.getEmail())
                .latitude(cmd.getLatitude())
                .longitude(cmd.getLongitude())
                .build();
        return domainService.update(id, patch);
    }

    @Override
    public void deactivate(UUID id) {
        log.debug("Command: deactivate organization {}", id);
        domainService.deactivate(id);
    }

    @Override
    public OrganizationMember addMember(UUID organizationId, AddMemberRequestDto cmd) {
        log.debug("Command: add member {} to organization {}", cmd.getUserId(), organizationId);
        return domainService.addMember(organizationId, cmd.getUserId(), cmd.getRole());
    }

    @Override
    public void removeMember(UUID organizationId, UUID userId) {
        log.debug("Command: remove member {} from organization {}", userId, organizationId);
        domainService.removeMember(organizationId, userId);
    }
}
