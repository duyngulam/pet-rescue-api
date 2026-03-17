package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.role.AssignPermissionsRequestDto;
import com.uit.petrescueapi.application.dto.role.CreateRoleRequestDto;
import com.uit.petrescueapi.domain.entity.Role;

public interface RoleCommandPort {
    Role create(CreateRoleRequestDto cmd);
    Role assignPermissions(Integer roleId, AssignPermissionsRequestDto cmd);
    void delete(Integer roleId);
}
