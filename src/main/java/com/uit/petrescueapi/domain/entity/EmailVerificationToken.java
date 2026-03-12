package com.uit.petrescueapi.domain.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * One-time email-verification token sent on sign-up.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationToken {

    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiresAt;
    private boolean used;
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isUsable() {
        return !used && !isExpired();
    }

    public void markUsed() {
        this.used = true;
    }
}
