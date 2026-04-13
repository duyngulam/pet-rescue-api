package com.uit.petrescueapi.application.dto.geo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoUserLocationDto {
    private UUID userId;
    private String name;
    private String avatarUrl;
    private Double lat;
    private Double lng;
    private boolean active;
    private OffsetDateTime lastSeenAt;
}
