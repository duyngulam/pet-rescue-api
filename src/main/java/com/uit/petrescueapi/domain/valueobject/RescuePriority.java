package com.uit.petrescueapi.domain.valueobject;

/**
 * Priority level for rescue cases - determines response urgency.
 */
public enum RescuePriority {
    /**
     * Life-threatening emergency - immediate response required (minutes matter).
     * Examples: trapped animal, drowning, severe injury, immediate danger.
     */
    CRITICAL,

    /**
     * Urgent situation - respond within hours.
     * Examples: injured but stable, sick, in unsafe location.
     */
    HIGH,

    /**
     * Standard rescue case - normal response time.
     * Examples: abandoned pet, lost animal, non-emergency situations.
     */
    NORMAL,

    /**
     * Informational report - no immediate action required.
     * Examples: spotted stray, potential future case, monitoring.
     */
    LOW
}
