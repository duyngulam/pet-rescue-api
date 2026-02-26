package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Secondary (driven) port — the application tells the infrastructure what
 * persistence operations it needs; the infrastructure decides <em>how</em>.
 *
 * This is identical in shape to {@link com.uit.petrescueapi.domain.repository.PetRepository}
 * and delegates there — kept as a separate interface so the application layer
 * never directly depends on the domain repository interface.
 */
public interface PetOutputPort {

    Pet save(Pet pet);

    Optional<Pet> findById(UUID id);

    List<Pet> findAll();

    Page<Pet> findAll(Pageable pageable);

    List<Pet> findByStatus(PetStatus status);

    Page<Pet> findByStatus(PetStatus status, Pageable pageable);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
