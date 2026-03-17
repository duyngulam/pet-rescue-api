package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.PetCurrentOwner;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository contract for the PetCurrentOwner entity.
 */
public interface PetCurrentOwnerRepository {

    PetCurrentOwner save(PetCurrentOwner owner);

    Optional<PetCurrentOwner> findByPetId(UUID petId);

    PetCurrentOwner upsert(PetCurrentOwner owner);
}
