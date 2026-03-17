package com.uit.petrescueapi.domain.valueobject;

/**
 * Organization lifecycle status — value object.
 * <ul>
 *   <li>{@code PENDING} – awaiting approval/activation</li>
 *   <li>{@code ACTIVE} – operational and active</li>
 *   <li>{@code INACTIVE} – deactivated or suspended</li>
 * </ul>
 */
public enum OrganizationStatus {
    PENDING,
    ACTIVE,
    INACTIVE
}
