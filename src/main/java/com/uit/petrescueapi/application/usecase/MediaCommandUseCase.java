package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.port.command.MediaCommandPort;
import com.uit.petrescueapi.domain.entity.MediaFile;
import com.uit.petrescueapi.domain.service.MediaFileDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Command (write) use-case for Media operations.
 * Translates parameters into domain calls and delegates business rules
 * to {@link MediaFileDomainService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MediaCommandUseCase implements MediaCommandPort {

    private final MediaFileDomainService domainService;

    @Override
    public MediaFile register(UUID uploaderId, String publicId, String resourceType,
                              String format, Integer width, Integer height,
                              Integer bytes, String folder) {
        log.debug("Command: register media file for uploader {}", uploaderId);
        MediaFile mediaFile = MediaFile.builder()
                .uploaderId(uploaderId)
                .publicId(publicId)
                .resourceType(resourceType)
                .format(format)
                .width(width)
                .height(height)
                .bytes(bytes)
                .folder(folder)
                .build();
        return domainService.register(mediaFile);
    }

    @Override
    public void delete(UUID mediaId) {
        log.debug("Command: delete media file {}", mediaId);
        domainService.delete(mediaId);
    }
}
