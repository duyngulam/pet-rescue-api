package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.rescue.CreateRescueCaseRequestDto;
import com.uit.petrescueapi.application.dto.rescue.RescueCaseResponseDto;
import com.uit.petrescueapi.application.dto.rescue.UpdateRescueCaseStatusRequestDto;

import java.util.UUID;

/**
 * Command (write) port for Rescue Case operations.
 * Handles case reporting, updates and status transitions.
 */
public interface RescueCaseCommandPort {

    RescueCaseResponseDto report(CreateRescueCaseRequestDto cmd);

    RescueCaseResponseDto update(UUID caseId, CreateRescueCaseRequestDto cmd);

    RescueCaseResponseDto changeStatus(UUID caseId, UpdateRescueCaseStatusRequestDto cmd);
}
