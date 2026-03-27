package com.uit.petrescueapi.presentation.mapper;

import com.uit.petrescueapi.application.dto.banner.BannerResponseDto;
import com.uit.petrescueapi.domain.entity.Banner;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper: Banner domain entity → BannerResponseDto for API responses.
 */
@Mapper(componentModel = "spring")
public interface BannerWebMapper {

    BannerResponseDto toDto(Banner banner);
}
