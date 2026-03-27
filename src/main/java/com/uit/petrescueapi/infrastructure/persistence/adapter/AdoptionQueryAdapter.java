package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.adoption.AdoptionResponseDto;
import com.uit.petrescueapi.application.dto.adoption.AdoptionSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.AdoptionQueryDataPort;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.infrastructure.persistence.projection.AdoptionDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.AdoptionSummaryProjection;
import com.uit.petrescueapi.infrastructure.persistence.repository.AdoptionQueryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Query-side adapter (CQRS read path) for AdoptionApplication.
 *
 * <p>Executes optimized JOIN queries via {@link AdoptionQueryJpaRepository},
 * maps infrastructure projections to application DTOs.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdoptionQueryAdapter implements AdoptionQueryDataPort {

    private final AdoptionQueryJpaRepository queryRepo;

    // ── List (summary) queries ──────────────────

    @Override
    public Page<AdoptionSummaryResponseDto> findAllSummaries(String status, Pageable pageable) {
        return queryRepo.findAllSummaries(status, pageable).map(this::toSummaryDto);
    }

    // ── Detail (single adoption application) query ──

    @Override
    public AdoptionResponseDto findById(UUID applicationId) {
        AdoptionDetailProjection proj = queryRepo.findDetailById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("AdoptionApplication", "applicationId", applicationId));
        return toResponseDto(proj);
    }

    // ── Projection → DTO mappers ────────────────

    private AdoptionSummaryResponseDto toSummaryDto(AdoptionSummaryProjection p) {
        return AdoptionSummaryResponseDto.builder()
                .applicationId(p.getApplicationId())
                .petName(p.getPetName())
                .applicantUsername(p.getApplicantUsername())
                .status(p.getStatus())
                .experience(p.getExperience())
                .liveCondition(p.getLiveCondition())
                .createdAt(p.getCreatedAt())
                .build();
    }

    private AdoptionResponseDto toResponseDto(AdoptionDetailProjection p) {
        return AdoptionResponseDto.builder()
                .applicationId(p.getApplicationId())
                .petId(p.getPetId())
                .petName(p.getPetName())
                .applicantId(p.getApplicantId())
                .applicantUsername(p.getApplicantUsername())
                .organizationId(p.getOrganizationId())
                .status(p.getStatus())
                .experience(p.getExperience())
                .liveCondition(p.getLiveCondition())
                .createdAt(p.getCreatedAt())
                .decidedAt(p.getDecidedAt())
                .decidedBy(p.getDecidedBy())
                .build();
    }
}
