package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.organization.AddMemberRequestDto;
import com.uit.petrescueapi.application.dto.organization.CreateOrganizationRequestDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationResponseDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationSummaryResponseDto;
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
 * Mock organization controller — shelters, vet centers, members.
 */
@RestController
@RequestMapping("/api/v1/organizations")
@Tag(name = "Organizations", description = "Shelters & vet centers management")
public class MockOrganizationController {

    private static final UUID ORG1 = UUID.fromString("a50e8400-e29b-41d4-a716-446655440000");
    private static final UUID ORG2 = UUID.fromString("a50e8400-e29b-41d4-a716-446655440001");
    private static final UUID USER1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    // ── Organizations CRUD ───────────────────────

    @GetMapping
    @Operation(summary = "List organizations (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<OrganizationSummaryResponseDto>>> list(
            @Parameter(description = "Filter by type") @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<OrganizationSummaryResponseDto> orgs = List.of(
                OrganizationSummaryResponseDto.builder()
                        .organizationId(ORG1).name("Happy Paws Shelter").type("SHELTER").status("ACTIVE").build(),
                OrganizationSummaryResponseDto.builder()
                        .organizationId(ORG2).name("City Vet Center").type("VET_CENTER").status("ACTIVE").build());
        PageResponse<OrganizationSummaryResponseDto> pr = PageResponse.<OrganizationSummaryResponseDto>builder()
                .content(orgs).page(page).size(size)
                .totalElements(2).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping
    @Operation(summary = "Create an organization")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> create(@RequestBody CreateOrganizationRequestDto req) {
        return ResponseEntity.status(201).body(
                ApiResponse.created(sampleOrg(UUID.randomUUID(), req.getName(), req.getType())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(sampleOrg(id, "Happy Paws Shelter", "SHELTER")));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update organization")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> update(@PathVariable UUID id,
                                                                @RequestBody CreateOrganizationRequestDto req) {
        return ResponseEntity.ok(ApiResponse.ok(sampleOrg(id, req.getName(), req.getType())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate organization")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(null, "Organization deactivated"));
    }

    // ── Members ──────────────────────────────────

    @GetMapping("/{id}/members")
    @Operation(summary = "List members of an organization (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<OrganizationMemberResponseDto>>> listMembers(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        OrganizationMemberResponseDto member = OrganizationMemberResponseDto.builder()
                .organizationId(id).userId(USER1)
                .username("johndoe").role("MANAGER").status("ACTIVE")
                .joinedAt(LocalDateTime.now())
                .build();
        PageResponse<OrganizationMemberResponseDto> pr = PageResponse.<OrganizationMemberResponseDto>builder()
                .content(List.of(member)).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping("/{id}/members")
    @Operation(summary = "Add a member to an organization")
    public ResponseEntity<ApiResponse<OrganizationMemberResponseDto>> addMember(@PathVariable UUID id,
                                                                         @RequestBody AddMemberRequestDto req) {
        OrganizationMemberResponseDto member = OrganizationMemberResponseDto.builder()
                .organizationId(id).userId(req.getUserId())
                .username("newmember").role(req.getRole()).status("ACTIVE")
                .joinedAt(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(ApiResponse.created(member));
    }

    @DeleteMapping("/{orgId}/members/{userId}")
    @Operation(summary = "Remove a member from an organization")
    public ResponseEntity<ApiResponse<Void>> removeMember(@PathVariable UUID orgId,
                                                           @PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(null, "Member removed"));
    }

    // ── Sample data ──────────────────────────────

    private OrganizationResponseDto sampleOrg(UUID id, String name, String type) {
        return OrganizationResponseDto.builder()
                .organizationId(id).name(name).type(type)
                .address("123 Main St, District 1, HCMC")
                .phone("+84-28-1234-5678")
                .latitude(10.762622).longitude(106.660172)
                .status("ACTIVE").createdBy(USER1)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
