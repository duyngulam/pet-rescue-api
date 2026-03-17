package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.PetMedicalRecord;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetMedicalRecordJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMedicalRecordEntityMapper {

    PetMedicalRecord toDomain(PetMedicalRecordJpaEntity entity);

    @Mapping(target = "pet", ignore = true)
    PetMedicalRecordJpaEntity toEntity(PetMedicalRecord domain);

    List<PetMedicalRecord> toDomainList(List<PetMedicalRecordJpaEntity> entities);
}
