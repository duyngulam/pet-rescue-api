package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetMedicalRecordResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetOwnershipResponseDto;
import com.uit.petrescueapi.application.port.out.PetDetailsQueryDataPort;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetMediaJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetMedicalRecordJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetsCurrentOwnerJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.repository.PetMediaJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.PetMedicalRecordJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.PetsCurrentOwnerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Query-side adapter (CQRS read path) for Pet sub-resource details.
 *
 * <p>Handles medical records, ownership history, and diary media
 * for a specific pet.</p>
 */
@Component
@RequiredArgsConstructor
public class PetDetailsQueryAdapter implements PetDetailsQueryDataPort {

    private final PetMedicalRecordJpaRepository petMedicalRecordJpaRepo;
    private final PetsCurrentOwnerJpaRepository petsCurrentOwnerJpaRepo;
    private final PetMediaJpaRepository petMediaJpaRepo;

    // ── Medical records ─────────────────────────

    @Override
    public Page<PetMedicalRecordResponseDto> findMedicalRecords(UUID petId, Pageable pageable) {
        return petMedicalRecordJpaRepo.findByPetIdAndDeletedFalse(petId, pageable)
                .map(this::toMedicalRecordDto);
    }

    // ── Ownerships ──────────────────────────────

    @Override
    public Page<PetOwnershipResponseDto> findOwnerships(UUID petId, Pageable pageable) {
        // PetsCurrentOwnerJpaEntity uses petId as @Id — single row per pet
        Optional<PetsCurrentOwnerJpaEntity> opt = petsCurrentOwnerJpaRepo.findById(petId);
        List<PetOwnershipResponseDto> list = opt
                .map(e -> List.of(toOwnershipDto(e)))
                .orElse(List.of());
        return new PageImpl<>(list, pageable, list.size());
    }

    // ── Diary media ─────────────────────────────

    @Override
    public Page<MediaFileResponseDto> findDiaryMedia(UUID petId, Pageable pageable) {
        List<PetMediaJpaEntity> all = petMediaJpaRepo.findAllByPetId(petId);
        List<MediaFileResponseDto> dtos = all.stream().map(this::toMediaDto).toList();

        // Manual pagination over the full list
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), dtos.size());
        List<MediaFileResponseDto> pageContent = (start < dtos.size())
                ? dtos.subList(start, end)
                : List.of();
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    // ── Entity → DTO mappers ────────────────────

    private PetMedicalRecordResponseDto toMedicalRecordDto(PetMedicalRecordJpaEntity e) {
        return PetMedicalRecordResponseDto.builder()
                .recordId(e.getRecordId())
                .petId(e.getPetId())
                .description(e.getDescription())
                .vaccine(e.getVaccine())
                .diagnosis(e.getDiagnosis())
                .recordDate(e.getRecordDate())
                .createdBy(e.getCreatedBy())
                .build();
    }

    private PetOwnershipResponseDto toOwnershipDto(PetsCurrentOwnerJpaEntity e) {
        return PetOwnershipResponseDto.builder()
                .petId(e.getPetId())
                .ownerType(e.getOwnerType())
                .ownerId(e.getOwnerId())
                .build();
    }

    private MediaFileResponseDto toMediaDto(PetMediaJpaEntity e) {
        return MediaFileResponseDto.builder()
                .mediaId(e.getMediaId())
                .url(e.getUrl())
                .type(e.getType())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
