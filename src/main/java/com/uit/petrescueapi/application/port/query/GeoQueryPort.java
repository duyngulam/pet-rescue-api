package com.uit.petrescueapi.application.port.query;

import com.uit.petrescueapi.application.dto.geo.GeoUserLocationDto;

import java.util.List;
import java.util.UUID;

public interface GeoQueryPort {
    List<GeoUserLocationDto> findNearby(UUID userId, double radiusKm, int limit);
}
