package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.organization.*;
import com.uit.petrescueapi.application.port.command.OrganizationCommandPort;
import com.uit.petrescueapi.application.port.query.OrganizationQueryPort;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import com.uit.petrescueapi.presentation.mapper.OrganizationWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for Organization endpoints.
 *
 * <p>Write operations use the command port ({@link OrganizationCommandPort}) which returns
 * domain entities — mapped to DTOs here via {@link OrganizationWebMapper}.</p>
 * <p>Read operations use the query port ({@link OrganizationQueryPort}) which returns DTOs
 * directly from JOIN projections (no domain entity involved — pure CQRS read side).</p>
 */
@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Organizations", description = "Organization CRUD and membership management")
public class OrganizationController {

    private final OrganizationCommandPort commandPort;
    private final OrganizationQueryPort queryPort;
    private final OrganizationWebMapper mapper;

    // ── Commands (write) ─────────────────────────

    @PostMapping
    @Operation(summary = "Create a new organization")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> create(
            @RequestBody CreateOrganizationRequestDto cmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(mapper.toDto(commandPort.create(cmd))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing organization")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> update(
            @PathVariable UUID id,
            @RequestBody CreateOrganizationRequestDto cmd) {
        return ResponseEntity.ok(ApiResponse.ok(mapper.toDto(commandPort.update(id, cmd))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate an organization (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable UUID id) {
        commandPort.deactivate(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Organization deactivated"));
    }

    @PostMapping("/{id}/members")
    @Operation(summary = "Add a member to an organization")
    public ResponseEntity<ApiResponse<OrganizationMemberResponseDto>> addMember(
            @PathVariable UUID id,
            @RequestBody AddMemberRequestDto cmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(mapper.toMemberDto(commandPort.addMember(id, cmd))));
    }

    @DeleteMapping("/{id}/members/{userId}")
    @Operation(summary = "Remove a member from an organization")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable UUID id,
            @PathVariable UUID userId) {
        commandPort.removeMember(id, userId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Member removed"));
    }

    // ── Queries (read) ──────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(queryPort.findById(id)));
    }

    @GetMapping
    @Operation(summary = "List all active organizations (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<OrganizationSummaryResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(queryPort.findAll(PageRequest.of(page, size)))));
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "List members of an organization (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<OrganizationMemberResponseDto>>> getMembers(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(queryPort.findMembers(id, PageRequest.of(page, size)))));
    }
}

