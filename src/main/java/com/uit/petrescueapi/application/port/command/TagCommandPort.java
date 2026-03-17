package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.tag.CreateTagRequestDto;
import com.uit.petrescueapi.domain.entity.Tag;

import java.util.UUID;

public interface TagCommandPort {
    Tag create(CreateTagRequestDto cmd);
    void delete(UUID tagId);
}
