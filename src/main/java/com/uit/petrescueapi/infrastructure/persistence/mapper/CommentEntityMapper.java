package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Comment;
import com.uit.petrescueapi.infrastructure.persistence.entity.CommentJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentEntityMapper {

    Comment toDomain(CommentJpaEntity entity);

    CommentJpaEntity toEntity(Comment domain);

    List<Comment> toDomainList(List<CommentJpaEntity> entities);
}
