package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.MediaFile;
import com.uit.petrescueapi.infrastructure.persistence.entity.MediaFileJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaFileEntityMapper {

    MediaFile toDomain(MediaFileJpaEntity entity);

    @Mapping(target = "uploader", ignore = true)
    MediaFileJpaEntity toEntity(MediaFile domain);

    List<MediaFile> toDomainList(List<MediaFileJpaEntity> entities);
}
