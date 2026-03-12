package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.RefreshToken;
import com.uit.petrescueapi.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenEntityMapper {

    RefreshToken toDomain(RefreshTokenJpaEntity entity);

    RefreshTokenJpaEntity toEntity(RefreshToken domain);
}
