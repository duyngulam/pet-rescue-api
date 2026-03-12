package com.uit.petrescueapi.domain.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Refresh-token entity stored in the database for token rotation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiresAt;
    private boolean revoked;
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isUsable() {
        return !revoked && !isExpired();
    }

    public void revoke() {
        this.revoked = true;
    }
}
