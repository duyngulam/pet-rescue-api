package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.PetMedicalRecord;
import com.uit.petrescueapi.domain.repository.PetMedicalRecordRepository;
import com.uit.petrescueapi.infrastructure.persistence.mapper.PetMedicalRecordEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.PetMedicalRecordJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PetMedicalRecordRepositoryAdapter implements PetMedicalRecordRepository {

    private final PetMedicalRecordJpaRepository jpa;
    private final PetMedicalRecordEntityMapper mapper;

    @Override
    public PetMedicalRecord save(PetMedicalRecord record) {
        return mapper.toDomain(jpa.save(mapper.toEntity(record)));
    }

    @Override
    public Optional<PetMedicalRecord> findById(UUID recordId) {
        return jpa.findById(recordId)
                .filter(e -> !e.isDeleted())
                .map(mapper::toDomain);
    }

    @Override
    public Page<PetMedicalRecord> findByPetId(UUID petId, Pageable pageable) {
        return jpa.findByPetIdAndDeletedFalse(petId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(UUID recordId) {
        jpa.findById(recordId).ifPresent(entity -> {
            entity.setDeleted(true);
            entity.setDeletedAt(LocalDateTime.now());
            jpa.save(entity);
        });
    }
}
