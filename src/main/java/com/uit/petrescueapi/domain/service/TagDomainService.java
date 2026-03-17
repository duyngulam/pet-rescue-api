package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.Tag;
import com.uit.petrescueapi.domain.exception.ResourceAlreadyExistsException;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain service encapsulating Tag business rules.
 *
 * Rules:
 *  - Tag codes must be unique.
 *  - Deletion is soft-delete (sets deleted = true).
 *
 * {@code @Transactional} lives here only — not on adapters or controllers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TagDomainService {

    private final TagRepository tagRepository;

    // ── Queries ─────────────────────────────────────

    @Transactional(readOnly = true)
    public Tag findById(UUID tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "tagId", tagId));
    }

    // ── Commands ────────────────────────────────────

    /**
     * Create a new tag.
     * Validates that the tag code is unique, then sets the id.
     */
    public Tag create(Tag tag) {
        log.info("Creating tag with code '{}'", tag.getCode());
        tagRepository.findByCode(tag.getCode()).ifPresent(existing -> {
            throw new ResourceAlreadyExistsException("Tag", "code", tag.getCode());
        });

        tag.setTagId(UUID.randomUUID());
        tag.setCreatedAt(LocalDateTime.now());
        return tagRepository.save(tag);
    }

    /**
     * Soft delete a tag.
     */
    public void delete(UUID tagId) {
        Tag tag = findById(tagId);
        tag.setDeleted(true);
        tag.setDeletedAt(LocalDateTime.now());
        tagRepository.save(tag);
        log.info("Soft-deleted tag {}", tagId);
    }
}
