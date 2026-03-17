package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.AdoptionApplication;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.repository.AdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain service encapsulating AdoptionApplication business rules.
 *
 * Rules:
 *  - New applications always start with status PENDING.
 *  - Approval: PENDING -> APPROVED (requires decidedBy).
 *  - Rejection: PENDING -> REJECTED (requires decidedBy).
 *  - Cancellation: PENDING -> CANCELED.
 *
 * {@code @Transactional} lives here only — not on adapters or controllers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdoptionDomainService {

    private final AdoptionApplicationRepository applicationRepository;

    // ── Queries ─────────────────────────────────────

    @Transactional(readOnly = true)
    public AdoptionApplication findById(UUID applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("AdoptionApplication", "applicationId", applicationId));
    }

    // ── Commands ────────────────────────────────────

    /**
     * Submit a new adoption application.
     * Sets the id, status to PENDING, and createdAt timestamp.
     */
    public AdoptionApplication submit(AdoptionApplication application) {
        log.info("Submitting adoption application for pet {}", application.getPetId());
        application.setApplicationId(UUID.randomUUID());
        application.setStatus("PENDING");
        application.setCreatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    /**
     * Approve an adoption application.
     * Validates the current status is PENDING before transitioning to APPROVED.
     */
    public AdoptionApplication approve(UUID applicationId, UUID decidedBy) {
        log.info("Approving adoption application {}", applicationId);
        AdoptionApplication app = findById(applicationId);
        validateStatus(app, "PENDING", "APPROVED");

        app.setStatus("APPROVED");
        app.setDecidedBy(decidedBy);
        app.setDecidedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(app);
    }

    /**
     * Reject an adoption application.
     * Validates the current status is PENDING before transitioning to REJECTED.
     */
    public AdoptionApplication reject(UUID applicationId, UUID decidedBy) {
        log.info("Rejecting adoption application {}", applicationId);
        AdoptionApplication app = findById(applicationId);
        validateStatus(app, "PENDING", "REJECTED");

        app.setStatus("REJECTED");
        app.setDecidedBy(decidedBy);
        app.setDecidedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(app);
    }

    /**
     * Cancel an adoption application.
     * Validates the current status is PENDING before transitioning to CANCELED.
     */
    public AdoptionApplication cancel(UUID applicationId) {
        log.info("Canceling adoption application {}", applicationId);
        AdoptionApplication app = findById(applicationId);
        validateStatus(app, "PENDING", "CANCELED");

        app.setStatus("CANCELED");
        app.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(app);
    }

    // ── Private helpers ─────────────────────────────

    private void validateStatus(AdoptionApplication app, String expectedCurrent, String targetStatus) {
        if (!expectedCurrent.equals(app.getStatus())) {
            throw new IllegalStateException(
                    String.format("Cannot transition adoption application status from %s to %s",
                            app.getStatus(), targetStatus));
        }
    }
}
