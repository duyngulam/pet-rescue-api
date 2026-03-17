package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.Permission;
import com.uit.petrescueapi.domain.repository.PermissionRepository;
import com.uit.petrescueapi.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository jpa;
    private final PermissionEntityMapper mapper;

    @Override
    public Permission save(Permission permission) {
        return mapper.toDomain(jpa.save(mapper.toEntity(permission)));
    }

    @Override
    public Optional<Permission> findById(Integer id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return mapper.toDomainList(jpa.findAll());
    }

    @Override
    public Optional<Permission> findByCode(String code) {
        return jpa.findByCode(code).map(mapper::toDomain);
    }
}
