package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.AdoptionApplication;
import com.uit.petrescueapi.infrastructure.persistence.entity.AdoptionApplicationJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdoptionApplicationEntityMapper {

    AdoptionApplication toDomain(AdoptionApplicationJpaEntity entity);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    @Mapping(target = "organization", ignore = true)
    AdoptionApplicationJpaEntity toEntity(AdoptionApplication domain);

    List<AdoptionApplication> toDomainList(List<AdoptionApplicationJpaEntity> entities);
}
