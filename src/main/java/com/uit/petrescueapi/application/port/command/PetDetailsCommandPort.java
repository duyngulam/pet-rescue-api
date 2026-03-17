package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.pet.CreateMedicalRecordRequestDto;
import com.uit.petrescueapi.domain.entity.PetMedicalRecord;

import java.util.UUID;

public interface PetDetailsCommandPort {
    PetMedicalRecord addMedicalRecord(UUID petId, CreateMedicalRecordRequestDto cmd);
}
