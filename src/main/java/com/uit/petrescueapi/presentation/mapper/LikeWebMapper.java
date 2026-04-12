package com.uit.petrescueapi.presentation.mapper;

import com.uit.petrescueapi.application.dto.like.LikeStatusDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikeWebMapper {
    LikeStatusDto toDto(LikeStatusDto status);
}
