package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.adoption.AdoptionResponseDto;
import com.uit.petrescueapi.application.dto.adoption.AdoptionSummaryResponseDto;
import com.uit.petrescueapi.application.dto.adoption.CreateAdoptionRequestDto;
import com.uit.petrescueapi.application.dto.adoption.DecisionRequestDto;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Mock adoption application controller.
 */
@RestController
@RequestMapping("/api/v1/adoptions")
@Tag(name = "Adoptions", description = "Adoption application management")
public class MockAdoptionController {

    private static final UUID APP1 = UUID.fromString("b50e8400-e29b-41d4-a716-446655440000");
    private static final UUID PET1 = UUID.fromString("c50e8400-e29b-41d4-a716-446655440000");
    private static final UUID USER1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID ORG1 = UUID.fromString("a50e8400-e29b-41d4-a716-446655440000");

    // ── Endpoints ────────────────────────────────

    @GetMapping
    @Operation(summary = "List adoption applications (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<AdoptionSummaryResponseDto>>> list(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<AdoptionSummaryResponseDto> apps = List.of(
                AdoptionSummaryResponseDto.builder()
                        .applicationId(APP1).petName("Buddy")
                        .applicantUsername("johndoe").status("PENDING")
                        .createdAt(LocalDateTime.now()).build());
        PageResponse<AdoptionSummaryResponseDto> pr = PageResponse.<AdoptionSummaryResponseDto>builder()
                .content(apps).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping
    @Operation(summary = "Submit an adoption application")
    public ResponseEntity<ApiResponse<AdoptionResponseDto>> create(@RequestBody CreateAdoptionRequestDto req) {
        AdoptionResponseDto dto = sampleApp(UUID.randomUUID(), "PENDING");
        dto.setPetId(req.getPetId());
        dto.setNote(req.getNote());
        return ResponseEntity.status(201).body(ApiResponse.created(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get adoption application by ID")
    public ResponseEntity<ApiResponse<AdoptionResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(sampleApp(id, "PENDING")));
    }

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve an adoption application")
    public ResponseEntity<ApiResponse<AdoptionResponseDto>> approve(@PathVariable UUID id,
                                                                       @RequestBody(required = false) DecisionRequestDto req) {
        AdoptionResponseDto dto = sampleApp(id, "APPROVED");
        dto.setDecidedAt(LocalDateTime.now());
        dto.setDecidedBy(USER1);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject an adoption application")
    public ResponseEntity<ApiResponse<AdoptionResponseDto>> reject(@PathVariable UUID id,
                                                                      @RequestBody(required = false) DecisionRequestDto req) {
        AdoptionResponseDto dto = sampleApp(id, "REJECTED");
        dto.setDecidedAt(LocalDateTime.now());
        dto.setDecidedBy(USER1);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel own adoption application")
    public ResponseEntity<ApiResponse<AdoptionResponseDto>> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(sampleApp(id, "CANCELLED")));
    }

    // ── Sample data ──────────────────────────────

    private AdoptionResponseDto sampleApp(UUID id, String status) {
        return AdoptionResponseDto.builder()
                .applicationId(id).petId(PET1).petName("Buddy")
                .applicantId(USER1).applicantUsername("johndoe")
                .organizationId(ORG1).status(status)
                .note("I have experience with dogs and a large backyard.")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
