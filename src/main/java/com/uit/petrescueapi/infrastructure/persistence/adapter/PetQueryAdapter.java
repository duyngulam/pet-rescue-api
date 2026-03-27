package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.organization.OrganizationSummaryResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.CloudStoragePort;
import com.uit.petrescueapi.application.port.out.PetQueryDataPort;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.infrastructure.persistence.projection.PetDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.PetSummaryProjection;
import com.uit.petrescueapi.infrastructure.persistence.repository.PetQueryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Query-side adapter (CQRS read path).
 *
 * <p>Executes optimized JOIN queries via {@link PetQueryJpaRepository},
 * maps infrastructure projections → application DTOs.</p>
 *
 * <p><b>To add a new query endpoint:</b> add a method here that
 * calls the JPA repository and maps the projection to a DTO.</p>
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetQueryAdapter implements PetQueryDataPort {

    private final PetQueryJpaRepository queryRepo;
    private final CloudStoragePort cloudStoragePort;

    // ── List (summary) queries ──────────────────

    @Override
    public Page<PetSummaryResponseDto> findAllSummaries(Pageable pageable) {
        return queryRepo.findAllSummaries(pageable).map(this::toSummaryDto);
    }

    @Override
    public Page<PetSummaryResponseDto> findAvailableSummaries(Pageable pageable) {
        return queryRepo.findAvailableSummaries(pageable).map(this::toSummaryDto);
    }

    @Override
    public Page<PetSummaryResponseDto> findSummariesByOrganizationId(UUID organizationId, Pageable pageable) {
        return queryRepo.findSummariesByOrganizationId(organizationId, pageable).map(this::toSummaryDto);
    }

    // ── Detail (single pet) query ───────────────

    @Override
    public PetResponseDto findById(UUID id) {
        PetDetailProjection proj = queryRepo.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet", "id", id));

        // Build URLs from public_ids (single source of truth)
        List<String> imageUrls = queryRepo.findImagePublicIdsById(id).stream()
                .filter(publicId -> publicId != null)
                .map(cloudStoragePort::buildUrl)
                .toList();

        return toResponseDto(proj, imageUrls);
    }

    // ── Projection → DTO mappers ────────────────

    private PetSummaryResponseDto toSummaryDto(PetSummaryProjection p) {
        PetSummaryResponseDto dto = PetSummaryResponseDto.builder()
                .id(p.getId())
                .name(p.getName())
                .species(p.getSpecies())
                .breed(p.getBreed())
                .age(p.getAge())
                .vaccinated(p.getVaccinated())
                .gender(p.getGender())
                .status(p.getStatus())
                .healthStatus(p.getHealthStatus())
                .organizationId(p.getOrganizationId())
                .build();

        if (p.getOrganizationId() != null) {
            dto.setOrganization(toOrgSummary(
                    p.getOrganizationId(),
                    p.getOrganizationName(),
                    p.getOrganizationType(),
                    p.getOrganizationStatus()));
        }

        return dto;
    }

    private PetResponseDto toResponseDto(PetDetailProjection p, List<String> imageUrls) {
        PetResponseDto dto = PetResponseDto.builder()
                .petId(p.getId())
                .name(p.getName())
                .species(p.getSpecies())
                .breed(p.getBreed())
                .age(p.getAge())
                .gender(p.getGender())
                .color(p.getColor())
                .weight(p.getWeight())
                .description(p.getDescription())
                .status(p.getStatus())
                .healthStatus(p.getHealthStatus())
                .vaccinated(p.getVaccinated())
                .neutered(p.getNeutered())
                .rescueDate(p.getRescueDate())
                .rescueLocation(p.getRescueLocation())
                .imageUrls(imageUrls)
                .shelterId(p.getShelterId())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();

        if (p.getOrganizationId() != null) {
            dto.setOrganization(toOrgSummary(
                    p.getOrganizationId(),
                    p.getOrganizationName(),
                    p.getOrganizationType(),
                    p.getOrganizationStatus()));
        }

        return dto;
    }

    private OrganizationSummaryResponseDto toOrgSummary(UUID id, String name, String type, String status) {
        return OrganizationSummaryResponseDto.builder()
                .organizationId(id)
                .name(name)
                .type(type)
                .status(status)
                .build();
    }
}
