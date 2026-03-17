package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.PetMedia;
import com.uit.petrescueapi.domain.repository.PetMediaRepository;
import com.uit.petrescueapi.infrastructure.persistence.mapper.PetMediaEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.PetMediaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PetMediaRepositoryAdapter implements PetMediaRepository {

    private final PetMediaJpaRepository jpa;
    private final PetMediaEntityMapper mapper;

    @Override
    public PetMedia save(PetMedia petMedia) {
        return mapper.toDomain(jpa.save(mapper.toEntity(petMedia)));
    }

    @Override
    public List<PetMedia> findByPetId(UUID petId) {
        return mapper.toDomainList(jpa.findAllByPetId(petId));
    }

    @Override
    public void delete(UUID mediaId) {
        jpa.deleteById(mediaId);
    }
}
