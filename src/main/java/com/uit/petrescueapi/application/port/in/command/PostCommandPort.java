package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.post.CreatePostRequestDto;
import com.uit.petrescueapi.application.dto.post.PostResponseDto;
import com.uit.petrescueapi.application.dto.post.UpdatePostRequestDto;

import java.util.UUID;

/**
 * Command (write) port for Post operations.
 * Handles post creation, updates and deletion.
 */
public interface PostCommandPort {

    PostResponseDto create(CreatePostRequestDto cmd);

    PostResponseDto update(UUID postId, UpdatePostRequestDto cmd);

    void delete(UUID postId);
}
