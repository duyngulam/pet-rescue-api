package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.application.dto.user.UserReputationResponseDto;
import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.application.dto.user.UserSummaryResponseDto;
import com.uit.petrescueapi.application.port.out.UserQueryDataPort;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.infrastructure.persistence.entity.RoleJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.UserJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.UserReputationJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.projection.UserDetailProjection;
import com.uit.petrescueapi.infrastructure.persistence.projection.UserSummaryProjection;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserQueryJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserReputationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Query-side adapter (CQRS read path) for User.
 *
 * <p>Executes optimized JOIN queries via {@link UserQueryJpaRepository},
 * maps infrastructure projections to application DTOs.</p>
 *
 * <p>For detail view, also loads roles from {@link UserJpaRepository}
 * via entity relationships. For reputation, uses {@link UserReputationJpaRepository}.</p>
 */
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryDataPort {

    private final UserQueryJpaRepository queryRepo;
    private final UserJpaRepository userJpaRepo;
    private final UserReputationJpaRepository userReputationJpaRepo;

    // ── List (summary) queries ──────────────────

    @Override
    public Page<UserSummaryResponseDto> findAllSummaries(Pageable pageable) {
        return queryRepo.findAllSummary(pageable).map(this::toSummaryDto);
    }

    // ── Detail (single user) query ──────────────

    @Override
    public UserResponseDto findById(UUID userId) {
        UserDetailProjection proj = queryRepo.findDetailById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        // Load entity to get roles (lazy collection)
        UserJpaEntity userEntity = userJpaRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        List<String> roles = userEntity.getRoles().stream()
                .map(RoleJpaEntity::getCode)
                .toList();

        return toResponseDto(proj, roles);
    }

    // ── Reputation query ────────────────────────

    @Override
    public UserReputationResponseDto getReputation(UUID userId) {
        UserReputationJpaEntity entity = userReputationJpaRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserReputation", "userId", userId));
        return toReputationDto(entity);
    }

    // ── Projection → DTO mappers ────────────────

    private UserSummaryResponseDto toSummaryDto(UserSummaryProjection p) {
        return UserSummaryResponseDto.builder()
                .userId(p.getUserId())
                .username(p.getUsername())
                .email(p.getEmail())
                .status(p.getStatus())
                .build();
    }

    private UserResponseDto toResponseDto(UserDetailProjection p, List<String> roles) {
        return UserResponseDto.builder()
                .userId(p.getUserId())
                .organizationId(p.getOrganizationId())
                .organizationName(p.getOrganizationName())
                .organizationRole(p.getOrganizationRole())
                .username(p.getUsername())
                .email(p.getEmail())
                .status(p.getStatus())
                .emailVerified(p.getEmailVerified())
                .roles(roles)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private UserReputationResponseDto toReputationDto(UserReputationJpaEntity e) {
        return UserReputationResponseDto.builder()
                .userId(e.getUserId())
                .score(e.getScore())
                .level(e.getLevel())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
