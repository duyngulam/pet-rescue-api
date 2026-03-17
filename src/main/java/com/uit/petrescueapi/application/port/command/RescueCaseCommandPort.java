package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.rescue.CreateRescueCaseRequestDto;
import com.uit.petrescueapi.application.dto.rescue.UpdateRescueCaseStatusRequestDto;
import com.uit.petrescueapi.domain.entity.RescueCase;

import java.util.UUID;

public interface RescueCaseCommandPort {
    RescueCase report(CreateRescueCaseRequestDto cmd, UUID reporterId);
    RescueCase update(UUID caseId, CreateRescueCaseRequestDto cmd);
    RescueCase changeStatus(UUID caseId, UpdateRescueCaseStatusRequestDto cmd);
}
