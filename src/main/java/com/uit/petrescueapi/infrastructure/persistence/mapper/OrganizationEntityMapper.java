package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.Organization;
import com.uit.petrescueapi.infrastructure.persistence.entity.OrganizationJpaEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganizationEntityMapper {

    Organization toDomain(OrganizationJpaEntity entity);

    OrganizationJpaEntity toEntity(Organization domain);

    List<Organization> toDomainList(List<OrganizationJpaEntity> entities);
}
