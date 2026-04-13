package com.uit.petrescueapi.application.dto.geo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocationUpdatedEventDto {
    private String type;
    private GeoUserLocationDto user;
}
