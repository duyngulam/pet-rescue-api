package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.application.dto.pet.CreateLocationRequestDto;
import com.uit.petrescueapi.application.dto.pet.CreateMedicalRecordRequestDto;
import com.uit.petrescueapi.application.dto.pet.AddDiaryMediaRequestDto;
import com.uit.petrescueapi.application.dto.pet.PetLocationResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetMedicalRecordResponseDto;

import java.util.UUID;

/**
 * Command (write) port for Pet sub-resource operations.
 * Handles medical records, locations and diary entries.
 */
public interface PetDetailsCommandPort {

    PetMedicalRecordResponseDto addMedicalRecord(UUID petId, CreateMedicalRecordRequestDto cmd);

    PetLocationResponseDto addLocation(UUID petId, CreateLocationRequestDto cmd);

    MediaFileResponseDto addDiaryEntry(UUID petId, AddDiaryMediaRequestDto cmd);
}
