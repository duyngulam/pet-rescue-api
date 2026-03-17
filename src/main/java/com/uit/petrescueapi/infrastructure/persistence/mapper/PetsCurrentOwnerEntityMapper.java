package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.PetCurrentOwner;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetsCurrentOwnerJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetsCurrentOwnerEntityMapper {

    PetCurrentOwner toDomain(PetsCurrentOwnerJpaEntity entity);

    PetsCurrentOwnerJpaEntity toEntity(PetCurrentOwner domain);

    List<PetCurrentOwner> toDomainList(List<PetsCurrentOwnerJpaEntity> entities);
}
