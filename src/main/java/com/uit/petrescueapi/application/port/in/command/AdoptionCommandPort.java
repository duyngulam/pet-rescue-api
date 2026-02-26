package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.adoption.AdoptionResponseDto;
import com.uit.petrescueapi.application.dto.adoption.CreateAdoptionRequestDto;
import com.uit.petrescueapi.application.dto.adoption.DecisionRequestDto;

import java.util.UUID;

/**
 * Command (write) port for Adoption operations.
 * Handles application submission, approval, rejection and cancellation.
 */
public interface AdoptionCommandPort {

    AdoptionResponseDto submit(CreateAdoptionRequestDto cmd);

    AdoptionResponseDto approve(UUID applicationId, DecisionRequestDto decision);

    AdoptionResponseDto reject(UUID applicationId, DecisionRequestDto decision);

    AdoptionResponseDto cancel(UUID applicationId);
}
