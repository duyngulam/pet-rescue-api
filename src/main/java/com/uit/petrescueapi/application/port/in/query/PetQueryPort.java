package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.domain.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Query (read) port for Pet operations.
 * Handles all read-only operations on the Pet aggregate.
 */
public interface PetQueryPort {

    Pet findById(UUID id);

    List<Pet> findAll();

    Page<Pet> findAll(Pageable pageable);

    List<Pet> findAvailable();

    Page<Pet> findAvailable(Pageable pageable);
}
