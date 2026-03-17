package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.Permission;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository contract for the Permission entity.
 */
public interface PermissionRepository {

    Permission save(Permission permission);

    Optional<Permission> findById(Integer id);

    List<Permission> findAll();

    Optional<Permission> findByCode(String code);
}
