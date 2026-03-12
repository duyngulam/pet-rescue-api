package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.PetOwnership;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetOwnershipJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetOwnershipEntityMapper {

    PetOwnership toDomain(PetOwnershipJpaEntity entity);

    PetOwnershipJpaEntity toEntity(PetOwnership domain);

    List<PetOwnership> toDomainList(List<PetOwnershipJpaEntity> entities);
}
