package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.UserReputation;
import com.uit.petrescueapi.infrastructure.persistence.entity.UserReputationJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserReputationEntityMapper {

    UserReputation toDomain(UserReputationJpaEntity entity);

    @Mapping(target = "user", ignore = true)
    UserReputationJpaEntity toEntity(UserReputation domain);

    List<UserReputation> toDomainList(List<UserReputationJpaEntity> entities);
}
