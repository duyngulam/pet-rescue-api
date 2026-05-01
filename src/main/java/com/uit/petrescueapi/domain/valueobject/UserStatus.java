package com.uit.petrescueapi.domain.valueobject;

/**
 * Lifecycle status of a user account.
 */
public enum UserStatus {
    PENDING_VERIFICATION,
    ACTIVE,
    LOCKED,
    INACTIVE,
    BANNED
}
