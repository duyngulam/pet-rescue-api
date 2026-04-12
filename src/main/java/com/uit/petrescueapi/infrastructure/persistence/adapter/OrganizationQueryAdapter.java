package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.OrganizationQueryDataPort;
import com.uit.petrescueapi.domain.valueobject.OrganizationStatus;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationMemberProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.OrganizationSummaryProjection;
import com.uit.petrescueapi.infrastructure.persistence.repository.OrganizationMemberJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.OrganizationQueryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationQueryAdapter implements OrganizationQueryDataPort {

    private final OrganizationQueryJpaRepository queryRepo;
    private final OrganizationMemberJpaRepository memberRepo;

    @Override
    public Page<OrganizationSummaryResponseDto> findAllSummary(OrganizationStatus status, Pageable pageable) {
        return queryRepo.findAllSummary(status, pageable).map(this::toSummaryDto);
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
                .status(p.getStatus())
                .streetAddress(p.getStreet_address())
                .wardName(p.getWard_name())
                .provinceName(p.getProvince_name())
                .phone(p.getPhone())
                .email(p.getEmail())
                .build();
    }

    private OrganizationResponseDto toDetailDto(OrganizationDetailProjection p) {
        return OrganizationResponseDto.builder()
                .organizationId(p.getOrganizationId())
                .name(p.getName())
                .description(p.getDescription())
                .type(p.getType())
                .streetAddress(p.getStreet_address())
                .wardName(p.getWard_name())
                .provinceName(p.getProvince_name())
                .phone(p.getPhone())
                .email(p.getEmail())
                .officialLink(p.getOfficial_link())
                .latitude(p.getLatitude())
                .longitude(p.getLongitude())
                .status(p.getStatus())
                .requestedByUserId(p.getRequested_by_user_id())
                .createdBy(p.getCreatedBy())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
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
