package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.PetMedia;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetMediaJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMediaEntityMapper {

    PetMedia toDomain(PetMediaJpaEntity entity);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "mediaFile", ignore = true)
    PetMediaJpaEntity toEntity(PetMedia domain);

    List<PetMedia> toDomainList(List<PetMediaJpaEntity> entities);
}
