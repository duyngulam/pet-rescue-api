package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Mock media upload controller.
 * In real implementation, integrate with cloud storage (S3, GCS, etc.).
 */
@RestController
@RequestMapping("/api/v1/media")
@Tag(name = "Media", description = "File upload & management")
public class MockMediaController {

    private static final UUID USER1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @PostMapping("/upload")
    @Operation(summary = "Upload a media file (returns URL after storage)")
    public ResponseEntity<ApiResponse<MediaFileResponseDto>> upload() {
        // In real implementation: accept MultipartFile, upload to cloud, return URL
        MediaFileResponseDto dto = MediaFileResponseDto.builder()
                .mediaId(UUID.randomUUID()).uploaderId(USER1)
                .url("https://storage.example.com/uploads/" + UUID.randomUUID() + ".jpg")
                .type("IMAGE").createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.status(201).body(ApiResponse.created(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get media file metadata by ID")
    public ResponseEntity<ApiResponse<MediaFileResponseDto>> getById(@PathVariable UUID id) {
        MediaFileResponseDto dto = MediaFileResponseDto.builder()
                .mediaId(id).uploaderId(USER1)
                .url("https://storage.example.com/uploads/buddy-01.jpg")
                .type("IMAGE").createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a media file")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(null, "Media deleted"));
    }
}
