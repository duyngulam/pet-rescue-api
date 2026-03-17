package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.pet.*;
import com.uit.petrescueapi.application.port.command.PetDetailsCommandPort;
import com.uit.petrescueapi.application.port.query.PetDetailsQueryPort;
import com.uit.petrescueapi.domain.entity.PetMedicalRecord;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pets/{petId}")
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "Pet Details", description = "Pet sub-resource operations")
public class PetDetailsController {

    private final PetDetailsCommandPort commandPort;
    private final PetDetailsQueryPort queryPort;

    @PostMapping("/medical-records")
    @Operation(summary = "Add a medical record for a pet")
    public ResponseEntity<ApiResponse<PetMedicalRecordResponseDto>> addMedicalRecord(
            @PathVariable UUID petId,
            @Valid @RequestBody CreateMedicalRecordRequestDto cmd) {
        PetMedicalRecord record = commandPort.addMedicalRecord(petId, cmd);
        PetMedicalRecordResponseDto dto = PetMedicalRecordResponseDto.builder()
                .recordId(record.getRecordId())
                .petId(record.getPetId())
                .description(record.getDescription())
                .vaccine(record.getVaccine())
                .diagnosis(record.getDiagnosis())
                .recordDate(record.getRecordDate())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(dto));
    }

    @GetMapping("/medical-records")
    @Operation(summary = "List medical records for a pet")
    public ResponseEntity<ApiResponse<PageResponse<PetMedicalRecordResponseDto>>> getMedicalRecords(
            @PathVariable UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(queryPort.findMedicalRecords(petId, PageRequest.of(page, size)))));
    }

    @GetMapping("/ownerships")
    @Operation(summary = "List ownership history for a pet")
    public ResponseEntity<ApiResponse<PageResponse<PetOwnershipResponseDto>>> getOwnerships(
            @PathVariable UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(queryPort.findOwnerships(petId, PageRequest.of(page, size)))));
    }
}
