package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;

import java.util.UUID;

/**
 * Query (read) port for Media operations.
 * Handles media file metadata lookups.
 */
public interface MediaQueryPort {

    MediaFileResponseDto findById(UUID mediaId);
}
