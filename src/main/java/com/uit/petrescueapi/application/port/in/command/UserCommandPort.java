package com.uit.petrescueapi.application.port.in.command;

import java.util.UUID;

/**
 * Command (write) port for User operations.
 * Handles profile updates, status changes, etc.
 */
public interface UserCommandPort {

    void updateStatus(UUID userId, String status);

    void deactivate(UUID userId);
}
