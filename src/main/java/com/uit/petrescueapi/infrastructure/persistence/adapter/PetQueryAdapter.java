package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.organization.OrganizationMinimalDto;
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
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetQueryAdapter implements PetQueryDataPort {

    private final PetQueryJpaRepository queryRepo;
    private final CloudStoragePort cloudStoragePort;

    // ── List (summary) queries with filters ──────────────────

    @Override
    public Page<PetSummaryResponseDto> findAllSummaries(Pageable pageable) {
        return findAllWithFilters(null, null, null, pageable);
    }

    @Override
    public Page<PetSummaryResponseDto> findAllWithFilters(String species, String breed, String gender, Pageable pageable) {
        return queryRepo.findAllWithFilters(species, breed, gender, pageable).map(this::toSummaryDto);
    }

    @Override
    public Page<PetSummaryResponseDto> findAvailableSummaries(Pageable pageable) {
        return findAvailableWithFilters(null, null, null, pageable);
    }

    @Override
    public Page<PetSummaryResponseDto> findAvailableWithFilters(String species, String breed, String gender, Pageable pageable) {
        return queryRepo.findAvailableWithFilters(species, breed, gender, pageable).map(this::toSummaryDto);
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

        List<String> imageUrls = queryRepo.findImagePublicIdsById(id).stream()
                .filter(publicId -> publicId != null)
                .map(cloudStoragePort::buildUrl)
                .toList();

        return toResponseDto(proj, imageUrls);
    }

    // ── Projection → DTO mappers ────────────────

    private PetSummaryResponseDto toSummaryDto(PetSummaryProjection p) {
        String imageUrl = p.getImagePublicId() != null 
                ? cloudStoragePort.buildUrl(p.getImagePublicId()) 
                : null;
        
        return PetSummaryResponseDto.builder()
                .petId(p.getId())
                .name(p.getName())
                .species(p.getSpecies())
                .breed(p.getBreed())
                .age(p.getAge())
                .ageDisplay(formatAge(p.getAge()))
                .vaccinated(p.getVaccinated())
                .gender(p.getGender())
                .status(p.getStatus())
                .healthStatus(p.getHealthStatus())
                .imageUrl(imageUrl)
                .organization(p.getOrganizationId() != null ? OrganizationMinimalDto.builder()
                        .organizationId(p.getOrganizationId())
                        .name(p.getOrganizationName())
                        .build() : null)
                .province(p.getProvinceName())
                .provinceCode(p.getProvinceCode())
                .ward(p.getWardName())
                .wardCode(p.getWardCode())
                .build();
    }

    private PetResponseDto toResponseDto(PetDetailProjection p, List<String> imageUrls) {
        return PetResponseDto.builder()
                .petId(p.getId())
                .name(p.getName())
                .species(p.getSpecies())
                .breed(p.getBreed())
                .age(p.getAge())
                .ageDisplay(formatAge(p.getAge()))
                .vaccinated(p.getVaccinated())
                .gender(p.getGender())
                .status(p.getStatus())
                .healthStatus(p.getHealthStatus())
                .organization(p.getOrganizationId() != null ? OrganizationMinimalDto.builder()
                        .organizationId(p.getOrganizationId())
                        .name(p.getOrganizationName())
                        .build() : null)
                .province(p.getProvinceName())
                .provinceCode(p.getProvinceCode())
                .ward(p.getWardName())
                .wardCode(p.getWardCode())
                .color(p.getColor())
                .weight(p.getWeight())
                .description(p.getDescription())
                .neutered(p.getNeutered())
                .rescueDate(p.getRescueDate())
                .rescueLocation(p.getRescueLocation())
                .imageUrls(imageUrls)
                .shelterId(p.getShelterId())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private String formatAge(Integer ageInMonths) {
        if (ageInMonths == null) return null;
        if (ageInMonths < 12) {
            return ageInMonths + " month" + (ageInMonths != 1 ? "s" : "");
        }
        int years = ageInMonths / 12;
        int months = ageInMonths % 12;
        if (months == 0) {
            return years + " year" + (years != 1 ? "s" : "");
        }
        return years + " year" + (years != 1 ? "s" : "") + " " + months + " month" + (months != 1 ? "s" : "");
    }
}
