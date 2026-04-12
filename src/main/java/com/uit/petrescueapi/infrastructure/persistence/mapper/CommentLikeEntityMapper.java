package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.CommentLike;
import com.uit.petrescueapi.infrastructure.persistence.entity.CommentLikeJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentLikeEntityMapper {

    CommentLike toDomain(CommentLikeJpaEntity entity);

    CommentLikeJpaEntity toEntity(CommentLike domain);
}
