package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.PasswordResetToken;
import com.uit.petrescueapi.infrastructure.persistence.entity.PasswordResetTokenJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenEntityMapper {

    PasswordResetToken toDomain(PasswordResetTokenJpaEntity entity);

    PasswordResetTokenJpaEntity toEntity(PasswordResetToken domain);
}
