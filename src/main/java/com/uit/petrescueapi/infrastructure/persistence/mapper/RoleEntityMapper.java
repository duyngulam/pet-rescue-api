package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Role;
import com.uit.petrescueapi.infrastructure.persistence.entity.RoleJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {

    @Mapping(source = "roleId", target = "id")
    Role toDomain(RoleJpaEntity entity);

    @Mapping(source = "id", target = "roleId")
    RoleJpaEntity toEntity(Role domain);
}
