package com.uit.petrescueapi.domain.valueobject;

/**
 * Organization lifecycle status — value object.
 * <ul>
 *   <li>{@code PENDING} – awaiting approval/activation</li>
 *   <li>{@code ACTIVE} – operational and active</li>
 *   <li>{@code INACTIVE} – deactivated or suspended</li>
 *   <li>{@code REJECTED} – rejected during approval flow</li>
 * </ul>
 */
public enum OrganizationStatus {
    PENDING,
    ACTIVE,
    INACTIVE,
    REJECTED
}
