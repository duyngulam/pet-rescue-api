package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.OrganizationQueryDataPort;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationMemberProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationSummaryProjection;
import com.uit.petrescueapi.infrastructure.persistence.repository.OrganizationMemberJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.OrganizationQueryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrganizationQueryAdapter implements OrganizationQueryDataPort {

    private final OrganizationQueryJpaRepository queryRepo;
    private final OrganizationMemberJpaRepository memberRepo;

    @Override
    public Page<OrganizationSummaryResponseDto> findAllSummary(Pageable pageable) {
        return queryRepo.findAllSummary(pageable).map(this::toSummaryDto);
    }

    @Override
    public Optional<OrganizationResponseDto> findById(UUID id) {
        return queryRepo.findDetailById(id).map(this::toDetailDto);
    }

    @Override
    public Page<OrganizationMemberResponseDto> findMembers(UUID organizationId, Pageable pageable) {
        return memberRepo.findByOrganizationId(organizationId, pageable).map(this::toMemberDto);
    }

    // ── Private mapping helpers ──────────────────────────

    private OrganizationSummaryResponseDto toSummaryDto(OrganizationSummaryProjection p) {
        return OrganizationSummaryResponseDto.builder()
                .organizationId(p.getOrganizationId())
                .name(p.getName())
                .type(p.getType())
                .streetAddress(p.getStreet_address())
                .wardCode(p.getWard_code())
                .ward(p.getWard())
                .provinceCode(p.getProvince_code())
                .province(p.getProvince())
                .phone(p.getPhone())
                .email(p.getEmail())
                .build();
    }

    private OrganizationResponseDto toDetailDto(OrganizationDetailProjection p) {
        String address = p.getStreet_address();
        return OrganizationResponseDto.builder()
                .organizationId(p.getOrganizationId())
                .name(p.getName())
                .type(p.getType())
                .address(address)
                .phone(p.getPhone())
                .email(p.getEmail())
                .latitude(p.getLatitude())
                .longitude(p.getLongitude())
                .status(p.getStatus())
                .createdBy(p.getCreatedBy())
                .createdAt(p.getCreatedAt())
                .build();
    }

    private OrganizationMemberResponseDto toMemberDto(OrganizationMemberProjection p) {
        return OrganizationMemberResponseDto.builder()
                .organizationId(p.getOrganizationId())
                .userId(p.getUserId())
                .username(p.getUsername())
                .role(p.getRole())
                .status(p.getStatus())
                .joinedAt(p.getJoinedAt())
                .build();
    }
}
