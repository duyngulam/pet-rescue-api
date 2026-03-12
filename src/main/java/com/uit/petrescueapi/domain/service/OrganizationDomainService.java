package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.Organization;
import com.uit.petrescueapi.domain.entity.OrganizationMember;
import com.uit.petrescueapi.domain.exception.ResourceAlreadyExistsException;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.repository.OrganizationMemberRepository;
import com.uit.petrescueapi.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain service encapsulating Organization business rules.
 *
 * Rules:
 *  • Newly created organizations always start with status {@code PENDING}.
 *  • An organization can only be deactivated (not hard-deleted) via {@code deactivate()}.
 *  • A user cannot be added to the same organization twice.
 *
 * {@code @Transactional} lives here only — not on adapters or controllers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrganizationDomainService {

    private final OrganizationRepository orgRepository;
    private final OrganizationMemberRepository memberRepository;

    // ── Commands ────────────────────────────────────

    public Organization create(Organization org) {
        log.info("Creating organization: {}", org.getName());
        org.setOrganizationId(UUID.randomUUID());
        org.setStatus("PENDING");
        return orgRepository.save(org);
    }

    public Organization update(UUID id, Organization patch) {
        log.debug("Updating organization {}", id);
        Organization existing = findOrThrow(id);
        if (patch.getName() != null)          existing.setName(patch.getName());
        if (patch.getType() != null)          existing.setType(patch.getType());
        if (patch.getStreet_address() != null) existing.setStreet_address(patch.getStreet_address());
        if (patch.getPhone() != null)         existing.setPhone(patch.getPhone());
        if (patch.getEmail() != null)         existing.setEmail(patch.getEmail());
        if (patch.getLatitude() != null)      existing.setLatitude(patch.getLatitude());
        if (patch.getLongitude() != null)     existing.setLongitude(patch.getLongitude());
        return orgRepository.save(existing);
    }

    public void deactivate(UUID id) {
        log.info("Deactivating organization {}", id);
        Organization org = findOrThrow(id);
        org.setStatus("INACTIVE");
        orgRepository.save(org);
    }

    public OrganizationMember addMember(UUID orgId, UUID userId, String role) {
        findOrThrow(orgId);
        if (memberRepository.exists(orgId, userId)) {
            throw new ResourceAlreadyExistsException("OrganizationMember", "userId", userId);
        }
        OrganizationMember member = OrganizationMember.builder()
                .organizationId(orgId)
                .userId(userId)
                .role(role)
                .status("ACTIVE")
                .joinedAt(LocalDateTime.now())
                .build();
        return memberRepository.save(member);
    }

    public void removeMember(UUID orgId, UUID userId) {
        log.debug("Removing member {} from organization {}", userId, orgId);
        memberRepository.delete(orgId, userId);
    }

    @Transactional(readOnly = true)
    public boolean isMember(UUID orgId, UUID userId) {
        return memberRepository.exists(orgId, userId);
    }

    // ── Helpers ──────────────────────────────────────

    @Transactional(readOnly = true)
    private Organization findOrThrow(UUID id) {
        return orgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", id));
    }
}
