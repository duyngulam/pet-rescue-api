package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.adoption.CreateAdoptionRequestDto;
import com.uit.petrescueapi.application.dto.adoption.DecisionRequestDto;
import com.uit.petrescueapi.domain.entity.AdoptionApplication;

import java.util.UUID;

public interface AdoptionCommandPort {
    AdoptionApplication submit(CreateAdoptionRequestDto cmd, UUID applicantId);
    AdoptionApplication approve(UUID applicationId, DecisionRequestDto decision, UUID decidedBy);
    AdoptionApplication reject(UUID applicationId, DecisionRequestDto decision, UUID decidedBy);
    AdoptionApplication cancel(UUID applicationId);
}
