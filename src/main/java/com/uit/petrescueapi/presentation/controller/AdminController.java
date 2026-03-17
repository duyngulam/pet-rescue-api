package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.admin.AssignOrgRoleRequestDto;
import com.uit.petrescueapi.application.dto.admin.CreateOrganizationAccountRequestDto;
import com.uit.petrescueapi.application.dto.organization.OrganizationMemberResponseDto;
import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.domain.entity.OrganizationMember;
import com.uit.petrescueapi.domain.entity.User;
import com.uit.petrescueapi.domain.exception.BusinessException;
import com.uit.petrescueapi.domain.service.OrganizationDomainService;
import com.uit.petrescueapi.domain.service.UserDomainService;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Administrative endpoints — ADMIN role required for all operations.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Administrative operations")
public class AdminController {

    private final UserDomainService userDomainService;
    private final OrganizationDomainService organizationDomainService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/organizations/{organizationId}/accounts")
    @Operation(summary = "Create a new user account and assign an organization role")
    public ResponseEntity<ApiResponse<UserResponseDto>> createOrganizationAccount(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateOrganizationAccountRequestDto cmd) {

        String systemRole = (cmd.getSystemRole() == null || cmd.getSystemRole().isBlank())
                ? "MEMBER"
                : cmd.getSystemRole();

        String hashedPassword = passwordEncoder.encode(cmd.getPassword());

        User user = userDomainService.createUser(
                cmd.getUsername(),
                cmd.getEmail(),
                hashedPassword,
                systemRole
        );

        organizationDomainService.addMember(organizationId, user.getId(), cmd.getOrganizationRole());

        UserResponseDto dto = UserResponseDto.builder()
                .userId(user.getId())
                .organizationId(organizationId)
                .organizationName(null)
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

    @PostMapping("/organizations/{organizationId}/members")
    @Operation(summary = "Assign an organization role to an existing user (target must not be an admin account)")
    public ResponseEntity<ApiResponse<OrganizationMemberResponseDto>> assignOrgRole(
            @PathVariable UUID organizationId,
            @Valid @RequestBody AssignOrgRoleRequestDto cmd) {

        User targetUser = userDomainService.findById(cmd.getUserId());
        if (targetUser.hasRole("ADMIN")) {
            throw new BusinessException("Cannot assign an organization role to an admin account", "FORBIDDEN_TARGET");
        }

        OrganizationMember member = organizationDomainService.addMember(
                organizationId, cmd.getUserId(), cmd.getOrganizationRole());

        OrganizationMemberResponseDto dto = OrganizationMemberResponseDto.builder()
                .organizationId(member.getOrganizationId())
                .userId(member.getUserId())
                .role(member.getRole())
                .status(member.getStatus())
                .joinedAt(member.getJoinedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(dto));
    }
}
