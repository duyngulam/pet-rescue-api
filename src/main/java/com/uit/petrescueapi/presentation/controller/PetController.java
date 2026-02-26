package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.pet.CreatePetRequestDto;
import com.uit.petrescueapi.application.dto.pet.UpdatePetRequestDto;
import com.uit.petrescueapi.application.dto.pet.PetResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetSummaryResponseDto;
import com.uit.petrescueapi.application.port.in.command.PetCommandPort;
import com.uit.petrescueapi.application.port.in.query.PetQueryPort;
import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.valueobject.PetStatus;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import com.uit.petrescueapi.presentation.mapper.PetWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller exposing Pet CRUD endpoints.
 * Uses CQRS: separate command and query ports.
 */
@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pets", description = "Pet CRUD operations")
public class PetController {

    private final PetCommandPort petCommandPort;
    private final PetQueryPort petQueryPort;
    private final PetWebMapper mapper;

    // ── Commands (write) ─────────────────────────

    @PostMapping
    @Operation(summary = "Create a new pet")
    public ResponseEntity<ApiResponse<PetResponseDto>> create(@Valid @RequestBody CreatePetRequestDto cmd) {
        Pet pet = petCommandPort.create(cmd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(mapper.toDto(pet)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing pet")
    public ResponseEntity<ApiResponse<PetResponseDto>> update(@PathVariable UUID id,
                                                       @Valid @RequestBody UpdatePetRequestDto cmd) {
        return ResponseEntity.ok(ApiResponse.ok(mapper.toDto(petCommandPort.update(id, cmd))));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change pet status")
    public ResponseEntity<ApiResponse<PetResponseDto>> changeStatus(@PathVariable UUID id,
                                                             @RequestParam PetStatus status) {
        return ResponseEntity.ok(ApiResponse.ok(mapper.toDto(petCommandPort.changeStatus(id, status))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a pet")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        petCommandPort.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Pet deleted"));
    }

    // ── Queries (read) ──────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get pet by ID (full detail)")
    public ResponseEntity<ApiResponse<PetResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(mapper.toDto(petQueryPort.findById(id))));
    }

    @GetMapping
    @Operation(summary = "List all pets (paginated, summary view) nếu muốn cuộn cuộn thì bảo bạn đổi api")
    public ResponseEntity<ApiResponse<PageResponse<PetSummaryResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Pet> pets = petQueryPort.findAll(PageRequest.of(page, size));
        PageResponse<PetSummaryResponseDto> pr = PageResponse.<PetSummaryResponseDto>builder()
                .content(pets.getContent().stream().map(mapper::toSummaryDto).toList())
                .page(pets.getNumber())
                .size(pets.getSize())
                .totalElements(pets.getTotalElements())
                .totalPages(pets.getTotalPages())
                .last(pets.isLast())
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @GetMapping("/available")
    @Operation(summary = "List available pets (paginated, summary view)")
    public ResponseEntity<ApiResponse<PageResponse<PetSummaryResponseDto>>> getAvailable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Pet> pets = petQueryPort.findAvailable(PageRequest.of(page, size));
        PageResponse<PetSummaryResponseDto> pr = PageResponse.<PetSummaryResponseDto>builder()
                .content(pets.getContent().stream().map(mapper::toSummaryDto).toList())
                .page(pets.getNumber())
                .size(pets.getSize())
                .totalElements(pets.getTotalElements())
                .totalPages(pets.getTotalPages())
                .last(pets.isLast())
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }
}
