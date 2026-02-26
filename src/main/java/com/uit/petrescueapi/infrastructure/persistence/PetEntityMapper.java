package com.uit.petrescueapi.infrastructure.persistence;

import com.uit.petrescueapi.domain.entity.Pet;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * MapStruct mapper: domain {@link Pet} ↔ JPA {@link PetJpaEntity}.
 */
@Mapper(componentModel = "spring")
public interface PetEntityMapper {

    Pet toDomain(PetJpaEntity entity);

    PetJpaEntity toEntity(Pet domain);

    List<Pet> toDomainList(List<PetJpaEntity> entities);
}
