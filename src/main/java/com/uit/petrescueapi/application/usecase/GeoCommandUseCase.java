package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.geo.GeoLocationUpdateRequestDto;
import com.uit.petrescueapi.application.dto.geo.GeoLocationUpdatedEventDto;
import com.uit.petrescueapi.application.dto.geo.GeoUserLocationDto;
import com.uit.petrescueapi.application.port.command.GeoCommandPort;
import com.uit.petrescueapi.domain.exception.BusinessException;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.infrastructure.persistence.entity.UserGeoLocationJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.UserJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserGeoLocationJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeoCommandUseCase implements GeoCommandPort {

    private final UserGeoLocationJpaRepository geoRepository;
    private final UserJpaRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${app.geo.update-rate-limit-ms:3000}")
    private long updateRateLimitMs;

    @Value("${app.geo.realtime-radius-km:2.0}")
    private double realtimeRadiusKm;

    @Value("${app.geo.realtime-nearby-limit:200}")
    private int realtimeNearbyLimit;

    @Override
    @Transactional
    public GeoUserLocationDto updateMyLocation(UUID userId, GeoLocationUpdateRequestDto request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        UserJpaEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        UserGeoLocationJpaEntity existing = geoRepository.findByUserId(userId).orElse(null);
        if (existing != null && existing.getUpdatedAt() != null) {
            long elapsed = Duration.between(existing.getUpdatedAt(), now).toMillis();
            if (elapsed < updateRateLimitMs) {
                throw new BusinessException("Location update too frequent. Please retry later.", "GEO_RATE_LIMITED");
            }
        }

        UserGeoLocationJpaEntity saved = geoRepository.save(UserGeoLocationJpaEntity.builder()
                .userId(userId)
                .lat(request.getLat())
                .lng(request.getLng())
                .active(true)
                .lastSeenAt(now)
                .updatedAt(now)
                .build());

        GeoUserLocationDto dto = GeoUserLocationDto.builder()
                .userId(userId)
                .name(resolveName(user))
                .avatarUrl(user.getAvatarUrl())
                .lat(saved.getLat())
                .lng(saved.getLng())
                .active(saved.isActive())
                .lastSeenAt(saved.getLastSeenAt())
                .build();

        GeoLocationUpdatedEventDto event = GeoLocationUpdatedEventDto.builder()
                .type("USER_LOCATION_UPDATED")
                .user(dto)
                .build();

        List<UserGeoLocationJpaEntity> nearbyUsers = geoRepository.findNearbyActive(
                userId, saved.getLat(), saved.getLng(), realtimeRadiusKm, realtimeNearbyLimit);

        for (UserGeoLocationJpaEntity nearby : nearbyUsers) {
            messagingTemplate.convertAndSendToUser(
                    nearby.getUserId().toString(),
                    "/queue/geo/updates",
                    event
            );
        }

        // Sender also receives its own update acknowledgement channel.
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/geo/updates",
                event
        );

        return dto;
    }

    @Override
    @Transactional
    public void markInactive(UUID userId) {
        geoRepository.markInactive(userId);
    }

    private String resolveName(UserJpaEntity user) {
        return user.getFullName() != null && !user.getFullName().isBlank()
                ? user.getFullName()
                : user.getUsername();
    }
}
