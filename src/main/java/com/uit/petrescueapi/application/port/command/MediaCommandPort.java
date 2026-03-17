package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.domain.entity.MediaFile;

import java.util.UUID;

public interface MediaCommandPort {
    MediaFile register(UUID uploaderId, String publicId, String resourceType, String format, Integer width, Integer height, Integer bytes, String folder);
    void delete(UUID mediaId);
}
