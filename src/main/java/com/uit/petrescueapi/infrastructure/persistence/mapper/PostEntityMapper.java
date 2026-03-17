package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Post;
import com.uit.petrescueapi.infrastructure.persistence.entity.MediaFileJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.PostJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.TagJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PostEntityMapper {

    @Mapping(target = "mediaIds", source = "mediaFiles", qualifiedByName = "mediaToIds")
    @Mapping(target = "tagIds", source = "tags", qualifiedByName = "tagsToIds")
    Post toDomain(PostJpaEntity entity);

    @Mapping(target = "mediaFiles", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "rescueCase", ignore = true)
    PostJpaEntity toEntity(Post domain);

    List<Post> toDomainList(List<PostJpaEntity> entities);

    @Named("mediaToIds")
    default List<UUID> mediaToIds(List<MediaFileJpaEntity> media) {
        if (media == null) return new ArrayList<>();
        return media.stream().map(MediaFileJpaEntity::getMediaId).toList();
    }

    @Named("tagsToIds")
    default List<UUID> tagsToIds(List<TagJpaEntity> tags) {
        if (tags == null) return new ArrayList<>();
        return tags.stream().map(TagJpaEntity::getTagId).toList();
    }
}
