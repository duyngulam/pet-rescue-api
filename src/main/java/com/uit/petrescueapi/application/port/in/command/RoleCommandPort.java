package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.role.AssignPermissionsRequestDto;
import com.uit.petrescueapi.application.dto.role.CreateRoleRequestDto;
import com.uit.petrescueapi.application.dto.role.RoleResponseDto;

import java.util.UUID;

/**
 * Command (write) port for Role operations.
 * Handles role creation, permission assignment and deletion.
 */
public interface RoleCommandPort {

    RoleResponseDto create(CreateRoleRequestDto cmd);

    RoleResponseDto assignPermissions(Integer roleId, AssignPermissionsRequestDto cmd);

    void delete(Integer roleId);
}
