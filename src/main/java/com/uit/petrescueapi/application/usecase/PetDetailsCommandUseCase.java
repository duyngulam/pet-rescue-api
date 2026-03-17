package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.pet.CreateMedicalRecordRequestDto;
import com.uit.petrescueapi.application.port.command.PetDetailsCommandPort;
import com.uit.petrescueapi.domain.entity.PetMedicalRecord;
import com.uit.petrescueapi.domain.repository.PetMedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Command (write) use-case for Pet sub-resource operations.
 * Handles medical records.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PetDetailsCommandUseCase implements PetDetailsCommandPort {

    private final PetMedicalRecordRepository medicalRecordRepository;

    @Override
    public PetMedicalRecord addMedicalRecord(UUID petId, CreateMedicalRecordRequestDto cmd) {
        log.debug("Command: add medical record for pet {}", petId);
        PetMedicalRecord record = PetMedicalRecord.builder()
                .recordId(UUID.randomUUID())
                .petId(petId)
                .description(cmd.getDescription())
                .vaccine(cmd.getVaccine())
                .diagnosis(cmd.getDiagnosis())
                .recordDate(cmd.getRecordDate() != null
                        ? LocalDate.parse(cmd.getRecordDate()).atStartOfDay()
                        : null)
                .createdAt(LocalDateTime.now())
                .build();
        return medicalRecordRepository.save(record);
    }
}
