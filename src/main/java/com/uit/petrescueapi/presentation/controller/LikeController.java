package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.like.LikeStatusDto;
import com.uit.petrescueapi.application.port.command.LikeCommandPort;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Likes", description = "Like and unlike operations for posts and comments")
public class LikeController {

    private final LikeCommandPort likeCommandPort;

    @PostMapping("/posts/{postId}/like")
    @Operation(
            summary = "Like a post",
            description = "Adds the authenticated user's like to the post. Operation is idempotent."
    )
    public ResponseEntity<ApiResponse<LikeStatusDto>> likePost(
            @PathVariable UUID postId,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(likeCommandPort.likePost(postId, userId)));
    }

    @DeleteMapping("/posts/{postId}/like")
    @Operation(
            summary = "Unlike a post",
            description = "Removes the authenticated user's like from the post. Operation is idempotent."
    )
    public ResponseEntity<ApiResponse<LikeStatusDto>> unlikePost(
            @PathVariable UUID postId,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(likeCommandPort.unlikePost(postId, userId)));
    }

    @PostMapping("/comments/{commentId}/like")
    @Operation(
            summary = "Like a comment",
            description = "Adds the authenticated user's like to the comment. Operation is idempotent."
    )
    public ResponseEntity<ApiResponse<LikeStatusDto>> likeComment(
            @PathVariable UUID commentId,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(likeCommandPort.likeComment(commentId, userId)));
    }

    @DeleteMapping("/comments/{commentId}/like")
    @Operation(
            summary = "Unlike a comment",
            description = "Removes the authenticated user's like from the comment. Operation is idempotent."
    )
    public ResponseEntity<ApiResponse<LikeStatusDto>> unlikeComment(
            @PathVariable UUID commentId,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(likeCommandPort.unlikeComment(commentId, userId)));
    }
}
