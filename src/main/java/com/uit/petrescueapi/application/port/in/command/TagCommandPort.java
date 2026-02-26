package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.tag.CreateTagRequestDto;
import com.uit.petrescueapi.application.dto.tag.TagResponseDto;

import java.util.UUID;

/**
 * Command (write) port for Tag operations.
 * Handles tag creation and deletion.
 */
public interface TagCommandPort {

    TagResponseDto create(CreateTagRequestDto cmd);

    void delete(UUID tagId);
}
