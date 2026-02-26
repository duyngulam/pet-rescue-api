package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.tag.TagResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Query (read) port for Tag operations.
 * Handles tag listing.
 */
public interface TagQueryPort {

    Page<TagResponseDto> findAll(Pageable pageable);
}
