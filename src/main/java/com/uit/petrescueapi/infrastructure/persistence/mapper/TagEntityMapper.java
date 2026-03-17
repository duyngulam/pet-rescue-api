package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Tag;
import com.uit.petrescueapi.infrastructure.persistence.entity.TagJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagEntityMapper {

    Tag toDomain(TagJpaEntity entity);

    TagJpaEntity toEntity(Tag domain);

    List<Tag> toDomainList(List<TagJpaEntity> entities);
}
