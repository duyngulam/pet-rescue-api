package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Banner;
import com.uit.petrescueapi.infrastructure.persistence.entity.BannerJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BannerEntityMapper {

    Banner toDomain(BannerJpaEntity entity);

    @Mapping(target = "media", ignore = true)
    BannerJpaEntity toEntity(Banner domain);

    List<Banner> toDomainList(List<BannerJpaEntity> entities);
}
