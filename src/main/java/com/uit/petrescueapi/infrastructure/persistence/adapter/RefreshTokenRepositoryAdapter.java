package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.RefreshToken;
import com.uit.petrescueapi.domain.repository.RefreshTokenRepository;
import com.uit.petrescueapi.infrastructure.persistence.mapper.RefreshTokenEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpa;
    private final RefreshTokenEntityMapper mapper;

    @Override
    public RefreshToken save(RefreshToken token) {
        return mapper.toDomain(jpa.save(mapper.toEntity(token)));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpa.findByToken(token).map(mapper::toDomain);
    }

    @Override
    public void revokeAllByUserId(UUID userId) {
        jpa.revokeAllByUserId(userId);
    }
}
