package com.uit.petrescueapi.application.port.in;

import com.uit.petrescueapi.application.dto.pet.CreatePetRequestDto;
import com.uit.petrescueapi.application.dto.pet.UpdatePetRequestDto;
import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Primary (driving) port — defines what the outside world can ask the application to do.
 */
public interface PetInputPort {

    Pet create(CreatePetRequestDto cmd);

    Pet update(UUID id, UpdatePetRequestDto cmd);

    void delete(UUID id);

    Pet findById(UUID id);

    List<Pet> findAll();

    Page<Pet> findAll(Pageable pageable);

    List<Pet> findAvailable();

    Page<Pet> findAvailable(Pageable pageable);

    Pet changeStatus(UUID id, PetStatus newStatus);
}
