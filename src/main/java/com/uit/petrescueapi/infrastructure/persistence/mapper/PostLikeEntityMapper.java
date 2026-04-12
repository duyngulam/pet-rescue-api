package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.PostLike;
import com.uit.petrescueapi.infrastructure.persistence.entity.PostLikeJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostLikeEntityMapper {

    PostLike toDomain(PostLikeJpaEntity entity);

    PostLikeJpaEntity toEntity(PostLike domain);
}
