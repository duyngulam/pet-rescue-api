package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.geo.GeoLocationUpdateRequestDto;
import com.uit.petrescueapi.application.dto.geo.GeoUserLocationDto;

import java.util.UUID;

public interface GeoCommandPort {
    GeoUserLocationDto updateMyLocation(UUID userId, GeoLocationUpdateRequestDto request);
    void markInactive(UUID userId);
}
