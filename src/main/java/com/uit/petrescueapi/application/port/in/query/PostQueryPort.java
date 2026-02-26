package com.uit.petrescueapi.application.port.in.query;

import com.uit.petrescueapi.application.dto.post.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Query (read) port for Post operations.
 * Handles post lookups and listing.
 */
public interface PostQueryPort {

    PostResponseDto findById(UUID postId);

    Page<PostResponseDto> findAll(Pageable pageable);
}
