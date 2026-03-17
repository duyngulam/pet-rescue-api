package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.MediaFile;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain service encapsulating MediaFile business rules.
 *
 * Rules:
 *  - New media files get a generated id and createdAt timestamp.
 *  - Deletion is soft-delete (sets deleted = true).
 *
 * {@code @Transactional} lives here only — not on adapters or controllers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MediaFileDomainService {

    private final MediaFileRepository mediaFileRepository;

    // ── Queries ─────────────────────────────────────

    @Transactional(readOnly = true)
    public MediaFile findById(UUID mediaId) {
        return mediaFileRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("MediaFile", "mediaId", mediaId));
    }

    // ── Commands ────────────────────────────────────

    /**
     * Register a new media file.
     * Sets the id and createdAt timestamp.
     */
    public MediaFile register(MediaFile mediaFile) {
        log.info("Registering media file for uploader {}", mediaFile.getUploaderId());
        mediaFile.setMediaId(UUID.randomUUID());
        mediaFile.setCreatedAt(LocalDateTime.now());
        return mediaFileRepository.save(mediaFile);
    }

    /**
     * Soft delete a media file.
     */
    public void delete(UUID mediaId) {
        MediaFile mediaFile = findById(mediaId);
        mediaFile.setDeleted(true);
        mediaFile.setDeletedAt(LocalDateTime.now());
        mediaFileRepository.save(mediaFile);
        log.info("Soft-deleted media file {}", mediaId);
    }
}
