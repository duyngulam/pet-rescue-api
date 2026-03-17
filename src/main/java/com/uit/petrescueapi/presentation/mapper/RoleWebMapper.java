package com.uit.petrescueapi.presentation.mapper;

import com.uit.petrescueapi.application.dto.role.RoleResponseDto;
import com.uit.petrescueapi.domain.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleWebMapper {
    @Mapping(target = "roleId", source = "id")
    RoleResponseDto toDto(Role role);
}
