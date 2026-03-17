package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Permission;
import com.uit.petrescueapi.infrastructure.persistence.entity.PermissionJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionEntityMapper {

    @Mapping(target = "id", source = "permissionId")
    Permission toDomain(PermissionJpaEntity entity);

    @Mapping(target = "permissionId", source = "id")
    PermissionJpaEntity toEntity(Permission domain);

    List<Permission> toDomainList(List<PermissionJpaEntity> entities);
}
