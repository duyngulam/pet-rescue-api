package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.pet.CreatePetRequestDto;
import com.uit.petrescueapi.application.dto.pet.UpdatePetRequestDto;
import com.uit.petrescueapi.application.port.in.command.PetCommandPort;
import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.service.PetDomainService;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Command (write) use-case for Pet operations.
 * Translates request DTOs into domain calls and delegates business rules
 * to {@link PetDomainService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PetCommandUseCase implements PetCommandPort {

    private final PetDomainService domainService;

    @Override
    public Pet create(CreatePetRequestDto cmd) {
        log.debug("Command: create pet '{}'", cmd.getName());
        Pet pet = Pet.builder()
                .name(cmd.getName())
                .species(cmd.getSpecies())
                .breed(cmd.getBreed())
                .age(cmd.getAge())
                .gender(cmd.getGender())
                .color(cmd.getColor())
                .weight(cmd.getWeight())
                .description(cmd.getDescription())
                .vaccinated(cmd.isVaccinated())
                .neutered(cmd.isNeutered())
                .rescueDate(cmd.getRescueDate())
                .rescueLocation(cmd.getRescueLocation())
                .imageUrls(cmd.getImageUrls())
                .shelterId(cmd.getShelterId())
                .build();
        return domainService.create(pet);
    }

    @Override
    public Pet update(UUID id, UpdatePetRequestDto cmd) {
        log.debug("Command: update pet {}", id);
        Pet patch = Pet.builder()
                .name(cmd.getName())
                .species(cmd.getSpecies())
                .breed(cmd.getBreed())
                .age(cmd.getAge())
                .gender(cmd.getGender())
                .color(cmd.getColor())
                .weight(cmd.getWeight())
                .description(cmd.getDescription())
                .status(cmd.getStatus())
                .healthStatus(cmd.getHealthStatus())
                .vaccinated(cmd.isVaccinated())
                .neutered(cmd.isNeutered())
                .imageUrls(cmd.getImageUrls())
                .build();
        return domainService.update(id, patch);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Command: delete pet {}", id);
        domainService.delete(id);
    }

    @Override
    public Pet changeStatus(UUID id, PetStatus newStatus) {
        log.debug("Command: change status of pet {} to {}", id, newStatus);
        return domainService.changeStatus(id, newStatus);
    }
}
