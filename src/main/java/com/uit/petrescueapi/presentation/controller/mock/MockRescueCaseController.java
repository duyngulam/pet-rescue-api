package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.rescue.CreateRescueCaseRequestDto;
import com.uit.petrescueapi.application.dto.rescue.RescueCaseResponseDto;
import com.uit.petrescueapi.application.dto.rescue.RescueCaseSummaryResponseDto;
import com.uit.petrescueapi.application.dto.rescue.UpdateRescueCaseStatusRequestDto;
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
 * Mock rescue case controller.
 */
@RestController
@RequestMapping("/api/v1/rescue-cases")
@Tag(name = "Rescue Cases", description = "Animal rescue case tracking")
public class MockRescueCaseController {

    private static final UUID CASE1 = UUID.fromString("d50e8400-e29b-41d4-a716-446655440000");
    private static final UUID PET1 = UUID.fromString("c50e8400-e29b-41d4-a716-446655440000");
    private static final UUID USER1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID ORG1 = UUID.fromString("a50e8400-e29b-41d4-a716-446655440000");

    @GetMapping
    @Operation(summary = "List rescue cases (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<RescueCaseSummaryResponseDto>>> list(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<RescueCaseSummaryResponseDto> cases = List.of(
                RescueCaseSummaryResponseDto.builder()
                        .caseId(CASE1).petName("Buddy").status("IN_PROGRESS")
                        .reporterUsername("johndoe").reportedAt(LocalDateTime.now()).build());
        PageResponse<RescueCaseSummaryResponseDto> pr = PageResponse.<RescueCaseSummaryResponseDto>builder()
                .content(cases).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping
    @Operation(summary = "Report a new rescue case")
    public ResponseEntity<ApiResponse<RescueCaseResponseDto>> create(@RequestBody CreateRescueCaseRequestDto req) {
        return ResponseEntity.status(201).body(
                ApiResponse.created(sampleCase(UUID.randomUUID(), "REPORTED")));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rescue case by ID")
    public ResponseEntity<ApiResponse<RescueCaseResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(sampleCase(id, "IN_PROGRESS")));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update rescue case details")
    public ResponseEntity<ApiResponse<RescueCaseResponseDto>> update(@PathVariable UUID id,
                                                              @RequestBody CreateRescueCaseRequestDto req) {
        return ResponseEntity.ok(ApiResponse.ok(sampleCase(id, "IN_PROGRESS")));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change rescue case status")
    public ResponseEntity<ApiResponse<RescueCaseResponseDto>> changeStatus(@PathVariable UUID id,
                                                                    @RequestBody UpdateRescueCaseStatusRequestDto req) {
        return ResponseEntity.ok(ApiResponse.ok(sampleCase(id, req.getStatus())));
    }

    private RescueCaseResponseDto sampleCase(UUID id, String status) {
        return RescueCaseResponseDto.builder()
                .caseId(id).petId(PET1).petName("Buddy")
                .reportedBy(USER1).reporterUsername("johndoe")
                .organizationId(ORG1).organizationName("Happy Paws Shelter")
                .status(status).reportedAt(LocalDateTime.now())
                .build();
    }
}
