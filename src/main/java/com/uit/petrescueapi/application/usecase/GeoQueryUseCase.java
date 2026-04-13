package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.geo.GeoUserLocationDto;
import com.uit.petrescueapi.application.port.query.GeoQueryPort;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.infrastructure.persistence.entity.UserGeoLocationJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.UserJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserGeoLocationJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeoQueryUseCase implements GeoQueryPort {

    private final UserGeoLocationJpaRepository geoRepository;
    private final UserJpaRepository userRepository;

    @Override
    public List<GeoUserLocationDto> findNearby(UUID userId, double radiusKm, int limit) {
        UserGeoLocationJpaEntity me = geoRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("GeoLocation", "userId", userId));

        List<UserGeoLocationJpaEntity> nearby = geoRepository.findNearbyActive(
                userId, me.getLat(), me.getLng(), radiusKm, limit);

        List<UUID> ids = nearby.stream().map(UserGeoLocationJpaEntity::getUserId).toList();
        Map<UUID, UserJpaEntity> usersById = userRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(UserJpaEntity::getUserId, Function.identity()));

        return nearby.stream()
                .map(g -> {
                    UserJpaEntity user = usersById.get(g.getUserId());
                    String name = user == null ? "Unknown" : resolveName(user);
                    String avatarUrl = user == null ? null : user.getAvatarUrl();
                    return GeoUserLocationDto.builder()
                            .userId(g.getUserId())
                            .name(name)
                            .avatarUrl(avatarUrl)
                            .lat(g.getLat())
                            .lng(g.getLng())
                            .active(g.isActive())
                            .lastSeenAt(g.getLastSeenAt())
                            .build();
                })
                .toList();
    }

    private String resolveName(UserJpaEntity user) {
        return user.getFullName() != null && !user.getFullName().isBlank()
                ? user.getFullName()
                : user.getUsername();
    }
}
