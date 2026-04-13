package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.geo.GeoLocationUpdateRequestDto;
import com.uit.petrescueapi.application.dto.geo.GeoUserLocationDto;
import com.uit.petrescueapi.application.port.command.GeoCommandPort;
import com.uit.petrescueapi.application.port.query.GeoQueryPort;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/geo")
@RequiredArgsConstructor
@Tag(name = "Geo", description = "Lightweight realtime geo presence")
public class GeoController {

    private final GeoCommandPort geoCommandPort;
    private final GeoQueryPort geoQueryPort;

    @PutMapping("/me/location")
    @Operation(summary = "Update my location", description = "Updates last-known location and marks user active.")
    public ResponseEntity<ApiResponse<GeoUserLocationDto>> updateMyLocation(
            @Valid @RequestBody GeoLocationUpdateRequestDto request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(geoCommandPort.updateMyLocation(userId, request)));
    }

    @GetMapping("/nearby")
    @Operation(summary = "Get nearby active users", description = "Returns nearby active users (name, avatar, basic location).")
    public ResponseEntity<ApiResponse<List<GeoUserLocationDto>>> nearby(
            Authentication authentication,
            @RequestParam(defaultValue = "2.0") double radiusKm,
            @RequestParam(defaultValue = "50") int limit) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(geoQueryPort.findNearby(userId, radiusKm, limit)));
    }
}
