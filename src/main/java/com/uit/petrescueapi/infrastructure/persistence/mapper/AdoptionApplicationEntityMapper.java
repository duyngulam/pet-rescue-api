package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.AdoptionApplication;
import com.uit.petrescueapi.infrastructure.persistence.entity.AdoptionApplicationJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdoptionApplicationEntityMapper {

    @Mapping(target = "decidedAt", source = "decidedAt", qualifiedByName = "offsetToLocal")
    AdoptionApplication toDomain(AdoptionApplicationJpaEntity entity);

    @Mapping(target = "decidedAt", source = "decidedAt", qualifiedByName = "localToOffset")
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "applicant", ignore = true)
    @Mapping(target = "organization", ignore = true)
    AdoptionApplicationJpaEntity toEntity(AdoptionApplication domain);

    List<AdoptionApplication> toDomainList(List<AdoptionApplicationJpaEntity> entities);

    @Named("offsetToLocal")
    default LocalDateTime offsetToLocal(OffsetDateTime odt) {
        return odt == null ? null : odt.toLocalDateTime();
    }

    @Named("localToOffset")
    default OffsetDateTime localToOffset(LocalDateTime ldt) {
        return ldt == null ? null : ldt.atOffset(ZoneOffset.UTC);
    }
}
