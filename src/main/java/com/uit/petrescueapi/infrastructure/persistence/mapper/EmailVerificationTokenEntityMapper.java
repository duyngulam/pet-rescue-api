package com.uit.petrescueapi.infrastructure.persistence.mapper;

import com.uit.petrescueapi.domain.entity.EmailVerificationToken;
import com.uit.petrescueapi.infrastructure.persistence.entity.EmailVerificationTokenJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailVerificationTokenEntityMapper {

    EmailVerificationToken toDomain(EmailVerificationTokenJpaEntity entity);

    EmailVerificationTokenJpaEntity toEntity(EmailVerificationToken domain);
}
