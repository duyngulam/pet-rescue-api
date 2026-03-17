package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.PetMedia;

import java.util.List;
import java.util.UUID;

/**
 * Domain repository contract for the PetMedia entity.
 */
public interface PetMediaRepository {

    PetMedia save(PetMedia petMedia);

    List<PetMedia> findByPetId(UUID petId);

    void delete(UUID mediaId);
}
