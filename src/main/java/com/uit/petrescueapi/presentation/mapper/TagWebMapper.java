package com.uit.petrescueapi.presentation.mapper;

import com.uit.petrescueapi.application.dto.tag.TagResponseDto;
import com.uit.petrescueapi.domain.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagWebMapper {
    TagResponseDto toDto(Tag tag);
}
