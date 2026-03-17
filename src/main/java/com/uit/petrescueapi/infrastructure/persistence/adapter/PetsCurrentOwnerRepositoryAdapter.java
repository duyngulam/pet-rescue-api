package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.PetCurrentOwner;
import com.uit.petrescueapi.domain.repository.PetCurrentOwnerRepository;
import com.uit.petrescueapi.infrastructure.persistence.mapper.PetsCurrentOwnerEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.PetsCurrentOwnerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PetsCurrentOwnerRepositoryAdapter implements PetCurrentOwnerRepository {

    private final PetsCurrentOwnerJpaRepository jpa;
    private final PetsCurrentOwnerEntityMapper mapper;

    @Override
    public PetCurrentOwner save(PetCurrentOwner owner) {
        return mapper.toDomain(jpa.save(mapper.toEntity(owner)));
    }

    @Override
    public Optional<PetCurrentOwner> findByPetId(UUID petId) {
        // The @Id of PetsCurrentOwnerJpaEntity is petId, so findById works
        return jpa.findById(petId).map(mapper::toDomain);
    }

    @Override
    public PetCurrentOwner upsert(PetCurrentOwner owner) {
        // JPA save performs insert-or-update when ID is provided
        return mapper.toDomain(jpa.save(mapper.toEntity(owner)));
    }
}
