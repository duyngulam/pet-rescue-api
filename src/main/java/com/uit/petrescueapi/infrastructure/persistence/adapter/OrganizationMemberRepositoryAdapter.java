package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.OrganizationMember;
import com.uit.petrescueapi.domain.repository.OrganizationMemberRepository;
import com.uit.petrescueapi.infrastructure.persistence.entity.OrganizationMemberJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.repository.OrganizationMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrganizationMemberRepositoryAdapter implements OrganizationMemberRepository {

    private final OrganizationMemberJpaRepository jpa;

    @Override
    public OrganizationMember save(OrganizationMember member) {
        OrganizationMemberJpaEntity entity = OrganizationMemberJpaEntity.builder()
                .organizationId(member.getOrganizationId())
                .userId(member.getUserId())
                .role(member.getRole())
                .status(member.getStatus())
                .joinedAt(member.getJoinedAt())
                .build();
        jpa.save(entity);
        return member;
    }

    @Override
    public void delete(UUID organizationId, UUID userId) {
        jpa.deleteByOrganizationIdAndUserId(organizationId, userId);
    }

    @Override
    public boolean exists(UUID organizationId, UUID userId) {
        return jpa.existsByOrganizationIdAndUserId(organizationId, userId);
    }

    @Override
    public UUID findOrganizationIdByUserId(UUID userId) {
        return jpa.findOrganizationIdByUserId(userId);
    }

    @Override
    public String findOrgRoleByUserId(UUID userId) {
        return jpa.findOrgRoleByUserId(userId);
    }

    @Override
    public Optional<String> findRoleByOrgAndUser(UUID organizationId, UUID userId) {
        return jpa.findRoleByOrgAndUser(organizationId, userId);
    }
}
