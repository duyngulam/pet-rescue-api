package com.uit.petrescueapi.infrastructure.persistence;

import com.uit.petrescueapi.application.port.out.PetOutputPort;
import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.repository.PetRepository;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that wires the domain repository + application output port
 * to the real JPA repository.
 *
 * ⚠️ No {@code @Transactional} here — transactions are managed at the service layer.
 */
@Component
@RequiredArgsConstructor
public class PetRepositoryAdapter implements PetRepository, PetOutputPort {

    private final PetJpaRepository jpa;
    private final PetEntityMapper mapper;

    @Override
    public Pet save(Pet pet) {
        PetJpaEntity entity = mapper.toEntity(pet);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Pet> findById(UUID id) {
        return jpa.findByIdAndNotDeleted(id).map(mapper::toDomain);
    }

    @Override
    public List<Pet> findAll() {
        return mapper.toDomainList(jpa.findAllNotDeleted());
    }

    @Override
    public Page<Pet> findAll(Pageable pageable) {
        return jpa.findAllNotDeleted(pageable).map(mapper::toDomain);
    }

    @Override
    public List<Pet> findByStatus(PetStatus status) {
        return mapper.toDomainList(jpa.findByStatus(status));
    }

    @Override
    public Page<Pet> findByStatus(PetStatus status, Pageable pageable) {
        return jpa.findByStatus(status, pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }
}
