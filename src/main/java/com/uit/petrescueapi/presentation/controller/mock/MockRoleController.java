package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.role.AssignPermissionsRequestDto;
import com.uit.petrescueapi.application.dto.role.CreateRoleRequestDto;
import com.uit.petrescueapi.application.dto.role.PermissionResponseDto;
import com.uit.petrescueapi.application.dto.role.RoleResponseDto;
import com.uit.petrescueapi.application.dto.role.RoleSummaryResponseDto;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Mock role & permission controller.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Roles & Permissions", description = "RBAC management (admin)")
public class MockRoleController {

    // ── Roles ────────────────────────────────────

    @GetMapping("/roles")
    @Operation(summary = "List all roles (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<RoleSummaryResponseDto>>> listRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<RoleSummaryResponseDto> roles = List.of(
                RoleSummaryResponseDto.builder().roleId(1).code("ADMIN").name("Administrator").build(),
                RoleSummaryResponseDto.builder().roleId(2).code("USER").name("Regular User").build(),
                RoleSummaryResponseDto.builder().roleId(3).code("VOLUNTEER").name("Volunteer").build());
        PageResponse<RoleSummaryResponseDto> pr = PageResponse.<RoleSummaryResponseDto>builder()
                .content(roles).page(page).size(size)
                .totalElements(3).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping("/roles")
    @Operation(summary = "Create a new role")
    public ResponseEntity<ApiResponse<RoleResponseDto>> createRole(@RequestBody CreateRoleRequestDto req) {
        return ResponseEntity.status(201).body(
                ApiResponse.created(sampleRole(4, req.getCode(), req.getName())));
    }

    @GetMapping("/roles/{id}")
    @Operation(summary = "Get role by ID")
    public ResponseEntity<ApiResponse<RoleResponseDto>> getRole(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(sampleRole(id, "ADMIN", "Administrator")));
    }

    @PutMapping("/roles/{id}")
    @Operation(summary = "Update a role")
    public ResponseEntity<ApiResponse<RoleResponseDto>> updateRole(@PathVariable Integer id,
                                                            @RequestBody CreateRoleRequestDto req) {
        return ResponseEntity.ok(ApiResponse.ok(sampleRole(id, req.getCode(), req.getName())));
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "Delete a role")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(null, "Role deleted"));
    }

    @PutMapping("/roles/{id}/permissions")
    @Operation(summary = "Assign permissions to a role")
    public ResponseEntity<ApiResponse<RoleResponseDto>> assignPermissions(@PathVariable Integer id,
                                                                   @RequestBody AssignPermissionsRequestDto req) {
        return ResponseEntity.ok(ApiResponse.ok(sampleRole(id, "ADMIN", "Administrator")));
    }

    // ── Permissions ──────────────────────────────

    @GetMapping("/permissions")
    @Operation(summary = "List all permissions (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<PermissionResponseDto>>> listPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<PermissionResponseDto> perms = List.of(
                samplePerm(1, "PET", "CREATE"),
                samplePerm(2, "PET", "READ"),
                samplePerm(3, "PET", "UPDATE"),
                samplePerm(4, "PET", "DELETE"),
                samplePerm(5, "USER", "READ"),
                samplePerm(6, "USER", "UPDATE"),
                samplePerm(7, "ORGANIZATION", "CREATE"),
                samplePerm(8, "POST", "CREATE"));
        PageResponse<PermissionResponseDto> pr = PageResponse.<PermissionResponseDto>builder()
                .content(perms).page(page).size(size)
                .totalElements(8).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    // ── Sample data ──────────────────────────────

    private RoleResponseDto sampleRole(Integer id, String code, String name) {
        return RoleResponseDto.builder()
                .roleId(id).code(code).name(name)
                .description(name + " role")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private PermissionResponseDto samplePerm(Integer id, String resource, String action) {
        return PermissionResponseDto.builder()
                .permissionId(id).resource(resource).action(action)
                .code(resource + ":" + action)
                .description(action + " " + resource.toLowerCase())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
