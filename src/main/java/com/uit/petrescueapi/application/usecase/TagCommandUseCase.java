package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.tag.CreateTagRequestDto;
import com.uit.petrescueapi.application.port.command.TagCommandPort;
import com.uit.petrescueapi.domain.entity.Tag;
import com.uit.petrescueapi.domain.service.TagDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Command (write) use-case for Tag operations.
 * Translates request DTOs into domain calls and delegates business rules
 * to {@link TagDomainService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagCommandUseCase implements TagCommandPort {

    private final TagDomainService domainService;

    @Override
    public Tag create(CreateTagRequestDto cmd) {
        log.debug("Command: create tag with code '{}'", cmd.getCode());
        Tag tag = Tag.builder()
                .code(cmd.getCode())
                .name(cmd.getName())
                .description(cmd.getDescription())
                .build();
        return domainService.create(tag);
    }

    @Override
    public void delete(UUID tagId) {
        log.debug("Command: delete tag {}", tagId);
        domainService.delete(tagId);
    }
}
