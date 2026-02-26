package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;

import java.util.UUID;

/**
 * Command (write) port for Media operations.
 * Handles file upload and deletion.
 */
public interface MediaCommandPort {

    MediaFileResponseDto upload(/* MultipartFile in real impl */);

    void delete(UUID mediaId);
}
