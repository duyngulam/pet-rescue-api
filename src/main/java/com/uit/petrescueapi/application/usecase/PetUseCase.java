package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.pet.CreatePetRequestDto;
import com.uit.petrescueapi.application.dto.pet.UpdatePetRequestDto;
import com.uit.petrescueapi.application.port.in.PetInputPort;
import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.service.PetDomainService;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Application use-case that implements the driving port.
 *
 * Translates commands into domain calls and delegates business rules
 * to {@link PetDomainService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PetUseCase implements PetInputPort {

    private final PetDomainService domainService;

    @Override
    public Pet create(CreatePetRequestDto cmd) {
        log.debug("Use-case: create pet '{}'", cmd.getName());
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
        log.debug("Use-case: update pet {}", id);
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
        domainService.delete(id);
    }

    @Override
    public Pet findById(UUID id) {
        return domainService.findById(id);
    }

    @Override
    public List<Pet> findAll() {
        return domainService.findAll();
    }

    @Override
    public Page<Pet> findAll(Pageable pageable) {
        return domainService.findAll(pageable);
    }

    @Override
    public List<Pet> findAvailable() {
        return domainService.findAvailable();
    }

    @Override
    public Page<Pet> findAvailable(Pageable pageable) {
        return domainService.findAvailable(pageable);
    }

    @Override
    public Pet changeStatus(UUID id, PetStatus newStatus) {
        return domainService.changeStatus(id, newStatus);
    }
}
