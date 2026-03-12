package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.Role;
import com.uit.petrescueapi.domain.repository.RoleRepository;
import com.uit.petrescueapi.infrastructure.persistence.mapper.RoleEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository jpa;
    private final RoleEntityMapper mapper;

    @Override
    public Optional<Role> findByCode(String code) {
        return jpa.findByCode(code).map(mapper::toDomain);
    }
}
