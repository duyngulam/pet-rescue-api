package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.role.PermissionResponseDto;
import com.uit.petrescueapi.application.dto.role.RoleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Query (read) port for Role operations.
 * Handles role listing and permission lookups.
 */
public interface RoleQueryPort {

    Page<RoleResponseDto> findAll(Pageable pageable);

    RoleResponseDto findById(Integer roleId);

    List<PermissionResponseDto> getPermissions(Integer roleId);
}
