package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetLocationResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetMedicalRecordResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetOwnershipResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for Pet sub-resource operations.
 * Handles medical records, locations, ownerships and diary queries.
 */
public interface PetDetailsQueryPort {

    Page<PetMedicalRecordResponseDto> findMedicalRecords(UUID petId, Pageable pageable);

    Page<PetLocationResponseDto> findLocations(UUID petId, Pageable pageable);

    Page<PetOwnershipResponseDto> findOwnerships(UUID petId, Pageable pageable);

    Page<MediaFileResponseDto> findDiaryMedia(UUID petId, Pageable pageable);
}
