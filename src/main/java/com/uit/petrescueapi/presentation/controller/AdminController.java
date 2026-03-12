package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.admin.CreateOrganizationAccountRequestDto;
import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.domain.entity.User;
import com.uit.petrescueapi.domain.service.OrganizationDomainService;
import com.uit.petrescueapi.domain.service.UserDomainService;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Administrative endpoints.
 *
 * <p>Used to create accounts that are immediately linked to an organization
 * with a specific organization role (OWNER / STAFF / VET / MEMBER).</p>
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Administrative operations")
public class AdminController {

    private final UserDomainService userDomainService;
    private final OrganizationDomainService organizationDomainService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/organizations/{organizationId}/accounts")
    @Operation(summary = "Create user account and assign organization role")
    public ResponseEntity<ApiResponse<UserResponseDto>> createOrganizationAccount(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateOrganizationAccountRequestDto cmd) {

        String systemRole = (cmd.getSystemRole() == null || cmd.getSystemRole().isBlank())
                ? "MEMBER"
                : cmd.getSystemRole();

        String hashedPassword = passwordEncoder.encode(cmd.getPassword());

        // 1) Create the user with the requested system role
        User user = userDomainService.createUser(
                cmd.getUsername(),
                cmd.getEmail(),
                hashedPassword,
                systemRole
        );

        // 2) Attach the user to the organization with the given organization role
        organizationDomainService.addMember(organizationId, user.getId(), cmd.getOrganizationRole());

        UserResponseDto dto = UserResponseDto.builder()
                .userId(user.getId())
                .organizationId(organizationId)
                .organizationName(null) // resolved on login via query path
                .organizationRole(cmd.getOrganizationRole())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .emailVerified(user.isEmailVerified())
                .roles(user.getRoles().stream().map(r -> r.getCode()).toList())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(dto));
    }
}
