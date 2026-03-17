package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.post.*;
import com.uit.petrescueapi.application.port.command.PostCommandPort;
import com.uit.petrescueapi.application.port.query.PostQueryPort;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import com.uit.petrescueapi.presentation.mapper.PostWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "Posts", description = "Community post management")
public class PostController {

    private final PostCommandPort commandPort;
    private final PostQueryPort queryPort;
    private final PostWebMapper mapper;

    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<ApiResponse<PostResponseDto>> create(
            @Valid @RequestBody CreatePostRequestDto cmd,
            Authentication authentication) {
        UUID authorId = UUID.fromString(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(mapper.toDto(commandPort.create(cmd, authorId))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post")
    public ResponseEntity<ApiResponse<PostResponseDto>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto cmd) {
        return ResponseEntity.ok(ApiResponse.ok(mapper.toDto(commandPort.update(id, cmd))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        commandPort.delete(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID")
    public ResponseEntity<ApiResponse<PostResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(queryPort.findById(id)));
    }

    @GetMapping
    @Operation(summary = "List all posts")
    public ResponseEntity<ApiResponse<PageResponse<PostSummaryResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                PageResponse.from(queryPort.findAll(PageRequest.of(page, size)))));
    }
}
