package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.repository.PetRepository;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Domain service that encapsulates Pet business rules.
 *
 * Key rules implemented here:
 *  • New pets always start with status AVAILABLE.
 *  • Status transitions are validated against an explicit allow-list.
 *  • Deletion is soft-delete (sets `deleted = true`).
 *
 * {@code @Transactional} is placed at the <strong>service layer only</strong>.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PetDomainService {

    private final PetRepository petRepository;

    // ── Status-transition matrix ───────────────────
    private static final Map<PetStatus, Set<PetStatus>> ALLOWED_TRANSITIONS = Map.of(
            PetStatus.AVAILABLE,   Set.of(PetStatus.PENDING, PetStatus.FOSTERED, PetStatus.UNAVAILABLE),
            PetStatus.PENDING,     Set.of(PetStatus.ADOPTED, PetStatus.AVAILABLE),
            PetStatus.ADOPTED,     Set.of(PetStatus.AVAILABLE),
            PetStatus.FOSTERED,    Set.of(PetStatus.AVAILABLE, PetStatus.PENDING, PetStatus.ADOPTED),
            PetStatus.UNAVAILABLE, Set.of(PetStatus.AVAILABLE, PetStatus.FOSTERED)
    );

    // ── Queries ─────────────────────────────────────

    @Transactional(readOnly = true)
    public Pet findById(UUID id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Pet> findAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Pet> findAvailable() {
        return petRepository.findByStatus(PetStatus.AVAILABLE);
    }

    @Transactional(readOnly = true)
    public Page<Pet> findAvailable(Pageable pageable) {
        return petRepository.findByStatus(PetStatus.AVAILABLE, pageable);
    }

    // ── Commands ────────────────────────────────────

    public Pet create(Pet pet) {
        log.info("Creating pet: {}", pet.getName());
        pet.setId(UUID.randomUUID());
        pet.setStatus(PetStatus.AVAILABLE);
        pet.setCreatedAt(LocalDateTime.now());
        return petRepository.save(pet);
    }

    public Pet update(UUID id, Pet updated) {
        Pet existing = findById(id);
        applyUpdates(existing, updated);
        existing.setUpdatedAt(LocalDateTime.now());
        return petRepository.save(existing);
    }

    public void delete(UUID id) {
        Pet pet = findById(id);
        pet.setDeleted(true);
        pet.setDeletedAt(LocalDateTime.now());
        petRepository.save(pet);
        log.info("Soft-deleted pet {}", id);
    }

    public Pet changeStatus(UUID id, PetStatus newStatus) {
        Pet pet = findById(id);
        PetStatus current = pet.getStatus();

        Set<PetStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition pet status from %s to %s", current, newStatus));
        }

        pet.setStatus(newStatus);
        pet.setUpdatedAt(LocalDateTime.now());
        return petRepository.save(pet);
    }

    // ── Private helpers ─────────────────────────────

    private void applyUpdates(Pet target, Pet source) {
        if (source.getName() != null)        target.setName(source.getName());
        if (source.getSpecies() != null)     target.setSpecies(source.getSpecies());
        if (source.getBreed() != null)       target.setBreed(source.getBreed());
        if (source.getAge() != null)         target.setAge(source.getAge());
        if (source.getGender() != null)      target.setGender(source.getGender());
        if (source.getColor() != null)       target.setColor(source.getColor());
        if (source.getWeight() != null)      target.setWeight(source.getWeight());
        if (source.getDescription() != null) target.setDescription(source.getDescription());
        if (source.getStatus() != null)      target.setStatus(source.getStatus());
        if (source.getHealthStatus() != null)target.setHealthStatus(source.getHealthStatus());
        target.setVaccinated(source.isVaccinated());
        target.setNeutered(source.isNeutered());
        if (source.getImageUrls() != null)   target.setImageUrls(source.getImageUrls());
    }
}
