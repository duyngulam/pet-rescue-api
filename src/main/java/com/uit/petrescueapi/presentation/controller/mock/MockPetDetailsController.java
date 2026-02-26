package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.application.dto.pet.AddDiaryMediaRequestDto;
import com.uit.petrescueapi.application.dto.pet.CreateLocationRequestDto;
import com.uit.petrescueapi.application.dto.pet.CreateMedicalRecordRequestDto;
import com.uit.petrescueapi.application.dto.pet.PetLocationResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetMedicalRecordResponseDto;
import com.uit.petrescueapi.application.dto.pet.PetOwnershipResponseDto;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Mock endpoints for pet sub-resources: medical records, locations, ownerships, diary.
 */
@RestController
@RequestMapping("/api/v1/pets")
@Tag(name = "Pet Details", description = "Medical records, locations, ownerships & diary")
public class MockPetDetailsController {

    private static final UUID PET1 = UUID.fromString("c50e8400-e29b-41d4-a716-446655440000");
    private static final UUID USER1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    // ── Medical Records ──────────────────────────

    @GetMapping("/{petId}/medical-records")
    @Operation(summary = "List medical records for a pet (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<PetMedicalRecordResponseDto>>> listMedicalRecords(
            @PathVariable UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PetMedicalRecordResponseDto rec = PetMedicalRecordResponseDto.builder()
                .recordId(UUID.randomUUID()).petId(petId)
                .description("Annual checkup — all healthy")
                .vaccine("Rabies vaccine (3-year)")
                .diagnosis("No issues found")
                .recordDate(LocalDateTime.now())
                .createdBy(USER1)
                .build();
        PageResponse<PetMedicalRecordResponseDto> pr = PageResponse.<PetMedicalRecordResponseDto>builder()
                .content(List.of(rec)).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping("/{petId}/medical-records")
    @Operation(summary = "Add a medical record")
    public ResponseEntity<ApiResponse<PetMedicalRecordResponseDto>> addMedicalRecord(
            @PathVariable UUID petId, @RequestBody CreateMedicalRecordRequestDto req) {
        PetMedicalRecordResponseDto rec = PetMedicalRecordResponseDto.builder()
                .recordId(UUID.randomUUID()).petId(petId)
                .description(req.getDescription())
                .vaccine(req.getVaccine())
                .diagnosis(req.getDiagnosis())
                .recordDate(LocalDateTime.now())
                .createdBy(USER1)
                .build();
        return ResponseEntity.status(201).body(ApiResponse.created(rec));
    }

    // ── Locations ────────────────────────────────

    @GetMapping("/{petId}/locations")
    @Operation(summary = "List GPS locations for a pet (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<PetLocationResponseDto>>> listLocations(
            @PathVariable UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PetLocationResponseDto loc = PetLocationResponseDto.builder()
                .locationId(UUID.randomUUID()).petId(petId)
                .latitude(10.762622).longitude(106.660172)
                .source("GPS_TRACKER").recordedAt(LocalDateTime.now())
                .build();
        PageResponse<PetLocationResponseDto> pr = PageResponse.<PetLocationResponseDto>builder()
                .content(List.of(loc)).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping("/{petId}/locations")
    @Operation(summary = "Record a new location")
    public ResponseEntity<ApiResponse<PetLocationResponseDto>> addLocation(
            @PathVariable UUID petId, @RequestBody CreateLocationRequestDto req) {
        PetLocationResponseDto loc = PetLocationResponseDto.builder()
                .locationId(UUID.randomUUID()).petId(petId)
                .latitude(req.getLatitude()).longitude(req.getLongitude())
                .source(req.getSource()).recordedAt(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(ApiResponse.created(loc));
    }

    // ── Ownerships ───────────────────────────────

    @GetMapping("/{petId}/ownerships")
    @Operation(summary = "List ownership history for a pet (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<PetOwnershipResponseDto>>> listOwnerships(
            @PathVariable UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PetOwnershipResponseDto ownership = PetOwnershipResponseDto.builder()
                .petId(petId).ownerType("ORGANIZATION")
                .ownerId(UUID.fromString("a50e8400-e29b-41d4-a716-446655440000"))
                .ownerName("Happy Paws Shelter")
                .fromTime(LocalDateTime.now().minusDays(30))
                .build();
        PageResponse<PetOwnershipResponseDto> pr = PageResponse.<PetOwnershipResponseDto>builder()
                .content(List.of(ownership)).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    // ── Diary (media) ────────────────────────────

    @GetMapping("/{petId}/diary")
    @Operation(summary = "List diary media for a pet (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<MediaFileResponseDto>>> listDiary(
            @PathVariable UUID petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        MediaFileResponseDto media = MediaFileResponseDto.builder()
                .mediaId(UUID.randomUUID()).uploaderId(USER1)
                .url("https://storage.example.com/pets/buddy-01.jpg")
                .type("IMAGE").createdAt(LocalDateTime.now())
                .build();
        PageResponse<MediaFileResponseDto> pr = PageResponse.<MediaFileResponseDto>builder()
                .content(List.of(media)).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping("/{petId}/diary")
    @Operation(summary = "Add a media entry to pet diary")
    public ResponseEntity<ApiResponse<MediaFileResponseDto>> addDiaryEntry(
            @PathVariable UUID petId, @RequestBody AddDiaryMediaRequestDto req) {
        MediaFileResponseDto media = MediaFileResponseDto.builder()
                .mediaId(req.getMediaId()).uploaderId(USER1)
                .url("https://storage.example.com/pets/buddy-new.jpg")
                .type("IMAGE").createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(ApiResponse.created(media));
    }
}