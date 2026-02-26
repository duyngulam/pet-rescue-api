package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.pet.CreatePetRequestDto;
import com.uit.petrescueapi.application.dto.pet.UpdatePetRequestDto;
import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.valueobject.PetStatus;

import java.util.UUID;

/**
 * Command (write) port for Pet operations.
 * Handles all state-changing operations on the Pet aggregate.
 */
public interface PetCommandPort {

    Pet create(CreatePetRequestDto cmd);

    Pet update(UUID id, UpdatePetRequestDto cmd);

    void delete(UUID id);

    Pet changeStatus(UUID id, PetStatus newStatus);
}
